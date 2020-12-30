import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.*;

import java.io.IOException;

public class SentimentAnalysisDemo {

    public static void main(String[] args) {
        //Initialize the client
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true) //Use with caution
                .build();

        //Define the request
        SentimentInput input = new SentimentInput("This is my first sentence analyzed by the Google Natural Language API. This is awesome!");
        MediaType contentType = MediaType.parse("application/json");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SentimentInput> adapter = moshi.adapter(SentimentInput.class);
        String sentimentInputJson = adapter.toJson(input);

        RequestBody requestBody = RequestBody.create(contentType, sentimentInputJson);

        final Request request = new Request.Builder()
                .url("https://language.googleapis.com/v1/documents:analyzeSentiment?key=AIzaSyAKoTqY09-di8DC3Z-3gUKjkVocyls6XYE")
                .post(requestBody)
                .build();
        //Make the HTTP call
        Call call = httpClient.newCall(request);

        //Make sure the call output is printed
        call.enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                String url = call.request().url().toString();
                System.err.println("The following HTTP call failed: " + url + e.getMessage());
            }

            public void onResponse(Call call, Response response) throws IOException {
                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<SentimentAnalysisOutput> jsonAdapter = moshi.adapter(SentimentAnalysisOutput.class);
                SentimentAnalysisOutput sentimentAnalysisOutput = jsonAdapter.fromJson(response.body().string());
                System.out.println("The sentiment score for the given sentence is " + sentimentAnalysisOutput.documentSentiment.score);
            }
        });
    }
}
