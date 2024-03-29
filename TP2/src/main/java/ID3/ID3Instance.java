package ID3;

import utils.DataFrame;

import java.io.IOException;
import java.util.*;

public class ID3Instance {

    private DataFrame dataFrame;
    private List<Node> roots;
    private int limitDepth;
    private boolean limitingDepth;

    private int limitNodes = 0;
    private int minTableValues = 0;

    public ID3Instance() {
        this.roots = new ArrayList<>();
        this.limitingDepth = false;
        this.roots = new ArrayList<>();
    }

    public ID3Instance(DataFrame dataFrame) {
        this.dataFrame = dataFrame;
        this.roots = new ArrayList<>();
        this.limitingDepth = false;
    }

    public ID3Instance(int limitDepth) {
        this.limitDepth = limitDepth;
        this.limitingDepth = true;
        this.roots = new ArrayList<>();
    }

    public ID3Instance(DataFrame dataFrame, int limitDepth) {
        this.dataFrame = dataFrame;
        this.limitDepth = limitDepth;
        this.limitingDepth = true;
        this.roots = new ArrayList<>();
    }

    public void train() {
        double generalEntropy = ShannonEntropy.getGeneralEntropy(dataFrame.percentage(1.0, "Survived"), dataFrame.percentage(0.0, "Survived"));
        Map<String, Double> gain = new HashMap<>();
        List<String> columnNames = new ArrayList<>(dataFrame.getColumnNames());
        columnNames.remove("Survived");
        Collections.shuffle(columnNames);
        if (limitNodes > 0)
            columnNames = columnNames.subList(0, limitNodes);
        for (String columnName : columnNames) {
            if (!columnName.equals("Survived")) {
                double attributeGain = generalEntropy + getAttributesGain(columnName, dataFrame);
                gain.put(columnName, attributeGain);
            }
        }

        roots.add(recursiveID3(dataFrame, getMaxGainAttribute(gain), limitNodes, limitDepth));
    }

    private double getAttributesGain(String columnName, DataFrame dataFrame) {
        double attributeGain = 0;
        for (Object value : dataFrame.columnValues(columnName)) {
            DataFrame dfWithValue = dataFrame.filter(value, columnName);
            attributeGain -= (((double) dfWithValue.size() / dataFrame.size()) * ShannonEntropy.getGeneralEntropy(dfWithValue.percentage(1.0, "Survived"), dfWithValue.percentage(0.0, "Survived")));
        }
        return attributeGain;
    }


    private Node recursiveID3(DataFrame df, String columnName, int limitNodes, int limitDepth) {
        if (columnName == null || df.getColumnNames().size() < 2 || (limitingDepth && limitDepth == 0) || df.size() <= minTableValues) {
            String nodeValue = "Yes";
            if (df.percentage(0.0, "Survived") > 0.5)
                nodeValue = "No";
            return new Node(nodeValue);
        }
        double positives = df.percentage(1.0, "Survived");
        double negatives = df.percentage(0.0, "Survived");
        if (positives == 1.0)
            return new Node("Yes");
        if (negatives == 1.0)
            return new Node("No");

        Node node = new Node(columnName);
        node.setMostCommon(positives > negatives ? "Yes" : "No");
        List<String> columnNames = new ArrayList<>(df.getColumnNames());
        columnNames.remove("Survived");
        Collections.shuffle(columnNames);
        if (limitNodes > 0 && columnNames.size() > limitNodes)
            columnNames = columnNames.subList(0, limitNodes);
        for (Object value : dataFrame.columnValues(columnName)) {
            double columnEntropy = ShannonEntropy.getGeneralEntropy(positives, negatives);

            Map<String, Double> gain = new HashMap<>();
            for (String columnKey : columnNames) {
                if (!columnKey.equals("Survived") && !columnKey.equals(columnName)) {
                    double attributeGain = columnEntropy + getAttributesGain(columnKey, df);
                    gain.put(columnKey, attributeGain);
                }
            }

            DataFrame newDf = df.filter(value, columnName);
            if (newDf.size() == 0) {
                newDf = df.clone();
                newDf.remove(columnName);
                node.addEdge(value, recursiveID3(newDf, null, limitNodes, limitDepth - 1));
            } else {
                newDf.remove(columnName);
                node.addEdge(value, recursiveID3(newDf, getMaxGainAttribute(gain), limitNodes, limitDepth - 1));
            }

        }
        return node;
    }

    public String classify(Map<String, Object> conditions) {
        int result = 0;
        for (Node root : roots) {
            if (classifyRecursive(root, conditions).equals("Yes")) {
                result++;
            } else {
                result--;
            }
        }
        return result > 0 ? "Yes" : "No";
    }

    private String classifyRecursive(Node currentNode, Map<String, Object> conditions) {
        if (currentNode.getValue().equals("Yes") || currentNode.getValue().equals("No"))
            return currentNode.getValue();

        for (Edge edge : currentNode.getEdges()) {
            if (edge.getValue().equals(conditions.get(currentNode.getValue()))) {
                return classifyRecursive(edge.getNode(), conditions);
            }
        }

        return currentNode.getMostCommon();
    }

    private String getMaxGainAttribute(Map<String, Double> gain) {
        double maxGain = -1;
        String columnName = null;
        for (String key : gain.keySet()) {
            double currentGain = gain.get(key);
            if (currentGain > maxGain) {
                maxGain = currentGain;
                columnName = key;
            }
        }
        return columnName;
    }

    public void setMinTableValues(int minTableValues) {
        this.minTableValues = minTableValues;
    }

    public void setDataFrame(DataFrame dataFrame) {
        this.dataFrame = dataFrame;
    }

    public void setLimitDepth(int limitDepth) {
        if(limitDepth >= 0)
            this.limitingDepth = true;
        this.limitDepth = limitDepth;
    }

    public void setLimitNodes(int limitNodes) {
        this.limitNodes = limitNodes;
    }
}

