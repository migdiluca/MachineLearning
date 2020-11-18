package src.kohonen;

import java.util.Arrays;
import java.util.List;

public class Vector {
    private final double[] elements;

    public Vector(double[] elements){
        this.elements = elements;
    }

    public Vector(List<Double> elements){
        double[] mapped = new double[elements.size()];

        for(int i=0; i<elements.size(); i++){
            mapped[i] = elements.get(i);
        }

        this.elements = mapped;
    }

    public Vector(int size) {
        this.elements = new double[size];
    }

    public static Vector ones(int dim){
        double[] elements = new double[dim];
        Arrays.fill(elements, 1);

        return new Vector(elements);
    }

    public static Vector zero(int dim){
        double[] elements = new double[dim];
        Arrays.fill(elements, 0);

        return new Vector(elements);
    }

    public void fill(double value) {
        Arrays.fill(this.elements, value);
    }

    public void dotInstance(double value) {
        for(int i = 0; i < this.elements.length; i++){
            this.elements[i] = this.elements[i] * value;
        }
    }

    public double getModule() {
        double sum = 0;
        for(int i = 0; i < this.elements.length; i++){
            sum += this.elements[i] * this.elements[i];
        }
        return Math.sqrt(sum);
    }

    public double get(int index){
        return elements[index];
    }

    public void set(int index, double value){
        elements[index] = value;
    }

    public int getDimension(){
        return elements.length;
    }

    public static double dot(Vector v1, Vector v2) {
        if(v1.getDimension() != v2.getDimension()) {
            throw new IllegalArgumentException("The two vectors have different dimensions");
        }

        double result = 0;
        for(int i = 0; i < v1.elements.length; i++){
            result += (v1.elements[i] * v2.elements[i]);
        }

        return result;
    }

    public static Vector scalarMultiplication(Vector vec, double scalar){
        double[] newValues = new double[vec.elements.length];
        for(int i = 0; i < vec.elements.length; i++){
            newValues[i] = vec.elements[i] * scalar;
        }
        return new Vector(newValues);
    }

    public void sumInstance(Vector v) {
        if(this.getDimension() != v.getDimension()) {
            throw new IllegalArgumentException("The two vectors have different dimensions");
        }

        for(int i = 0; i < v.elements.length; i++){
            this.elements[i] = this.elements[i] + v.elements[i];
        }
    }

    public static Vector sum(Vector v1, Vector v2){
        if(v1.getDimension() != v2.getDimension()) {
            throw new IllegalArgumentException("The two vectors have different dimensions");
        }

        double[] newValues = new double[v1.elements.length];
        for(int i = 0; i < v1.elements.length; i++){
            newValues[i] = v1.elements[i] + v2.elements[i];
        }

        return new Vector(newValues);
    }

    public static Vector subtract(Vector v1, Vector v2){
        return sum(v1, scalarMultiplication(v2, -1));
    }

    public static double distance(Vector v1, Vector v2){
        if(v1.getDimension() != v2.getDimension()) {
            throw new IllegalArgumentException("The two vectors have different dimensions");
        }

        double aux = 0;
        for(int i=0; i<v1.elements.length; i++){
            aux += Math.pow(v1.elements[i] - v2.elements[i], 2);
        }

        return Math.sqrt(aux);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        for(int i = 0; i < elements.length; i++) {
            if (i != 0)
                sb.append(", ");
            sb.append(elements[i]);
        }
        sb.append(']');
        return sb.toString();
    }
}
