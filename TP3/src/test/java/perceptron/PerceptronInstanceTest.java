package perceptron;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.math.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PerceptronInstanceTest {
    private PerceptronInstance perceptronInstance;

    @Before
    public void before(){
        perceptronInstance = (new PerceptronBuilder())
                .setDimension(2)
                .setBias(1)
                .setLearningRate(0.01)
                .create();
    }

    @Test
    public void simple(){
        Map<Vector, Double> trainValues = new HashMap<>();

        trainValues.put(new Vector(Arrays.asList(1.0, 1.0)), 1.0);
        trainValues.put(new Vector(Arrays.asList(1.0, -1.0)), 1.0);
        trainValues.put(new Vector(Arrays.asList(3.0, -8.0)), 1.0);
        trainValues.put(new Vector(Arrays.asList(40.0, 20.0)), 1.0);

        trainValues.put(new Vector(Arrays.asList(-1.0, -1.0)), -1.0);
        trainValues.put(new Vector(Arrays.asList(-11.0, 1.0)), -1.0);
        trainValues.put(new Vector(Arrays.asList(-0.78, 29.0)), -1.0);
        trainValues.put(new Vector(Arrays.asList(-1000.0, -29.0)), -1.0);

        compareWithTrain(trainValues, 100);
    }

    @Test
    public void AND(){
        Map<Vector, Double> trainValues = new HashMap<>();

        trainValues.put(new Vector(Arrays.asList(1.0, 1.0)), 1.0);
        trainValues.put(new Vector(Arrays.asList(1.0, -1.0)), -1.0);
        trainValues.put(new Vector(Arrays.asList(-1.0, 1.0)), -1.0);
        trainValues.put(new Vector(Arrays.asList(-1.0, -1.0)), -1.0);

        compareWithTrain(trainValues, 100);
    }

    @Test
    public void OR(){
        Map<Vector, Double> trainValues = new HashMap<>();

        trainValues.put(new Vector(Arrays.asList(1.0, 1.0)), 1.0);
        trainValues.put(new Vector(Arrays.asList(1.0, -1.0)), 1.0);
        trainValues.put(new Vector(Arrays.asList(-1.0, 1.0)), 1.0);
        trainValues.put(new Vector(Arrays.asList(-1.0, -1.0)), -1.0);

        compareWithTrain(trainValues, 100);
    }

    private void compareWithTrain(Map<Vector, Double> values, int trainingRounds){
        //Train
        for(int t=0; t < trainingRounds; t++){
            for(Map.Entry<Vector, Double> entry : values.entrySet()){
                perceptronInstance.train(entry.getKey(), entry.getValue());
            }
        }

        //Classify
        for(Map.Entry<Vector, Double> entry : values.entrySet()){
            Assert.assertEquals(Math.round(entry.getValue()), Math.round(perceptronInstance.classify(entry.getKey())));
        }
    }
}
