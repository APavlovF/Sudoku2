/**
 * Created by Oleksandr on 30.08.2016.
 */
public class Techniq {

    Runtime runtime = Runtime.getRuntime();

    public long getTotalMemory() {
        return runtime.totalMemory() / 1024;
    }

    public long getFreeMemory() {
        return runtime.freeMemory() / 1024;
    }

    public long getUsedMemory() {
        return getTotalMemory() - getFreeMemory();
    }

    public Runtime getRuntime() {
        return runtime;
    }
}
