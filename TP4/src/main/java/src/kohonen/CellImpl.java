package src.kohonen;

import src.kohonen.Vector;

public class CellImpl implements Cell {
    private Vector weights;

    public CellImpl(Vector weights){
        this.weights = weights;
    }

    @Override
    public Vector getWeights() {
        return weights;
    }

    @Override
    public double weightDistance(Vector vec) {
        return Vector.distance(weights, vec);
    }

    @Override
    public void sumWeights(Vector factor) {
        weights = Vector.sum(weights, factor);
    }
}
