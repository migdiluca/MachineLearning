package perceptron;

import utils.math.Hyperplane;
import utils.math.Point;
import utils.math.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class OptimalHyperplane {


    public static Vector findOptimalHyperplane(Vector weights, List<Map<String, String>> values, int kClosesNeighbors){
        Hyperplane suboptimalHyperplane = new Hyperplane(weights);

        List<Point> points = values.stream()
                .map(e -> new Point(
                        new Vector(Arrays.asList(
                                Double.valueOf(e.get("x")),
                                Double.valueOf(e.get("y"))
                        )),
                        e.get("c")
                ))
                .collect(Collectors.toList());

        Comparator<Point> comparator = Comparator.comparingDouble(
                (Point a) -> suboptimalHyperplane.pointDistance(a.getVector())
        ).reversed();

        List<Point> categoryA = points.stream()
                .filter(v -> v.getCategory() == 1)
                .sorted(comparator)
//                .limit(kClosesNeighbors)
                .collect(Collectors.toList());

        List<Point> categoryB = points.stream()
                .filter(v -> v.getCategory() == -1)
                .sorted(comparator)
//                .limit(kClosesNeighbors)
                .collect(Collectors.toList());

        return pickNeighborCombination(categoryA, categoryB, points);

    }


    private static Vector pickNeighborCombination(List<Point> categoryA, List<Point> categoryB, List<Point> points){
        LineMarginResult maxMarginCombination = null;
        double maxMargin = Double.NEGATIVE_INFINITY;
        for(Point a : categoryA){
            for(Point b : categoryB){
                for(Point c : categoryA){
                    if(c == a)
                        continue;
                    LineMarginResult newVal = findLineMargin(a, b, c, maxMargin, points);
                    if(newVal.margin > maxMargin){
                        maxMarginCombination = newVal;
                        maxMargin = newVal.margin;
                    }
                }
                for(Point c : categoryB){
                    if(c == b)
                        continue;
                    LineMarginResult newVal = findLineMargin(a, b, c, maxMargin, points);
                    if(newVal.margin > maxMargin){
                        maxMarginCombination = newVal;
                        maxMargin = newVal.margin;
                    }
                }
            }
        }

        return Optional.ofNullable(maxMarginCombination).map(v -> v.weights).orElse(null);
    }

    static class LineMarginResult {
        Vector weights;
        double margin;

        public LineMarginResult(Vector weights, double margin) {
            this.weights = weights;
            this.margin = margin;
        }
    }
    private static LineMarginResult findLineMargin(Point a, Point b, Point c, double currentMargin, List<Point> points){
        Point grouped, alone;
        if(a.getCategory() == c.getCategory()){
            grouped = a;
            alone = b;
        }else{
            grouped = b;
            alone = a;
        }

        SeparationHyperplane h1 = new SeparationHyperplane(Hyperplane.ofLine(grouped.getVector(), c.getVector()), alone);

        double distance = h1.getHyperplane().pointDistance(alone.getVector());

        if(Math.abs(distance) < currentMargin)
            return new LineMarginResult(null, Double.NEGATIVE_INFINITY);

        SeparationHyperplane movedHyperplane = new SeparationHyperplane(h1.getHyperplane().move(distance / 2));

        SeparationHyperplane h2 = new SeparationHyperplane(h1.getHyperplane().move(distance));

        boolean result = verifyHyperplane(h1, points)
                && verifyHyperplane(h2, points);

        if(result){
            return new LineMarginResult(movedHyperplane.getHyperplane().getCoefficients(), Math.abs(distance));
        }else{
            return new LineMarginResult(null, Double.NEGATIVE_INFINITY);
        }
    }

    protected static boolean verifyHyperplane(SeparationHyperplane hyperplane, List<Point> points){
        for(Point p : points){
            if(!hyperplane.verifyClassification(p))
                return false;
        }
        return true;
    }
}
