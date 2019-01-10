package parser;

import parser.Token;
import parser.TokenType;

public class Scanner {

    private String text;
    private int numberRow;
    private int numberCol;
    private int numberSymbol;

    private int oldNumberRow;
    private int oldNumberCol;
    private int oldNumberSymbol;

    private String tokenMathSymbols = "+-/*%;(){},[]";
    private TokenType[] tokenMathMass = {TokenType.PLUS, TokenType.MINUS, TokenType.SLASH, TokenType.STAR, TokenType.PERCENT, TokenType.SEMICOLON, TokenType.OPEN_PARENTHESIS, TokenType.CLOSE_PARENTHESIS, TokenType.OPEN_CURLY_BRACE, TokenType.CLOSE_CURLY_BRACE, TokenType.COMMA, TokenType.OPEN_SQUARE, TokenType.CLOSE_SQUARE};
    private String tokenCompareSymbols = "<>=";
    private TokenType[] tokenCompareMass = {TokenType.LESS, TokenType.GREAT, TokenType.ASSIGN};
    private TokenType[] tokenCompareEquallyMass = {TokenType.LESS_EQUALLY, TokenType.GREAT_EQUALLY, TokenType.EQUALLY};

    public Scanner(String text) {
        this.text = text;
        numberRow = 1;
        numberCol = 1;
    }

    public Token nextScanner() {
        ignoringSymbols();

        if (isIdSymbol(text.charAt(numberSymbol)))
            return scannerID();
        if (isCompare(text.charAt(numberSymbol)))
            return scannerCompare();
        if (text.charAt(numberSymbol) == '!')
            return scannerNotEqually();
        if (text.charAt(numberSymbol) == '\'')
            return scannerChar();
        if (text.charAt(numberSymbol) == '\"')
            return scannerString();
        if (isDigit(text.charAt(numberSymbol)))
            return scannerNumber();
        if (isMathOrSpec(text.charAt(numberSymbol)))
            return scannerMathOrSpec();
        if (text.charAt(numberSymbol) == '\0')
            return scannerEof();
        else {
            char ch = text.charAt(numberSymbol);
            addNumberSymbol();
            return scannerError("Неизвестный символ \'" + ch + "\'");
        }
    }

    private void ignoringSymbols() {
        boolean canScanner = false;
        while (!canScanner) {
            canScanner = true;
            while (text.charAt(numberSymbol) == ' ' || text.charAt(numberSymbol) == '\t' || text.charAt(numberSymbol) == '\n' || text.charAt(numberSymbol) == '\r') {
                addNumberSymbol();
                canScanner = false;
            }
            while (text.charAt(numberSymbol) == '#') {
                while (text.charAt(numberSymbol) != '\n')
                    addNumberSymbol();
                addNumberSymbol();
                canScanner = false;
            }
        }
    }

    private Token scannerID() {
        String str = "" + text.charAt(numberSymbol);
        addNumberSymbol();
        while (isIdSymbol(text.charAt(numberSymbol)) || isDigit(text.charAt(numberSymbol))) {
            str += text.charAt(numberSymbol);
            addNumberSymbol();
        }
        if (str.equals("public"))
            return new Token(str, TokenType.PUBLIC);
        if (str.equals("static"))
            return new Token(str, TokenType.STATIC);
        if (str.equals("void"))
            return new Token(str, TokenType.VOID);
        if (str.equals("char"))
            return new Token(str, TokenType.CHAR);
        if (str.equals("double"))
            return new Token(str, TokenType.DOUBLE);
        if (str.equals("main"))
            return new Token(str, TokenType.MAIN);
        if (str.equals("class"))
            return new Token(str, TokenType.CLASS);
        if (str.equals("new"))
            return new Token(str, TokenType.NEW);
        if (str.equals("while"))
            return new Token(str, TokenType.WHILE);

        return  new Token(str, TokenType.ID);
    }

    private Token scannerCompare() {
        int index = tokenCompareSymbols.indexOf(text.charAt(numberSymbol));
        addNumberSymbol();
        if (text.charAt(numberSymbol) == '=') {
            addNumberSymbol();
            return new Token(tokenCompareEquallyMass[index]);
        } else
            return  new Token(tokenCompareMass[index]);
    }

    private Token scannerNotEqually() {
        addNumberSymbol();
        if (text.charAt(numberSymbol) == '=') {
            addNumberSymbol();
            return new Token(TokenType.NOT_EQUALLY);
        }
        else
            return scannerError("Ошибка считывания NOT_EQUALLY");
    }

    private Token scannerChar() {
        addNumberSymbol();
        if(text.charAt(numberSymbol + 1) == '\'') {
            numberSymbol += 2;
            return new Token(text.charAt(numberSymbol - 2) + "", TokenType.TYPE_CHAR);
        } else
            return scannerError("Ошибка считывания TYPE_CHAR");
    }

    private Token scannerString() {
        addNumberSymbol();
        String str = "";
        while (text.charAt(numberSymbol) != '\"' && text.charAt(numberSymbol) != '\n') {
            str += text.charAt(numberSymbol);
            addNumberSymbol();
        }
        if (text.charAt(numberSymbol) == '\"') {
            addNumberSymbol();
            return new Token(str, TokenType.TYPE_STRING);
        } else
            return scannerError("Ошибка считывания TYPE_STRING");
    }

    private Token scannerNumber() {
        String str = "" + text.charAt(numberSymbol);
        addNumberSymbol();
        while (isDigit(text.charAt(numberSymbol))) {
            str += text.charAt(numberSymbol);
            addNumberSymbol();
        }
        if (text.charAt(numberSymbol) == '.') {
            str += text.charAt(numberSymbol);
            addNumberSymbol();
            while (isDigit(text.charAt(numberSymbol))) {
                str += text.charAt(numberSymbol);
                addNumberSymbol();
            }
            try {
                Double.parseDouble(str);
                return  new Token(str, TokenType.TYPE_DOUBLE);
            } catch (NumberFormatException e) {
                return scannerError("Ошибка считывания TYPE_DOUBLE слишком длинный");
            }
        } else {
            try {
                Integer.parseInt(str);
                return new Token(str, TokenType.TYPE_INT);
            } catch (NumberFormatException e) {
                return scannerError("Ошибка считывания TYPE_INT слишком длинный");
            }
        }
    }

    private Token scannerMathOrSpec() {
        int index = tokenMathSymbols.indexOf(text.charAt(numberSymbol));
        addNumberSymbol();
        return new Token(tokenMathMass[index]);
    }

    private Token scannerEof() {
        return new Token(TokenType.EOF);
    }

    private Token scannerError(String text) {
        return new Token(text + " row " + numberRow + " col " + numberCol + " ", TokenType.ERROR);
    }

    private boolean isIdSymbol(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '$' || c == '_';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isCompare(char c) {
        return tokenCompareSymbols.indexOf(text.charAt(numberSymbol)) != -1;
    }

    private boolean isMathOrSpec(char c) {
        return tokenMathSymbols.indexOf(text.charAt(numberSymbol)) != -1;
    }

    private void addNumberSymbol() {
        numberCol++;
        if(text.charAt(numberSymbol) == '\n') {
            numberRow++;
            numberCol = 1;
        }
        numberSymbol++;
    }

    public int getNumberRow() {
        return numberRow;
    }

    public int getNumberCol() {
        return numberCol;
    }

    public void save() {
        oldNumberCol = numberCol;
        oldNumberRow = numberRow;
        oldNumberSymbol = numberSymbol;
    }

    public void ret() {
        numberCol = oldNumberCol;
        numberRow = oldNumberRow;
        numberSymbol = oldNumberSymbol;
    }
}
