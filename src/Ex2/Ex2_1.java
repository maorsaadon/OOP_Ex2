import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.*;
import java.util.Random;
import Thread.java;


public class Ex2_1 {
    public static class MyCustomFormatter extends Formatter {
        @Override
        public String format(LogRecord message) {
            StringBuffer sb = new StringBuffer();
            sb.append(message.getLevel());
            sb.append(": ");
            sb.append(message.getMessage());
            return sb.toString();
        }
    }


    public static int sum_lines(String file) {
        int currentSum = 0;
        try {
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String lines = reader.readLine();
            while (lines != null) {
                currentSum++;
                lines = reader.readLine();
            }
        } catch (IOException exe) {
            exe.printStackTrace();
        }

        return currentSum;
    }


    public static String[] createTextFiles(int n, int seed, int bound) {

        String[] fhs = new String[n];
        for (int i = 1; i <= n; i++) {

            fhs[i - 1] = "file_" + i;
            Logger logger = Logger.getLogger("MyLog");
            FileHandler fh;
            try {
                // This block configure the logger with handler and formatter
                fh = new FileHandler("C:\\JAVA\\OOP_Ex2\\file_" + i, true);
                logger.addHandler(fh);
                SimpleFormatter formatter = new SimpleFormatter();
                fh.setFormatter(formatter);
                fh.setFormatter(new MyCustomFormatter());

                Random rand = new Random(seed);
                int random = rand.nextInt(bound);
                for (int j = 1; j < random; j++) {
                    logger.info("i love life, she love lif, he love life, they love life, we all love life\n");
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fhs;
    }

    public static int getNumOfLines(String[] fileNames) {
        int counter = 0;
        for (int i = 0; i < fileNames.length; i++) {
            counter += sum_lines(fileNames[i]);
        }
        return counter;
    }

    public int getNumOfLinesThreads(String[] fileNames) {
        for (int i = 0; i < fileNames.length; i++) {
            Thread t1 = new Thread(fileNames[i]);
        }
    }

//    public int getNumOfLinesThreadPool(String[] fileNames) {
//    }


    public static void main(String[] args) {
        //1
        String[] str = createTextFiles(2, 1, 10);
        System.out.println(Arrays.toString(str));
        //2
        System.out.println(getNumOfLines(str));
        //3
    }
}



