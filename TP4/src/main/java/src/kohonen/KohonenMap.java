package src.kohonen;

import src.kohonen.Vector;

import java.util.List;
import java.util.function.Supplier;

public class KohonenMap {

    @FunctionalInterface
    public interface NeighborhoodFunction {
        Double apply(Integer time, Double timeConstant);
    }

    @FunctionalInterface
    public interface LearningRateFunction {
        Double apply(Integer time, Double timeConstant);
    }


    public static class Builder {
        private Lattice lattice;
        private double timeConstant;
        private NeighborhoodFunction neighborhoodFunction;
        private LearningRateFunction learningRateFunction;

        public Builder(){}

        public Builder setLattice(Lattice lattice){
            this.lattice = lattice;
            return this;
        }

        public Builder setTimeConstant(double timeConstant){
            this.timeConstant = timeConstant;
            return this;
        }

        public Builder setNeighborhoodFunction(NeighborhoodFunction neighborhoodFunction){
            this.neighborhoodFunction = neighborhoodFunction;
            return this;
        }

        public Builder setLearningRateFunction(LearningRateFunction learningRateFunction){
            this.learningRateFunction = learningRateFunction;
            return this;
        }

        public KohonenMap create(){
            return new KohonenMap(lattice, timeConstant, neighborhoodFunction, learningRateFunction);
        }

        public static double neighborhoodFunction(int time, double timeConstant){
            double r0 = 3;
            return r0*Math.exp(-time/timeConstant);
        }

        public static double learningRateFunction(int time, double timeConstant){
            double a0 = 1;
            return a0*Math.exp(-time/timeConstant);
        }
    }

    private final Lattice lattice;
    private final double timeConstant;
    private final NeighborhoodFunction neighborhoodFunction;
    private final LearningRateFunction learningRateFunction;

    private int time = 0;

    public KohonenMap(Lattice lattice, double timeConstant, NeighborhoodFunction neighborhoodFunction,
                      LearningRateFunction learningRateFunction){
        this.lattice = lattice;
        this.timeConstant = timeConstant;
        this.neighborhoodFunction = neighborhoodFunction;
        this.learningRateFunction = learningRateFunction;
    }

    public void step(Vector input){
        Cell bmu = lattice.bestMatchingUnit(input);

        List<Cell> neighborhood = lattice.getNeighbors(bmu,
                neighborhoodFunction.apply(time, timeConstant) *
                learningRateFunction.apply(time, timeConstant)
        );

        for(Cell neighbor : neighborhood){
            updateWeights(neighbor, input);
        }

        time++;
    }

    public Lattice.Coord activate(Vector input){
         return lattice.bestMatchingUnitCoord(input);
    }

    private void updateWeights(Cell neighbor, Vector input){
        Vector factor = Vector.subtract(input, neighbor.getWeights());

        neighbor.sumWeights(Vector.scalarMultiplication(factor, learningRateFunction.apply(time, timeConstant)));
    }

}
