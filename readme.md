Random
The Random class is used to generate random numbers. 
It provides several methods for generating different types of random values, such as nextInt() for generating a random integer and nextDouble() for generating a random double-precision floating-point value.
The Random class uses a seed value to initialize the random number generator. The same seed value will always produce the same sequence of random numbers.
This can be useful for testing or debugging, as it allows you to reproduce the same random values over and over again.

Thread
Thread is a separate flow of execution in a program. A Java program can have multiple threads running concurrently, each performing a different task.
the way we create a new thread is:
Extending the Thread class: To create a new thread by extending the Thread class, you need to override the run() method, which is the entry point for the new thread.
Then, you can create an instance of your subclass and call its start() method to start the new thread.

ThreadPool
A thread pool is a group of pre-initialized, reusable threads that can be used to execute tasks concurrently.
ThreadPools are used to manage the execution of multiple threads and to reduce the overhead of creating and destroying threads for short-lived tasks.

A Callable is a functional interface that represents a task that can return a value and may throw an exception.
It is similar to the Runnable interface, but it can return a value and is designed to be used with the Executor framework.
To create a Callable in Java, you need to implement the call() method.
This method takes no arguments and returns a value of the generic type V. It can also throw a checked exception.






















The Main Interface
1.Sender
2.Member

1.Sender
This interface describes the Observerable (update sender). This interface hold the function that the GroupAdmin actualize.

Declaration
import observer.Sender;  

//methods to register and unregister observers
void register(Member obj);
void unregister(Member obj) throws Exception;

//Inserts the string into this character sequence.
void insert(int offset, String obj);

// Appends the specified string to this character sequence.
void append(String obj);

// Removes the characters in a substring of this sequence.
void delete(int start, int end);

// Erases the last change done to the document, reverting it to an older state. 
void undo();
2.Member
This interface describes the Observer (update reciever). This interface hold the function that ConcreteMember actualize.

Declaration
import observer.Member;

public void update(UndoableStringBuilder usb);
The Main Classes
1.UndoableStringBuilder
2.AdminGroup
3.ConcreteMember

1.UndoableStringBuilder
This class use the StringBuilder class methods to make our UndoableStringBuilder class.
We crate methods using the methods that StringBuilder have.
The idea of this class is to give us the latest thing that the object had.
This UndoableStringBuilder class take a StringBuilder and give us the perv word or sentence that this StringBuilder had with using stack.

2.GroupAdmin
This class actualize the functions of the Sender that describe the Observerbale, and we also have function that notify the observers and function to return the size of the member.
GroupAdmin contains the state pool (UdoableStringBuilder) and also contains an HashSet that holds all customers who should receive updates on any changes made to the state pool , the class has a constructor that builds a GroupAdmin from UdoableStringBuilder and HashSet. In every function we call notify() in order to update all the member about every change that has been made to the UndoableStringBuilder.

notify(HashSet<Member>);
Usage
import observer.GroupAdmin;  

//Add the Member to the GroupAdmin.  
GroupAdmin.register(Member);

//Remove the Member to the GroupAdmin.  
GroupAdmin.unregister(Member);

//Inserts the string into this character sequence.  
GroupAdmin.insert(int,String);

//Appends the specified String to this  character sequence.  
GroupAdmin.append(String);

//Removes the characters in a substring of this sequence.  
GroupAdmin.delete(int, int);

//Performs a return to the previous state.  
GroupAdmin.undo();

//Return the number of the members
GroupAdmin.getSizeMember();
3.ConcreteMember
This class actualize the functions of the Member that describe the Observer, and we also have function to getData().
ConcreteMember contains String that update every time the GroupAdmin notify about a change in other word its a copy (copy sallow) of the UndoableStringBuilder

Usage
import observer.ConcreteMember;

//Update the String that ConcreteMember hold according to change of the UndoableStringBuilder of the GroupAdmin
ConcreteMember.update(UndoableStringBuilder);

//Return the String that ConcreteMember hold
ConcreteMember.getData();
Download & run the program
In order to run this project follow this steps:

Download zip from our reposetory : push on code -> Download zip.
Extract all on yor computer.
Open a new java project.
Open the file in your java's workspace.
Press on pom.xml.
Press ok.
Run the project.
