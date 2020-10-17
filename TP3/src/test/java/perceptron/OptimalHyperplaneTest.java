package perceptron;

import org.junit.Assert;
import org.junit.Test;
import utils.CsvReader;
import utils.TP31ValuesGenerator;
import utils.math.Hyperplane;
import utils.math.Point;
import utils.math.Vector;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OptimalHyperplaneTest {
    static String TP31_PATH = "./data/TP3-1.csv";

    @Test
    public void test() throws IOException {
        final List<Map<String, String>> TP31_values = CsvReader.readCsv(TP31_PATH, ",");

        List<Point> points = TP31_values.stream()
                .map(e -> new Point(
                        new Vector(Arrays.asList(
                                Double.valueOf(e.get("x")),
                                Double.valueOf(e.get("y"))
                        )),
                        e.get("c")
                ))
                .collect(Collectors.toList());

        Vector v = new Vector(Arrays.asList(-0.7368696585422518,1.0,-0.9786060453026446));
        SeparationHyperplane separationHyperplane = new SeparationHyperplane(new Hyperplane(v));

        Assert.assertTrue(OptimalHyperplane.verifyHyperplane(separationHyperplane, points));

    }

    @Test
    public void verifyHyperplaneFromPerceptron() throws IOException {
//        TP31ValuesGenerator.generate(100, TP31_PATH);

        final List<Map<String, String>> TP31_values = CsvReader.readCsv(TP31_PATH, ",");

        Vector weights = trainPerceptron(TP31_values);

        List<Point> points = TP31_values.stream()
                .map(e -> new Point(
                        new Vector(Arrays.asList(
                                Double.valueOf(e.get("x")),
                                Double.valueOf(e.get("y"))
                        )),
                        e.get("c")
                ))
                .collect(Collectors.toList());

        SeparationHyperplane separationHyperplane = new SeparationHyperplane(
                new Hyperplane(weights),
                points.get(0)
        );

        Assert.assertTrue(OptimalHyperplane.verifyHyperplane(separationHyperplane, points));

        separationHyperplane = new SeparationHyperplane(separationHyperplane.getHyperplane().move(10));

        Assert.assertFalse(OptimalHyperplane.verifyHyperplane(separationHyperplane, points));

    }

    private Vector trainPerceptron(List<Map<String, String>> TP31_values) throws IOException {

        PerceptronInstance perceptronInstance = (new PerceptronBuilder())
                .setBias(1)
                .setDimension(2)
                .setLearningRate(0.01)
                .create();

        //Train
        boolean error = true;
        while(error){
            error = false;
            for(Map<String, String> line : TP31_values){
                Vector input = new Vector(Arrays.asList(
                        Double.valueOf(line.get("x")),
                        Double.valueOf(line.get("y"))
                ));

                error |= perceptronInstance.train(input, Double.parseDouble(line.get("c")));
            }
        }

        return perceptronInstance.getWeights();
    }
}
