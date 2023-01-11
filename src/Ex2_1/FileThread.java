package Ex2_1;

import static Ex2_1.Ex2_1.sum_lines;

/**
 * this class is extends from thread class that implemented by runnable interface
 * it represents a task that can return a value
 * this class create object that call file_name - for calculate the sum lines in each file
 */
public class FileThread extends java.lang.Thread {
    String file_name;
    int sum_lines;

    /**
     * this constructor its implements all the objects that thread have
     * and make the string file_name
     * @param file_name- each file
     */
    public FileThread(String file_name, String name) {
        super(name);
        this.file_name = file_name;
    }

    /**
     * This method takes no arguments and returns a value of the sum of lines in each file
     * @return the amount of lines in the file
     */
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
