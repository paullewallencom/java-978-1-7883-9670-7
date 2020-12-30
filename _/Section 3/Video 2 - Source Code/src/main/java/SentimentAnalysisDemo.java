import okhttp3.OkHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class SentimentAnalysisDemo {

    public static void main(String[] args) {
        SpringApplication.run(SentimentAnalysisDemo.class, args);
    }

    @RequestMapping("/sentimentScore")
    @ResponseBody
    Double getSentimentScore(@RequestParam String input) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true) //Use with caution
                .build();
        return SentimentAnalysisRequestUtils.getSentimentScore(httpClient, input).orElse(Double.NaN);
    }

    @RequestMapping("/mostCommonWord")
    @ResponseBody
    public static String mostCommonWord(String input) {
        //split input using syntax analysis
        //map reduce to count
        //find the word with the highest count
        return null; //ToDo Change this
    }

    @RequestMapping("/numberOfRequestsToday")
    @ResponseBody
    public static int numberOfRequestsInTheLastDay() {
        //retrieve the requests from the log
        //filter by date - for today
        //count the results
        return 0; //ToDo Change this
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
