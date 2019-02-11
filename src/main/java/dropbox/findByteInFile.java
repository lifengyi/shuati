package dropbox;

import java.io. *;
import java.util.*;


/**
 *  Rolling Hash 技术主要用于检测重复文件块，
 *  扫描文件块中的
 *  可以参考寻找重复文件代码中的注释
 *
 *  此处用rolling hash检测两个byte array是否相同
 *
 */

public class findByteInFile {

    public static void main(String[] args) {
        String pattern = "abc123";
        String file = "/Users/fenli/Downloads/test/5.txt";
        findByteInFile proc = new findByteInFile();

        try {
            System.out.println(proc.findByte(pattern.getBytes(), file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean findByte(byte[] pattern, String fileName)
            throws IOException {
        File file = new File(fileName);

        int patternSize = pattern.length;
        long fileSize = file.length();
        if(fileSize < patternSize) {
            return false;
        }

        //int bufferSize = Math.max(8 * patternSize, 8 * 1024);
        int bufferSize = 12;
        byte[] buffer = new byte[(int)bufferSize];

        long totalRead = 0;
        long nextReadSize = 0;
        boolean byteIsFound = false;

        RandomAccessFile raf = new RandomAccessFile(file, "r");
        raf.read(buffer, 0, patternSize);
        totalRead += patternSize;

        while(totalRead < fileSize) {
            nextReadSize = Math.min(fileSize - totalRead, bufferSize - patternSize);
            raf.read(buffer, patternSize, (int)nextReadSize);
            totalRead += nextReadSize;

            if(validate(pattern, buffer, patternSize + (int)nextReadSize)) {
                byteIsFound = true;
                break;
            }
            //shift(buffer, patternSize + (int)nextReadSize, patternSize);
            System.arraycopy(buffer, (int)nextReadSize, buffer, 0, patternSize);
        }

        System.out.println(String.format("total read: %d, file size = %d", totalRead, fileSize));
        raf.close();
        return byteIsFound;
    }

    private boolean validate(byte[] pattern, byte[] buffer, int size) {
        int patternSize = pattern.length;
        // int patternRollingHash = new RollingHash(pattern).hash();
        // RollingHash rh = new RollingHash();
        for(int i = 0; i < size; ++i) {
            //rh.addByte(buffer[i]);
            if(i >= patternSize - 1) {
                if(compare(pattern, buffer, i - patternSize + 1, i)){
                    return true;
                }
                //if(patternRollingHash == rh.hash()) {
                //    return true
                //}
                //rh.removeByte(buffer[i - patternSize + 1]);
            }
        }
        return false;
    }

    boolean compare(byte[] pattern, byte[] buffer, int start, int end) {
        for(int i = start; i <= end; ++i) {
            if(pattern[i - start] != buffer[i]) {
                return false;
            }
        }
        return true;
    }
}
