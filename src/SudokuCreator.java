import java.util.*;

/**
 * Created by Oleksandr on 04.09.2016.
 */
public class SudokuCreator extends BruteSolver {

    public SudokuCreator(ValueSetter valueSetter) {
        super(valueSetter);
    }

    public void createGrid() {
        //valueSetter.clearGrid();
        //setCheckSum(0);
        //bruteGuess();
        //valueSetter.initiateGrid();
        int level = 27;
        //System.out.println("generated full grid " + Grid.getGrid().getCells());
        Map<Integer, Integer> puzzleCells = new HashMap<>();
        Set<Integer> keysSet = new HashSet<>();
        Random random = new Random();
        do {
            keysSet.add(random.nextInt(80));
        } while (keysSet.size() < level);

        for (Integer key : keysSet) {
            puzzleCells.put(key, Grid.getGrid().getCells().get(key));
        }
        for (int i = 0; i < SIZE * SIZE; i++) {
            Grid.getGrid().getCells().put(i, 0);
        }
        Grid.getGrid().getCells().putAll(puzzleCells);
        //System.out.println("filtered grid " + Grid.getGrid().getCells());
        //clearStacks();
    }


}
