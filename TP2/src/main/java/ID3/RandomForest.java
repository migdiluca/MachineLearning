package ID3;

import utils.DataFrame;
import utils.ID3Utils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RandomForest {
    public static int[][] run(int trainingSize, int treesAmount, int nodeLimits, double testPercentage) throws IOException {
        DataFrame dataFrame = ID3Utils.readAndProcessDataFrame();
        DataFrame test = dataFrame.getShufflePercent(testPercentage);

        ID3Instance instance = new ID3Instance();
        for(int i = 0; i < treesAmount; i++) {
            DataFrame trainDf = dataFrame.getRandomValues(trainingSize);
            instance.setDataFrame(trainDf);
            instance.run(nodeLimits);
        }

        return ID3Utils.confussionMatrix(test, instance);
    }
}
