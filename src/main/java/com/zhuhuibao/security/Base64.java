package com.zhuhuibao.security;

public class Base64 {
    private static final byte[] encodingTable = {
            65, 66, 67, 68, 69, 70, 71,
            72, 73, 74, 75, 76, 77, 78,
            79, 80, 81, 82, 83, 84, 85,
            86, 87, 88, 89, 90,
            97, 98, 99, 100, 101, 102, 103,
            104, 105, 106, 107, 108, 109, 110,
            111, 112, 113, 114, 115, 116, 117,
            118,
            119, 120, 121, 122,
            48, 49, 50, 51, 52, 53, 54,
            55, 56, 57,
            43, 47};

    private static final byte[] decodingTable = new byte[''];

    static {
        for (int i = 65; i <= 90; i++) {
            decodingTable[i] = (byte) (i - 65);
        }

        for (int i = 97; i <= 122; i++) {
            decodingTable[i] = (byte) (i - 97 + 26);
        }

        for (int i = 48; i <= 57; i++) {
            decodingTable[i] = (byte) (i - 48 + 52);
        }

        decodingTable[43] = 62;
        decodingTable[47] = 63;
    }

    public static byte[] encode(byte[] data) {
        int modulus = data.length % 3;
        byte[] bytes;
        if (modulus == 0) {
            bytes = new byte[4 * data.length / 3];
        } else {
            bytes = new byte[4 * (data.length / 3 + 1)];
        }

        int dataLength = data.length - modulus;

        int i = 0;
        for (int j = 0; i < dataLength; j += 4) {
            int a1 = data[i] & 0xFF;
            int a2 = data[(i + 1)] & 0xFF;
            int a3 = data[(i + 2)] & 0xFF;

            bytes[j] = encodingTable[(a1 >>> 2 & 0x3F)];
            bytes[(j + 1)] = encodingTable[((a1 << 4 | a2 >>> 4) & 0x3F)];
            bytes[(j + 2)] = encodingTable[((a2 << 2 | a3 >>> 6) & 0x3F)];
            bytes[(j + 3)] = encodingTable[(a3 & 0x3F)];

            i += 3;
        }
        int b1, b2, b3, d1, d2;
        switch (modulus) {
            case 0:
                break;
            case 1:
                d1 = data[(data.length - 1)] & 0xFF;
                b1 = d1 >>> 2 & 0x3F;
                b2 = d1 << 4 & 0x3F;

                bytes[(bytes.length - 4)] = encodingTable[b1];
                bytes[(bytes.length - 3)] = encodingTable[b2];
                bytes[(bytes.length - 2)] = 61;
                bytes[(bytes.length - 1)] = 61;
                break;
            case 2:
                d1 = data[(data.length - 2)] & 0xFF;
                d2 = data[(data.length - 1)] & 0xFF;

                b1 = d1 >>> 2 & 0x3F;
                b2 = (d1 << 4 | d2 >>> 4) & 0x3F;
                b3 = d2 << 2 & 0x3F;

                bytes[(bytes.length - 4)] = encodingTable[b1];
                bytes[(bytes.length - 3)] = encodingTable[b2];
                bytes[(bytes.length - 2)] = encodingTable[b3];
                bytes[(bytes.length - 1)] = 61;
        }

        return bytes;
    }

    public static byte[] decode(byte[] data) {
        byte[] bytes;
        if (data[(data.length - 2)] == 61) {
            bytes = new byte[(data.length / 4 - 1) * 3 + 1];
        } else {
            if (data[(data.length - 1)] == 61) {
                bytes = new byte[(data.length / 4 - 1) * 3 + 2];
            } else {
                bytes = new byte[data.length / 4 * 3];
            }
        }
        int i = 0;
        for (int j = 0; i < data.length - 4; j += 3) {
            byte b1 = decodingTable[data[i]];
            byte b2 = decodingTable[data[(i + 1)]];
            byte b3 = decodingTable[data[(i + 2)]];
            byte b4 = decodingTable[data[(i + 3)]];

            bytes[j] = (byte) (b1 << 2 | b2 >> 4);
            bytes[(j + 1)] = (byte) (b2 << 4 | b3 >> 2);
            bytes[(j + 2)] = (byte) (b3 << 6 | b4);

            i += 4;
        }

        if (data[(data.length - 2)] == 61) {
            byte b1 = decodingTable[data[(data.length - 4)]];
            byte b2 = decodingTable[data[(data.length - 3)]];

            bytes[(bytes.length - 1)] = (byte) (b1 << 2 | b2 >> 4);
        } else if (data[(data.length - 1)] == 61) {
            byte b1 = decodingTable[data[(data.length - 4)]];
            byte b2 = decodingTable[data[(data.length - 3)]];
            byte b3 = decodingTable[data[(data.length - 2)]];

            bytes[(bytes.length - 2)] = (byte) (b1 << 2 | b2 >> 4);
            bytes[(bytes.length - 1)] = (byte) (b2 << 4 | b3 >> 2);
        } else {
            byte b1 = decodingTable[data[(data.length - 4)]];
            byte b2 = decodingTable[data[(data.length - 3)]];
            byte b3 = decodingTable[data[(data.length - 2)]];
            byte b4 = decodingTable[data[(data.length - 1)]];

            bytes[(bytes.length - 3)] = (byte) (b1 << 2 | b2 >> 4);
            bytes[(bytes.length - 2)] = (byte) (b2 << 4 | b3 >> 2);
            bytes[(bytes.length - 1)] = (byte) (b3 << 6 | b4);
        }

        return bytes;
    }

    public static byte[] decode(String data) {
        byte[] bytes;
        if (data.charAt(data.length() - 2) == '=') {
            bytes = new byte[(data.length() / 4 - 1) * 3 + 1];
        } else {
            if (data.charAt(data.length() - 1) == '=') {
                bytes = new byte[(data.length() / 4 - 1) * 3 + 2];
            } else {
                bytes = new byte[data.length() / 4 * 3];
            }
        }
        int i = 0;
        for (int j = 0; i < data.length() - 4; j += 3) {
            byte b1 = decodingTable[data.charAt(i)];
            byte b2 = decodingTable[data.charAt(i + 1)];
            byte b3 = decodingTable[data.charAt(i + 2)];
            byte b4 = decodingTable[data.charAt(i + 3)];

            bytes[j] = (byte) (b1 << 2 | b2 >> 4);
            bytes[(j + 1)] = (byte) (b2 << 4 | b3 >> 2);
            bytes[(j + 2)] = (byte) (b3 << 6 | b4);

            i += 4;
        }

        if (data.charAt(data.length() - 2) == '=') {
            byte b1 = decodingTable[data.charAt(data.length() - 4)];
            byte b2 = decodingTable[data.charAt(data.length() - 3)];

            bytes[(bytes.length - 1)] = (byte) (b1 << 2 | b2 >> 4);
        } else if (data.charAt(data.length() - 1) == '=') {
            byte b1 = decodingTable[data.charAt(data.length() - 4)];
            byte b2 = decodingTable[data.charAt(data.length() - 3)];
            byte b3 = decodingTable[data.charAt(data.length() - 2)];

            bytes[(bytes.length - 2)] = (byte) (b1 << 2 | b2 >> 4);
            bytes[(bytes.length - 1)] = (byte) (b2 << 4 | b3 >> 2);
        } else {
            byte b1 = decodingTable[data.charAt(data.length() - 4)];
            byte b2 = decodingTable[data.charAt(data.length() - 3)];
            byte b3 = decodingTable[data.charAt(data.length() - 2)];
            byte b4 = decodingTable[data.charAt(data.length() - 1)];

            bytes[(bytes.length - 3)] = (byte) (b1 << 2 | b2 >> 4);
            bytes[(bytes.length - 2)] = (byte) (b2 << 4 | b3 >> 2);
            bytes[(bytes.length - 1)] = (byte) (b3 << 6 | b4);
        }

        return bytes;
    }
}
