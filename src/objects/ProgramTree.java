package objects;

public class ProgramTree {
    public Node node;
    private ProgramTree up;
    public ProgramTree left;
    public ProgramTree right;

    private ProgramTree(ProgramTree left, ProgramTree right, ProgramTree up, Node node) {
        this.left = left;
        this.right = right;
        this.up = up;
        this.node = node;
    }

    public ProgramTree(String name) {
        node = Node.createClass(name);
        up = null;
        left = null;
        right = null;
    }

    public void setLeft(Node node) {
        left = new ProgramTree(null, null, this, node);
    }

    public void setRight(Node node) {
        right = new ProgramTree(null, null, this, node);
    }

    public ProgramTree findUpFunction(String name) {
        ProgramTree i = this;
        while (i != null && !(name.equals(i.node.name) && i.node.typeObject == TypeObject.FUNCTION)) {
            i = i.up;
        }
        return i;
    }

    public ProgramTree findUpVarOrArray(String name) {
        ProgramTree i = this;
        while (i != null && !(name.equals(i.node.name) && (i.node.typeObject == TypeObject.VAR || i.node.typeObject == TypeObject.ARRAY))) {
            i = i.up;
        }
        return i;
    }

    public void print(int n) {
        for (int i = 0; i < n; i++)
            System.out.print("\t");

        System.out.println(node);

        if (right != null)
            right.print(n + 1);

        if (left != null)
            left.print(n);
    }

    public ProgramTree findUpArray(String name) {
        ProgramTree i = this;
        while (i != null && !(name.equals(i.node.name) && i.node.typeObject == TypeObject.ARRAY)) {
            i = i.up;
        }
        return i;
    }

    public ProgramTree findUpVar(String name) {
        ProgramTree i = this;
        while (i != null && !(name.equals(i.node.name) && i.node.typeObject == TypeObject.VAR)) {
            i = i.up;
        }
        return i;
    }

    public ProgramTree findUp(String name) {
        ProgramTree i = this;
        while (i != null && !name.equals(i.node.name)) {
            i = i.up;
        }
        return i;
    }
}
