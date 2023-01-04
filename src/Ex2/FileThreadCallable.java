package Ex2;
import java.util.concurrent.*;

import static Ex2.Ex2_1.sum_lines;
public class FileThreadCallable implements Callable {
    String file_name;
    int sum_lines;

    public FileThreadCallable(String file_name) {
        super();
        this.file_name = file_name;
    }

    @Override
    public Object call() throws Exception {
        return sum_lines(file_name);
    }
}
