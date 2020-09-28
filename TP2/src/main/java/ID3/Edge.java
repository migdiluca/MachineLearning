package ID3;

public class Edge<V> {
    private Node node;
    private V value;

    public Edge(Node node, V value) {
        this.node = node;
        this.value = value;
    }

    public Node getNode() {
        return node;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public V getAttributeName() {
        return value;
    }

    public void setAttributeName(V attributeName) {
        this.value = attributeName;
    }
}
