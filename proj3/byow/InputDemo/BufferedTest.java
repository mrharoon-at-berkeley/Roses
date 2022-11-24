package byow.InputDemo;


import java.io.*;

public class BufferedTest {
    // Needs close() method to print into file!
    public static void main(String[] args) {
        try {
            File file = new File("./file.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write('a');
            writer.write("b");
            writer.append("no");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
