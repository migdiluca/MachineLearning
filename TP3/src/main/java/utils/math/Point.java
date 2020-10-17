package utils.math;

public class Point{
    private final Vector vector;
    private final int category;

    public Point(Vector vector, int category) {
        this.vector = vector;
        this.category = category;
    }

    public Point(Vector vector, double category) {
        this(vector, (int) category);
    }

    public Point(Vector vector, String category) {
        this(vector, Double.parseDouble(category));
    }

    public Vector getVector() {
        return vector;
    }

    public int getCategory() {
        return category;
    }
}
