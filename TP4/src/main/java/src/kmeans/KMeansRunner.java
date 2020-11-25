package src.kmeans;

import src.utils.math.Vector;

import java.util.List;
import java.util.function.Function;

public class KMeansRunner {
    public static KMeansInstance runKMeansSimulation(List<Vector> data, int runs) {
        KMeansInstance lowestKMean = null;

        double lowestVariance = Double.MAX_VALUE;
        for (int i = 0; i < runs; i++) {
            KMeansInstance kMeansInstance = new KMeansInstance(2);
            kMeansInstance.train(data);
            double variance = kMeansInstance.getVariance();
            if(variance < lowestVariance) {
                lowestKMean = kMeansInstance;
                lowestVariance = variance;
            }
        }
        return lowestKMean;
    }
}
