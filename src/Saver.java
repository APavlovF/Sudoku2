import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Oleksandr on 27.08.2016.
 */
public class Saver {

    public void save() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("grid.init"));
            objectOutputStream.writeObject(Grid.getGrid().getCells());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("grid.init"));
            Grid.getGrid().setCells((Map<Integer, Integer>) objectInputStream.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
