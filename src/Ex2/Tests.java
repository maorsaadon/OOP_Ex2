package Ex2_2;

import org.junit.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import java.util.concurrent.*;

public class Tests {
    public static final Logger logger = LoggerFactory.getLogger(Tests.class);

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
        var sumTask = customExecutor.submit(task);
        final int sum;
        try {
            sum = (int) sumTask.get(1, TimeUnit.MILLISECONDS);
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
        var priceTask = customExecutor.submit(() -> {
            return 1000 * Math.pow(1.02, 5);
        }, TaskType.COMPUTATIONAL);
        var reverseTask = customExecutor.submit(callable2, TaskType.IO);
        final Double totalPrice;
        final String reversed;
        try {
            totalPrice = (Double) priceTask.get();
            reversed = (String) reverseTask.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        logger.info(() -> "Reversed String = " + reversed);
        logger.info(() -> String.valueOf("Total Price = " + totalPrice));
        logger.info(() -> "Current maximum priority = " +
                customExecutor.getCurrentMax());
                customExecutor.gracefullyTerminate();
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