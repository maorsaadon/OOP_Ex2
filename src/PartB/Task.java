package PartB;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import static PartB.TaskType.*;

/**
 * <p>
 *   Represents an operation that can be run asynchronously and can return a value of some type. <br>
 *   The class Task<T> extend from FutureTask<T> and implements Comparable<Task<T>>, Callable<T>. <br>
 *   The class Task<T> hold TaskType object and Callable<T>.
 * </p>
 * @param <T> generic task
 */
public class Task<T> extends FutureTask<T>
            implements Comparable<Task<T>>, Callable<T>{

    private TaskType taskType;
    private Callable<T> callable;

    /**
     * <p>
     *     Constructor that get two parameters.<br>
     * </p>
     * @param callable
     * @param taskType
     */
    private Task(Callable<T> callable, TaskType taskType) {
        super(callable);
        setTaskType(taskType);
        setCallable(callable);
    }

    /**
     * <p>
     *      Constructor that get one parameters.<br>
     *      taskType will be automatically type 3 (OTHER)
     * </p>
     * @param callable
     */
    private Task( Callable<T> callable) {
        super(callable);
        setTaskType();
        setCallable(callable);
    }

    /**
     * <p>
     *     In order to get the class's callable <br>
     * </p>
     * @return callable
     */
    public Callable<T> getCallable() {
        return callable;
    }

    /**
     * <p>
     *     In order to set the class's callable <br>
     * </p>
     * @param callable get a callable task to do.
     */
    public void setCallable(Callable<T> callable) {
        this.callable = callable;
    }

    /**
     * <p>
     *     In order to get the class's taskType <br>
     * </p>
     * @return taskType
     */
    public TaskType getTaskType() {
        return taskType;
    }

    /**
     * <p>
     *     In order to set the class's taskType <br>
     * </p>
     * @param taskType
     */
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    /**
     * <p>
     *     In order to save default value in taskType <br>
     * </p>
     */
    public void setTaskType() {
        setTaskType(OTHER);
    }

    /**
     * <p>
     *     A Factory method that will create instances of the class <br>
     * </p>
     * @param callable get a callable task to do.
     * @param taskType get the priority of the new task.
     * @return  generic object, new task.
     */
    public static<T> Task<T> createTask(Callable<T> callable, TaskType taskType){
        return new Task<T>(callable, taskType);
    }

    /**
     * <p>
     *     A Factory method that will create instances of the class <br>
     * </p>
     * @param callable get a callable task to do.
     * @return generic object, new task.
     */
    public static<T> Task<T> createTask(Callable<T> callable){
        return new Task<T>(callable);
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
        Task<?> task = (Task<?>) o;
        return taskType == task.taskType && callable.equals(task.callable);
    }

    /**
     * <p>
     *     Java Object hashCode() is a native method and returns the integer hash code value of the object.
     * </p>
     * @return It returns the hash code value for the given objects.
     */
    @Override
    public int hashCode() {
        return Objects.hash(taskType, callable);
    }

    /**
     * <p>
     *     in order to know what is the type of the task. <br>
     * </p>
     * @return int that relate to the task type by the request.
     */
    public int getPriority(){
        if (taskType.getType() == COMPUTATIONAL) return COMPUTATIONAL.getPriorityValue();
        if (taskType.getType() == IO) return IO.getPriorityValue();
        else return OTHER.getPriorityValue();
    }

    /**
     * <p>
     *     A task that returns a result and may throw an exception. <br>
     *     Implementors define a single method with no arguments called call. <br>
     * </p>
     * @return result
     * @throws Exception
     */
    @Override
    public T call() throws Exception {
        return callable.call();
    }

    /**
     * <p>
     *     The method compares the Number object that invoked the method to the argument. <br>
     * </p>
     * @param other the object to be compared.
     * @return If the argument is greater than the other argument then -1 is returned.
     *         If the argument is equal to the other argument then 0 is returned.
     *         If the argument is less than the other argument then 1 is returned.
     */
    @Override
    public int compareTo(Task<T> other) {
        final long diff = other.taskType.getPriorityValue() - taskType.getPriorityValue();
        return 0 == diff ? 0 : 0 > diff ? 1 : -1;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskType=" + taskType +
                ", callable=" + callable +
                '}';
    }
}
