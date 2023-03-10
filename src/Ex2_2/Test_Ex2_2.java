package Ex2_2;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;


public class Test_Ex2_2 {
    public static final Logger logger = LoggerFactory.getLogger(Test_Ex2_2.class);

    @Test
    void TaskType() {
        TaskType tt1 = TaskType.IO;
        assertEquals(2, tt1.getPriorityValue());
        tt1.setPriority(5);
        assertEquals(5, tt1.getPriorityValue());
    }

    @Test
    void Task_constructors() {
        Callable<String> callable = () -> "| abcd |";

        Task<String> t1 = Task.createTask(callable);
        Task<String> t2 = Task.createTask(callable, TaskType.IO);
        assertEquals(3, t1.getPriority());
        assertEquals(2, t2.getPriority());
    }

    @Test
    void Task_call() throws Exception {
        Callable<String> callable = () -> "| abcd |";
        Task<String> t1 = Task.createTask(callable);
        assertEquals("| abcd |", t1.call());
    }

    @Test
    void Task_get_type() {
        Callable<String> callable = () -> "| abcd |";
        Task<String> t1 = Task.createTask(callable, TaskType.IO);
        assertEquals(2, t1.getPriority());
    }

    @Test
    void Task_compare() {
        Callable<String> callable = () -> "| abcd |";
        Task<String> t1 = Task.createTask(callable, TaskType.IO);
        Callable<String> callable2 = () -> "|";
        Task<String> t2 = Task.createTask(callable2, TaskType.IO);
        Task<String> t3 = Task.createTask(callable2, TaskType.COMPUTATIONAL);
        Task<String> t4 = Task.createTask(callable2, TaskType.OTHER);
        assertEquals(0, t1.compareTo(t2));
        assertEquals(1, t2.compareTo(t4));
        assertEquals(-1, t3.compareTo(t2));

    }

    @Test
    void customExecutor_submit() {
        Callable<String> callable = () -> {
            Thread.sleep(3000);
            return "| abcd |";
        };

        Task<String> t1 = Task.createTask(callable, TaskType.IO);
        CustomExecutor customExecutor = new CustomExecutor();
        assertNotNull(customExecutor.submit(t1));
        assertNotNull(customExecutor.submit(callable));
        assertNotNull(customExecutor.submit(callable, TaskType.COMPUTATIONAL));
        assertEquals(3, customExecutor.getActiveCount());
        assertThrows(NullPointerException.class, () -> customExecutor.submit((Task<String>) null));

    }

    @Test
    void getMax() {
        Task<Integer> task1 = Task.createTask(() -> {
            Thread.sleep(100000);
            return 1;
        }, TaskType.COMPUTATIONAL);
        Task<Integer> task2 = Task.createTask(() -> {
            Thread.sleep(100000);
            return 1;
        }, TaskType.IO);
        Task<Integer> task3 = Task.createTask(() -> {
            Thread.sleep(100000);
            return 1;
        }, TaskType.OTHER);
        CustomExecutor customExecutor = new CustomExecutor();
        customExecutor.submit(task3);
        customExecutor.submit(task3);
        customExecutor.submit(task3);
        customExecutor.submit(task2);
        customExecutor.submit(task2);
        customExecutor.submit(task2);
        customExecutor.submit(task2);
        customExecutor.submit(task2);
        System.out.println(Arrays.toString(customExecutor.getTaskTypeArr()));
        assertEquals(2, customExecutor.getCurrentMax());
        customExecutor.submit(task1);
        assertEquals(1, customExecutor.getCurrentMax());


    }

    @Test
    void answer_of_tasks() {
        Task<Integer> task1 = Task.createTask(() -> {
            Thread.sleep(1000);
            return 1;
        }, TaskType.COMPUTATIONAL);
        Task<Integer> task2 = Task.createTask(() -> {
            Thread.sleep(1000);
            return 1;
        }, TaskType.IO);
        Task<Integer> task3 = Task.createTask(() -> {
            Thread.sleep(1000);
            return 1;
        }, TaskType.OTHER);
        CustomExecutor customExecutor = new CustomExecutor();
        Future<Integer> f3 = customExecutor.submit(task3);
        Future<Integer> f2 = customExecutor.submit(task2);
        Future<Integer> f1 = customExecutor.submit(task1);
        int answer;
        try {
            answer = f1.get() + f2.get() + f3.get();
            Thread.sleep(3000);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        assertEquals(3, answer);
    }

    @Test
    void gracefullyTerminate() {
        Task<Integer> task1 = Task.createTask(() -> {
            Thread.sleep(1000);
            return 1;
        }, TaskType.COMPUTATIONAL);
        CustomExecutor customExecutor = new CustomExecutor();
        for (int i = 0; i < 50; i++) {
            customExecutor.submit(task1);
        }
        customExecutor.gracefullyTerminate();
        assertThrows(RejectedExecutionException.class, () -> customExecutor.submit(task1));
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(50, customExecutor.getCompletedTaskCount());
        assertTrue(customExecutor.isTerminated());
    }

    @Test
    public void Task() throws ExecutionException, InterruptedException {

        CustomExecutor customExecutor = new CustomExecutor();

        Task task1 = Task.createTask(()->{//Make an invalid task (divided by zero).
            return 10;
        });
        var exception  = customExecutor.submit(task1); //The CustomExecutor get invalid task and catch the exception.

        Task task2 = Task.createTask(()->{
                    String a = "Maor the Quin";
                    String b = " and Matan the King";
                    return a+b;}
                , TaskType.OTHER);
        var string_1  = customExecutor.submit(task2);
        assertEquals("Maor the Quin and Matan the King", string_1.get());

        // Use the Factory method without a TaskType.
        Task<Integer> task3 = Task.createTask(() -> {
            Thread.sleep(1000);
            return 1;
        });
        assertEquals(TaskType.OTHER, task3.getTaskType());//Check that the default is OTHER = 3.

        //Use the Factory method with a TaskType.
        Task<Integer> task4 = Task.createTask(() -> {
            Thread.sleep(1000);
            return 1;
        }, TaskType.COMPUTATIONAL);
        //Check that the default task type is set correctly and the compare method
        assertEquals(-1, task4.compareTo(task3));
    }



    @Test
    public void partialTest() {
        CustomExecutor customExecutor = new CustomExecutor();
        var task = Task.createTask(() -> {
            int sum = 0;
            for (int i = 1; i <= 10; i++) {
                sum += i;
            }
            return sum;
        }, TaskType.COMPUTATIONAL);
        var sumTask = customExecutor.submit((Callable) task);
        final int sum;
        try {
            sum = (int) sumTask.get(400, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Sum of 1 through 10 = " + sum);
        Callable<Double> callable1 = () -> {
            return 1000 * Math.pow(1.02, 5);
        };
        Callable<String> callable2 = () -> {
            StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
            return sb.reverse().toString();
        };
        // var is used to infer the declared type automatically
        var priceTask = customExecutor.submit(() -> 1000 * Math.pow(1.02, 5), TaskType.IO);
        var reverseTask = customExecutor.submit(callable2, TaskType.IO);
        final Double totalPrice;
        final String reversed;
        try {
            totalPrice = priceTask.get();
            reversed = reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        for (int i = 1; i < 50; i++) {
            customExecutor.submit(() -> {
                Thread.sleep(1000);
                return 1000 * Math.pow(1.021, 5);
            }, TaskType.IO);
        }
        TaskType tt = TaskType.COMPUTATIONAL;
        customExecutor.submit(() -> {
            return 1000 * Math.pow(1.022, 5);
        },TaskType.OTHER);


        logger.info(() -> "Reversed String = " + reversed);
        logger.info(() -> "Total Price = " + totalPrice);
        logger.info(() -> "Current maximum priority = " +
                customExecutor.getCurrentMax());
        customExecutor.gracefullyTerminate();
    }
}
