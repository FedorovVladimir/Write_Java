package Diagram;

import parser.TokenType;

public class OneSymbol {

    private boolean isTerminal;
    private TokenType typeTerminal;
    private TypeNeterminal typeNeterminal;

    private OneSymbol(boolean isTerminal, TokenType typeTerminal, TypeNeterminal typeNeterminal) {
        this.isTerminal = isTerminal;

        this.typeTerminal = typeTerminal;
        this.typeNeterminal = typeNeterminal;
    }

    boolean isTerminal() {
        return isTerminal;
    }

    TokenType getTypeTerminal() {
        return typeTerminal;
    }

    TypeNeterminal getTypeNeterminal() {
        return typeNeterminal;
    }

    static OneSymbol createTerminal(TokenType typeTerminal) {
        return new OneSymbol(true, typeTerminal, TypeNeterminal.ARRAY);
    }

    static OneSymbol createNeterminal(TypeNeterminal typeNeterminal) {
        return new OneSymbol(false, TokenType.ID, typeNeterminal);
    }

    @Override
    public String toString() {
        if (isTerminal)
            return typeTerminal.toString();
        else
            return typeNeterminal.toString();
    }
}
