import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.*;

import java.io.IOException;
import java.util.Optional;

public class SentimentAnalysisRequestUtils {
    public static Optional<Double> getSentimentScore(OkHttpClient httpClient, String inputString) {
        //Define the request
        SentimentInput input = new SentimentInput(inputString);
        MediaType contentType = MediaType.parse("application/json");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SentimentInput> adapter = moshi.adapter(SentimentInput.class);
        String sentimentInputJson = adapter.toJson(input);

        RequestBody requestBody = RequestBody.create(contentType, sentimentInputJson);

        final Request request = new Request.Builder()
                .url("https://language.googleapis.com/v1/documents:analyzeSentiment?key=AIzaSyB1LFPu9WgZacJg2JKgspngkfNfAgui8vg")
                .post(requestBody)
                .build();
        //Make the HTTP call
        Call call = httpClient.newCall(request);

        Optional<Double> result;
        try {
            Response response = call.execute();
            JsonAdapter<SentimentAnalysisOutput> jsonAdapter = moshi.adapter(SentimentAnalysisOutput.class);
            SentimentAnalysisOutput sentimentAnalysisOutput = jsonAdapter.fromJson(response.body().string());
            result = Optional.ofNullable(sentimentAnalysisOutput.documentSentiment.score);
        } catch (IOException e) {
            e.printStackTrace();
            result = Optional.empty();
        }
        return result;
    }
}
