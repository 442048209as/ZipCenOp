package zip;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class CenOp {
    static int[] cenFlag = new int[]{80, 75, 1, 2};
    static byte[] cenEncryptedFlag = new byte[]{9, 8};
    static byte[] cenNotEncryptedFlag = new byte[]{0, 8};

    public static void main(String[] args) {
        if (args.length != 2 || args[1].equals("e") || args[1].equals("r")) {
            System.out.println("""
                    tastypear@gmail.com
                    coolapk.com
                    no source here, but u can reverse as u like
                    
                    Code refactoring through reverse engineering by fantasquex@gmail.com

                    usage:
                    ZipCenOp.jar <option> <file>
                    option:
                        r : recover a PKZip
                        e : do a fake encryption
                    """);
            return;
        }
        try {
            operate(args[1], args[0]);
        } catch (IOException e) {
            System.out.println("internal error.");
            e.printStackTrace();
        }
    }

    public static void operate(String file, String method) throws IOException {
        File zip = new File(file);
        long length = zip.length();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
            MappedByteBuffer buffer = (randomAccessFile).getChannel()
                    .map(FileChannel.MapMode.READ_WRITE, 0, length);
            for (int position = 0; position < length; position++) {
                for (int offset = 0; offset < 4 && position + offset < length && buffer.get(position + offset) == cenFlag[offset]; offset++) {
                    if (offset == 3) {
                        if (method.equals("r")) {
                            buffer.put(position + 8, cenNotEncryptedFlag);
                        } else if (method.equals("e")) {
                            buffer.put(position + 8, cenEncryptedFlag);
                        }
                        position += 10;
                    }
                }
            }
            System.out.println("success!");
        }
    }
}
