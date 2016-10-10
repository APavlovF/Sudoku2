import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksandr on 26.08.2016.
 */
public class Square extends Column {

    private List<Integer> cellIndexes;

    @Override
    public int getGroupId(int cellId) {
        return getSquareId(cellId);
    }

    public int getSquareId(int cellId) {
        int factor = (int) Math.sqrt(SIZE);
        int rowFactor = (int) Math.floor(getRowId(cellId) / factor);
        return rowFactor * factor + getColumnId(cellId) / factor;
    }

    @Override
    public List<Integer> getCellIndexes(int groupId) {
        cellIndexes = new ArrayList<>();
        int factor = (int) Math.sqrt(SIZE);
        int rowFactor = (int) Math.floor(groupId / factor);
        for (int i = 0; i < factor; i++) {
            for (int j = 0; j < factor; j++) {
                int cellId = (groupId * SIZE) - (groupId - rowFactor * factor) * (SIZE - factor) + i * SIZE + j;
                cellIndexes.add(i * factor + j, cellId);
            }
        }
        return cellIndexes;
    }
}
