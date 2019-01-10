package Diagram;

import objects.Node;
import objects.ProgramTree;
import objects.TypeData;
import parser.Scanner;
import parser.Token;
import parser.TokenType;

public class Diagram {

    private ProgramTree tree;
    private ProgramTree thisTree;
    private ProgramTree saveTree;

    private Scanner scanner;

    public Diagram(Scanner scanner) {
        this.scanner = scanner;
    }

    public void program() {
        nextToken(TokenType.CLASS, "Ожидался класс");
        Token tokenClass = nextToken(TokenType.ID, "Ожидался идентификатор");
        addClass(tokenClass);
        nextToken(TokenType.OPEN_CURLY_BRACE, "Ожидался символ {");
        Token token = nextTokenRead();
        while (token.getType() != TokenType.CLOSE_CURLY_BRACE && token.getType() != TokenType.EOF) {
            if (isFunction(token))
                function();
            else if (isDate(token))
                date();
            else
                printError("Неизвестный символ");
            token = nextTokenRead();
        }
        nextToken(TokenType.CLOSE_CURLY_BRACE, "Ожидался символ }");
    }
    private void addClass(Token token) {
        tree = thisTree = new ProgramTree(token.getText());
        thisTree.setRight(Node.createEmptyNode());
        thisTree = thisTree.right;
    }


    private boolean isFunction(Token token) {
        return token.getType() == TokenType.VOID || token.getType() == TokenType.PUBLIC;
    }
    private void function() {
        Token token = scanner.nextScanner();

        Token tokenFunction;
        if (token.getType() == TokenType.VOID) {
            tokenFunction = nextToken(TokenType.ID, "Ожидался идентификатор");
        } else {
            nextToken(TokenType.STATIC, "Ожидался static");
            nextToken(TokenType.VOID, "Ожидался void");
            tokenFunction = nextToken(TokenType.MAIN, "Ожидался main");
        }
        addFunction(tokenFunction);
        
        nextToken(TokenType.OPEN_PARENTHESIS, "Ожидался символ (");
        nextToken(TokenType.CLOSE_PARENTHESIS, "Ожидался символ )");

        token = nextTokenRead();
        if (isOperatorsAndDate(token))
            operatorsAndDate();
        else
            printError("Ожидался символ {");
    }
    private void addFunction(Token token) {
        if (thisTree.findUpFunction(token.getText()) != null)
            printSemError("Функция '" + token.getText() + "' уже была объявлена");
        thisTree.setLeft(Node.createFunction(token.getText()));
        thisTree = thisTree.left;
    }



    private boolean isOperatorsAndDate(Token token) {
        return token.getType() == TokenType.OPEN_CURLY_BRACE;
    }
    private void operatorsAndDate() {
        nextToken(TokenType.OPEN_CURLY_BRACE, "Ожидался символ {");

        in();
        Token token = nextTokenRead();
        while (token.getType() != TokenType.CLOSE_CURLY_BRACE && token.getType() != TokenType.EOF) {
            if (isOperator(token))
                operator();
            else if (isDate(token))
                date();
            else
                printError("Неизвестный символ");
            token = nextTokenRead();
        }

        out();
        nextToken(TokenType.CLOSE_CURLY_BRACE, "Ожидался символ }");
    }
    private void in() {
        saveTree = thisTree;
        thisTree.setRight(Node.createEmptyNode());
        thisTree = thisTree.right;
    }
    private void out() {
        thisTree = saveTree;
    }



    private boolean isDate(Token token) {
        return token.getType() == TokenType.DOUBLE || token.getType() == TokenType.CHAR;
    }
    private void date() {
        Token token = scanner.nextScanner();

        TypeData typeData;
        if (token.getType() == TokenType.CHAR) {
            typeData = TypeData.CHAR;
        } else  {
            typeData = TypeData.DOUBLE;
        }

        token = nextTokenRead();
        if (isVeriable(token))
            veriable(typeData);
        else
            printError("Ожидался идентификатор");

        token = nextTokenRead();
        while (token.getType() == TokenType.COMMA) {
            scanner.nextScanner();
            token = nextTokenRead();
            if (isVeriable(token))
                veriable(typeData);
            else
                printError("Ожидался идентификатор");
            token = nextTokenRead();
        }

        nextToken(TokenType.SEMICOLON, "Ожидался символ ;");
    }



    private boolean isVeriable(Token token) {
        return token.getType() == TokenType.ID;
    }
    private void veriable(TypeData typeData) {
        Token varName = nextToken(TokenType.ID, "Ожидался идентификатрор");

        boolean init = false;

        Token token = nextTokenRead();
        if (token.getType() == TokenType.ASSIGN) {
            nextToken(TokenType.ASSIGN, "Ожидался символ =");
            token = nextTokenRead();
            if (isExpression(token))
                expression();
            else
                printError("Ожидалось выражение");
            init = true;
        } else if (token.getType() == TokenType.OPEN_SQUARE) {
            nextToken(TokenType.OPEN_SQUARE, "Ожидался символ [");
            nextToken(TokenType.CLOSE_SQUARE, "Ожидался символ ]");
            if (thisTree.findUpVarOrArray(varName.getText()) != null)
                printSemError("Идентификатор " + varName.getText() + " уже использовался");
            if (nextTokenRead().getType() == TokenType.ASSIGN) {
                nextToken(TokenType.ASSIGN, "Ожидался символ =");
                nextToken(TokenType.NEW, "Ожидался символ new");
                token = scanner.nextScanner();

                TypeData typeDataMass;
                if (token.getType() != TokenType.CHAR && token.getType() != TokenType.DOUBLE)
                    printError("Ожидался тип");
                if (token.getType() == TokenType.DOUBLE)
                    typeDataMass = TypeData.DOUBLE;
                else
                    typeDataMass = TypeData.CHAR;

                nextToken(TokenType.OPEN_SQUARE, "Ожидался символ [");
                Token tokenN = nextToken(TokenType.TYPE_INT, "Ожидалось целое");
                if (thisTree.findUpArray(varName.getText()) != null) {
                    Node mass = thisTree.findUpArray(varName.getText()).node;
                    mass.n = Integer.parseInt(tokenN.getText());
                    mass.isInit = true;
                }
                nextToken(TokenType.CLOSE_SQUARE, "Ожидался символ ]");
                if (typeData == typeDataMass)
                    addArray(typeData, true, varName, Integer.parseInt(tokenN.getText()));
                else
                    printSemError("Не верный тип массива");
            } else {
                addArray(typeData, false, varName, 0);
            }
            return;
        }
        addVar(typeData, init, varName);
    }
    private void addArray(TypeData typeData, boolean init, Token name, int n) {
        if (thisTree.findUpArray(name.getText()) != null) {
            printSemError("Массив " + name.getText() + " уже существует");
        } else {
            thisTree.setLeft(Node.createArray(name.getText(), typeData, n));
            thisTree.left.node.isInit = init;
            thisTree = thisTree.left;
        }
    }
    private void addVar(TypeData typeData, boolean init, Token name) {
        if (thisTree.findUpVar(name.getText()) != null) {
            printSemError("Переменная " + name.getText() + " уже существует");
        } else {
            thisTree.setLeft(Node.createVar(name.getText(), typeData));
            thisTree.left.node.isInit = init;
            thisTree = thisTree.left;
        }
    }



    private boolean isOperator(Token token) {
        return token.getType() == TokenType.SEMICOLON ||
                isOperatorsAndDate(token) ||
                isLoopWhile(token) ||
                token.getType() == TokenType.ID;
    }
    private void operator() {
        Token token = nextTokenRead();

        if (token.getType() == TokenType.SEMICOLON)
            nextToken(TokenType.SEMICOLON, "Ожидался символ ;");
        else if (isOperatorsAndDate(token)) {
            saveTree = thisTree;
            thisTree.setLeft(Node.createEmptyNode());
            thisTree = thisTree.left;
            operatorsAndDate();
        }
        else if (isLoopWhile(token))
            loopWhile();
        else if (token.getType() == TokenType.ID) {
            Token tokenName = nextToken(TokenType.ID, "Ожидался идентификатор");

            token = nextTokenRead();
            if (isAssignment(token)) {
                Node node = assignment(tokenName);  
            }
            else if (token.getType() == TokenType.OPEN_PARENTHESIS) {
                nextToken(TokenType.OPEN_PARENTHESIS, "Ожидался символ (");
                if (thisTree.findUpFunction(tokenName.getText()) == null)
                    printSemError("Функция '" + tokenName.getText() + "()' не определена");
                nextToken(TokenType.CLOSE_PARENTHESIS, "Ожидался символ )");
                nextToken(TokenType.SEMICOLON, "Ожидался символ ;");
            } else if (token.getType() == TokenType.OPEN_SQUARE) {
                nextToken(TokenType.OPEN_SQUARE, "Ожидался символ [");

                Token indexArray = nextToken(TokenType.TYPE_INT, "Ожидалось целое");

                if (thisTree.findUpArray(tokenName.getText()) != null) {
                    Node mass = thisTree.findUpArray(tokenName.getText()).node;
                    if (mass.n <= Integer.parseInt(indexArray.getText()))
                        printSemError("Выход за границу массива");

                }
                nextToken(TokenType.CLOSE_SQUARE, "Ожидался символ ]");

                token = nextTokenRead();
                if (isAssignment(token))
                    assignment(tokenName);
            } else {
                printError("Неизвестная команда");
            }
        } else
            printError("Неизвестный оператор");

    }



    private boolean isAssignment(Token token) {
        return token.getType() == TokenType.ASSIGN;
    }
    private Node assignment(Token tokenName) {
        nextToken(TokenType.ASSIGN, "Ожидался символ =");
        Token token = nextTokenRead();
        if (isExpression(token)) {
            Node node = expression();
            if(thisTree.findUpVarOrArray(tokenName.getText()) == null)
                printSemError("Переменная или массив '" + tokenName.getText() + "' не найдена");
            else {
                if (inType(thisTree.findUpVarOrArray(tokenName.getText()).node.typeData, node.typeData)) {
                    Node mass = thisTree.findUpVarOrArray(tokenName.getText()).node;
                    mass.isInit = true;
                } else {
                    printSemError("Не верный тип");
                }
            }
            return node;
        } else  {
            nextToken(TokenType.NEW, "Ожидался new");
            token = scanner.nextScanner();
            TokenType typeMass =  token.getType();
            if (typeMass != TokenType.DOUBLE && typeMass != TokenType.CHAR)
                printError("Ожидался тип");
            nextToken(TokenType.OPEN_SQUARE, "Ожидался символ [");
            Token tokenN = nextToken(TokenType.TYPE_INT, "Ожидалось целое");

            if(thisTree.findUpArray(tokenName.getText()) == null)
                printSemError("Массив '" + tokenName.getText() + "' не найден");
            else {
                TypeData typeDataMass;
                if (typeMass == TokenType.CHAR)
                    typeDataMass = TypeData.CHAR;
                else
                    typeDataMass = TypeData.DOUBLE;
                if (thisTree.findUpArray(tokenName.getText()).node.typeData  == typeDataMass) {
                    Node mass = thisTree.findUpVarOrArray(tokenName.getText()).node;
                    mass.isInit = true;
                    mass.n = Integer.parseInt(tokenN.getText());
                } else {
                    printSemError("Не верный тип массива");
                }
            }
            nextToken(TokenType.CLOSE_SQUARE, "Ожидался символ ]");
            return Node.createArray("", TypeData.DOUBLE, 3);
        }
    }
    private boolean inType(TypeData typeData1, TypeData typeData2) {
        if (typeData1 == TypeData.DOUBLE && isNumber(typeData2))
            return true;
        if (typeData1 == TypeData.INTEGER && isNumber(typeData2) && typeData2 != TypeData.DOUBLE)
            return true;
        if (typeData1 == TypeData.CHAR && typeData2 == TypeData.CHAR)
            return true;
        return false;
    }



    private boolean isLoopWhile(Token token) {
        return token.getType() == TokenType.WHILE;
    }
    private void loopWhile() {
        nextToken(TokenType.WHILE, "Ожидался while");
        nextToken(TokenType.OPEN_PARENTHESIS, "Ожидался символ (");
        Token token = nextTokenRead();
        if (isExpression(token)) {
            Node node = expression();
            if (node.typeData != TypeData.CHAR)
                printSemError("Ожидался тип char");
        }
        else
            printError("Ожидалось выражение");
        nextToken(TokenType.CLOSE_PARENTHESIS, "Ожидался символ )");
        token = nextTokenRead();
        if (isOperator(token))
            operator();
        else
            printError("Ожидался оператор");
    }



    private boolean isExpression(Token token) {
        return isExpression2(token);
    }
    private Node expression() {
        Node node = expression2();
        TypeData typeData = node.typeData;

        Token token = nextTokenRead();
        while (token.getType() == TokenType.EQUALLY || token.getType() == TokenType.NOT_EQUALLY) {
            scanner.nextScanner();

            Node node2 = expression2();
            TypeData typeData2 = node2.typeData;
            if (isNumber(typeData) && isNumber(typeData2)) {
                node.typeData = TypeData.CHAR;
            } else {
                node.typeData = TypeData.UNKNOW;
                printSemError("Неопределенный тип");
            }

            token = nextTokenRead();
        }
        return node;
    }



    private boolean isExpression2(Token token) {
        return isExpression3(token);
    }
    private Node expression2() {
        Node node = expression3();
        TypeData typeData = node.typeData;

        Token token = nextTokenRead();
        while (token.getType() == TokenType.GREAT || token.getType() == TokenType.GREAT_EQUALLY || token.getType() == TokenType.LESS || token.getType() == TokenType.LESS_EQUALLY) {
            scanner.nextScanner();

            Node node2 = expression3();
            TypeData typeData2 = node2.typeData;
            if (isNumber(typeData) && isNumber(typeData2)) {
                node.typeData = TypeData.CHAR;
            } else {
                node.typeData = TypeData.UNKNOW;
                printSemError("Неопределенный тип");
            }

            token = nextTokenRead();
        }
        return node;
    }



    private boolean isExpression3(Token token) {
        return isExpression4(token);
    }
    private Node expression3() {
        Node node = expression4();
        TypeData typeData = node.typeData;


        Token token = nextTokenRead();
        while (token.getType() == TokenType.PLUS || token.getType() == TokenType.MINUS) {
            scanner.nextScanner();
            Node node2 = expression4();
            TypeData typeData2 = node2.typeData;

            node.typeData = toTypeDataPlusMinus(typeData, typeData2);
            if (typeData == TypeData.UNKNOW)
                printSemError("Неопределенный тип");

            token = nextTokenRead();
        }
        return node;
    }
    private TypeData toTypeDataPlusMinus(TypeData typeData1, TypeData typeData2) {
        if (typeData1 == TypeData.STRING || typeData2 == TypeData.STRING) {
            if (typeData1 != TypeData.UNKNOW && typeData2 != TypeData.UNKNOW) {
                return TypeData.STRING;
            } else {
                return TypeData.UNKNOW;
            }
        } else
            return toTypeDataSlashStar(typeData1, typeData2);
    }



    private boolean isExpression4(Token token) {
        return isExpression5(token);
    }
    private Node expression4() {
        Node node = expression5();

        Token token = nextTokenRead();
        while (token.getType() == TokenType.STAR || token.getType() == TokenType.SLASH || token.getType() == TokenType.PERCENT) {
            scanner.nextScanner();
            Node node2 = expression5();
            TypeData typeData2 = node2.typeData;

            if (token.getType() == TokenType.PERCENT)
                node.typeData = toTypeDataPercent(node.typeData, typeData2);
            else
                node.typeData = toTypeDataSlashStar(node.typeData, typeData2);

            if (node.typeData == TypeData.UNKNOW) {
                printSemError("Неопределенный тип");
            }
            token = nextTokenRead();
        }
        return node;
    }
    private TypeData toTypeDataSlashStar(TypeData typeData1, TypeData typeData2) {
        if (typeData1 == TypeData.DOUBLE || typeData2 == TypeData.DOUBLE) {
            if (isNumber(typeData1) && isNumber(typeData2)) {
                return TypeData.DOUBLE;
            } else {
                return TypeData.UNKNOW;
            }
        } else if (typeData1 == TypeData.INTEGER || typeData2 == TypeData.INTEGER) {
            if (isNumber(typeData1) && isNumber(typeData2)) {
                return TypeData.INTEGER;
            } else {
                return TypeData.UNKNOW;
            }
        } else if (typeData1 == TypeData.CHAR || typeData2 == TypeData.CHAR) {
            if (isNumber(typeData1) && isNumber(typeData2)) {
                return TypeData.CHAR;
            } else {
                return TypeData.UNKNOW;
            }
        } else
            return TypeData.UNKNOW;
    }
    private TypeData toTypeDataPercent(TypeData typeData1, TypeData typeData2) {
        if (isNumber(typeData1) && typeData2 == TypeData.INTEGER) {
            return TypeData.INTEGER;
        } else
            return TypeData.UNKNOW;
    }
    private Boolean isNumber(TypeData typeData) {
        return typeData == TypeData.DOUBLE ||
                typeData == TypeData.INTEGER ||
                typeData == TypeData.CHAR;
    }


    private boolean isExpression5(Token token) {
        return isExpression6(token) ||
                token.getType() == TokenType.PLUS ||
                token.getType() == TokenType.MINUS;
    }
    private Node expression5() {
        Token token = nextTokenRead();

        boolean isZnak = false;
        while (token.getType() == TokenType.PLUS || token.getType() == TokenType.MINUS) {
            scanner.nextScanner();

            token = nextTokenRead();
            isZnak = true;
        }

        Node node = expression6();
        if (isZnak) {
            if (node.typeData != TypeData.DOUBLE && node.typeData != TypeData.INTEGER) {
                printSemError("Неопределенный тип");
                return Node.createUnknow();
            } else
                return node;
        } else {
            return node;
        }
    }



    private boolean isExpression6(Token token) {
        return token.getType() == TokenType.ID ||
                token.getType() == TokenType.OPEN_PARENTHESIS ||
                token.getType() == TokenType.TYPE_INT ||
                token.getType() == TokenType.TYPE_DOUBLE ||
                token.getType() == TokenType.TYPE_CHAR ||
                token.getType() == TokenType.TYPE_STRING;
    }
    private Node expression6() {
        Token token = scanner.nextScanner();

        if (token.getType() == TokenType.ID) {
            Token tokenName = token;
            token = nextTokenRead();

            if (token.getType() == TokenType.OPEN_SQUARE) {
                nextToken(TokenType.OPEN_SQUARE, "Ожидался символ [");
                nextToken(TokenType.TYPE_INT, "Ожидалось целое");
                nextToken(TokenType.CLOSE_SQUARE, "Ожидался символ ]");
                if (thisTree.findUpVarOrArray(tokenName.getText()) != null)
                    return Node.createConst(thisTree.findUpVarOrArray(tokenName.getText()).node.typeData);
                else {
                    printSemError("Неизвестная переменная");
                    return Node.createUnknow();
                }
            } //else {
                //                nextToken(TokenType.OPEN_PARENTHESIS, "Ожидался символ (");
//                nextToken(TokenType.CLOSE_PARENTHESIS, "Ожидался символ )");
//                if (thisTree.findUp(tokenName.getText()) != null)
//                    return Node.createFunction(tokenName.getText());
//                else
//                    return Node.createUnknow();
//
//            }
            else {
                if (thisTree.findUpVarOrArray(tokenName.getText()) != null)
                    return Node.createConst(thisTree.findUpVarOrArray(tokenName.getText()).node.typeData);
                else {
                    printSemError("Неизвестная переменная");
                    return Node.createUnknow();
                }
            }
        } else if (token.getType() == TokenType.OPEN_PARENTHESIS) {

            token = nextTokenRead();
            Node node = null;
            if (isExpression(token))
                node = expression();
            else
                printError("Ожидалось выражение");
            nextToken(TokenType.CLOSE_PARENTHESIS, "Ожидался символ )");
            return node;
        } else if (token.getType() == TokenType.TYPE_INT) {
            return Node.createConst(TypeData.INTEGER);
        } else if (token.getType() == TokenType.TYPE_DOUBLE) {
            return Node.createConst(TypeData.DOUBLE);
        } else if (token.getType() == TokenType.TYPE_STRING) {
            return Node.createConst(TypeData.STRING);
        } else {
            return Node.createConst(TypeData.CHAR);
        }
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



    public void printError(String text) {
        System.out.println(text + " строка " + scanner.getNumberRow() + ", столбец " + scanner.getNumberCol());
        System.exit(1);
    }

    private void printSemError(String text) {
        System.out.println(text + " строка " + scanner.getNumberRow() + ", столбец " + scanner.getNumberCol());
    }

    public ProgramTree getTree() {
        return tree;
    }
}
