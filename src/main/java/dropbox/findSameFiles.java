package dropbox;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


/**
 *
 *
 * 需要熟悉以下相关文件API操作：
 *
 * File[] File.listFiles()
 * Long File.length()
 *
 * RandomAccessFiles raf = new RandomAccessFile (File, “r+”)
 * raf.read(byte[], int offset, int readSize);
 * MessageDigest md5 = MessageDigest.getInstance(“MD5”);
 * Md5.update(byte[], int offset, int readSize);
 *
 * String hashvalue = new BigInteger(1, md5.digest()).toString();
 *
 *
 *
 * 思路：
 * 1. 文件大小
 * 2. 文件总体hash， MD5,SHA256,SHA512
 * 3. 文件分块hash
 * 4. 文件按位比较
 *
 *
 * Follow up:  文件很多怎么办？
 *   1. 本地文件系统： 基于size，过滤第一步
 *                  不同的size做比较： 小文件可以直接比较， 大文件求hash（分块hash和全文hash）
 *                  多线程运行，貌似瓶颈基本在I/O，所以多机器更适合
 *
 *   2. 分布式文件系统：考虑做一个文件(块)的哈希值数据库，方便查询
 *      多机分布式计算更适合
 *
 *
 * Follow up： 目录很深？
 * 注意DFS爆栈的可能性，BFS内存使F用率以及用来存储文件路径的字符数组是否会溢出；
 *
 *
 *
 * Follow up ： 可能出现的情况有什么？
 *
 * 文件被同时读写,可以使用FileLock，对于多线程而言，都是基于java语言，
 * 使用同一种文件锁，是没什么问题；
 *
 * 但是如果是多进程呢？而且其他进程是其他语言写的，那么设计到以下问题：
 * 操作系统可能提供多种风格的锁：unix-like 提供POSIX和BSD-style锁
 * 前者是lock record，后者是record whole file,
 *
 * 同时考虑到文件可能被不同的文件系统map，不同文件系统对于网络文件映射的锁机制也有所不同，
 * maybe advisory or mandatory, 同一个文件在不同的文件系统上被不同进程读写？
 *
 * 考虑修改/更新文件时，不是直接在原文件上修改，而是新文件和旧文件进行比较，
 * 然后写diff文件
 *
 *
 * Follow up: 注意主动沟通问题，是否需要检测符号链接：
 *
 *   import java.noi.file.Files;
 *
 *       File file = new File(file_path);
 *       boolean isSymbolicFile = Files.isSymbolicLink(file.toPath());
 *       String realPath = file.getCanonicalPath();
 *       if(visited.contains(realPath)) {
 *           ...
 *       }
 *
 *   另一种方法就是：直接file.getCanonicalPath()
 *   然后判断是否visited，
 *
 *   考虑另一个问题：如果symbol link链接到root directory意外，如何处理？
 *
 *
 *
 *
 * Follow up:  于全文见做checksum可能会false positive
 *
 * SHA512 > SHA256 > MD5
 *
 *
 *
 * Follow up：文件很大，怎么办？
 *
 * 那么可以使用类似于分布式文件系统那样，大文件分块
 * 存储，每个chunk有自己的哈希值，对于本地文件也可
 * 以用这个方法，对本地文件进行分块，按照分块来进行
 * 哈希去重
 *
 * 上述方案是基于分块，如果要继续缩小粒度的话，如何
 * 操作？
 * 是按照byte进行比较？ no， 按照byte扫描粒度太小
 * 对于大文件费时
 *
 * 可以使用rolling hash对于每一个分块进行更小粒度
 * 的去重，Data Domain使用这种技术，LBFS使用的
 * rolling hash实现是Rabin Fingerprint，
 * 其他实现方法还有Rabin-Karp
 *
 *
 * 提供下一种思路：
 * rsync。类unix系统上，大家常用来做备份的rsync，
 * 其实也应用了数据去重技术，它通过在服务器端固定分块，
 * 在客户端逐字节比较来实现去重。我不确定是否应该算是
 * 固定分块还是可变分块，所以单独列了出来。
 *
 * rsync的缺点，是必须有明确的历史版本才能实现去重，
 * 不能实现全局去重。rsync只能检测到重复数据，并不能
 * 减少存储量。要减少存储量还要使用delta encoding。
 * 通过是用类似rsync的算法，得到新增文件与其历史文件的
 * 变化值delta，可以不必立即重建这个新增文件并存储，
 * 而是只存储这个delta数据，在需要时候重建。进而减少数
 * 据存储量。最成功的网盘Dropbox使用这种方式实现数据去重
 *
 *

 *
 */

public class findSameFiles {

    private int bufferSize = 1024;

    public static void main(String[] args) {
        String path = "/Users/fenli/Downloads/test";
        findSameFiles proc = new findSameFiles();
        List<List<String>> dumplicatedFiles = proc.findDupFiles(path);
        for(List<String> list : dumplicatedFiles) {
            System.out.println(list.toString());
        }
    }

    List<List<String>> findDupFiles(String path) {
        List<List<String>> result = new ArrayList<>();
        if(path == null || path.length() == 0) {
            return result;
        }

        File file = new File(path);
        if(!file.isDirectory()) {
            return result;
        }

        Map<Long, List<String>> filesWithSameSize = new HashMap<>();
        findFilesWithSameSize(file, filesWithSameSize);

        Map<String, List<String>> filesWithSameHash = new HashMap<>();
        for(Long size : filesWithSameSize.keySet()) {
            List<String> list = filesWithSameSize.get(size);
            if(list.size() > 1) {
                findFilesWithSameHash(list, filesWithSameHash);
            }
        }

        // copy all the values to result
        // or compare files
        for(String md5 : filesWithSameHash.keySet())  {
            List<String> list = filesWithSameHash.get(md5);
            if(list.size() > 1) {
                result.add(list);
            }
        }

        return result;
    }


    /**
     * MD5 is not a secure hashing algorithm
     * we still implement our checksum via MD5;
     *
     * For files in local file system: we juet compare the whole MD5
     * FOR files in distributed file system: we can compare the chunks' MD5
     *
     */
    void findFilesWithSameHash(List<String> files, Map<String, List<String>> filesWithSameHash) {
        for (String fileName : files) {
            File file = new File(fileName);
            String hashValue = null;

            try {
                hashValue = getHashValue(file);
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
                continue;
            }

            if (filesWithSameHash.containsKey(hashValue)) {
                filesWithSameHash.get(hashValue).add(fileName);
            } else {
                List<String> fileList = new ArrayList<>();
                fileList.add(fileName);
                filesWithSameHash.put(hashValue, fileList);
            }
        }

    }

    String getHashValue(File file) throws IOException, NoSuchAlgorithmException {
        long totalRead = 0, readSize = 0, totalSize = file.length();
        byte[] buffer = new byte[bufferSize];

        RandomAccessFile raf = new RandomAccessFile(file, "r");
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        while (totalRead < totalSize) {
            readSize = Math.min(bufferSize, totalSize - totalRead);
            raf.read(buffer, 0, (int)readSize);
            md5.update(buffer, 0, (int)readSize);
            totalRead += readSize;
        }
        raf.close();

        return new BigInteger(1, md5.digest()).toString();
    }

    /**
     *  how to process symbol link?
     *  how to process circular dependency for symbol link
     *  what if the symbol link point to the file out of the root directory
     */
    void findFilesWithSameSize(File file, Map<Long, List<String>> filesWithSameSize) {
        LinkedList<File> queue = new LinkedList<>();
        queue.offer(file);

        while(!queue.isEmpty()) {
            File cur = queue.poll();
            File[] fileList = cur.listFiles();
            for(File next : fileList) {
                if(next.isDirectory()) {
                    queue.offer(next);
                    continue;
                }

                long size = next.length();
                String path = next.getAbsolutePath();
                if(filesWithSameSize.containsKey(size)) {
                    filesWithSameSize.get(size).add(path);
                } else {
                    List<String> listWithSameSize = new ArrayList<>();
                    listWithSameSize.add(path);
                    filesWithSameSize.put(size, listWithSameSize);
                }
            }
        }
    }
}
