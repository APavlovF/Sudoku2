/**
 * Created by Oleksandr on 30.08.2016.
 */
public class ValueSetter {

    private int SIZE = Grid.getGrid().getSIZE();
    private MainScene mainScene;

    public ValueSetter(MainScene mainScene) {
        this.mainScene = mainScene;
    }

    public void setValue(int cellId, int value) {
        Grid.getGrid().getCells().put(cellId, value);
        Grid.getGrid().getCellPossibleValues().get(cellId).clear();
    }

    public void initiateGrid() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                mainScene.fillSquare(i, j);
                try {
                    mainScene.drawNumber(String.valueOf(Grid.getGrid().getCells().get(i * SIZE + j)), i, j);
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("empty cell # " + ((i * SIZE + j)));
                } catch (NumberFormatException eN) {
                    System.out.println("empty cell # " + ((i * SIZE + j)));
                }
            }
        }
    }

    public void clearGrid() {
        for (int i = 0; i < SIZE * SIZE; i++) {
            Grid.getGrid().getCells().put(i, 0);
        }
        Grid.getGrid().getCellPossibleValues().clear();
        initiateGrid();
        //mainScene.getBruteSolver().clearStacks();
        //mainScene.getBruteSolver().setCheckSum(0);
        //mainScene.getBruteSolver().setCellToCheck(0);
        //mainScene.getBruteSolver().setValueToCheck(0);
        BruteSolver.bruteCounter = 0;
        Solver.countSimple = 0;
        Solver.countUniquer = 0;
    }

    public void errorMessage(){
        mainScene.getErrorStage().show();
    }
}
