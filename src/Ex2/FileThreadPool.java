package Ex2;
import java.util.concurrent.*;

import static Ex2.Ex2_1.sum_lines;
public class FileThreadPool implements Callable {
    String[] file_name;
    int sum_lines;

    public FileThreadPool(String[] file_name) {
        this.file_name = file_name;
    }

    @Override
    public Object call() throws Exception {
        for (int i = 0; i < file_name.length; i++) {
            setSum_lines(sum_lines(file_name[i]));
            int r = 500;
        }
        return getSum_lines();
    }

//    public void whenTaskSubmitted_ThenFutureResultObtained(){
//        FileThreadPool task = new FileThreadPool(file_name);
//        Future<Integer> future = executorService.submit(task);
//
//        assertEquals(120, future.get().intValue());
//    }
    public int setSum_lines(int sum_lines){
        return this.sum_lines += sum_lines;
    }
    public int getSum_lines(){
        return sum_lines;
    }
}
