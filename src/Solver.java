import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Oleksandr on 29.08.2016.
 */
public class Solver {

    int SIZE = Grid.getGrid().getSIZE();
    CellOperator cellOperator = new CellOperator();
    ValueSetter valueSetter;

    private boolean flag;

    private List<GroupOfCells> groupOfCellsList = Grid.getGrid().getGroupOfCellsList();

    static int countSimple = 0;
    static int countUniquer = 0;

    public Solver(ValueSetter valueSetter) {
        this.valueSetter = valueSetter;
    }

    public int simpleCheck() {
        /**
         * check by all rows, than - columns and squares
         **/
        for (GroupOfCells groupOfCells : groupOfCellsList) {
            /*iteration for all rows | columns | squares*/
            for (int i = 0; i < SIZE; i++) {
                oneGroupOfCellsCheck(groupOfCellsList.indexOf(groupOfCells), i);
            }
        }
        return getCheckSum();
    }

    public void simpleCheck2() {
        Set<Map.Entry<Integer, Integer>> cells = Grid.getGrid().getCells().entrySet();
        for (Map.Entry<Integer, Integer> cell : cells) {
            if (cell.getValue() == 0) {
                threeGroupsOfCellsCheck(cell.getKey());
            }
        }
    }

    /**
     * set cells' possible values for one row | column | square
     * groupOfCellsType explanation: 0 is a Row(), 1 is a Column(), 2 is a Square()
     */
    public void oneGroupOfCellsCheck(int groupOfCellsType, int groupId) {
      /*iteration for all cells in a row | column | square*/
        do {
            restart:
            {
                for (Integer cellIndex : groupOfCellsList.get(groupOfCellsType).getCellIndexes(groupId)) {
                    if (Grid.getGrid().getCells().get(cellIndex) == 0) {
                        cellOperator.setOneCellInOneGroupPossibleValues(cellIndex);
                        if (Grid.getGrid().getCellPossibleValues().get(cellIndex).size() == 1) {
                            valueSetter.setValue(cellIndex, Grid.getGrid().getCellPossibleValues().get(cellIndex).get(0));
                            threeGroupsOfCellsCheck(cellIndex);
                            flag = true;
                            break restart;
                        } else {
                            flag = false;
                        }
                        countSimple++;
                    }
                }
                /**key = possible cell value, value = cell id
                 */
                try {
                    Set<Map.Entry<Integer, Integer>> uniqueValuesSet = cellOperator.uniqueValuesForOneGroupOfCells(groupOfCellsList.get(groupOfCellsType), groupId).entrySet();
                    if (uniqueValuesSet.isEmpty()) {
                        flag = false;
                    } else {
                        for (Map.Entry<Integer, Integer> cellValue : uniqueValuesSet) {
                            if (cellValue.getValue() != null) {
                                valueSetter.setValue(cellValue.getValue(), cellValue.getKey());
                                threeGroupsOfCellsCheck(cellValue.getValue());
                                flag = true;
                                break restart;
                            } else {
                                flag = false;
                            }
                        }
                    }
                } catch (NullPointerException e) {
                    flag = false;
                }
                countUniquer++;
            }
        } while (flag);
    }

    public void threeGroupsOfCellsCheck(int cellId) {
        for (GroupOfCells groupOfCells : groupOfCellsList) {
            oneGroupOfCellsCheck(groupOfCellsList.indexOf(groupOfCells), groupOfCells.getGroupId(cellId));
        }
    }

    public List<GroupOfCells> getGroupOfCellsList() {
        return groupOfCellsList;
    }

    public int getCheckSum() {
        int checkSum = 0;
        for (int i = 0; i < SIZE * SIZE; i++) {
            try {
                checkSum += Grid.getGrid().getCells().get(i);
            } catch (NullPointerException e) {
                System.out.println("sum error");
            }
        }
        return checkSum;
    }

}
