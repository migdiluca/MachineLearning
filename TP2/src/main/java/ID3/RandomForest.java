package ID3;

import utils.DataFrame;
import utils.ID3Utils;

import java.io.IOException;

public class RandomForest {
    public static int[][] run(int treesAmount, int nodeLimits, double testPercentage, int limitDepth, int minTableValues) throws IOException {
        DataFrame dataFrame = ID3Utils.readAndProcessDataFrame();
        DataFrame test;
        if (testPercentage > 0)
            test = dataFrame.getShufflePercent(testPercentage);
        else
            test = dataFrame;

        ID3Instance instance = new ID3Instance();

        instance.setLimitNodes(nodeLimits);
        instance.setLimitDepth(limitDepth);
        instance.setMinTableValues(minTableValues);

        for (int i = 0; i < treesAmount; i++) {
            DataFrame trainDf = dataFrame;
            if(testPercentage > 0)
                trainDf = dataFrame.getRandomValues();
            instance.setDataFrame(trainDf);
            instance.train();
        }

        return ID3Utils.confussionMatrix(test, instance);
    }
}
