import java.io.Serializable;
import java.util.*;

/**
 * Created by Oleksandr on 28.08.2016.
 */
public final class Grid implements Serializable {

    final private int SIZE = 9;

    private static Grid grid = new Grid();

    private Map<Integer, Integer> cells;
    private Map<Integer, List<Integer>> cellPossibleValues;
    private List<GroupOfCells> groupOfCellsList;

    private Grid() {
        cells = new HashMap<>();
        cellPossibleValues = new HashMap<>();
        groupOfCellsList = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            cells.put(i, 0);
        }
        groupOfCellsList.add(0, new Row());
        groupOfCellsList.add(1, new Column());
        groupOfCellsList.add(2, new Square());
    }

    public static Grid getGrid() {
        return grid;
    }

    public static void setGrid(Grid grid) {
        Grid.grid = grid;
    }

    public int getSIZE() {
        return SIZE;
    }

    public Map<Integer, Integer> getCells() {
        return cells;
    }

    public void setCells(Map<Integer, Integer> cells) {
        this.cells = cells;
    }

    public Map<Integer, List<Integer>> getCellPossibleValues() {
        return cellPossibleValues;
    }

    public void setCellPossibleValues(int cellId, List<Integer> possibleValues) {
        //Collections.shuffle(possibleValues);
        cellPossibleValues.put(cellId, possibleValues);
    }

    public void shuffleCellPossibleValues(int cellId, List<Integer> possibleValues) {
        Collections.shuffle(possibleValues);
        cellPossibleValues.put(cellId, possibleValues);
    }

    public List<GroupOfCells> getGroupOfCellsList() {
        return groupOfCellsList;
    }

}
