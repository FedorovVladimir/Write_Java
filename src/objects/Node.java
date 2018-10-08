package objects;

public class Node {
    String name;
    TypeObject typeObject;
    public TypeData typeData;

    public int n;
    public boolean isInit;

    private Node() {
        isInit = false;
    }

    public static Node createVar(String name, TypeData typeData) {
        Node node = new Node();
        node.typeObject = TypeObject.VAR;
        node.name = name;
        node.typeData = typeData;
        return node;
    }

    public static Node createArray(String name, TypeData typeData, int n) {
        Node node = new Node();
        node.typeObject = TypeObject.ARRAY;
        node.name = name;
        node.typeData = typeData;
        node.n = n;
        return node;
    }

    public static Node createFunction(String name) {
        Node node = new Node();
        node.typeObject = TypeObject.FUNCTION;
        node.name = name;
        node.typeData = TypeData.VOID;
        return node;
    }

    static Node createClass(String name) {
        Node node = new Node();
        node.typeObject = TypeObject.CLASS;
        node.name = name;
        node.typeData = TypeData.VOID;
        return node;
    }

    public static Node createEmptyNode() {
        Node node = new Node();
        node.typeObject = TypeObject.EMPTY;
        return node;
    }

    public static Node createConst(TypeData typeData) {
        Node node = new Node();
        node.typeObject = TypeObject.CONST;
        node.typeData = typeData;
        return node;
    }

    public static Node createUnknow() {
        Node node = new Node();
        node.typeObject = TypeObject.CONST;
        node.typeData = TypeData.UNKNOW;
        return node;
    }

    @Override
    public String toString() {
        if (typeObject == TypeObject.EMPTY)
            return typeObject.toString();

        String str = typeObject.toString();
        if (typeObject != TypeObject.CLASS)
            str += " " + typeData;
        str += " " + name;
        if (typeObject == TypeObject.ARRAY)
            str += " n=" + n;
        if (typeObject != TypeObject.CLASS && typeObject != TypeObject.FUNCTION)
            str += " init-" + isInit;
        return str;
    }
}
