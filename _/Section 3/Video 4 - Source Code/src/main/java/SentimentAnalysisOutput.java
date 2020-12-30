import java.util.List;

public class SentimentAnalysisOutput {
    DocumentSentiment documentSentiment;
    String language;
    List<Sentence> sentences;

    static class DocumentSentiment {
        Double magnitude;
        Double score;
    }
    static class Sentence {
        Text text;
        Sentiment sentiment;

        static class Text {
            String content;
            Integer beginOffset;
        }
        static class Sentiment {
            Double magnitude;
            Double score;
        }
    }
}
