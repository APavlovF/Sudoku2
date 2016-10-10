import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oleksandr on 27.08.2016.
 */
public interface GroupOfCells {

    Grid grid = Grid.getGrid();

    int SIZE = grid.getSIZE();

    int getGroupId(int cellId);

    List<Integer> getCellIndexes(int groupId);

    List<Integer> getCellValues(int groupId);
}
