package PartA;
import java.util.concurrent.*;

import static PartA.Ex2_1.sum_lines;

public class FileThreadCallable implements Callable {
    String file_name;

    public FileThreadCallable(String file_name) {
        super();
        this.file_name = file_name;
    }

    @Override
    public Object call() throws Exception {
        return sum_lines(file_name);
    }
}
