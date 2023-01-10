
#OOP_EX2

In this assignment create several text files and calculate the total number of lines in these files.  
We will use three methods:  
• Normal method without using threads  
• Use of threads  
• Using ThreadPool  

## The main classes

1.Thread  
2.ThreadPool   
3.Ex2_1  

<ins>**Ex2_1**</ins>  

In this class we using radnom class to get a radnom genertor number to decide the amount of lines to put in each file.  

after this we made all 4 function to calculate the sum lines of all files and the time it took for each function.  
the diffrent in each function is in what way he calculate the sum lines.  

first function is to creates txt files and to random generate amount of lines for each file and put each file in a array.  
  
    public static String[] createTextFiles(int n, int seed, int bound)
       
second function is to calculate the sum lines of all files in the array and return the sum.  

    public static int getNumOfLines(String[] fileNames) {
       
third function is to calculate the sum lines of all files in the array using threads.  

    public static int getNumOfLinesThreads(String[] fileNames) {
       
forth function is to calculate the sum lines of all files in the array using threadPool.  

    public static int getNumOfLinesThreadPool(String[] fileNames) {
       
<ins>**Thread**</ins>

Thread is a separate flow of execution in a program. A Java program can have multiple threads running concurrently, each performing a different task.\
the way we create a new thread is:
We extending the Thread class: To create a new thread by extending the Thread class, we need to override the run() method, which is the entry point for the new thread.
Then, we can create an instance of our subclass and call its start() method to start the new thread(that happend in our Ex2_1 class).

```java
import static PartA.Ex2_1.sum_lines;

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

    public int setSum_lines(int sum_lines) {
        return this.sum_lines += sum_lines;
    }

    public int getSum_lines() {
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

import static PartA.Ex2_1.sum_lines;

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
