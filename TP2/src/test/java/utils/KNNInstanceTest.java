package utils;

import KNN.KNNInstance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class KNNInstanceTest {
    private KNNInstance<String> knnInstance;

    @Before
    public void before(){
        knnInstance = new KNNInstance<>();
    }

    @Test
    public void classifyCorrectlyKNN(){
        knnInstance.train(new ArrayList<Double>(){{
            add(1.0);
            add(0.0);
        }}, "a");

        knnInstance.train(new ArrayList<Double>(){{
            add(2.0);
            add(0.0);
        }}, "a");

        knnInstance.train(new ArrayList<Double>(){{
            add(-1.0);
            add(0.0);
        }}, "b");

        knnInstance.train(new ArrayList<Double>(){{
            add(-2.0);
            add(0.0);
        }}, "b");

        String category = knnInstance.classify(new ArrayList<Double>(){{
            add(0.5);
            add(0.0);
        }}, 3, false);

        Assert.assertEquals("Should belong to class 'a'", "a", category);

        category = knnInstance.classify(new ArrayList<Double>(){{
            add(-0.5);
            add(0.0);
        }}, 3, false);

        Assert.assertEquals("Should belong to class 'b'", "b", category);
    }

    @Test
    public void classifyCorrectlyKNNWeighted(){
        knnInstance.train(new ArrayList<Double>(){{
            add(90.0);
            add(0.0);
        }}, "a");

        knnInstance.train(new ArrayList<Double>(){{
            add(100.0);
            add(0.0);
        }}, "a");

        knnInstance.train(new ArrayList<Double>(){{
            add(-1.0);
            add(0.0);
        }}, "b");

        String category = knnInstance.classify(new ArrayList<Double>(){{
            add(0.5);
            add(0.0);
        }}, 3, false);

        Assert.assertEquals("Should belong to class 'a'", "a", category);

        category = knnInstance.classify(new ArrayList<Double>(){{
            add(0.5);
            add(0.0);
        }}, 3, true);

        Assert.assertEquals("Should belong to class 'b'", "b", category);
    }

    @Test
    public void samePositionKNNWeighted(){
        knnInstance.train(new ArrayList<Double>(){{
            add(10.0);
            add(20.0);
        }}, "a");

        knnInstance.train(new ArrayList<Double>(){{
            add(15.0);
            add(-5.0);
        }}, "a");

        knnInstance.train(new ArrayList<Double>(){{
            add(0.01);
            add(1.01);
        }}, "b");

        knnInstance.train(new ArrayList<Double>(){{
            add(-5.0);
            add(-2312.0);
        }}, "b");


        knnInstance.train(new ArrayList<Double>(){{
            add(0.0);
            add(0.0);
        }}, "a");

        knnInstance.train(new ArrayList<Double>(){{
            add(0.0);
            add(0.0);
        }}, "a");

        knnInstance.train(new ArrayList<Double>(){{
            add(0.0);
            add(0.0);
        }}, "b");

        String category = knnInstance.classify(new ArrayList<Double>(){{
            add(0.0);
            add(0.0);
        }}, 100, true);

        Assert.assertEquals("Should belong to class 'a'", "a", category);

        knnInstance.train(new ArrayList<Double>(){{
            add(0.0);
            add(0.0);
        }}, "b");

        knnInstance.train(new ArrayList<Double>(){{
            add(0.0);
            add(0.0);
        }}, "b");

        category = knnInstance.classify(new ArrayList<Double>(){{
            add(0.0);
            add(0.0);
        }}, 100, true);

        Assert.assertEquals("Should belong to class 'b'", "b", category);
    }
}
