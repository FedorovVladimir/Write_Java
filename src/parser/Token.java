package parser;

public class Token {
    private String text;
    private TokenType type;

    Token(TokenType type) {
        this.text = "";
        this.type = type;
    }

    Token(String text, TokenType type) {
        this.text = text;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        String str = String.valueOf(type);
        if (!text.equals(""))
            str += " " + text;
        return str;
    }
}
