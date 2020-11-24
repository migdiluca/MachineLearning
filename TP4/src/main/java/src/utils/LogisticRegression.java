package src.utils;

import src.utils.math.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LogisticRegression {
    private boolean trained;
    private double[] thetas;

    public LogisticRegression(){
        trained = false;
    }

    public void train(double[][] X, double[] Y, double n){
        thetas = maximumLikelihoodGradientAscent(X, Y, n);
        trained = true;

        System.out.println(Arrays.toString(thetas));
    }

    public double predict(double[] X){
        assert trained;

        return predict(X, thetas);
    }

    public static double predict(double[] X, double[] thetas){
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

        double gradientNorm;
        double[] gradient;
        int steps = 10000 * 2;
        double[] prevThetas;
        do{
            prevThetas = theta;
            gradient = new double[m];
            Arrays.fill(gradient, 0);

            for (int i = 0; i<X.length; i++){
                double[] sample = X[i];

                double aux = Y[i] - predict(sample, theta);

                for(int j = 0; j < sample.length; j++){
                    gradient[j] += sample[j] * aux;
                }
            }

            theta = elementSum(theta, scalarProduct(n, gradient));
            steps--;

            gradientNorm = (new Vector(Arrays.stream(gradient).boxed().collect(Collectors.toList()))).norm();

            System.out.println(gradientNorm);
        }while(gradientNorm > 15);

        return theta;
    }

    public static double sigmoid(double x){
        if(x < 0){
            double exp = Math.exp(x);
            return exp / (1 + exp);
        }else{
            return 1.0 / (1 + Math.exp(-x));
        }
    }

    private static boolean compareThetas(double[] t1, double[] t2){
        if(t1 == null || t2 == null)
            return false;
        if(t1.length != t2.length)
            return false;

        Vector v1 = new Vector(Arrays.stream(t1).boxed().collect(Collectors.toList())),
                v2 = new Vector(Arrays.stream(t2).boxed().collect(Collectors.toList()));

        double distance = v1.distance(v2);
        System.out.println(distance);
        return distance < v1.norm();
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
