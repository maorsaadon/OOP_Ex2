package PartA;

import java.util.Arrays;

import static PartA.Ex2_1.createTextFiles;
import static PartA.Ex2_1.getNumOfLines;
import java.io.File;

public class Ex2 {
    public static void main(String[] args) {
        Ex2_1 x= new Ex2_1();
        //1
        String[] str = createTextFiles(50, 50, 1000);
        System.out.println("part A, Function 1, done : " + Arrays.toString(str));
        //2
        System.out.println("part A, Function 2, done : " + getNumOfLines(str));
        //3
        System.out.println("part A, Function 3, done : " + x.getNumOfLinesThreads(str));
        //4
        System.out.println("part A, Function 4, done : " + x.getNumOfLinesThreadPool(str));

        deleteFiles(100);
    }

    public static void deleteFiles(int n){
        for(int i=0;i<n;i++){
            String name = "file_"+i+".txt";
            File file = new File(name);
            file.deleteOnExit();
        }
    }

}
