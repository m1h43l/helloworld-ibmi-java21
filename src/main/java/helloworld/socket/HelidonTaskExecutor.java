package helloworld.socket;

import java.io.Closeable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

// This interface is borrowed from the Helidon project: io.helidon.common.task.HelidonTaskExecutor
public interface HelidonTaskExecutor extends Closeable {

    /**
     * Executes a task.
     *
     * @param task an interruptable task
     * @param <T>  type ov value returned by task
     * @return a future for a value returned by the task
     */
    <T> Future<T> execute(InterruptableTask<T> task);

    /**
     * Verifies if the executor is terminated.
     *
     * @return outcome of test
     */
    boolean isTerminated();

    /**
     * Terminate executor waiting for any running task to complete for a specified
     * timeout period. It will only wait for those {@link InterruptableTask}s that
     * are not interruptable.
     *
     * @param timeout timeout period
     * @param unit    timeout period unit
     * @return outcome of shutdown process
     */
    boolean terminate(long timeout, TimeUnit unit);

    /**
     * Force termination by forcefully interrupting all tasks. Shall only be called
     * if {@link #terminate} returns {@code false}.
     */
    void forceTerminate();
}