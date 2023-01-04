package Ex2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.*;
import java.util.Random;



public class Ex2_1 {
    public static class MyCustomFormatter extends Formatter {
        @Override
        public String format(LogRecord message) {
            StringBuffer sb = new StringBuffer();
            sb.append(message.getLevel());
            sb.append(": ");
            sb.append(message.getMessage());
            return sb.toString();
        }
    }


    public static int sum_lines(String file) {
        int currentSum = 0;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String lines = reader.readLine();
            while (lines != null) {
                currentSum++;
                lines = reader.readLine();
            }
        } catch (IOException exe) {
            exe.printStackTrace();
        }
        return currentSum;
    }


    public static String[] createTextFiles(int n, int seed, int bound) {
        System.out.println("creating " + n + " files with random number of lines from seed: " + seed +
                " to bound: " + bound);
        String[] fhs = new String[n];
        Random rand = new Random(seed);
        int random = rand.nextInt(bound);
        for (int i = 1; i <= n; i++) {

            fhs[i - 1] = "file_" + i;
            Logger logger = Logger.getLogger("MyLog");
            FileHandler fh;
            try {
                // This block configure the logger with handler and formatter
                fh = new FileHandler("C:\\JAVA\\OOP_Ex2\\file_" + i, true);
                logger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
                fh.setFormatter(new MyCustomFormatter());

                for (int j = 1; j < random; j++) {
                    logger.info("i love life, she love lif, he love life, they love life, we all love life\n");
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fhs;
    }

    public static int getNumOfLines(String[] fileNames) {
        long start = System.currentTimeMillis();
        int counter = 0;
        for (int i = 0; i < fileNames.length; i++) {
            counter += sum_lines(fileNames[i]);
        }
        long end = System.currentTimeMillis();
        long elapsedTime= end - start;
        System.out.println("Function number 1 run for :" + elapsedTime);
        return counter;
    }

    public static int getNumOfLinesThreads(String[] fileNames) {
        long start = System.currentTimeMillis();
        int sum = 0;
        FileThread[] arr = new FileThread[fileNames.length];
        for (int i = 0; i < arr.length; i++)
            arr[i] = new FileThread(fileNames[i], "Thread_"+i);
        for (int i = 0; i < arr.length; i++)
            arr[i].start();
        for (int i = 0; i < arr.length; i++) {
            try {
                arr[i].join();
                sum+= arr[i].getSum_lines();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        long elapsedTime= end - start;
        System.out.println("Function number 1 run for :" + elapsedTime);
        return sum;
    }

    public static int getNumOfLinesThreadPool(String[] fileNames) {
        long start = System.currentTimeMillis();
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(fileNames.length);
        int sum = 0;
        ArrayList<Future<Integer>> future = new ArrayList<Future<Integer>>();
        for (int i = 0; i < fileNames.length; i++) {
            Future<Integer> f = pool.submit(new FileThreadCallable(fileNames[i]));
            future.add(f);
        }
        for (int i = 0; i < fileNames.length; i++) {
            try {
                Future<Integer> f = future.get(i);
                sum+= f.get();
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            };
        }
        long end = System.currentTimeMillis();
        long elapsedTime= end - start;
        System.out.println("Function number 1 run for :" + elapsedTime);
        return sum;
    }


    public static void main(String[] args) {
        //1
        String[] str = createTextFiles(2, 1, 30);
        System.out.println("part A, Function 1, done : " + Arrays.toString(str));
        //2
        System.out.println("part A, Function 2, done : " + getNumOfLines(str));
        //3
        System.out.println("part A, Function 3, done : " + getNumOfLinesThreads(str));
        //4
        System.out.println("part A, Function 4, done : " + getNumOfLinesThreadPool(str));
    }
}



