package structure;

public class Powierzchnia {
    private final Node[] node;

    public Powierzchnia(Node node1, Node node2) {

        node = new Node[2];
        this.node[0] = node1;
        this.node[1] = node2;
    }

    public Node[] getNodes() {
        return node;
    }
}
