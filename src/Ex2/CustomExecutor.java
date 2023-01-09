package Ex2_2;

import java.util.Objects;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;


public class CustomExecutor extends ThreadPoolExecutor{
    static int num = Runtime.getRuntime().availableProcessors();
    private int[] taskTypeArr = {0,0,0};


    public CustomExecutor() {
        super(num/2, num - 1, 300L, MILLISECONDS,
                new PriorityBlockingQueue<>());
    }
    @Override
    protected void beforeExecute(Thread thread, Runnable run) {
        super.beforeExecute(thread, run);
        int priority = getCurrentMax();
        if (1<=priority && priority<=3)
            taskTypeArr[priority-1]--;
    }


    public <T> Future<T> submit (Task<T> task){
        if (task == null || task.getCallable() == null)
            throw new NullPointerException();
        taskTypeArr[task.getTaskType().getPriorityValue()-1]++;
        RunnableFuture<T> futereTask = newTaskFor(task);
        execute(futereTask);
        return futereTask;
    }

    public <T> Future<T> submit (Callable<T> tCallable, TaskType typePriority){
        Task<T> task = Task.createTask(tCallable, typePriority);
        return submit(task);
    }

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


    public void gracefullyTerminate()  {
        try {
            super.awaitTermination((long) 0.1, SECONDS);
            super.shutdownNow();
        }catch (InterruptedException err) {
            System.err.println(err);
            err.printStackTrace();
        }

    }

    public int getCurrentMax() {
        if(taskTypeArr[0] >0)
            return 1;
        if (taskTypeArr[1] > 0)
            return 2;
        if(taskTypeArr[2] > 0)
            return 3;
       return 0;

    }


}











