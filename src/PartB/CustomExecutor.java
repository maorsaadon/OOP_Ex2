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


public class CustomExecutor<T> extends ThreadPoolExecutor{
    static int num = Runtime.getRuntime().availableProcessors();

    private int[] taskTypeArr = {0,0,0,0,0,0,0,0,0,0};

    public static int getNum() {
        return num;
    }

    public static void setNum(int num) {
        CustomExecutor.num = num;
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

    public void setTaskTypeArr(int[] taskTypeArr) {
        this.taskTypeArr = taskTypeArr;
    }

//    public static String toString(){
//
//        return getTaskTypeArr().toString();
//    }
//
    /**
     * empty constructor
     */
    public CustomExecutor() {
        super(num/2, num - 1, 300L, MILLISECONDS,
                new PriorityBlockingQueue<>());
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
        if (1<=priority && priority<=10)
            taskTypeArr[priority-1]--;
    }

    /**
     * <p>
     *     Returns a RunnableFuture for the given runnable and default value. <br>
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
            type.setPriority(type.getPriorityValue());
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

//    @Override
//    protected void afterExecute(Runnable run, Throwable throw1) {
//        super.afterExecute(run, throw1);
//        if (throw1 == null) {
//            int priority = getCurrentMax();
//            if (1 <= priority && priority <= 3)
//                taskTypeArr[priority - 1]--;
//        }
//        else
//            System.out.println("encountered exception- " +throw1.getMessage());
//    }

    /**
     *  <p>
     *      A method whose purpose is to return the highest priority of a currently queued task
     *      without accessing the queue when this method is called.
     *  </p>
     * @return int that represents the priorityץ
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
    public static void main(String[] args) throws InterruptedException {
        Callable<Integer> callable1 = ()->8000*50;
        Callable<Integer> callable2 = ()->8*5;
        Callable<Integer> callable3 = ()->8*1;
        Task<Integer> task1 = Task.createTask(callable1,TaskType.COMPUTATIONAL);
        Task<Integer> task2 = Task.createTask(callable2,TaskType.IO);
        Task<Integer> task3 = Task.createTask(callable3,TaskType.COMPUTATIONAL);
        CustomExecutor c = new CustomExecutor();
        Future<Integer> f1 = c.submit(task1);
        Future<Integer> f2 = c.submit(task2);
        Future<Integer> f3 = c.submit(task3);
        try{
            System.out.println(f1.get());
            System.out.println(f2.get());
            System.out.println(f3.get());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        for (int i = 0; i < 100; i++) {
            int finalI = i;
            Callable<Integer> callable = ()->10* finalI;
            Task<Integer> task= Task.createTask(callable,TaskType.IO);
            int priority = i;
            //.setTypePriority(priority);
            Future<Integer> f = c.submit(task);
            System.out.println("max before: "+c.getCurrentMax());
            try{
                System.out.println(f.get());
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        System.out.println("max after: " + c.getCurrentMax());
        c.gracefullyTerminate();
    }



}
