package Ex2.PartB;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class Task<T> extends FutureTask<T>
            implements Comparable<Task<T>>, Callable<T>{

    private TaskType taskType;
    private Callable<T> callable;

    private Task(Callable<T> callable, TaskType taskType) {
        super(callable);
        setTaskType(taskType);
        setCallable(callable);
    }

    private Task( Callable<T> callable) {
        super(callable);
        setTaskType();
        setCallable(callable);
    }

    public Callable<T> getCallable() {
        return callable;
    }

    public void setCallable(Callable<T> callable) {
        this.callable = callable;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public void setTaskType() {
        setTaskType(TaskType.OTHER);
    }




    @Override
    public T call() throws Exception {
        return callable.call();
    }

    public static<T> Task<T> createTask(Callable<T> callable, TaskType taskType){
       return new Task<T>(callable, taskType);
    }


    public static<T> Task<T> createTask(Callable<T> callable){
        return new Task<T>(callable);
    }

    @Override
    public int compareTo(Task<T> other) {
        final long diff = other.taskType.getPriorityValue() - taskType.getPriorityValue();
        return 0 == diff ? 0 : 0 > diff ? 1 : -1;
    }

    public boolean equals(Task<T> other) {
        if (compareTo(other) == 0)
            return true;
        return false;
    }

    public int hashcode() {
        return this.taskType.getPriorityValue()*this.callable.hashCode();
    }


}




