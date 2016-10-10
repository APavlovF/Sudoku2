import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksandr on 26.08.2016.
 */
public class Column extends Row {

    private List<Integer> cellIndexes;

    @Override
    public int getGroupId(int cellId) {
       return getColumnId(cellId);
    }

    public int getColumnId(int cellId) {
        return (int) Math.floor(cellId - getRowId(cellId) * SIZE);
    }

    @Override
    public List<Integer> getCellIndexes(int groupId) {
        cellIndexes = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            cellIndexes.add(i, groupId + i * SIZE);
        }
        return cellIndexes;
    }

}
