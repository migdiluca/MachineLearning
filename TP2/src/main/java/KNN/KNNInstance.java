package KNN;

import utils.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class KNNInstance<T> {
    private class Point {
        Vector v;
        T category;

        public Point(Vector v, T category) {
            this.v = v;
            this.category = category;
        }
    }
    private final Set<T> categories;
    private final List<Point> map;

    public KNNInstance(){
        this.map = new ArrayList<>();
        this.categories = new HashSet<>();
    }

    public void train(List<Double> values, T category){
        categories.add(category);
        map.add(new Point(new Vector(values), category));
    }

    public T classify(List<Double> values, int k, boolean weighted){
        Vector vec = new Vector(values);

        Comparator<Point> comparator = Comparator.comparingDouble(a -> a.v.distance(vec));

        List<Point> kClosest = map.stream()
                .sorted(comparator)
                .limit(k)
                .collect(Collectors.toList());

        return weighted ? categoryByClosestWeighted(vec, kClosest) : categoryByClosest(kClosest);
    }

    private T categoryByClosest(List<Point> closest){
        T maxCategory = null;
        long maxValue = 0;
        for(T category: categories){
            long newValue = closest.stream().filter(c->c.category.equals(category)).count();

            if(newValue > maxValue){
                maxValue = newValue;
                maxCategory = category;
            }
        }

        return Optional.ofNullable(maxCategory).orElseThrow(IllegalArgumentException::new);
    }

    private T categoryByClosestWeighted(Vector vec, List<Point> closest){
        T maxCategory = null;
        double maxValue = 0;

        Map<T, Integer> infinitiesCount = new HashMap<>();

        for(T category: categories){
            double newValue = closest.stream().mapToDouble(
                    c -> c.category == category ? weight(vec, c.v) : 0
            ).sum();

            if(Double.compare(newValue, Double.POSITIVE_INFINITY) == 0){
                infinitiesCount.put(category, 1 + Optional.ofNullable(infinitiesCount.get(category)).orElse(0));
            }

            if(newValue > maxValue){
                maxValue = newValue;
                maxCategory = category;
            }
        }

        if(infinitiesCount.size() != 0){
            int maxCount = 0;
            for(Map.Entry<T, Integer> entry: infinitiesCount.entrySet()){
                int newCount = entry.getValue();

                if(newCount > maxCount){
                    maxCategory = entry.getKey();
                    maxCount = newCount;
                }
            }
        }

        return Optional.ofNullable(maxCategory).orElseThrow(IllegalArgumentException::new);
    }

    private double weight(Vector a, Vector b){
        double distance = Vector.distance(a, b);

        if(Double.compare(distance, 0) == 0)
            return Double.POSITIVE_INFINITY;

        return 1/Math.pow(distance, 2);
    }
}
