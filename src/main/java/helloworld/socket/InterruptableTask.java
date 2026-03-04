package helloworld.socket;

import java.util.concurrent.Callable;

// This interface is borrowed from the Helidon project: io.helidon.common.task.InterruptableTask
public interface InterruptableTask<T> extends Runnable, Callable<T> {

    /**
     * Signals if a task can be interrupted at the time this method is called.
     *
     * @return outcome of interruptable test
     */
    boolean canInterrupt();

    @Override
    default void run() {
        try {
            call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    default T call() throws Exception {
        run();
        return null;
    }
}