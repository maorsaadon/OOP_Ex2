package Ex2.PartB;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;


public class CustomExecutor extends ThreadPoolExecutor{
    static int num = Runtime.getRuntime().availableProcessors();

    private int[] taskTypeArr = {0,0,0};

    public static int getNum() {
        return num;
    }

    public static void setNum(int num) {
        CustomExecutor.num = num;
    }

    public int[] getTaskTypeArr() {
        return taskTypeArr;
    }

    public void setTaskTypeArr(int[] taskTypeArr) {
        this.taskTypeArr = taskTypeArr;
    }


    public CustomExecutor() {
        super(num/2, num - 1, 300L, MILLISECONDS,
                new PriorityBlockingQueue<>());
    }
    @Override
    protected void beforeExecute(Thread thread, Runnable run) {
        int priority = getCurrentMax();
        if (1<=priority && priority<=3)
            taskTypeArr[priority-1]--;
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        int priority = getCurrentMax();
        TaskType type = TaskType.IO;
        if (1<=priority && priority<=3)
            type.setPriority(priority);
        return Task.createTask(callable, type);
    }


    public <T> Future<T> submit (Task<T> task){
        if (task == null || task.getCallable() == null)
            throw new NullPointerException();
        taskTypeArr[task.getPriority()-1]++;
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

    public int hashCode() {
        return this.taskTypeArr.hashCode()*super.getQueue().hashCode();
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
            Task<Integer> task= Task.createTask(callable,TaskType.COMPUTATIONAL);
            int priority = i%3;
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











