import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Thread extends java.lang.Thread {
    String name;
    int sum;

    public Thread(String name, int loop) {
        super(name);
    }

    public void run() {
            int sum = sum_lines(name);
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
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

}
