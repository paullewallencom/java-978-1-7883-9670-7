import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@EnableAutoConfiguration
public class SentimentAnalysisDemo {
    private Interceptor loggingInterceptor = chain -> {
        Request request = chain.request();
        Path log = Paths.get("logFile.txt");
        String content = LocalDateTime.now().toString() + "," + request.url().toString() + System.lineSeparator();
        Files.write(log, content.getBytes(), StandardOpenOption.APPEND);
        return chain.proceed(request);
    };
    private OkHttpClient httpClient = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true) //Use with caution
            .addInterceptor(loggingInterceptor)
            .build();

    public static void main(String[] args) {
        SpringApplication.run(SentimentAnalysisDemo.class, args);
    }

    @RequestMapping("/sentimentScore")
    @ResponseBody
    Double getSentimentScore(@RequestParam String input) {
        return SentimentAnalysisRequestUtils.getSentimentScore(httpClient, input).orElse(Double.NaN);
    }

    @RequestMapping("/mostCommonWord")
    @ResponseBody
    public String mostCommonWord(String input) {
        //split input using syntax analysis
        //map reduce to count
        //find the word with the highest count
        return SentimentAnalysisRequestUtils.getMostCommonWord(httpClient, input).orElse("Error - could not perform request.");
    }

    @RequestMapping("/numberOfRequestsToday")
    @ResponseBody
    public static long numberOfRequestsInTheLastDay() throws IOException {
        //retrieve the requests from the log
        List<String> logEntries = Files.readAllLines(Paths.get("logFile.txt"));
        //filter by date - for today
        //count the results
        return logEntries.stream()
                .map(x -> x.split(",")) //first element is the time, the second is the URL
                .map(x -> LocalDateTime.parse(x[0])) //parse the first element as a time
                .filter(time -> time.toLocalDate().equals(LocalDate.now()))
                .count();
    }

    @RequestMapping("/maxSentimentScore")
    @ResponseBody
    public static double maxSentimentScore(String input) {
        //get the sentiment score for each sentence
        //find the sentence with the highest score
        return 0d; //ToDo Change this
    }

    @RequestMapping("/minSentimentScore")
    @ResponseBody
    public static double minSentimentScore(String input) {
        //get the sentiment score for each sentence
        //find the sentence with the lowest score
        return 0d; //ToDo Change this
    }

    @RequestMapping("/averageSentimentScore")
    @ResponseBody
    public static double averageSentimentScore(String input) {
        //get the sentiment score for each sentence
        //find out the average sentiment score
        return 0d; //ToDo Change this
    }
}