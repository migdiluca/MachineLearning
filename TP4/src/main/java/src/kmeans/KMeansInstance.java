package src.kmeans;

import src.utils.math.Vector;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;


public class KMeansInstance {
    public static BiFunction<List<Vector>, Integer, Map<Integer, List<Vector>>> RANDOM_POINTS_INITIALIZER
            = KMeansInstance::randomPointsInitializer;

    private final int K;

    private List<Vector> allPoints;
    private Map<Integer, Vector> kMeans;
    private Map<Integer, List<Vector>> points;

    private final BiFunction<List<Vector>, Integer, Map<Integer, List<Vector>>> pointsInitializer;

    public KMeansInstance(int k) {
        this(k, RANDOM_POINTS_INITIALIZER);
    }

    public KMeansInstance(int k, BiFunction<List<Vector>, Integer, Map<Integer, List<Vector>>> pointsInitializer) {
        K = k;
        this.pointsInitializer = pointsInitializer;

        kMeans = new HashMap<>();
    }

    public void train(List<Vector> values){
        allPoints = values;
        points = pointsInitializer.apply(values, K);

        Map<Integer, Vector> prevKMeans = null;

        while (!kMeans.equals(prevKMeans)){
            prevKMeans = new HashMap<>(kMeans);

            kMeans = points.entrySet().stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                        if(e.getValue().isEmpty())
                            return kMeans.get(e.getKey());

                        return Vector.centroid(e.getValue());
                    }));

            regroup();
        }
    }

    public int classify(Vector value){
        Comparator<Map.Entry<Integer, Vector>> comparator =
                Comparator.comparingDouble(a -> a.getValue().distance(value));

        return kMeans.entrySet().stream()
                .min(comparator)
                .map(Map.Entry::getKey)
                .orElseThrow(IllegalArgumentException::new);
    }

    private void regroup(){
        Map<Integer, List<Vector>> newPoints = new HashMap<>(K);

        for(int i=0; i<K; i++){
            newPoints.put(i, new LinkedList<>());
        }

        for(Vector v : allPoints){
            int c = classify(v);

            newPoints.get(c).add(v);
        }

        points = newPoints;
    }

    private static Map<Integer, List<Vector>> randomPointsInitializer(List<Vector> values, int K){
        Random rnd = new Random();

        Map<Integer, List<Vector>> newPoints = new HashMap<>(K);

        for(int i=0; i<K; i++){
            newPoints.put(i, new LinkedList<>());
        }

        for(Vector v : values){
            newPoints.get(rnd.nextInt(K)).add(v);
        }

        return newPoints;
    }
}
