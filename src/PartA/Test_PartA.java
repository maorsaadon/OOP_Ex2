package PartA;
import org.junit.jupiter.api.*;

import java.io.File;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class Tests {

    static String[] fileNames;
    static Ex2_1 test = new Ex2_1();

    public static void deleteFiles(int n){
        for(int i=1;i<=n;i++){
            String name = "file_"+i+".txt";
            File file = new File(name);
            file.deleteOnExit();
        }
    }

    @BeforeAll
    public static void runOnceBefore() {
        fileNames = Ex2_1.createTextFiles(1000,(int)(Math.random()*100),99999);
        System.out.println("Starting testes...");
    }


    @BeforeEach
    public void runBeforeEach() {
        System.out.println("----------------------------------------");
        System.out.println("Running new test...");
    }

    @AfterEach
    public void runAfterEach() {
        System.out.println("Finished test.\n");

    }

    @org.junit.jupiter.api.Test
    void getNumOfLines() {
        long start = System.currentTimeMillis();
        int x = Ex2_1.getNumOfLines(fileNames);
        System.out.println("[NORMAL] Total lines " + x + ". \n Time: " + (System.currentTimeMillis() - start) + "ms");
    }

    @org.junit.jupiter.api.Test
    void getNumOfLinesThreads() {
        long start = System.currentTimeMillis();
        int x = test.getNumOfLinesThreads(fileNames);
        System.out.println("[THREAD] Total lines " + x + ". Time: " + (System.currentTimeMillis() - start) + "ms");
    }

    @org.junit.jupiter.api.Test
    void getNumOfLinesThreadPool() {
        long start = System.currentTimeMillis();
        int x = test.getNumOfLines(fileNames);
        System.out.println("[THREAD POOL] Total lines " + x + ". \n Time: " + (System.currentTimeMillis() - start) + "ms");
    }

    @AfterAll
    public static void runOnceAfter() {
        System.out.println("\nTestes ended!");
        deleteFiles(1000);
    }



}




