package perceptron;

import utils.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PerceptronInstance {
    private final int dimension;
    private Vector weights;
    private final Function<Double, Double> activationFunction;
    private final double bias;
    private final double learningRate;

    public PerceptronInstance(int dimension, Vector weights, Function<Double, Double> activationFunction, double bias,
                              double learningRate) {
        this.dimension = dimension;
        this.weights = weights;
        this.activationFunction = activationFunction;
        this.bias = bias;
        this.learningRate = learningRate;
    }

    public boolean train(Vector input, double expectedOutput){
        if(input.getDimension() != this.dimension)
            throw new IllegalArgumentException("Input vector must be of the same dimension");

        double classifiedValue = classify(input);

        if(Double.compare(classifiedValue, expectedOutput) == 0)
            return false;

        Vector inputWithBias = addBias(input);

        List<Double> newValues = new ArrayList<>(this.dimension + 1);
        for(int i=0; i<this.dimension + 1; i++){
            double newValue = this.weights.getValues().get(i)
                    + this.learningRate * (expectedOutput - classifiedValue) * inputWithBias.getValues().get(i);
            newValues.add(newValue);
        }

        this.weights = new Vector(newValues);

        return true;
    }

    public double classify(Vector input){
        Vector inputWithBias = addBias(input);

        double dotProduct = Vector.dotProduct(inputWithBias, weights);
        return activationFunction.apply(dotProduct);
    }

    private Vector addBias(Vector a){
        List<Double> newValues = new ArrayList<>();
        newValues.add(bias);
        newValues.addAll(a.getValues());
        return new Vector(newValues);
    }

    public Vector getWeights() {
        return weights;
    }
}
