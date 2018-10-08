package Diagram;

import parser.Scanner;
import parser.Token;
import parser.TokenType;

import java.util.ArrayDeque;
import java.util.Deque;

public class Automatic {

    private Deque<OneSymbol> symbols = new ArrayDeque<>();
    private Scanner scanner;

    public Automatic(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        Cell cell = new Cell();
        cell.oneSymbols = new OneSymbol[]{
                OneSymbol.createNeterminal(TypeNeterminal.S)
        };
        add(cell);

        while (!symbols.isEmpty()) {
            OneSymbol nextOneSymbol = read();
            if (nextOneSymbol.isTerminal()) {
                nextToken(nextOneSymbol.getTypeTerminal(), nextTokenRead() + " ожидался " + nextOneSymbol.getTypeTerminal());
                remove();
            } else {
                switch (nextOneSymbol.getTypeNeterminal()) {
                    case S:
                        progClass();
                        break;

                    case MNOGO_OPISANIA:
                        mnogoOpisania();
                        break;

                    case DATA:
                        data();
                        break;

                    case LIST:
                        list();
                        break;

                    case LIST_PLUS:
                        listPlus();
                        break;

                    case ASSIGN:
                        assign();
                        break;

                    case ARRAY_OR_NEW_ARRAY:
                        arrayOrNewArray();
                        break;

                    case FUNCTION:
                        function();
                        break;

                    case OPERATORS_AND_OPISANIA_IN_CURCLY_BRACE:
                        operatorsAndDateInCurlyBrace();
                        break;

                    case OPERATORS_AND_DATA:
                        operatorsAndData();
                        break;

                    case OPERATOR:
                        operator();
                        break;

                    case ASSIGN_FUNCTION_ELEMENT:
                        assignFunctionElement();
                        break;

                    case EXPRESSION_OR_NEW_ARRAY:
                        expressionOrNewArray();
                        break;

                    case ARRAY:
                        array();
                        break;

                    case EXPRESSION:
                        expression();
                        break;

                    case EXPRESSION_PLUS:
                        expressionPlus();
                        break;

                    case EXPRESSION2:
                        expression2();
                        break;

                    case EXPRESSION2_PLUS:
                        expression2Plus();
                        break;

                    case EXPRESSION3:
                        expression3();
                        break;

                    case EXPRESSION3_PLUS:
                        expression3Plus();
                        break;

                    case EXPRESSION4:
                        expression4();
                        break;

                    case EXPRESSION4_PLUS:
                        expression4Plus();
                        break;

                    case EXPRESSION5:
                        expression5();
                        break;

                    case EXPRESSION6:
                        expression6();
                        break;

                    case PEREMENNAY_ELEMENT:
                        peremenElem();
                        break;

                    default:
                        printError("Неизвестная конструкция", nextOneSymbol);
                }
            }
        }

        if (nextTokenRead().getType() != TokenType.EOF) {
            printError("Текст после программы");
        } else {
            System.out.println("Ошибок не обнаружено");
        }
    }

    private void peremenElem() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.OPEN_SQUARE)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.CLOSE_SQUARE),
                    OneSymbol.createTerminal(TokenType.TYPE_INT),
                    OneSymbol.createTerminal(TokenType.OPEN_SQUARE)
            };
        change(cell);
    }

    private void expression6() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.ID)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.PEREMENNAY_ELEMENT),
                    OneSymbol.createTerminal(TokenType.ID)
            };
        else if (nextToken.getType() == TokenType.TYPE_INT)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.TYPE_INT)
            };
        else if (nextToken.getType() == TokenType.TYPE_DOUBLE)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.TYPE_DOUBLE)
            };
        else if (nextToken.getType() == TokenType.TYPE_CHAR)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.TYPE_CHAR)
            };
        else if (nextToken.getType() == TokenType.TYPE_STRING)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.TYPE_STRING)
            };
        else if (nextToken.getType() == TokenType.OPEN_PARENTHESIS)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.CLOSE_PARENTHESIS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION),
                    OneSymbol.createTerminal(TokenType.OPEN_PARENTHESIS)
            };
        else {
            printError("Ожидалось выражение");
        }
        change(cell);
    }

    private void expression5() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.PLUS)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION5),
                    OneSymbol.createTerminal(TokenType.PLUS)
            };
        else if (nextToken.getType() == TokenType.MINUS)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION5),
                    OneSymbol.createTerminal(TokenType.MINUS)
            };
        else if (isExpression(nextToken.getType()))
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION6)
            };
        else {
            printError("Ожидалось выражение");
        }
        change(cell);
    }

    private void expression4Plus() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.STAR)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION4_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION5),
                    OneSymbol.createTerminal(TokenType.STAR)
            };
        else if (nextToken.getType() == TokenType.SLASH)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION4_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION5),
                    OneSymbol.createTerminal(TokenType.SLASH)
            };
        else if (nextToken.getType() == TokenType.PERCENT)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION4_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION5),
                    OneSymbol.createTerminal(TokenType.PERCENT)
            };
        change(cell);
    }

    private void expression4() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (isExpression(nextToken.getType()))
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION4_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION5)
            };
        else {
            printError("Ожидалось выражение");
        }
        change(cell);
    }

    private void expression3Plus() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.PLUS)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION3_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION4),
                    OneSymbol.createTerminal(TokenType.PLUS)
            };
        else if (nextToken.getType() == TokenType.MINUS)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION3_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION4),
                    OneSymbol.createTerminal(TokenType.MINUS)
            };
        change(cell);
    }

    private void expression3() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (isExpression(nextToken.getType()))
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION3_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION4)
            };
        else {
            printError("Ожидалось выражение");
        }
        change(cell);
    }

    private void expression2Plus() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.LESS)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION2_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION3),
                    OneSymbol.createTerminal(TokenType.LESS)
            };
        else if (nextToken.getType() == TokenType.LESS_EQUALLY)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION2_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION3),
                    OneSymbol.createTerminal(TokenType.LESS_EQUALLY)
            };
        else if (nextToken.getType() == TokenType.GREAT)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION2_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION3),
                    OneSymbol.createTerminal(TokenType.GREAT)
            };
        else if (nextToken.getType() == TokenType.GREAT_EQUALLY)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION2_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION3),
                    OneSymbol.createTerminal(TokenType.GREAT_EQUALLY)
            };
        change(cell);
    }

    private void expression2() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (isExpression(nextToken.getType()))
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION2_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION3)
            };
        else {
            printError("Ожидалось выражение");
        }
        change(cell);
    }

    private void expressionPlus() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.EQUALLY)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION2),
                    OneSymbol.createTerminal(TokenType.EQUALLY)
            };
        else if (nextToken.getType() == TokenType.NOT_EQUALLY)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION2),
                    OneSymbol.createTerminal(TokenType.NOT_EQUALLY)
            };
        change(cell);
    }

    private void expression() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (isExpression(nextToken.getType())) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION2)
            };
        } else {
            printError("Ожидалось выражение");
        }
        change(cell);
    }

    private boolean isExpression(TokenType tokenType) {
        return tokenType == TokenType.ID ||
                tokenType == TokenType.OPEN_PARENTHESIS ||
                tokenType == TokenType.TYPE_INT ||
                tokenType == TokenType.TYPE_DOUBLE ||
                tokenType == TokenType.TYPE_CHAR ||
                tokenType == TokenType.TYPE_STRING;
    }

    private void array() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.DOUBLE) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.CLOSE_SQUARE),
                    OneSymbol.createTerminal(TokenType.TYPE_INT),
                    OneSymbol.createTerminal(TokenType.OPEN_SQUARE),
                    OneSymbol.createTerminal(TokenType.DOUBLE)
            };
        } else if (nextToken.getType() == TokenType.CHAR) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.CLOSE_SQUARE),
                    OneSymbol.createTerminal(TokenType.TYPE_INT),
                    OneSymbol.createTerminal(TokenType.OPEN_SQUARE),
                    OneSymbol.createTerminal(TokenType.CHAR)
            };
        }  else {
            printError("Ожидалось описание массива");
        }
        change(cell);
    }

    private void expressionOrNewArray() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (isExpression(nextToken.getType())) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION)
            };
        } else if (nextToken.getType() == TokenType.NEW) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.ARRAY),
                    OneSymbol.createTerminal(TokenType.NEW)
            };
        } else {
            printError("Ожидалось выражение или объявление массива");
        }
        change(cell);
    }

    private void assignFunctionElement() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.ASSIGN) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION_OR_NEW_ARRAY),
                    OneSymbol.createTerminal(TokenType.ASSIGN)
            };
        } else if (nextToken.getType() == TokenType.OPEN_PARENTHESIS) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.CLOSE_PARENTHESIS),
                    OneSymbol.createTerminal(TokenType.OPEN_PARENTHESIS)
            };
        } else if (nextToken.getType() == TokenType.OPEN_SQUARE) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION),
                    OneSymbol.createTerminal(TokenType.ASSIGN),
                    OneSymbol.createTerminal(TokenType.CLOSE_SQUARE),
                    OneSymbol.createTerminal(TokenType.TYPE_INT),
                    OneSymbol.createTerminal(TokenType.OPEN_SQUARE)
            };
        }
        change(cell);
    }

    private void operator() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.SEMICOLON) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.SEMICOLON)
            };
        } else if (nextToken.getType() == TokenType.OPEN_CURLY_BRACE) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.OPERATORS_AND_OPISANIA_IN_CURCLY_BRACE),
            };
        } else if (nextToken.getType() == TokenType.WHILE) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.OPERATOR),
                    OneSymbol.createTerminal(TokenType.CLOSE_PARENTHESIS),
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION),
                    OneSymbol.createTerminal(TokenType.OPEN_PARENTHESIS),
                    OneSymbol.createTerminal(TokenType.WHILE),
            };
        } else if (nextToken.getType() == TokenType.ID) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.ASSIGN_FUNCTION_ELEMENT),
                    OneSymbol.createTerminal(TokenType.ID),
            };
        } else {
            printError("Неизвестный оператор");
        }
        change(cell);
    }

    private void operatorsAndData() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.SEMICOLON || nextToken.getType() == TokenType.OPEN_CURLY_BRACE || nextToken.getType() == TokenType.WHILE || nextToken.getType() == TokenType.ID) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.OPERATORS_AND_DATA),
                    OneSymbol.createNeterminal(TypeNeterminal.OPERATOR)
            };
        } else if (nextToken.getType() == TokenType.DOUBLE || nextToken.getType() == TokenType.CHAR) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.OPERATORS_AND_DATA),
                    OneSymbol.createNeterminal(TypeNeterminal.DATA)
            };
        }
        change(cell);
    }

    private void operatorsAndDateInCurlyBrace() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.OPEN_CURLY_BRACE) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.CLOSE_CURLY_BRACE),
                    OneSymbol.createNeterminal(TypeNeterminal.OPERATORS_AND_DATA),
                    OneSymbol.createTerminal(TokenType.OPEN_CURLY_BRACE)
            };
        } else {
            printError("Ожидалось описание в скобках");
        }

        change(cell);
    }

    private void function() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.PUBLIC) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.OPERATORS_AND_OPISANIA_IN_CURCLY_BRACE),
                    OneSymbol.createTerminal(TokenType.CLOSE_PARENTHESIS),
                    OneSymbol.createTerminal(TokenType.OPEN_PARENTHESIS),
                    OneSymbol.createTerminal(TokenType.MAIN),
                    OneSymbol.createTerminal(TokenType.VOID),
                    OneSymbol.createTerminal(TokenType.STATIC),
                    OneSymbol.createTerminal(TokenType.PUBLIC)
            };
        } else if (nextToken.getType() == TokenType.VOID) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.OPERATORS_AND_OPISANIA_IN_CURCLY_BRACE),
                    OneSymbol.createTerminal(TokenType.CLOSE_PARENTHESIS),
                    OneSymbol.createTerminal(TokenType.OPEN_PARENTHESIS),
                    OneSymbol.createTerminal(TokenType.ID),
                    OneSymbol.createTerminal(TokenType.VOID)
            };
        } else {
            printError("Ожидалось описание функции");
        }
        change(cell);
    }

    private void arrayOrNewArray() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.ASSIGN) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.ARRAY),
                    OneSymbol.createTerminal(TokenType.NEW),
                    OneSymbol.createTerminal(TokenType.ASSIGN)
            };
        }
        change(cell);
    }

    private void assign() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.ASSIGN) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.EXPRESSION),
                    OneSymbol.createTerminal(TokenType.ASSIGN)
            };
        } else if (nextToken.getType() == TokenType.OPEN_SQUARE) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.ARRAY_OR_NEW_ARRAY),
                    OneSymbol.createTerminal(TokenType.CLOSE_SQUARE),
                    OneSymbol.createTerminal(TokenType.OPEN_SQUARE)
            };
        }
        change(cell);
    }

    private void listPlus() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.COMMA)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.LIST),
                    OneSymbol.createTerminal(TokenType.COMMA)
            };
        change(cell);
    }

    private void list() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.ID) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.LIST_PLUS),
                    OneSymbol.createNeterminal(TypeNeterminal.ASSIGN),
                    OneSymbol.createTerminal(TokenType.ID)
            };
        } else {
            printError("Ожидался идентификатор");
        }
        change(cell);
    }

    private void data() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.CHAR)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.SEMICOLON),
                    OneSymbol.createNeterminal(TypeNeterminal.LIST),
                    OneSymbol.createTerminal(TokenType.CHAR)
            };
        else if (nextToken.getType() == TokenType.DOUBLE) {
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.SEMICOLON),
                    OneSymbol.createNeterminal(TypeNeterminal.LIST),
                    OneSymbol.createTerminal(TokenType.DOUBLE)
            };
        } else {
            printError("Ожидалось описание данных");
        }
        change(cell);
    }

    private void mnogoOpisania() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.CHAR || nextToken.getType() == TokenType.DOUBLE)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.MNOGO_OPISANIA),
                    OneSymbol.createNeterminal(TypeNeterminal.DATA),
            };
        else if (nextToken.getType() == TokenType.VOID || nextToken.getType() == TokenType.PUBLIC)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createNeterminal(TypeNeterminal.MNOGO_OPISANIA),
                    OneSymbol.createNeterminal(TypeNeterminal.FUNCTION),
            };
        change(cell);
    }

    private void progClass() {
        Token nextToken = nextTokenRead();
        Cell cell = new Cell();
        if (nextToken.getType() == TokenType.CLASS)
            cell.oneSymbols = new OneSymbol[]{
                    OneSymbol.createTerminal(TokenType.CLOSE_CURLY_BRACE),
                    OneSymbol.createNeterminal(TypeNeterminal.MNOGO_OPISANIA),
                    OneSymbol.createTerminal(TokenType.OPEN_CURLY_BRACE),
                    OneSymbol.createTerminal(TokenType.ID),
                    OneSymbol.createTerminal(TokenType.CLASS)
            };
        else
            printError("Ожидался класс");
        change(cell);
    }

    private Token nextToken(TokenType type, String text) {
        Token token = scanner.nextScanner();
        if (token.getType() != type)
            printError(text);
        return token;
    }
    private Token nextTokenRead() {
        scanner.save();
        Token token = scanner.nextScanner();
        scanner.ret();
        return token;
    }
    private void printError(String text) {
        System.out.println(text + " строка " + scanner.getNumberRow() + ", столбец " + scanner.getNumberCol());
        System.exit(1);
    }
    private void printError(String text, OneSymbol nextOneSymbol) {
        System.out.println(text + " " + nextOneSymbol);
        System.exit(1);
    }

    private void add(Cell cell) {
        for (OneSymbol oneSymbol: cell.oneSymbols)
            symbols.add(oneSymbol);
    }
    
    private void remove() {
        symbols.remove(symbols.getLast());
        System.out.println(this);
    }

    private void change(Cell cell) {
        symbols.remove(symbols.getLast());
        add(cell);
        System.out.println(this);
    }
    
    private OneSymbol read() {
        return symbols.getLast();
    }

    @Override
    public String toString() {
        if (symbols.isEmpty())
            return "Автомат пуст";
        else {
            String string = "";
            for (OneSymbol oneSymbol: symbols)
                string += oneSymbol.toString() + "    ";
            return string;
        }
    }
}
