package PartA;

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
