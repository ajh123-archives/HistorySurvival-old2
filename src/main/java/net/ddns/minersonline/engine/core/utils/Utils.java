package net.ddns.minersonline.engine.core.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Utils {
    public static FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static String loadResource(String fileName) throws Exception{
        String result;
        try(InputStream in = Utils.class.getResourceAsStream(fileName)) {
            assert in != null;
            try(Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())){
                result = scanner.useDelimiter("\\A").next();
            }
        }
        return result;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public static ByteBuffer ioResourceToByteBuffer(String resource) throws IOException, NullPointerException {
        //URL url = Utils.class.getClassLoader().getResource(resource);
        //if (url == null)
        //    throw new IOException("Unable to find classpath resource" + resource);
        byte[] data;
        InputStream stream = Utils.class.getResourceAsStream(resource);
        ByteArrayOutputStream bufferRaw = new ByteArrayOutputStream();

        int nRead;
        byte[] d = new byte[16384];

        while ((nRead = stream.read(d, 0, d.length)) != -1) {
            bufferRaw.write(d, 0, nRead);
        }
        data = bufferRaw.toByteArray();

        ByteBuffer buffer = BufferUtils.createByteBuffer(data.length);
        buffer.put(data);
        buffer.flip();

        return buffer;
    }
}
