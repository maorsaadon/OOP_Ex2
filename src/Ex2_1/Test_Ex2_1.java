package Ex2_1;
import org.junit.jupiter.api.*;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertThrows;


public class Test_Ex2_1 {

    static String[] filesNames;
    static Ex2_1 test = new Ex2_1();


    @BeforeAll
    public static void runOnceBefore() {
        filesNames = Ex2_1.createTextFiles(1000,(int)(Math.random()*100),99999);
        System.out.println("Starting testes...");
    }


    @BeforeEach
    public void runBeforeEach() {
        System.out.println("----------------------------------------------");
    }

    @AfterEach
    public void runAfterEach() {
        System.out.println("Finished test.\n");

    }

    @org.junit.jupiter.api.Test
    void InCorrectInput() {
        Throwable exception1 = assertThrows(IllegalArgumentException.class,
                ()->{Ex2_1.createTextFiles(100,10,-1000);});

        Throwable exception2 = assertThrows(NegativeArraySizeException.class,
                ()->{Ex2_1.createTextFiles(-100,10,1000);});
    }

    @org.junit.jupiter.api.Test
    void getNumOfLines() {
        long start = System.currentTimeMillis();
        int x = Ex2_1.getNumOfLines(filesNames);
        System.out.println("part A, Function 2, sum of lines: " + x + ".\nTime: " + (System.currentTimeMillis() - start) + "ms");
    }

    @org.junit.jupiter.api.Test
    void getNumOfLinesThreads() {
        long start = System.currentTimeMillis();
        int x = test.getNumOfLinesThreads(filesNames);
        System.out.println("part A, Function 3, sum of lines: " + x + ".\nTime: " + (System.currentTimeMillis() - start) + "ms");
    }

    @org.junit.jupiter.api.Test
    void getNumOfLinesThreadPool() {
        long start = System.currentTimeMillis();
        int x = test.getNumOfLinesThreadPool(filesNames);
        System.out.println("part A, Function 4, sum of lines: " + x + ".\nTime: " + (System.currentTimeMillis() - start) + "ms");
    }

    @AfterAll
    public static void runOnceAfter_deleteFiles() {
        System.out.println("\nTests ended successfully!");
    }

}








