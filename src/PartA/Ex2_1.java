package PartA;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;



public class Ex2_1 {

    /**
     * this function sum the amount of lines in each file
     * @param file
     * @return the amount of lines
     */
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

    /**
     * this function create text files, put each file in the array
     * and write to each one a random amount of lines
     * @param n -represent the amount of files
     * @param seed - used to initialize a pseudorandom number generator
     * @param bound -the max integer that the random number can be
     * @return arrays of files
     */
    public static String[] createTextFiles(int n, int seed, int bound){
        String[] files = new String[n];
        Random rand = new Random(seed);
        for (int i = 0; i < n; i++) {
            String name= "file_" + (i+1)+ ".txt";
            files[i] = name;
            try {
                FileWriter myWriter = new FileWriter(name);
                int random = rand.nextInt(bound);
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

    /**
     * this function take the array of files and count the amount of line in each file and sum it together
     * @param fileNames- array of files names
     * @return the amount of lines in all files
     */
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

    /**
     * this function use threads to sum all lines in all files
     * we take the array of files and for each file use a diffrent thread to calculate the amount of lines
     * and add that to the object sum
     * @param fileNames- array of files names
     * @return the amount of lines in all files
     */
    public int getNumOfLinesThreads(String[] fileNames) {
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

    /**
     * this function sum the lines of all the files using ThreadPool
     *
     * @param fileNames- array of files names
     * @return the amount of lines in all files
     */
    public int getNumOfLinesThreadPool(String[] fileNames) {
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



}



