package ID3;

import utils.DataFrame;
import utils.ID3Utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ID3 {
    public static int[][] run(double testPercentage) throws IOException {
        DataFrame dataFrame = ID3Utils.readAndProcessDataFrame();

        ID3Instance instance = new ID3Instance(dataFrame);
        instance.run(0);

        DataFrame test = dataFrame.getShufflePercent(testPercentage);

        return ID3Utils.confussionMatrix(test, instance);
    }
}
