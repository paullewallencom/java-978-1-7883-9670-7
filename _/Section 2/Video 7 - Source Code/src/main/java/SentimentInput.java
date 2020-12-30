public class SentimentInput {
    String encodingType;
    Document document;

    public SentimentInput(String content) {
        if(content.isEmpty()) {
            throw new IllegalArgumentException("JSON content should not be empty!");
        }
        this.encodingType = "UTF8";
        this.document = new Document("PLAIN_TEXT", content);
    }

    static class Document {
        String type;
        String content;

        public Document(String type, String content) {
            this.type = type;
            this.content = content;
        }
    }
}
