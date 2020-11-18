package perceptron;

import utils.math.Hyperplane;
import utils.math.Point;
import utils.math.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class OptimalHyperplane {


    public static SeparationHyperplane findOptimalHyperplane(Vector weights, List<Map<String, String>> values, int kClosesNeighbors){
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
                .limit(kClosesNeighbors)
                .collect(Collectors.toList());

        List<Point> categoryB = points.stream()
                .filter(v -> v.getCategory() == -1)
                .sorted(comparator)
                .limit(kClosesNeighbors)
                .collect(Collectors.toList());

        return pickNeighborCombination(categoryA, categoryB, points);

    }


    private static SeparationHyperplane pickNeighborCombination(List<Point> categoryA, List<Point> categoryB, List<Point> points){
        LineMarginResult maxMarginCombination = null;
        double maxMargin = Double.NEGATIVE_INFINITY;
        for(Point a : categoryA){
            for(Point b : categoryB){
                for(Point c : categoryA){
                    if(c == a)
                        continue;
                    LineMarginResult newVal = findLineMargin(a, b, c, maxMargin, points, categoryA, categoryB);
                    if(newVal.margin > maxMargin){
                        maxMarginCombination = newVal;
                        maxMargin = newVal.margin;
                    }
                }
                for(Point c : categoryB){
                    if(c == b)
                        continue;
                    LineMarginResult newVal = findLineMargin(a, b, c, maxMargin, points, categoryA, categoryB);
                    if(newVal.margin > maxMargin){
                        maxMarginCombination = newVal;
                        maxMargin = newVal.margin;
                    }
                }
            }
        }

        return Optional.ofNullable(maxMarginCombination).map(v -> v.separationHyperplane).orElse(null);
    }

    static class LineMarginResult {
        SeparationHyperplane separationHyperplane;
        double margin;

        public LineMarginResult(SeparationHyperplane separationHyperplane, double margin) {
            this.separationHyperplane = separationHyperplane;
            this.margin = margin;
        }
    }
    private static LineMarginResult findLineMargin(Point a, Point b, Point c, double currentMargin, List<Point> points,
                                                   List<Point> categoryA, List<Point> categoryB){
        Point grouped, alone;
        if(a.getCategory() == c.getCategory()){
            grouped = a;
            alone = b;
        }else{
            grouped = b;
            alone = a;
        }

        SeparationHyperplane h1 = new SeparationHyperplane(Hyperplane.ofLine(grouped.getVector(), c.getVector()), alone);

        SeparationHyperplane moved = new SeparationHyperplane(
                h1.getHyperplane().move(h1.getHyperplane().pointDistance(alone.getVector()) / 2)
        );

        Point closestA = closestDistance(categoryA, moved.getHyperplane()),
                closestB = closestDistance(categoryB, moved.getHyperplane());

        Hyperplane aux = moved.getHyperplane()
                .move(
                        moved.getHyperplane().pointDistance(closestA.getVector())
                );

        aux = aux.move(
                aux.pointDistance(closestB.getVector()) / 2
        );

        SeparationHyperplane movedHyperplane = new SeparationHyperplane(aux);

        boolean result = verifyHyperplane(movedHyperplane, points);

        double margin = Math.abs(aux.pointDistance(closestA.getVector()))
                + Math.abs(aux.pointDistance(closestB.getVector()));

        if(result){
            return new LineMarginResult(movedHyperplane, margin);
        }else{
            return new LineMarginResult(null, Double.NEGATIVE_INFINITY);
        }
    }

    private static Point closestDistance(List<Point> points, Hyperplane hyperplane){
        Comparator<Point> comparator = Comparator.comparingDouble(
                (Point a) -> Hyperplane.pointDistance(hyperplane, a.getVector())
        ).reversed();

        return points.stream()
                .min(comparator)
                .orElseThrow(()->new IllegalArgumentException("Could not found a min!"));
    }

    protected static boolean verifyHyperplane(SeparationHyperplane hyperplane, List<Point> points){
        for(Point p : points){
            if(!hyperplane.verifyClassification(p))
                return false;
        }
        return true;
    }
}
