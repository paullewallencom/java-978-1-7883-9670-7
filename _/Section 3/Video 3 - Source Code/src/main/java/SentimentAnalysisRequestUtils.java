import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public static Optional<String> getMostCommonWord(OkHttpClient httpClient, String inputString) {
        //Define the request
        SentimentInput input = new SentimentInput(inputString);
        MediaType contentType = MediaType.parse("application/json");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<SentimentInput> adapter = moshi.adapter(SentimentInput.class);
        String sentimentInputJson = adapter.toJson(input);

        RequestBody requestBody = RequestBody.create(contentType, sentimentInputJson);

        final Request request = new Request.Builder()
                .url("https://language.googleapis.com/v1/documents:analyzeSyntax?key=AIzaSyB1LFPu9WgZacJg2JKgspngkfNfAgui8vg")
                .post(requestBody)
                .build();
        //Make the HTTP call
        Call call = httpClient.newCall(request);

        Optional<String> result;
        try {
            Response response = call.execute();
            JsonAdapter<SyntaxAnalysisOutput> jsonAdapter = moshi.adapter(SyntaxAnalysisOutput.class);
            SyntaxAnalysisOutput syntaxAnalysisOutput = jsonAdapter.fromJson(response.body().string());
            //Extract the word from each sentence
            List<String> words = syntaxAnalysisOutput.tokens.stream()
                    .map(token -> token.text.content)
                    .collect(Collectors.toList());
            //Count how many times does each word appear
            Map<String, Long> wordsPopularity = words.stream()
                    .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
            //Extract the most popular word
            Map.Entry<String, Long> mostPopularWord = wordsPopularity.entrySet().stream()
                    .max(Comparator.comparing(Map.Entry::getValue))
                    .get();
            System.out.println("The word " + mostPopularWord.getKey() + " appeared " + mostPopularWord.getValue() + " times.");
            result = Optional.of(mostPopularWord.getKey());

        } catch (IOException e) {
            e.printStackTrace();
            result = Optional.empty();
        }
        return result;
    }
}
