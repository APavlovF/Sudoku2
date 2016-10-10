import java.util.*;

/**
 * Created by Oleksandr on 06.09.2016.
 */
public class CellOperator {

    private List<GroupOfCells> groupOfCellsList = Grid.getGrid().getGroupOfCellsList();

    public List<Integer> oneCellInOneGroupPossibleValues(int cellId) {
        Set<Integer> currentValues = new HashSet<>();
        /**add existing values from row and column and square containing the checked cell         *
         */
        for (GroupOfCells groupOfCells : groupOfCellsList) {
            currentValues.addAll(groupOfCells.getCellValues(groupOfCells.getGroupId(cellId)));
        }
        /**iteration for numbers from 1 to 9 - and setting correspondent possible values to its cells
         */
        List<Integer> cellPossibleValues = new ArrayList<>();
        for (int i = 1; i < Grid.getGrid().getSIZE() + 1; i++) {
            if (!currentValues.contains(i)) {
                cellPossibleValues.add(i);
            }
        }
        return cellPossibleValues;
    }

    public void setOneCellInOneGroupPossibleValues(int cellId) {
        Grid.getGrid().setCellPossibleValues(cellId, oneCellInOneGroupPossibleValues(cellId));
    }

    public void shuffleOneCellInOneGroupPossibleValues(int cellId) {
        Grid.getGrid().shuffleCellPossibleValues(cellId, oneCellInOneGroupPossibleValues(cellId));
    }

    public Map<Integer, Integer> uniqueValuesForOneGroupOfCells(GroupOfCells groupOfCells, int groupId) {
        /**key = possible cell value, value = cell id
         */
        Map<Integer, Integer> uniqueValues = new HashMap<>();
        for (int i = 0; i < Grid.getGrid().getSIZE(); i++) {
            int cellId = groupOfCells.getCellIndexes(groupId).get(i);
            List<Integer> possibleValues = Grid.getGrid().getCellPossibleValues().get(cellId);
            for (Integer value : possibleValues) {
                if (!uniqueValues.containsKey(value)) {
                    uniqueValues.put(value, cellId);
                } else {
                    uniqueValues.put(value, null);
                }
            }
        }
        return uniqueValues;
    }

}
