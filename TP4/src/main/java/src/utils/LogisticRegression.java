package src.utils;

import java.util.Arrays;

public class LogisticRegression {
    private boolean trained;
    private double[] thetas;

    public LogisticRegression(){
        trained = false;
    }

    public void train(double[][] X, double[] Y, double n){
        System.out.println(Arrays.toString(Y));
        System.out.println(X.length);
        thetas = maximumLikelihoodGradientAscent(X, Y, n);
        trained = true;
    }

    public double predict(double[] X){
        assert trained;

        return predict(X, thetas);
    }

    private static double predict(double[] X, double[] thetas){
        double z = dotProduct(thetas, X);

        return sigmoid(z);
    }

    public int classify(double[] X){
        double aux = predict(X);
        System.out.println(aux);
        return aux > 0.5 ? 1 : 0;
    }

    private static double[] maximumLikelihoodGradientAscent(double[][] X, double[] Y, double n){
        int m = X[0].length;

        double[] theta = new double[m];
        Arrays.fill(theta, 0);

        int errors = 1000;
        while(errors > 500){
            errors = 0;
            double[] gradient = new double[m];
            Arrays.fill(gradient, 0);

            for (int i = 0; i<X.length; i++){
                double[] sample = X[i];

                double aux = Y[i] - predict(sample, theta);

                errors += Double.compare(aux, 0.0) == 0 ? 0 : 1;

                for(int j = 0; j < sample.length; j++){
                    gradient[j] += sample[j] * aux;
                }
            }

            theta = elementSum(theta, scalarProduct(n, gradient));
        }

        return theta;
    }

    public static double sigmoid(double x){
        return 1.0 / (1 + Math.exp(-x));
    }

    private static double dotProduct(double[] v1, double[] v2){
        assert v1.length == v2.length;

        double result = 0;

        for(int i=0; i< v1.length; i++){
            result += v1[i] * v2[i];
        }

        return result;
    }

    private static double[] elementSum(double[] v1, double[] v2){
        assert v1.length == v2.length;

        double[] result = new double[v1.length];
        Arrays.fill(result, 0);

        for(int i=0; i< v1.length; i++){
            result[i] = v1[i] + v2[i];
        }

        return result;
    }

    private static double[] scalarProduct(double scalar, double[] v){
        double[] result = new double[v.length];

        for(int i=0; i< v.length; i++){
            result[i] = scalar * v[i];
        }

        return result;
    }
}
