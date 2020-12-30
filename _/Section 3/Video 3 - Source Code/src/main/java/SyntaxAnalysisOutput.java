import java.util.List;

public class SyntaxAnalysisOutput {
    List<Sentence> sentences;
    List<Token> tokens;

    static class Text {
        String content;
    }

    static class Sentence {
        Text text;
    }

    static class Token {
        Text text;
    }
}
