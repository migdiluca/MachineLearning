package ID3;

import utils.DataFrame;
import utils.ID3Utils;

import java.io.IOException;

public class ID3 {
    public static int[][] run(double testPercentage) throws IOException {
        DataFrame dataFrame = ID3Utils.readAndProcessDataFrame();

        ID3Instance instance = new ID3Instance(dataFrame);
        instance.train(0);

        DataFrame test = dataFrame.getShufflePercent(testPercentage);

        return ID3Utils.confussionMatrix(test, instance);
    }

    public static int[][] run(double testPercentage, int limitDepth, int minTableValues) throws IOException {
        DataFrame dataFrame = ID3Utils.readAndProcessDataFrame();

        ID3Instance instance = new ID3Instance(dataFrame, limitDepth);
        instance.train(0);
        instance.setMinTableValues(minTableValues);

        DataFrame test = dataFrame.getShufflePercent(testPercentage);

        return ID3Utils.confussionMatrix(test, instance);
    }
}
