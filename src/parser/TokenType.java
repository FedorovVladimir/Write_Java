package parser;

public enum TokenType {
    TYPE_INT,
    TYPE_CHAR,
    TYPE_STRING,
    TYPE_DOUBLE,
    ID,

    PLUS,
    MINUS,
    SLASH,
    STAR,
    PERCENT,

    COMMA,
    OPEN_SQUARE,
    CLOSE_SQUARE,

    LESS,
    GREAT,
    LESS_EQUALLY,
    GREAT_EQUALLY,
    EQUALLY,
    NOT_EQUALLY,

    SEMICOLON,
    OPEN_PARENTHESIS,
    CLOSE_PARENTHESIS,
    OPEN_CURLY_BRACE,
    CLOSE_CURLY_BRACE,
    ASSIGN,

    PUBLIC,
    STATIC,
    VOID,
    CHAR,
    DOUBLE,
    MAIN,
    CLASS,
    NEW,
    WHILE,

    ERROR,
    EOF
}
