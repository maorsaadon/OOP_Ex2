package Ex2.PartA;

import static Ex2.PartA.Ex2_1.sum_lines;

public class FileThread extends java.lang.Thread {
    String file_name;
    int sum_lines;

    public FileThread(String file_name, String name) {
        super(name);
        this.file_name = file_name;
    }

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
