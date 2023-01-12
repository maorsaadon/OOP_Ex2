package Ex2_1;
import org.junit.jupiter.api.*;

import static Ex2_1.Ex2_1.RunningTime;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertThrows;


public class Test_Ex2_1 {
    static int n = 1000;
    static int seed = (int)(Math.random()*100);
    static int bound = 99999;

    @BeforeAll
    public static void runOnceBefore() {
        System.out.println("Start running for input:\nNumber of files: " + n + "\nSeed: " + seed + "\nBound: " + bound);
    }
    @org.junit.jupiter.api.Test
    void testCreateTextFiles() {
        Throwable exception1 = assertThrows(IllegalArgumentException.class,
                ()->{Ex2_1.createTextFiles(100,10,-1000);});

        Throwable exception2 = assertThrows(NegativeArraySizeException.class,
                ()->{Ex2_1.createTextFiles(-100,10,1000);});
    }

    @org.junit.jupiter.api.Test
    void testRunningTime() throws InterruptedException {
        RunningTime(n, seed, bound);
    }


    @AfterAll
    public static void runOnceAfter_deleteFiles() {
        System.out.println("\nTests ended successfully!");
    }

}








