package src.hierarchicalClustering;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import src.kmeans.KMeansInstance;
import src.utils.math.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HierarchicalClusteringInstanceTest {

    private List<Vector> values;

    @Before
    public void before(){
        values = Arrays.asList(
                new Vector(Arrays.asList(-2000.0, -2000.0)),
                new Vector(Arrays.asList(-2005.0, -2005.0)),
                new Vector(Arrays.asList(2000.0, 2000.0)),
                new Vector(Arrays.asList(2005.0, 2005.0)),
                new Vector(Arrays.asList(-3000.0, -3000.0)),
                new Vector(Arrays.asList(3000.0, 3000.0))
        );
    }

    @Test
    public void correctDepth(){
        HierarchicalClusteringInstance.HCNode root = HierarchicalClusteringInstance.formTree(values);

        int depth = maxDepthR(root);

        Assert.assertEquals(3, depth);
    }

    private int maxDepthR(HierarchicalClusteringInstance.HCNode node){
        if(node.getChild1() == null)
            return 0;

        return 1 + Math.max(maxDepthR(node.getChild1()), maxDepthR(node.getChild2()));
    }
}
