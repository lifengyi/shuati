package poc;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;

public class Test_File {

    @Test
    public void test_RandomAccessFile() throws IOException {
        String path = "/Users/fenli/Downloads/test/3.txt";
        File file = new File(path);
        boolean isSymbolicFile = Files.isSymbolicLink(file.toPath());
        System.out.println(isSymbolicFile);
        System.out.println(file.getAbsolutePath());
        System.out.println(file.getCanonicalPath());
        RandomAccessFile raf = new RandomAccessFile(file, "rw");

        /*
        long totalRead = 0, readSize = 0, totalSize = file.length();
        int bufferSize = 5;
        System.out.println("total file size : " + totalSize);

        byte[] buffer = new byte[bufferSize];
        raf.read(buffer, 0, bufferSize);
        System.out.println(new String(buffer));

        //raf.read(buffer, 0, bufferSize);
        //System.out.println(new String(buffer));

        //raf.seek(2 + raf.getFilePointer());
        //raf.read(buffer, 0, bufferSize);
        //System.out.println(new String(buffer));

        String tmp = "9876543210";
        raf.seek(totalSize);
        raf.write(tmp.getBytes());
        raf.write("\n".getBytes());
        */

        //String str = null;
        //while((str = raf.readLine()) != null) {
        //    System.out.println(String.format("Len:%d, str:%s", str.length(), str));
        //}
        //System.out.println("Done.");



        String str = raf.readLine();
        System.out.println(String.format("Len:%d, str:%s", str.length(), str));
        str = raf.readLine();
        System.out.println(String.format("Len:%d, str:%s", str.length(), str));

        Long pos = raf.getFilePointer();
        System.out.println(str.length());
        raf.seek(pos - str.length() - 1);  //Need to process the '\n'
        str = raf.readLine();
        System.out.println(String.format("Len:%d, str:%s", str.length(), str));



        raf.close();
        return;
    }
}
