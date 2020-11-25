package src.utils.math;

import java.util.List;

public class VectorWithBool extends Vector {

    private boolean bool;

    public VectorWithBool(List<Double> values, boolean bool) {
        super(values);
        this.bool = bool;
    }

    public VectorWithBool(double[] values, boolean bool) {
        super(values);
        this.bool = bool;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }
}
