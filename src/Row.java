import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksandr on 26.08.2016.
 */
public class Row implements GroupOfCells, Serializable {

    private List<Integer> cellIndexes;
    private List<Integer> cellValues;

    @Override
    public int getGroupId(int cellId) {
        return getRowId(cellId);
    }

    public int getRowId(int cellId) {
        return (int) Math.floor(cellId / SIZE);
    }

    @Override
    public List<Integer> getCellIndexes(int groupId) {
        cellIndexes = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            cellIndexes.add(i, groupId * SIZE + i);
        }
        return cellIndexes;
    }

    @Override
    public List<Integer> getCellValues(int groupId) {
        cellValues = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            cellValues.add(i, grid.getCells().get(getCellIndexes(groupId).get(i)));
        }
        return cellValues;
    }
}