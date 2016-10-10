import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Oleksandr on 26.08.2016.
 */
public class SudokuTest {

    int expectedResult;
    Object[] expectedArray = {1, 2, 4, 5, 6, 8,};
    int result1;
    int result2;
    int result3;
    Object[] resultArray;

    GroupOfCells row = new Row();
    GroupOfCells square = new Square();
    GroupOfCells column = new Column();

    MainScene mainScene = new MainScene();
    ValueSetter valueSetter = new ValueSetter(mainScene);
    Solver solver = new Solver(valueSetter);

    @Test
    public void getGroupIdTest() {

        expectedResult = 0;

        Assert.assertEquals(expectedResult, result1);
    }

    @Test
    public void getCellIndexesTest() {

        expectedResult = 10;

        result1 = row.getCellIndexes(1).get(1);
        result2 = column.getCellIndexes(1).get(1);
        result3 = square.getCellIndexes(0).get(4);

        Assert.assertEquals(expectedResult, result1);
        Assert.assertEquals(expectedResult, result2);
        Assert.assertEquals(expectedResult, result3);
    }

    @Test
    public void getCellValueTest() {
        GroupOfCells row1 = new Row();
        GroupOfCells row2 = new Row();

        expectedResult = 9;
        result1 = row2.getCellIndexes(0).get(5);
        List<Integer> list = row1.getCellValues(0);

        result1 = list.get(5);

        Assert.assertEquals(expectedResult, result1);
    }

    @Test
    public void simpleCheckTests() {

        List<GroupOfCells> groupOfCellsList = Grid.getGrid().getGroupOfCellsList();

        //simpleCheck.oneCellInOneGroupPossibleValues(cell);

        //0 - row, 0 - 1st
        //simpleCheck.oneGroupOfCellsCheck(0, 0);

        //simpleCheck.simpleCheck();

        //resultArray = cell.getCellPossibleValues().toArray();

        Assert.assertArrayEquals(expectedArray, resultArray);
    }

    @Test
    public void setUniqueValuesTest() {

        //System.out.println(Grid.getGrid().getCells().get(8).getValue());

        //simpleCheck.simpleCheck();
        //simpleCheck.uniqueValuesForOneGroupOfCells(row);

        expectedResult = 9;

        Assert.assertEquals(expectedResult, result1);
    }

    @Test
    public void saverTest() {

        Saver saver = new Saver();

        //expectedResult = Grid.getGrid().getSIZE();
        saver.save();

        saver.open();
        //Grid.getGrid().getCells().get(0).setValue(8);

        //result1 = Grid.getGrid().getSIZE();

        //Assert.assertEquals(expectedResult, result1);

    }

    @Test
    public void setTest() {
        List<Integer> setA = new ArrayList<>();
        List<Integer> setB = new ArrayList<>();

        setA.add(1);
        setB.add(2);
        setA = setB;

        System.out.println(setA.size());
        Assert.assertEquals(setA.get(0),setB.get(0));

    }

}
