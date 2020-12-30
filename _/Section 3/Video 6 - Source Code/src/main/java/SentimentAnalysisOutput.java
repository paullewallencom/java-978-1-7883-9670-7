import java.util.List;

public class SentimentAnalysisOutput {
    DocumentSentiment documentSentiment;
    String language;
    List<Sentence> sentences;

    static class DocumentSentiment {
        Double magnitude;
        Double score;

        @Override
        public String toString() {
            return "DocumentSentiment{" +
                    "magnitude=" + magnitude +
                    ", score=" + score +
                    '}';
        }
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

    @Override
    public String toString() {
        return "SentimentAnalysisOutput{" +
                "documentSentiment=" + documentSentiment +
                ", language='" + language + '\'' +
                ", sentences=" + sentences +
                '}';
    }
}
