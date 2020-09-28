package ID3;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Edge> edges;
    private String value;
    private String mostCommon;

    public Node(String value) {
        this.value = value;
        this.edges = new ArrayList<>();
    }

    public void addEdge(Object value, Node node) {
        edges.add(new Edge(node, value));
    }

    public String getMostCommon() {
        return mostCommon;
    }

    public void setMostCommon(String mostCommon) {
        this.mostCommon = mostCommon;
    }

    public void addEdge(String value, Node node) {
        edges.add(new Edge(node, value));
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
