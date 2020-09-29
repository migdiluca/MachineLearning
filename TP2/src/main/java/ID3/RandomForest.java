package ID3;

import utils.DataFrame;
import utils.ID3Utils;

import java.io.IOException;

public class RandomForest {
    public static int[][] run(int trainingSize, int treesAmount, int nodeLimits, double testPercentage, int limitDepth, int minTableValues) throws IOException {
        DataFrame dataFrame = ID3Utils.readAndProcessDataFrame();
        DataFrame test = dataFrame.getShufflePercent(testPercentage);
        ID3Instance instance;
        if(limitDepth > 0)
            instance = new ID3Instance(limitDepth);
        else
            instance = new ID3Instance();
        instance.setMinTableValues(minTableValues);
        for(int i = 0; i < treesAmount; i++) {
            DataFrame trainDf = dataFrame.getRandomValues(trainingSize);
            instance.setDataFrame(trainDf);
            instance.train(nodeLimits);
        }

        return ID3Utils.confussionMatrix(test, instance);
    }
}
