package perceptron;

import utils.math.Hyperplane;
import utils.math.Point;
import utils.math.Vector;

public class SeparationHyperplane {
    public static class CouldNotClassify extends Exception {
        public CouldNotClassify() {
            super();
        }
    }

    private final Hyperplane hyperplane;

    public SeparationHyperplane(Hyperplane hyperplane, Point point){
        this.hyperplane = checkIfShouldInvert(hyperplane, point);
    }

    public SeparationHyperplane(Hyperplane hyperplane) {
        this.hyperplane = hyperplane;
    }

    public Hyperplane getHyperplane() {
        return hyperplane;
    }

    public int classify(Vector v) throws CouldNotClassify{
        double pointDistance = hyperplane.pointDistance(v);

        if(Double.compare(hyperplane.pointDistance(v), 0) == 0)
            throw new CouldNotClassify();

        return pointDistance > 0 ? 1 : -1;
    }

    public boolean verifyClassification(Point p){
        try{
            return classify(p.getVector()) == p.getCategory();
        }catch (CouldNotClassify c){
            return true;
        }
    }

    private static Hyperplane checkIfShouldInvert(Hyperplane hyperplane, Point point){
        double result = hyperplane.pointDistance(point.getVector()) > 0 ? 1.0 : -1.0;

        if(Double.compare(result, point.getCategory()) == 0)
            return hyperplane;

        return hyperplane.invertNormal();
    }
}
