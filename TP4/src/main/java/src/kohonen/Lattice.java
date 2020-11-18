package src.kohonen;

import src.kohonen.Vector;

import java.util.List;
import java.util.Objects;

public interface Lattice {

    class Coord{
        private final int i;
        private int j;
        public Coord(int i, int j){
            this.i=i;
            this.j=j;
        }

        public int getI() {
            return i;
        }

        public int getJ() {
            return j;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coord coord = (Coord) o;
            return i == coord.i &&
                    j == coord.j;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j);
        }
    }

    class Neighbor {
        double distance;
        Cell cell;

        public Neighbor(Cell cell, double distance){
            this.distance = distance;
            this.cell = cell;
        }
    }
    Cell get(int i, int j);

    public List<Cell> getNeighbors(Cell cell, double maxDistance);

    public List<Cell> getAllCells();

    public Cell bestMatchingUnit(Vector input);

    public Coord bestMatchingUnitCoord(Vector input);

}
