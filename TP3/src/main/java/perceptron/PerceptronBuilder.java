package perceptron;

import utils.math.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class PerceptronBuilder {

    public static Function<Double, Double> STEP_ACTIVATION_FUNCTION =
            (Double a) -> a > 0 ? 1.0 : -1.0;

    public static Supplier<Double> RANDOM_WEIGHT_SUPPLIER = Math::random;

    private Integer dimension;
    private Vector weights;
    private Function<Double, Double> activationFunction;
    private Double bias;
    private Double learningRate;

    private Supplier<Double> weightSupplier;

    public void setWeightSupplier(Supplier<Double> weightSupplier) {
        this.weightSupplier = weightSupplier;
    }

    public PerceptronBuilder() {
    }

    public PerceptronBuilder setDimension(int dimension) {
        this.dimension = dimension;
        return this;
    }

    public PerceptronBuilder setWeights(Vector weights) {
        this.weights = weights;
        return this;
    }

    public PerceptronBuilder setActivationFunction(Function<Double, Double> activationFunction) {
        this.activationFunction = activationFunction;
        return this;
    }

    public PerceptronBuilder setBias(double bias) {
        this.bias = bias;
        return this;
    }

    public PerceptronBuilder setLearningRate(double learningRate) {
        this.learningRate = learningRate;
        return this;
    }

    public PerceptronInstance create(){
        if(dimension == null && weights == null)
            throw new IllegalArgumentException("Either dimension or weights should be specified");
        else if(dimension == null)
            dimension = weights.getDimension() - 1;

        if(weights == null)
            generateWeightsFromSupplier();

        activationFunction = Optional.ofNullable(activationFunction).orElse(STEP_ACTIVATION_FUNCTION);

        bias = Optional.ofNullable(bias).orElse(0.0);

        learningRate = Optional.ofNullable(learningRate).orElse(1.0);

        return new PerceptronInstance(dimension, weights, activationFunction, bias, learningRate);
    }

    private void generateWeightsFromSupplier(){
        weightSupplier = Optional.ofNullable(weightSupplier).orElse(RANDOM_WEIGHT_SUPPLIER);

        List<Double> values = new ArrayList<>(dimension);
        for(int i=0; i<dimension+1; i++){
            values.add(weightSupplier.get());
        }
        weights = new Vector(values);
    }
}
