package utils;

import java.util.Collections;
import java.util.List;

public class DivideTrainAndTest {
    public static class Division<T>{
        public List<T> test, train;
    }

    public static <T> Division<T> divide(List<T> values, double trainRate){
        Division<T> division = new Division<>();

        Collections.shuffle(values);

        int trainMaxIndex = (int) (values.size() * trainRate);
        int testMaxIndex = values.size() - trainMaxIndex;

        division.train = values.subList(0, trainMaxIndex);
        division.test = values.subList(trainMaxIndex, trainMaxIndex + testMaxIndex);

        return division;
    }
}
