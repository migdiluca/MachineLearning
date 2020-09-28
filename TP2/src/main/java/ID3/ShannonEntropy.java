package ID3;

public class ShannonEntropy {

    public static double getGeneralEntropy(double positive, double negative) {
        double resp = 0;
        if(positive > 0)
            resp -= positive * log2(positive);
        if(negative > 0)
            resp -= negative * log2(negative);
        return resp;
    }


    private static double log2(double N)
    {
        return Math.log(N) / Math.log(2);
    }
}