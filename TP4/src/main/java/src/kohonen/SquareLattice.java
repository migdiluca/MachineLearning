package src.kohonen;

import src.kohonen.Vector;

import java.util.*;
import java.util.function.Supplier;

public class SquareLattice implements Lattice {

    private final Cell[][] lattice;

    public SquareLattice(Supplier<Cell> cellGenerator, int dim){
        this.lattice = new Cell[dim][dim];

        generateLatticeWithSupplier(cellGenerator, dim);
    }

    private void generateLatticeWithSupplier(Supplier<Cell> cellGenerator, int dim){
        for(int i = 0; i < dim; i++){
            for(int j = 0; j < dim; j++){
                this.lattice[i][j] = cellGenerator.get();
            }
        }
    }

    @Override
    public Cell get(int i, int j){
        return lattice[i][j];
    }

    @Override
    public List<Cell> getNeighbors(Cell cell, double distance) {

        List<Cell> result = new ArrayList<>();

        Set<Coord> addedCoords = new HashSet<>();

        Queue<Coord> frontier = new LinkedList<>();
        Coord cellCoord = findCoordinate(cell);

        if(cellCoord == null)
            throw new IllegalArgumentException("The cell passed as parameter does not belong to the lattice");

        frontier.add(cellCoord);

        while(!frontier.isEmpty()){
            Coord current = frontier.poll();

            double eucDistance = euclideanDistance(cellCoord.getI(), cellCoord.getJ(), current.getI(), current.getJ());
            if(eucDistance > distance)
                continue;

            result.add(lattice[current.getI()][current.getJ()]);

            Coord newCoord;

            if(current.getI() + 1 < lattice.length){
                newCoord = new Coord(current.getI() + 1, current.getJ());
                if(!addedCoords.contains(newCoord)){
                    addedCoords.add(newCoord);
                    frontier.add(newCoord);
                }
            }

            if(current.getI() - 1 >= 0){
                newCoord = new Coord(current.getI() - 1, current.getJ());
                if(!addedCoords.contains(newCoord)){
                    addedCoords.add(newCoord);
                    frontier.add(newCoord);
                }
            }

            if(current.getJ() + 1 < lattice.length){
                newCoord = new Coord(current.getI(), current.getJ() + 1);
                if(!addedCoords.contains(newCoord)){
                    addedCoords.add(newCoord);
                    frontier.add(newCoord);
                }
            }

            if(current.getJ() - 1 >= 0){
                newCoord = new Coord(current.getI(), current.getJ() - 1);
                if(!addedCoords.contains(newCoord)){
                    addedCoords.add(newCoord);
                    frontier.add(newCoord);
                }
            }

        }

        return result;
    }

    @Override
    public List<Cell> getAllCells() {
        List<Cell> cells = new ArrayList<>();
        for(int i=0; i<lattice.length; i++){
            for(int j=0; j<lattice.length; j++){
                cells.add(lattice[i][j]);
            }
        }
        return cells;
    }

    @Override
    public Cell bestMatchingUnit(Vector input) {
        Coord coord = bestMatchingUnitCoord(input);

        return lattice[coord.getI()][coord.getJ()];
    }

    @Override
    public Coord bestMatchingUnitCoord(Vector input) {
        double minDistance = Double.MAX_VALUE;
        Coord minCellCoord = null;

        for(int i=0; i < lattice.length; i++){
            for(int j=0; j < lattice.length; j++){
                Cell cell = lattice[i][j];

                double newDistance = cell.weightDistance(input);

                if(newDistance < minDistance){
                    minCellCoord = new Coord(i, j);
                    minDistance = newDistance;
                }
            }
        }

        return minCellCoord;
    }

    private Coord findCoordinate(Cell cell){
        for(int i=0; i<lattice.length; i++){
            for(int j=0; j<lattice.length; j++){
                if(lattice[i][j] == cell)
                    return new Coord(i, j);
            }
        }
        return null;
    }

    private double euclideanDistance(int i, int j, int k, int w){
        return Math.sqrt(Math.pow(i-k, 2) + Math.pow(j-w, 2));
    }
}
