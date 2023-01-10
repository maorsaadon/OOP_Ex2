
# OOP_EX2

This project is split into two parts, but in both parts the plan is based on the following cornerstones:  
  1. Callable
  2. Future   
  3. Thread
  4. Thread Pool


### Callable
A task that returns a result and may throw an exception. Implementors define a single method with no arguments called call.  
The Callable interface is similar to Runnable, in that both are designed for classes whose instances are potentially executed by another thread.   
A Runnable, however, does not return a result and cannot throw a checked exception.  
The Executors class contains utility methods to convert from other common forms to Callable classes.  

### Future

A Future represents the result of an asynchronous computation. Methods are provided to check if the computation is complete, to wait for its completion, and to retrieve the result of the computation. The result can only be retrieved using method get when the computation has completed, blocking if necessary until it is ready. Cancellation is performed by the cancel method. Additional methods are provided to determine if the task completed normally or was cancelled. Once a computation has completed, the computation cannot be cancelled. If you would like to use a Future for the sake of cancellability but not provide a usable result, you can declare types of the form Future<?> and return null as a result of the underlying task.

When the _call()_ method completes, answer must be stored in an object known to the main thread, so that the main thread can know about the result that 
the thread returned. How will the program store and obtain this result later?  
For this, a Future object can be used. Think of a Future as an object that holds the result – it may not hold it right now, but it will do so in the future 
(once the Callable returns).    
Thus, a Future is basically one way the main thread can keep track of the progress and result from other threads.  
To implement this interface, 5 methods have to be overridden, but as the example below uses a concrete implementation from the library,  
only the important methods are listed here.  
Observe that Callable and Future do two different things – Callable is similar to Runnable, in that it encapsulates a task that   
is meant to run on another thread, whereas a Future is used to store a result obtained from a different thread.  
In fact, the Future can be made to work with Runnable as well, which is something that will become clear when Executors come into the picture.  

## Thread

A thread is a thread of execution in a program. The Java Virtual Machine allows an application to have multiple threads of execution running concurrently.
Every thread has a priority. Threads with higher priority are executed in preference to threads with lower priority. Each thread may or may not also be marked as a daemon. When code running in some thread creates a new Thread object, the new thread has its priority initially set equal to the priority of the creating thread, and is a daemon thread if and only if the creating thread is a daemon.

When a Java Virtual Machine starts up, there is usually a single non-daemon thread (which typically calls the method named main of some designated class). The Java Virtual Machine continues to execute threads until either of the following occurs:
 - The exit method of class Runtime has been called and the security manager has permitted the exit operation to take place.
 - All threads that are not daemon threads have died, either by returning from the call to the run method or by throwing an exception that propagates beyond the run method.

There are two ways to create a new thread of execution. One is to declare a class to be a subclass of Thread. This subclass should override the run method of class Thread. An instance of the subclass can then be allocated and started.

The _run()_ method of thread class is called if the thread was constructed using a separate Runnable object otherwise this method does nothing and
returns. When the _run()_ method calls, the code specified in the _run()_ method is executed. You can call the _run()_ method multiple times.

The _start()_ method of thread class is used to begin the execution of thread. The result of this method is two threads that are running concurrently:  
the current thread (which returns from the call to the __start()__) and the other thread (which executes its __run()__).

When a program calls the start() method, a new thread is created and then the run() method is executed.   
But if we directly call the run() method then no new thread will be created and run() method will be executed as a normal method call on the current
calling thread itself and no multi-threading will take place.

## Thread Pool

In Java, threads are mapped to system-level threads, which are the operating system's resources. If we create threads uncontrollably, we may run out of these resources quickly.
The operating system does the context switching between threads as well — in order to emulate parallelism. A simplistic view is that the more threads we spawn, the less time each thread spends doing actual work.
The Thread Pool pattern helps to save resources in a multithreaded application and to contain the parallelism in certain predefined limits.
When we use a thread pool, we write our concurrent code in the form of parallel tasks and submit them for execution to an instance of a thread pool. This instance controls several re-used threads for executing these tasks.
The pattern allows us to control the number of threads the application creates and their life cycle. We're also able to schedule tasks' execution and keep incoming tasks in a queue.

## PartA

In this part we will create several text files and calculate the total number of lines in these files.  
We will use three methods:  
• Normal method without using threads  
• Use of threads  
• Using ThreadPool  

### The main classes

1. Ex2_1 
2. FileThread  
3. FileThreadCallable 

<ins>**Ex2_1**</ins>  

In this class we using radnom class to get a radnom genertor number to decide the amount of lines to put in each file.  

after this we made all 4 function to calculate the sum lines of all files and the time it took for each function.  
the diffrent in each function is in what way he calculate the sum lines.  

first function is to creates txt files and to random generate amount of lines for each file and put each file in a array.  
  
    public static String[] createTextFiles(int n, int seed, int bound)
       
second function is to calculate the sum lines of all files in the array and return the sum.  

    public static int getNumOfLines(String[] fileNames) 
       
third function is to calculate the sum lines of all files in the array using threads.  

    public static int getNumOfLinesThreads(String[] fileNames) 
       
forth function is to calculate the sum lines of all files in the array using threadPool.  

    public static int getNumOfLinesThreadPool(String[] fileNames) 
       
<ins>**FileThread**</ins>

Thread is a separate flow of execution in a program. A Java program can have multiple threads running concurrently, each performing a different task.  
the way we create a new thread is:
We extending the Thread class: To create a new thread by extending the Thread class, we need to override the run() method, which is the entry point for the new thread. 
Then, we can create an instance of our subclass and call its start() method to start the new thread(that happend in our Ex2_1 class).  

<ins>**FileThreadCallable**</ins>

To calculate the number of lines of all the files using ThreadPool, we will build a helper class that implements the Callable interface, which has a method
call calculates the number of lines of one file.  

A thread pool is a group of pre-initialized, reusable threads that can be used to execute tasks concurrently.
ThreadPools are used to manage the execution of multiple threads and to reduce the overhead of creating and destroying threads for short-lived tasks.

A Callable is a functional interface that represents a task that can return a value and may throw an exception.
It is similar to the Runnable interface, but it can return a value and is designed to be used with the Executor framework.
To create a Callable in Java, you need to implement the call() method.
This method takes no arguments and returns a value of the generic type V. It can also throw a checked exception.  

This class is implemented by callable functional interface, it represents a task that can return a value and may throw an exception.  
This class create object that call file_name - for calculate the sum lines in each file.  

### UML diagram of the classes

![image](https://user-images.githubusercontent.com/118104946/211318064-889235e8-fb96-4a1f-84d6-b840f6c109ad.png)

### compare running times

Number of files | getNumOfLines | getNumOfLinesThreads | getNumOfLinesThreadPool
| :---: | :---: | :---: | :---: |
100 | 143 ms | 141 ms | 71 ms
300 | 1835 ms | 1581 ms | 442 ms
500 | 2488 ms | 2387 ms | 726 ms

We can see that the run time of the sum lines using ThreadPool is less then the rest when checking for large amount of files, 
because `ThreadPool` is using a reuse threads, rather then creating a new thread for each task, create a new thread cost more.
and he supports multiple tasks, whereas the Threads class supports a single task.
The ThreadPool is designed to submit and execute multiple tasks,so because of that it will calculate all the files faster then thread.  

secondly we can see that thread fast then the second function(sum lines in normal way), because it perform multiple tasks concurrently.
creating a new thread is generally faster and requires fewer resources than creating a new process.
This is because creating a new thread involves creating a new execution path within an existing process, while creating a new process involves creating a completely new instance of a program.
In addition, the running time of a thread may be affected by the presence of other concurrent threads within the same process.
If multiple threads within a process are competing for the same resources, this can lead to delays and reduced performance.
On the other hand, processes are typically isolated from one another, so the presence of other processes should not have a significant impact on the running time of a particular process.

### Download & run the program

In order to run this project follow this steps:  
  1. Download zip from our reposetory : push on code -> Download zip.  
  2. Extract all on yor computer.  
  3. Open a new java project.  
  4. Open the file in your java's workspace.  
  5. go to Ex2 class.   
  7. Run the project.  
  
## PartB
  
  Int this part we will create a new type that represents an asynchronous task with priority and a new ThreadPool type that supports owning tasks priority.  
  
### The main classes

  1. TaskType
  2. Task   
  3. CustomExecutor 

<ins>**TaskType**</ins>

 This class present enum TaskType which describes the task type and its priority

<ins>**Task**</ins>

 Represents an operation that can be run asynchronously and can return a value of some type.  
 The class Task<T> extend from FutureTask<T> and implements Comparable<Task<T>>, Callable<T>.  
 The class Task<T> hold TaskType object and Callable<T>.  
 Task's methods:

<ins>**CustomExecutor**</ins>
  
  Represents a new type of ThreadPool that supports a priority queue tasks (Each task in the queue is of type Task).  
  CustomExecutor will create a Task before putting it in the queue by passing Callable<T> and an enum of TaskType type.  
  CustomExecutor will execute the tasks according to their priority.  
  CustomExecutor's methods:
  
### UML diagram of the classes
  
  ![ClassDiagram_PartB](https://user-images.githubusercontent.com/105555708/211603790-d34cd191-3551-41ff-b3b1-33e224a64875.png)
  
### Download & run the program

  In order to run this project follow this steps:  
  1. Download zip from our reposetory : push on code -> Download zip.  
  2. Extract all on yor computer.  
  3. Open a new java project.  
  4. Open the file in your java's workspace.  
  5. Make a main in order to use the project as you like.  
  
