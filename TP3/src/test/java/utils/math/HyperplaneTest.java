package utils.math;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class HyperplaneTest {

    @Test
    public void pointDistance(){
        Vector vector = Vector.ofZero(3);
        Hyperplane plane = new Hyperplane(new Vector(Arrays.asList(1.0, 1.0, 1.0, 1.0)));

        Assert.assertEquals(0, Double.compare(Math.abs(Hyperplane.pointDistance(plane, vector)), 1/Math.sqrt(3)));
    }

    @Test
    public void distanceSign(){
        Hyperplane hyperplane = new Hyperplane(new Vector(Arrays.asList(1.0, 0.0, 0.0)));
        Vector vector = new Vector(Arrays.asList(1.0, 0.0));

        Assert.assertTrue(Hyperplane.pointDistance(hyperplane, vector) > 0);

        vector = new Vector(Arrays.asList(-1.0, 0.0));

        Assert.assertTrue(Hyperplane.pointDistance(hyperplane, vector) < 0);

        vector = new Vector(Arrays.asList(0.0, 0.0));

        Assert.assertEquals(0, Double.compare(Hyperplane.pointDistance(hyperplane, vector), 0));

    }

    @Test
    public void moveTest(){
        Hyperplane hyperplane = new Hyperplane(new Vector(Arrays.asList(1.0, 0.0, 0.0)));
        Vector vector = new Vector(Arrays.asList(1.0, 0.0));

        double distance = Hyperplane.pointDistance(hyperplane, vector);

        hyperplane = hyperplane.move(distance);

        Assert.assertEquals(0, Double.compare(Hyperplane.pointDistance(hyperplane, vector), 0));
    }
}
