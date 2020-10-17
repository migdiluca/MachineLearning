package utils.math;

import org.junit.Assert;
import org.junit.Test;
import perceptron.SeparationHyperplane;

import java.util.Arrays;

public class SeparationHyperplaneTest {
    @Test
    public void inversionOfHyperplane(){
        Hyperplane hyperplane = new Hyperplane(new Vector(Arrays.asList(1.0, 0.0, 0.0)));
        Point point = new Point(new Vector(Arrays.asList(1.0, 0.0)), 1);

        SeparationHyperplane separationHyperplane = new SeparationHyperplane(
                hyperplane,
                point
        );

        Assert.assertTrue(separationHyperplane.verifyClassification(point));

        separationHyperplane = new SeparationHyperplane(
                hyperplane.invertNormal(),
                point
        );

        Assert.assertTrue(separationHyperplane.verifyClassification(point));
    }

    @Test
    public void separation(){
        Hyperplane hyperplane = new Hyperplane(new Vector(Arrays.asList(1.0, 0.0, 0.0)));
        Point point = new Point(new Vector(Arrays.asList(1.0, 0.0)), 1);

        SeparationHyperplane separationHyperplane = new SeparationHyperplane(
                hyperplane,
                point
        );

        Assert.assertTrue(separationHyperplane.verifyClassification(point));

        point = new Point(new Vector(Arrays.asList(-1.0, 0.0)), 1);

        Assert.assertFalse(separationHyperplane.verifyClassification(point));
    }
}
