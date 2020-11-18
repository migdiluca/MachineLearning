package src.kohonen;

import src.kohonen.Vector;

public interface Cell {

    public Vector getWeights();

    public double weightDistance(Vector vec);

    public void sumWeights(Vector factor);

}
