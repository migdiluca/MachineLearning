package utils.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Hyperplane {
    private final Vector coefficients;

    public Hyperplane(Vector coefficients){
        this.coefficients = normalizeNormal(coefficients);
    }

    private Vector normalizeNormal(Vector coefficients){
        Vector normal = Vector.vectorFromIndexes(coefficients,
                IntStream.range(0, coefficients.getDimension() - 1).boxed().collect(Collectors.toList())
        );

        Vector normalizedSlope = normal.normalize();

        List<Double> values = new ArrayList<>(normalizedSlope.getValues());
        values.add(coefficients.getValues().get(coefficients.getValues().size()-1));
        return new Vector(values);
    }

    public Vector getCoefficients() {
        return coefficients;
    }

    public Hyperplane invertNormal(){
        return invertNormal(this);
    }

    private static Hyperplane invertNormal(Hyperplane hyperplane){
        List<Double> values = new ArrayList<>(hyperplane.coefficients.getValues());
        for(int i=0; i<values.size()-1; i++){
            values.set(i, values.get(i) * (-1));
        }
        return new Hyperplane(new Vector(values));
    }

    private static void checkPlaneAndPointDimensions(Hyperplane plane, Vector point){
        if(plane.coefficients.getDimension() - 1 != point.getDimension())
            throw new IllegalArgumentException(
                    String.format("Incompatible dimensions. Hyperplane dimension is %s and vector is %s.",
                            plane.coefficients.getDimension(), point.getDimension())
            );
    }

    private static void checkVectorDimension(Vector a, int dim){
        if(a.getDimension() != dim)
            throw new IllegalArgumentException(
                    String.format("Vectors should have dimension %s but found %s.",
                            a.getDimension(), dim)
            );
    }

    public double pointDistance(Vector point){
        return pointDistance(this, point);
    }

    public Hyperplane move(double distance){
        return move(this, distance);
    }

    public static Hyperplane move(Hyperplane hyperplane, double distance){
        List<Double> values = new ArrayList<>(hyperplane.coefficients.getValues());
        values.set(values.size()-1, values.get(values.size()-1) - distance);
        return new Hyperplane(new Vector(values));
    }

    public static Hyperplane ofLine(Vector a, Vector b){
        checkVectorDimension(a, 2);
        checkVectorDimension(b, 2);

        double x1 = a.getValues().get(0),
                y1 = a.getValues().get(1),
                x2 = b.getValues().get(0),
                y2 = b.getValues().get(1);

        if(x2 == x1)
            return new Hyperplane(new Vector(Arrays.asList(1.0, 0.0, -x1)));

        double xCoefficient = -(y2-y1)/(x2-x1);
        double bCoefficient = -(y1 - x1 * xCoefficient);
        return new Hyperplane(new Vector(Arrays.asList(xCoefficient, 1.0, bCoefficient)));
    }

    public static double pointDistance(Hyperplane plane, Vector point){
        checkPlaneAndPointDimensions(plane, point);

        Vector slope = Vector.vectorFromIndexes(plane.coefficients,
                IntStream.range(0, plane.coefficients.getDimension() - 1).boxed().collect(Collectors.toList())
        );

        return (
                Vector.dotProduct(slope, point)
                + plane.coefficients.getValues().get(plane.coefficients.getDimension() - 1)
        ) / slope.norm();
    }
}
