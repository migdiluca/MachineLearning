package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vector {
    private final int dimension;
    private final List<Double> values;

    public Vector(List<Double> values){
        this.values = values;
        this.dimension = values.size();
    }

    public int getDimension() {
        return dimension;
    }

    public List<Double> getValues() {
        return values;
    }

    public double norm(){
        return Vector.norm(this);
    }

    public Vector sum(Vector a){
        return Vector.sum(this, a);
    }

    public Vector subtract(Vector a){
        return Vector.subtract(this, a);
    }

    public Vector scalarMultiplication(double scalar){
        return Vector.scalarMultiplication(this, scalar);
    }

    public double distance(Vector a){
        return Vector.distance(this, a);
    }

    public static double distance(Vector a, Vector b){
        return Vector.norm(Vector.subtract(a, b));
    }

    private static void checkDimensions(Vector a, Vector b){
        if(a.dimension != b.dimension)
            throw new IllegalArgumentException("Vectors should be of the same dimension");
    }

    public static Vector subtract(Vector a, Vector b){
        checkDimensions(a, b);

        List<Double> newValues = new ArrayList<>();
        for(int i = 0; i < a.values.size(); i++){
            newValues.add(a.values.get(i) - b.values.get(i));
        }

        return new Vector(newValues);
    }

    public static Vector scalarMultiplication(Vector a, double scalar){
        List<Double> newValues = new ArrayList<>();

        for(Double val : a.values){
            newValues.add(scalar * val);
        }

        return new Vector(newValues);
    }

    public static Vector sum(Vector a, Vector b){
        checkDimensions(a, b);

        List<Double> newValues = new ArrayList<>();
        for(int i = 0; i < a.values.size(); i++){
            newValues.add(a.values.get(i) + a.values.get(i));
        }

        return new Vector(newValues);
    }

    public static double norm(Vector v){
        double sum = 0.0;

        for(Double val: v.values){
            sum += Math.pow(val, 2);
        }

        return Math.sqrt(sum);
    }

    public static Vector ofZero(int dim){
        List<Double> list = new ArrayList<>(Collections.nCopies(dim, 0.0));
        return new Vector(list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vector vector = (Vector) o;

        if (dimension != vector.dimension) return false;
        return values.equals(vector.values);
    }

    @Override
    public int hashCode() {
        int result = dimension;
        result = 31 * result + values.hashCode();
        return result;
    }
}
