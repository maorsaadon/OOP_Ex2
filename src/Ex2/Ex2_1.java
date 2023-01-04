package Ex2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
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

    public static String[] createTextFiles(int n, int seed, int bound){
        String[] files = new String[n];
        Random rand = new Random(seed);
        int random = rand.nextInt(bound);
        for (int i = 0; i < n; i++) {
            String name= "file_" + (i+1)+ ".txt";
            files[i] = name;
            try {
                FileWriter myWriter = new FileWriter(name);
                for (int j = 0; j < random; j++) {
                    myWriter.write("i love life, she love lif, he love life, they love life, we all love life\n");
                }
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }


        }
        return files;
    }

    public static int getNumOfLines(String[] fileNames) {
        long start = System.currentTimeMillis();
        int counter = 0;
        for (int i = 0; i < fileNames.length; i++) {
            counter += sum_lines(fileNames[i]);
        }
        long end = System.currentTimeMillis();
        System.out.println("Function 'getNumOfLines' run for: " + (end - start) + " ms.");
        return counter;
    }

    public static int getNumOfLinesThreads(String[] fileNames) {
        long start = System.currentTimeMillis();
        int sum = 0;
        FileThread[] arr = new FileThread[fileNames.length];
        for (int i = 0; i < arr.length; i++){
            arr[i] = new FileThread(fileNames[i], "Thread_" + i);
            arr[i].start();
            try {
                arr[i].join();
                sum+= arr[i].getSum_lines();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Function 'getNumOfLinesThreads' run for: " + (end - start) + " ms.");
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
        for (Future<Integer> f : future) {
            try {
                sum+= f.get();
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {};

        }

        pool.shutdown();
        long end = System.currentTimeMillis();
        System.out.println("Function 'getNumOfLinesThreadPool' run for: " + (end - start) + " ms.");
        return sum;
    }


    public static void main(String[] args) {
        //1
        String[] str = createTextFiles(5, 1, 30);
        System.out.println("part A, Function 1, done : " + Arrays.toString(str));
        //2
        System.out.println("part A, Function 2, done : " + getNumOfLines(str));
        //3
        System.out.println("part A, Function 3, done : " + getNumOfLinesThreads(str));
        //4
        System.out.println("part A, Function 4, done : " + getNumOfLinesThreadPool(str));
    }
}



