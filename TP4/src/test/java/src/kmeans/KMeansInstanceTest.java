package src.kmeans;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import src.utils.math.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KMeansInstanceTest {

    @Test
    public void classifyCorrectly(){
        List<Vector> classA = Arrays.asList(
                new Vector(Arrays.asList(-100.0, -100.0)),
                new Vector(Arrays.asList(-100.0, -129.0)),
                new Vector(Arrays.asList(-200.0, -129.0))
                );

        List<Vector> classB = Arrays.asList(
                new Vector(Arrays.asList(100.0, 100.0)),
                new Vector(Arrays.asList(100.0, 129.0)),
                new Vector(Arrays.asList(200.0, 129.0))
        );

        KMeansInstance kMeansInstance = new KMeansInstance(2);

        List<Vector> trainData = new ArrayList<>();
        trainData.addAll(classA);
        trainData.addAll(classB);

        kMeansInstance.train(trainData);

        int c = kMeansInstance.classify(classA.get(0));

        for(Vector v : classA){
            Assert.assertEquals(c, kMeansInstance.classify(v));
        }

        for(Vector v : classB){
            Assert.assertEquals(1 - c, kMeansInstance.classify(v));
        }
    }
}
