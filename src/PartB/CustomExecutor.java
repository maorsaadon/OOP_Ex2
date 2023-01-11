package PartB;

import java.util.Arrays;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * <p>
 *     Represents a new type of ThreadPool that supports a priority queue tasks
 *     (Each task in the queue is of type Task). <br>
 *     CustomExecutor will create a Task before putting it in the queue by passing Callable<T>
 *     and an enum of TaskType type. <br>
 *     CustomExecutor will execute the tasks according to their priority. <br>
 * </p>
 */
public class CustomExecutor extends ThreadPoolExecutor{
    static int num = Runtime.getRuntime().availableProcessors();

    private int[] taskTypeArr = {0,0,0,0,0,0,0,0,0,0};


    /**
     * empty constructor
     */
    public CustomExecutor() {
        super(num/2, num - 1, 300L, MILLISECONDS,
                new PriorityBlockingQueue<>());
    }

    /**
     * <p>
     *     In order to get the class's TaskTypeArr <br>
     * </p>
     * @return TaskTypeArr
     */
    public int[] getTaskTypeArr() {
        return taskTypeArr;
    }


    /**
     * <p>
     *     Method of ThreadPoolExecutor class is invoked before executing the given Runnable in the given thread. <br>
     *     This method is invoked by thread t that will execute task r, and may be used to re-initialize ThreadLocals,
     *     or to perform logging. <br>
     * </p>
     * @param thread the thread that will run task {@code r}
     * @param run the task that will be executed
     */
    @Override
    protected void beforeExecute(Thread thread, Runnable run) {
        int priority = getCurrentMax();
        if (1<=priority && priority<=3)
            taskTypeArr[priority-1]--;
    }

    /**
     * <p>
     *     Returns a RunnableFuture for the given callable and default value. <br>
     * </p>
     * @param callable the callable task being wrapped
     * @return a RunnableFuture which, when run, will call the underlying callable and which, as a Future,
     *         will yield the callable's result as its result and provide for cancellation of the underlying task. <br>
     */
    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        int priority = getCurrentMax();
        TaskType type = TaskType.OTHER;
        if (1<=priority && priority<=10)
            type.setPriority(priority);
        return Task.createTask(callable, type);
    }

    /**
     * <p>
     *     Indicates whether some other object is "equal to" this one.
     * </p>
     * @param o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomExecutor that = (CustomExecutor) o;
        return Arrays.equals(taskTypeArr, that.taskTypeArr);
    }

    /**
     * <p>
     *     Java Object hashCode() is a native method and returns the integer hash code value of the object.
     * </p>
     * @return It returns the hash code value for the given objects.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(taskTypeArr);
    }

    /**
     * <p>
     *     A method for submitting Task instances to a priority task queue. <br>
     * </p>
     * @param task
     * @return a Future representing the pending results of the task.
     */
    public <T> Future<T> submit (Task<T> task){
        if (task == null || task.getCallable() == null)
            throw new NullPointerException();
        taskTypeArr[task.getPriority()-1]++;
        RunnableFuture<T> futereTask = newTaskFor(task);
        execute(futereTask);
        return futereTask;
    }

    /**
     * <p>
     *     A method whose purpose is to submit to a queue an operation that can be performed
     *     asynchronously with the addition of TaskType. <br>
     * </p>
     * @param tCallable get a callable task to do.
     * @param typePriority  generic object, new task.
     * @return a Future representing the pending results of the task.
     */
    public <T> Future<T> submit (Callable<T> tCallable, TaskType typePriority){
        Task<T> task = Task.createTask(tCallable, typePriority);
        return submit(task);
    }

    /**
     * <p>
     *     A method whose purpose is to submit to a queue an operation that can be performed asynchronously
     *     without a TaskType as a parameter. <br>
     *     Submits a Runnable task for execution. <br>
     * </p>
     * @param tCallable the task to submit
     * @return a Future representing the pending results of the task.
     */
    @Override
    public <T> Future<T> submit (Callable<T> tCallable){
        Task<T> task = Task.createTask(tCallable);
        return submit(task);
    }

    /**
     *  <p>
     *      A method whose purpose is to return the highest priority of a currently queued task
     *      without accessing the queue when this method is called.
     *  </p>
     * @return int that represents the priority.
     */
    public int getCurrentMax(){
        for(int i =0; i<10;i++){
            if(taskTypeArr[i] > 0){
                return i+1;
            }
        }
        return 0;
    }

    /**
     * <p>
     *     A method whose purpose is to prevent the insertion of additional tasks into the queue,
     *     the execution of all tasks remaining in the queue and the termination of all tasks currently
     *     in execution in the collection of threads of the CustomExecutor. <br>
     * </p>
     */
    public void gracefullyTerminate()  {
        try {
            super.awaitTermination((long) 2, SECONDS);
            super.shutdown();
        }catch (InterruptedException err) {
            System.err.println(err);
            err.printStackTrace();
        }

    }

}











