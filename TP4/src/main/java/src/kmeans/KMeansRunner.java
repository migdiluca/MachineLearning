package src.kmeans;

import src.utils.math.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class KMeansRunner {
    public static KMeansInstance runKMeansSimulation(List<Vector> data, int runs, int k) {
        KMeansInstance lowestKMean = null;

        double lowestVariance = Double.MAX_VALUE;
        for (int i = 0; i < runs; i++) {
            KMeansInstance kMeansInstance = new KMeansInstance(k);
            kMeansInstance.train(data);
            double variance = kMeansInstance.getVariance();
            if(variance < lowestVariance) {
                lowestKMean = kMeansInstance;
                lowestVariance = variance;
            }
        }
        return lowestKMean;
    }

    public static List<KMeansInstance> runKMeansGroupSimulation(List<Vector> data, int runs, int k) {
        List<KMeansInstance> resp = new ArrayList<>(runs);

        for (int i = 0; i < runs; i++) {
            KMeansInstance kMeansInstance = new KMeansInstance(k);
            kMeansInstance.train(data);
            resp.add(kMeansInstance);
        }
        return resp;
    }
}
