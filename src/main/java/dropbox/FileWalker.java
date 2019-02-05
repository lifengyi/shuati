package dropbox;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


public class FileWalker {
    public void walk( String path ) {

        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                System.out.println( "\nEnter Dir:" + f.getAbsoluteFile() );
                walk( f.getAbsolutePath() );
                System.out.println( "Out Dir:" + f.getAbsoluteFile()  + "\n");
            }
            else {
                System.out.println( "File:" + f.getAbsoluteFile()  + ", size:" + f.length());
            }
        }
    }

    public void walk_bfs(String path) {
        File root = new File(path);
        LinkedList<File> dirs = new LinkedList<>();
        dirs.offer(root);

        while(!dirs.isEmpty()) {
            File curDir = dirs.poll();
            System.out.println("\nCurrent dir is " + curDir.getAbsolutePath());

            File[] fileLists = curDir.listFiles();
            for(File file : fileLists) {
                if(file.isDirectory()) {
                    dirs.offer(file);
                    System.out.println("Find directory: " + file.getAbsolutePath());
                } else {
                    System.out.println("Find file: " + file.getAbsolutePath());
                }
            }
        }
    }

    public void populateDuplicateFilesWithSize(Map<Long, Set<String>> duplicateFilesWithSameSize, File dirctory) {
        if(!dirctory.isDirectory()) {
            return;
        }

        for(File f : dirctory.listFiles()) {
            if(!f.isDirectory()) {
                long size = f.length();
                if(!duplicateFilesWithSameSize.containsKey(size)) {
                    duplicateFilesWithSameSize.put(size, new HashSet<>());
                }
                duplicateFilesWithSameSize.get(size).add(f.getAbsolutePath());
            } else {
                populateDuplicateFilesWithSize(duplicateFilesWithSameSize, f);
            }
        }
    }

    String getFileMD5(File inputFile) {
        String hashString = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            RandomAccessFile file = new RandomAccessFile(inputFile, "r");

            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            long totalFileSize = inputFile.length();
            long totalReadSize = 0;
            long readSize = 0;
            while (totalReadSize < totalFileSize) {
                readSize = Math.min(bufferSize, totalFileSize - totalReadSize);

                file.seek(totalReadSize);
                file.read(buffer, 0, (int) readSize);
                md.update(buffer);

                totalReadSize += readSize;
            }

            String fileHash = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        return hashString;
    }

    public void populateDuplicateFilesWithSameMD5(Map<String, Set<String>> duplicateFilesWithSameMD5,
                                                  long size, Set<String> files) {
        for(String fileString : files) {
            File f = new File(fileString);
            String md5 = getFileMD5(f);

            String key = md5 + size;
            if (!duplicateFilesWithSameMD5.containsKey(key)) {
                duplicateFilesWithSameMD5.put(key, new HashSet<>());
            }
            duplicateFilesWithSameMD5.get(key).add(fileString);
        }
    }


    public static void main(String[] args) {
        FileWalker fw = new FileWalker();
        //fw.walk_bfs("/Users/fenli/Downloads/test");

        String root = "/Users/fenli/Downloads/test";

        /**
         * 可以读取symbol link
         * 如果读取到symbol link怎么办？
         * 如果出现 cycle怎么办？如何保证不会无限循环；
         * 如果link指向的是当前root下？或者root之外的路径？
         */
        //File directory = new File("/path/symlink/foo/bar").getCanonicalFile();
        //String[] files = directory.listFiles();

        File dir = new File(root);
        Map<Long, Set<String>> duplicateFilesWithSameSize = new HashMap<>();
        fw.populateDuplicateFilesWithSize(duplicateFilesWithSameSize, dir);

        Map<String, Set<String>> duplicateFilesWithSameMD5 = new HashMap<>();
        for(long key : duplicateFilesWithSameSize.keySet()) {
            if(duplicateFilesWithSameSize.get(key).size() > 1) {
                fw.populateDuplicateFilesWithSameMD5(duplicateFilesWithSameMD5, key, duplicateFilesWithSameSize.get(key));
            }
        }

        List<List<String>> result = new ArrayList<>();
        for(String key: duplicateFilesWithSameMD5.keySet()) {
            if(duplicateFilesWithSameMD5.get(key).size() > 1) {
                List<String> dupFiles = new ArrayList<>();
                dupFiles.addAll(duplicateFilesWithSameMD5.get(key));
                result.add(dupFiles);
            }
        }


        for(List<String> dup : result) {
            System.out.println("dup: " + dup.toString());
        }
    }
}



