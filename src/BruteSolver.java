import java.util.*;

/**
 * Created by Oleksandr on 31.08.2016.
 */
public class BruteSolver extends Solver {

    private Map<Integer, Integer> cells;
    private int cellToCheck = 0;
    private int valueToCheck = 0;
    private int tryValue;
    private boolean isSolveProcess;
    public static int bruteCounter = 0;

    Stack<Map<Integer, Integer>> cellsStack = new Stack<>();
    Stack<Integer> cellToCheckStack = new Stack<>();
    Stack<Integer> valueToCheckStack = new Stack<>();

    private int controlSum = SIZE * getRowSum();
    private int checkSum;

    public BruteSolver(ValueSetter valueSetter) {
        super(valueSetter);
    }

    public void bruteGuess() {
        //long startTime = System.currentTimeMillis();
        while (checkSum != controlSum) {
            findCellForCheck();
            findPossibleValuesForCell();
            bruteCounter++;
            if (bruteCounter > 800) {
                checkSum = controlSum;
                //System.out.println("in stack " + cellsStack.size());
                valueSetter.errorMessage();
                break;
            }
        }
        System.out.println("count simple " + countSimple);
        System.out.println("count unique " + countUniquer);
        System.out.println("brute " + bruteCounter);
        System.out.println("checksum " + checkSum + ", controlsum " + controlSum);
    }

    void findCellForCheck() {
        for (int i = cellToCheck; i < SIZE * SIZE; i++) {
            if (Grid.getGrid().getCells().get(i) == 0) {
                cellToCheck = i;
                break;
            }
        }
    }

    void findPossibleValuesForCell() {
        try {
            if (isSolveProcess) {
                cellOperator.setOneCellInOneGroupPossibleValues(cellToCheck);
            } else {
                cellOperator.shuffleOneCellInOneGroupPossibleValues(cellToCheck);
            }
            tryValue = Grid.getGrid().getCellPossibleValues().get(cellToCheck).get(valueToCheck);
            updateStack();
            tryPossibleValue();
            /*if there was no exception, current tryValue "passed", and solver should check the first possible value in the next cell*/
            valueToCheck = 0;
        } catch (IndexOutOfBoundsException e) {
            if (cellsStack.size() > 1) {
                restoreGrid();
                cellToCheck = cellToCheckStack.pop();
                valueToCheck = valueToCheckStack.pop();
                valueToCheck++;
                //System.out.println("exception in find possible values.celltocheck " + cellToCheck);
                //System.out.println("exception in find possible values. valuetocheck No. " + valueToCheck);
            } else {
                System.out.println("***stack size " + cellsStack.size());
                System.out.println("!!! NO SOLUTION !!!");
                Grid.getGrid().setCells(cellsStack.peek());
                checkSum = controlSum;
                valueSetter.errorMessage();
            }
        }
    }

    void updateStack() {
        saveCells();
        cellToCheckStack.push(cellToCheck);
        valueToCheckStack.push(valueToCheck);
    }

    void saveCells() {
        cells = new HashMap<>();
        cells.putAll(Grid.getGrid().getCells());
        cellsStack.push(cells);
    }

    void clearStacks() {
        cellsStack.removeAllElements();
        cellToCheckStack.removeAllElements();
        valueToCheckStack.removeAllElements();
        updateStack();
    }

    void restoreGrid() {
        Grid.getGrid().setCells(cellsStack.pop());
        valueSetter.initiateGrid();
    }

    void tryPossibleValue() {
        valueSetter.setValue(cellToCheck, tryValue);
        checkSum = simpleCheck();
    }

    public int getRowSum() {
        int sum = 0;
        for (int i = 0; i < SIZE + 1; i++) {
            sum += i;
        }
        return sum;
    }

    public void setCheckSum(int checkSum) {
        this.checkSum = checkSum;
    }

    public void setCellToCheck(int cellToCheck) {
        this.cellToCheck = cellToCheck;
    }

    public void setValueToCheck(int valueToCheck) {
        this.valueToCheck = valueToCheck;
    }

    public void setSolveProcess(boolean solveProcess) {
        isSolveProcess = solveProcess;
    }
}
