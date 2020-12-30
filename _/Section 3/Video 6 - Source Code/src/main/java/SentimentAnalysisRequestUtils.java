import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import javafx.util.Pair;
import okhttp3.*;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
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

    public static Map<String, Double> getSentimentScorePerSentence(OkHttpClient httpClient, String inputString) {
        Moshi moshi = new Moshi.Builder().build();

        if(isResponseCached(inputString, "sentiment")) { //It is cached
            String cachedJson = getCachedResponse(inputString, "sentiment");
            JsonAdapter<SentimentAnalysisOutput> adapter = moshi.adapter(SentimentAnalysisOutput.class);
            try {
                SentimentAnalysisOutput output = adapter.fromJson(cachedJson);
                return output.sentences.stream()
                        .map(sentence -> new Pair<>(sentence.text.content, sentence.sentiment.score))
                        .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
            } catch (IOException e) {
                e.printStackTrace();
                return new HashMap<>();
            }
        } else { //It is not cached
            //Define the request
            SentimentInput input = new SentimentInput(inputString);
            MediaType contentType = MediaType.parse("application/json");
            JsonAdapter<SentimentInput> adapter = moshi.adapter(SentimentInput.class);
            String sentimentInputJson = adapter.toJson(input);

            RequestBody requestBody = RequestBody.create(contentType, sentimentInputJson);

            final Request request = new Request.Builder()
                    .url("https://language.googleapis.com/v1/documents:analyzeSentiment?key=AIzaSyB1LFPu9WgZacJg2JKgspngkfNfAgui8vg")
                    .post(requestBody)
                    .build();
            //Make the HTTP call
            Call call = httpClient.newCall(request);

            try {
                Response response = call.execute();
                JsonAdapter<SentimentAnalysisOutput> jsonAdapter = moshi.adapter(SentimentAnalysisOutput.class);
                String responseBody = response.body().string();
                storeCachedResponse(inputString, "sentiment", responseBody);
                SentimentAnalysisOutput sentimentAnalysisOutput = jsonAdapter.fromJson(responseBody);
                return sentimentAnalysisOutput.sentences.stream()
                        .map(sentence -> new Pair<>(sentence.text.content, sentence.sentiment.score))
                        .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new HashMap<>();
        }
    }

    private static void storeCachedResponse(String inputString, String methodName, String responseBody) {
        String inputHash = hashSha256(inputString);
        Path cachedFilePath = Paths.get("cache_" + methodName + "_" + inputHash);
        try {
            Files.write(cachedFilePath, responseBody.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getCachedResponse(String inputString, String methodName) {
        String inputHash = hashSha256(inputString);
        Path cachedFilePath = Paths.get("cache_" + methodName + "_" + inputHash);
        try {
            return new String(Files.readAllBytes(cachedFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static boolean isResponseCached(String inputString, String methodName) {
        String inputHash = hashSha256(inputString);
        Path cachedFilePath = Paths.get("cache_" + methodName + "_" + inputHash);
        return Files.exists(cachedFilePath);
    }

    private static String hashSha256(String textToHash) {
        String hashedString = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(textToHash.getBytes());
            hashedString = new BigInteger(1, hashBytes).toString(16); //Hexadecimal conversion
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedString;
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
