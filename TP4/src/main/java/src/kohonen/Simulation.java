/*
package src.kohonen;

import utils.CsvReader;
import utils.Vector;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static utils.Statistics.mean;

public class Simulation {

    public static void run(){
        try {
            sim();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sim() throws IOException {
        int numberOfIterations = 300000;

        //Kohonen map generator
        int latticeDim = 3;
        int inputDim = 7;
        double timeConstant = numberOfIterations/Math.log(inputDim);
        System.out.println("\nInitializing Kohonen Map with the following parameters:");
        System.out.println(" - Number of iterations: " + numberOfIterations);
        System.out.println(" - Time constant: " + timeConstant);
        System.out.println(" - Lattice dimension: " + latticeDim);
        System.out.println(" - Input dimension: " + inputDim);
        Lattice lattice = new SquareLattice(cellGenerator(inputDim), latticeDim);
        KohonenMap map = (new KohonenMap.Builder())
                .setLattice(lattice)
                .setNeighborhoodFunction(Simulation::neighborhoodFunction)
                .setLearningRateFunction(Simulation::learningRateFunction)
                .setTimeConstant(timeConstant)
                .create();

        //Read europe.csv for training
        Map<String, Vector> categories;
        try{
            categories = (new CsvReader(
                    "./unsupervised-learning/europe.csv",
                    ",",
                    true)
            )
                    .read()
                    .standardizeValues()
                    .getCategories();

        }catch (IOException e){
            e.printStackTrace();
            return;
        }


        //Training
        System.out.println("\nStarting training");
        for(int t=0; t< numberOfIterations; t++){
            for(Vector vector : categories.values()){
                map.step(vector);
            }
        }

        //Done training
        System.out.println("\nDone training!");

        //Creating a matrix to classify input values into the matrix coordinates
        FrequencyMatrix frequencyMatrix = new FrequencyMatrix(latticeDim);
        for(String key : categories.keySet()){
            Vector vector = categories.get(key);

            Lattice.Coord coord = map.activate(vector);
            frequencyMatrix.addMember(coord.getI(), coord.getJ(), key);
        }

        //Creating directories to save result
        (new File("./unsupervised-learning/results/")).mkdir();
        (new File("./unsupervised-learning/results/kohonen")).mkdir();

        try(BufferedWriter bw = new BufferedWriter(
                new FileWriter("./unsupervised-learning/results/kohonen/cellPopulation"))){
            //Set dimensions of matrix
            bw.write(latticeDim + " " + latticeDim + "\n");

            //Print frequency of categories inside each cell
            System.out.println("\nNumber of members on each cell:");
            for(int i=0; i<latticeDim; i++){
                for(int j=0; j<latticeDim; j++){
                    System.out.print(frequencyMatrix.getCellPopulation(i, j) + " ");
                    bw.write(frequencyMatrix.getCellPopulation(i, j) + "\n");
                }
                System.out.print("\n");
            }
        }

        try(BufferedWriter bw = new BufferedWriter(
                new FileWriter("./unsupervised-learning/results/kohonen/cellMembers"))){
            //Set dimensions of matrix
            bw.write(latticeDim + " " + latticeDim + "\n");

            //Print members of each coordinates
            System.out.println("\nMembers on each coordinate (i, j):");
            for(int i=0; i<latticeDim; i++){
                for(int j=0; j<latticeDim; j++){
                    System.out.print("(" + i + ", " + j +"): " + frequencyMatrix.getCellMembers(i, j) + "\n");
                    bw.write(frequencyMatrix.getCellMembers(i, j) + "\n");
                }
            }
        }

        try(BufferedWriter bw = new BufferedWriter(
                new FileWriter("./unsupervised-learning/results/kohonen/cellDistance"))) {
            //Set dimensions of matrix
            bw.write(latticeDim + " " + latticeDim + "\n");

            //Print average distance of neighbors
            System.out.println("\nAverage distance of neighbors:");
            for(int i=0; i<latticeDim; i++){
                for(int j=0; j<latticeDim; j++){
                    Cell cell = lattice.get(i, j);
                    List<Cell> neighbors = lattice.getNeighbors(cell, 1)
                            .stream().filter(n->!n.equals(cell)).collect(Collectors.toList());

                    List<Double> distances = new ArrayList<>();
                    for(Cell neighbor: neighbors){
                        distances.add(
                                neighbor.weightDistance(cell.getWeights())
                        );
                    }

                    double mean = mean(distances);
                    System.out.print(mean + " ");
                    bw.write(mean + "\n");
                }
                System.out.print("\n");
            }
        }

        try(BufferedWriter bw = new BufferedWriter(
                new FileWriter("./unsupervised-learning/results/kohonen/cellWeights"))) {
            //Set dimensions of matrix
            bw.write(latticeDim + " " + latticeDim + "\n");

            //Save cell weights
            for(int i=0; i<latticeDim; i++) {
                for (int j = 0; j < latticeDim; j++) {
                    Cell cell = lattice.get(i, j);
                    bw.write(cell.getWeights() + "\n");
                }
            }
        }

    }

    public static double neighborhoodFunction(int time, double timeConstant){
        double r0 = 3;
        return r0*Math.exp(-time/timeConstant);
    }

    public static double learningRateFunction(int time, double timeConstant){
        double a0 = 1;
        return a0*Math.exp(-time/timeConstant);
    }

    public static Supplier<Cell> cellGenerator(int dim){
        return () -> {
            double[] values = new double[dim];
            for(int i=0; i<dim; i++){
                values[i] = Math.random();
            }
            return new CellImpl(new Vector(values));
        };
    }
}
*/
