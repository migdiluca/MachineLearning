package src.kohonen;

import java.util.ArrayList;
import java.util.List;

public class FrequencyMatrix {

    private static class CellMembers {
        List<String> members;

        public CellMembers(){
            this.members = new ArrayList<>();
        }

        public void add(String member){
            this.members.add(member);
        }

        public List<String> getMembers(){
            return this.members;
        }
    }

    private final CellMembers[][] matrix;

    public FrequencyMatrix(int dim){
        this.matrix = new CellMembers[dim][dim];

        for(int i=0; i<dim; i++){
            for(int j=0; j<dim; j++){
                this.matrix[i][j] = new CellMembers();
            }
        }
    }

    public void addMember(int i, int j, String member){
        CellMembers cellMembers = this.matrix[i][j];

        cellMembers.add(member);
    }

    public int getCellPopulation(int i, int j){
        return this.matrix[i][j].getMembers().size();
    }

    public List<String> getCellMembers(int i, int j){
        return this.matrix[i][j].getMembers();
    }
}
