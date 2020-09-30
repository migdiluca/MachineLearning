package ID3;

import utils.DataFrame;
import utils.ID3Utils;

import java.io.IOException;

public class ID3 {
    public static int[][] run(double testPercentage) throws IOException {
        DataFrame dataFrame = ID3Utils.readAndProcessDataFrame();

        DataFrame test;
        if (testPercentage > 0)
            test = dataFrame.getShufflePercent(testPercentage);
        else
            test = dataFrame;

        ID3Instance instance = new ID3Instance(dataFrame);
        instance.train();

        return ID3Utils.confussionMatrix(test, instance);
    }

    public static int[][] run(double testPercentage, int limitDepth, int minTableValues) throws IOException {
        DataFrame dataFrame = ID3Utils.readAndProcessDataFrame();

        DataFrame test;
        if (testPercentage > 0)
            test = dataFrame.getShufflePercent(testPercentage);
        else
            test = dataFrame;

        ID3Instance instance = new ID3Instance(dataFrame);
        instance.setLimitDepth(limitDepth);
        instance.setMinTableValues(minTableValues);
        instance.train();

        return ID3Utils.confussionMatrix(test, instance);
    }
}
