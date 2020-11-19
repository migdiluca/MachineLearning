package src.hierarchicalClustering;

import src.utils.math.Vector;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HierarchicalClusteringInstance {
    public static class HCNode {
        private final HCNode child1, child2;
        private final List<Vector> group;
        private final Vector centroid;
        private double distance;

        public HCNode(HCNode child1, HCNode child2, double distance) {
            this.child1 = child1;
            this.child2 = child2;

            group = new LinkedList<>();
            group.addAll(child1.group);
            group.addAll(child2.group);

            centroid = Vector.centroid(group);
        }

        public HCNode(Vector leaf){
            group = new LinkedList<>();
            group.add(leaf);

            centroid = leaf;

            child1 = null;
            child2 = null;
        }

        public double distance(HCNode other){
            return Math.abs(centroid.distance(other.centroid));
        }

        public HCNode getChild1() {
            return child1;
        }

        public HCNode getChild2() {
            return child2;
        }

        public List<Vector> getGroup() {
            return group;
        }

        public Vector getCentroid() {
            return centroid;
        }

        public double getDistance() {
            return distance;
        }
    }

    public static HCNode formTree(List<Vector> values){
        List<HCNode> groups = values.stream()
                .map(HCNode::new)
                .collect(Collectors.toList());

        while(groups.size() > 1){
            int iMin = -1, jMin = -1;
            double minDistance = Double.MAX_VALUE;

            for(int i=0; i<groups.size(); i++){
                for(int j=0; j<i; j++){
                    double newDistance = groups.get(i).distance(groups.get(j));

                    if(newDistance < minDistance){
                        minDistance = newDistance;

                        iMin = i;
                        jMin = j;
                    }
                }
            }

            groups.add(new HCNode(groups.get(iMin), groups.get(jMin), minDistance));
            groups.remove(iMin);
            groups.remove(jMin);
        }

        return groups.get(0);
    }
}
