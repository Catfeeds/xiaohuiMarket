package com.xlibao.io.entry;

/**
 * @author chinahuangxc on 2017/7/10.
 */
public class ByteUtil {

    public static void writeShort(byte[] data, int startIndex, int shortValue) {
        data[(startIndex++)] = ((byte) (shortValue >>> 8 & 0xFF));
        data[(startIndex++)] = ((byte) (shortValue & 0xFF));
    }

    public static void writeInt(byte[] data, int startIndex, int intValue) {
        data[(startIndex++)] = ((byte) (intValue >>> 24 & 0xFF));
        data[(startIndex++)] = ((byte) (intValue >>> 16 & 0xFF));
        data[(startIndex++)] = ((byte) (intValue >>> 8 & 0xFF));
        data[(startIndex++)] = ((byte) (intValue & 0xFF));
    }

    public static void writeLong(byte[] data, int startIndex, long longValue) {
        data[(startIndex++)] = ((byte) (longValue >>> 56));
        data[(startIndex++)] = ((byte) (longValue >>> 48));
        data[(startIndex++)] = ((byte) (longValue >>> 32));
        data[(startIndex++)] = ((byte) (longValue >>> 24));
        data[(startIndex++)] = ((byte) (longValue >>> 16));
        data[(startIndex++)] = ((byte) (longValue >>> 8));
        data[(startIndex++)] = ((byte) (longValue));
    }

    public static short readShort(byte[] data, int startIndex) {
        int a = data[(startIndex++)];
        int b = data[(startIndex++)];
        return (short) (((a & 0xFF) << 8) + ((b & 0xFF)));
    }

    public static int readInt(byte[] data, int startIndex) {
        int a = data[(startIndex++)];
        int b = data[(startIndex++)];
        int c = data[(startIndex++)];
        int d = data[(startIndex++)];
        return ((a & 0xFF) << 24) + ((b & 0xFF) << 16) + ((c & 0xFF) << 8) + ((d & 0xFF));
    }

    public static long readLong(byte[] data, int startIndex) {
        long a = data[(startIndex++)];
        long b = data[(startIndex++)];
        long c = data[(startIndex++)];
        long d = data[(startIndex++)];
        long e = data[(startIndex++)];
        long f = data[(startIndex++)];
        long g = data[(startIndex++)];
        long h = data[(startIndex++)];
        return (a << 56) + ((b & 0xFF) << 48) + ((c & 0xFF) << 40) + ((d & 0xFF) << 32) + ((e & 0xFF) << 24) + ((f & 0xFF) << 16) + ((g & 0xFF) << 8) + ((h & 0xFF));
    }

    public static String readUTF(byte[] data, int startIndex) {
        int utfLength = readShort(data, startIndex);

        byte[] bytes;
        char[] chars;

        bytes = new byte[utfLength];
        chars = new char[utfLength];

        int count = 0;
        int charArrCount = 0;
        int index = startIndex + 2;

        System.arraycopy(data, index, bytes, 0, utfLength);
        while (count < utfLength) {
            int c = bytes[count] & 0xFF;
            if (c > 127) {
                break;
            }
            count++;
            chars[(charArrCount++)] = ((char) c);
        }
        while (count < utfLength) {
            int c = bytes[count] & 0xFF;
            int char2;
            switch (c >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    count++;
                    chars[(charArrCount++)] = ((char) c);
                    break;
                case 12:
                case 13:
                    count += 2;
                    if (count > utfLength) {
                        System.out.println("malformed input: partial character at end");
                    }
                    char2 = bytes[(count - 1)];
                    if ((char2 & 0xC0) != 128) {
                        System.out.println("malformed input around byte " + count);
                    }
                    chars[(charArrCount++)] = ((char) ((c & 0x1F) << 6 | char2 & 0x3F));
                    break;
                case 14:
                    count += 3;
                    if (count > utfLength) {
                        System.out.println("malformed input: partial character at end");
                    }
                    char2 = bytes[(count - 2)];
                    int char3 = bytes[(count - 1)];
                    if (((char2 & 0xC0) != 128) || ((char3 & 0xC0) != 128)) {
                        System.out.println("malformed input around byte " + (count - 1));
                    }
                    chars[(charArrCount++)] = ((char) ((c & 0xF) << 12 | (char2 & 0x3F) << 6 | (char3 & 0x3F)));
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    System.out.println("malformed input around byte " + count);
            }
        }
        return new String(chars, 0, charArrCount);
    }
}