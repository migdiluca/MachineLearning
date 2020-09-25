package utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DivideTrainAndTestTest {

    private List<Double> entries;

    @Before
    public void before(){
        entries = new ArrayList<>();
        entries.add(1.0);
        entries.add(2.0);
        entries.add(3.0);
        entries.add(4.0);
        entries.add(5.0);
        entries.add(6.0);
    }

    @Test
    public void testSizesSum(){
        DivideTrainAndTest.Division<Double> division = DivideTrainAndTest.divide(entries, 0.5);

        Assert.assertEquals("Sum of the sizes of train and test should be the same as the initial size",
                entries.size(), division.test.size() + division.train.size());
    }

    @Test
    public void testSizesRate(){
        DivideTrainAndTest.Division<Double> division = DivideTrainAndTest.divide(entries, 0.5);

        Assert.assertEquals("Train size should be half of entries",
                division.train.size(), (int) (entries.size() * 0.5));
    }
}
