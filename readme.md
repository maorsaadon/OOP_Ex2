
# Ex2_PartA

## Ex2_1

In this class we using radnom class to get a radnom genertor number to decide the amount of lines to put in each file.\
this is what random class do:\
<ins>**Random**</ins>\
The Random class is used to generate random numbers. 
It provides several methods for generating different types of random values, such as nextInt() for generating a random integer and nextDouble() for generating a random double-precision floating-point value.
The Random class uses a seed value to initialize the random number generator. The same seed value will always produce the same sequence of random numbers.
This can be useful for testing or debugging, as it allows you to reproduce the same random values over and over again.

after this we made all 4 function to calculate the sum lines of all files and the time it took for each function.  
the diffrent in each function is in what way he calculate the sum lines.  

first function is to creates txt files and to random generate amount of lines for each file and put each file in a array.  
  
```java
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
```
second function is to calculate the sum lines of all files in the array and return the sum.  
```java
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
```
third function is to calculate the sum lines of all files in the array using threads.  
```java
/**
     * this function use threads to sum all lines in all files
     * we take the array of files and for each file use a diffrent thread to calculate the amount of lines
     * and add that to the object sum
     * @param fileNames- array of files names
     * @return the amount of lines in all files
     */
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
```
forth function is to calculate the sum lines of all files in the array using threadPool.  
```java
/**
     * this function sum the lines of all the files using ThreadPool
     *
     * @param fileNames- array of files names
     * @return the amount of lines in all files
     */
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
```

## The main classes

1.Thread\
2.ThreadPool


<ins>**Thread**</ins>

Thread is a separate flow of execution in a program. A Java program can have multiple threads running concurrently, each performing a different task.\
the way we create a new thread is:
We extending the Thread class: To create a new thread by extending the Thread class, we need to override the run() method, which is the entry point for the new thread.
Then, we can create an instance of our subclass and call its start() method to start the new thread(that happend in our Ex2_1 class).

```java
import static Ex2.Ex2_1.sum_lines;

public class FileThread extends java.lang.Thread {
    String file_name;
    int sum_lines;

    public FileThread(String file_name, String name) {
        super(name);
        this.file_name = file_name;
    }

    @Override
    public void run() {
        setSum_lines(sum_lines(file_name));
    }

    public int setSum_lines(int sum_lines){
        return this.sum_lines += sum_lines;
    }
    public int getSum_lines(){
        return sum_lines;
    }

}
```

<ins>**ThreadPool**</ins>

A thread pool is a group of pre-initialized, reusable threads that can be used to execute tasks concurrently.
ThreadPools are used to manage the execution of multiple threads and to reduce the overhead of creating and destroying threads for short-lived tasks.

A Callable is a functional interface that represents a task that can return a value and may throw an exception.
It is similar to the Runnable interface, but it can return a value and is designed to be used with the Executor framework.
To create a Callable in Java, you need to implement the call() method.
This method takes no arguments and returns a value of the generic type V. It can also throw a checked exception.

```java
import java.util.concurrent.*;

import static Ex2.Ex2_1.sum_lines;

/**
 * this class is implemented by callable functional interface
 * it represents a task that can return a value and may throw an exception
 * this class create object that call file_name - for calculate the sum lines in each file
 */
public class FileThreadCallable implements Callable {
    String file_name;

    /**
     * this constructor its implementing all the objects that callable have
     * and make the string file_name
     * @param file_name- each file
     */
    public FileThreadCallable(String file_name) {
        super();
        this.file_name = file_name;
    }

    /**
     * This method takes no arguments and returns a value of the sum of lines in each file
     * @return the amount of lines in the file
     * @throws Exception- if the call function will not work
     */
    @Override
    public Object call() throws Exception {
        return sum_lines(file_name);
    }
}
```

## UML diagram of the classes

![image](https://user-images.githubusercontent.com/118104946/211318064-889235e8-fb96-4a1f-84d6-b840f6c109ad.png)

## compare running times

![image](https://user-images.githubusercontent.com/118104946/211316585-58fe31a6-c0a8-4751-a37c-be17562f15f0.png)

we can see that the run time of the sum lines using ThreadPool is less then the rest when checking for large amount of files, 
because `ThreadPool` is using a reuse threads, rather then creating a new thread for each task, create a new thread cost more.
and he supports multiple tasks, whereas the Threads class supports a single task.
The ThreadPool is designed to submit and execute multiple tasks,so because of that it will calculate all the files faster then thread.  

secondly we can see that thread fast then the second function(sum lines in normal way), because it perform multiple tasks concurrently.
creating a new thread is generally faster and requires fewer resources than creating a new process.
This is because creating a new thread involves creating a new execution path within an existing process, while creating a new process involves creating a completely new instance of a program.
In addition, the running time of a thread may be affected by the presence of other concurrent threads within the same process.
If multiple threads within a process are competing for the same resources, this can lead to delays and reduced performance.
On the other hand, processes are typically isolated from one another, so the presence of other processes should not have a significant impact on the running time of a particular process.








## Download & run the program

In order to run this project follow this steps:  
  1. Download zip from our reposetory : push on code -> Download zip.  
  2. Extract all on yor computer.  
  3. Open a new java project.  
  4. Open the file in your java's workspace.  
  5. go to Ex2_1 class.  
  6. go to main function.  
  7. Run the project.  
