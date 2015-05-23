/*
 * Voxem
 * Copyright (c) 2014-2015, Maxim Roncacé <caseif@caseif.net>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.caseif.voxem.util;

import org.lwjgl.BufferUtils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtil {
    public static ByteBuffer asByteBuffer(BufferedImage img) {
        ByteBuffer buffer = BufferUtils.createByteBuffer(img.getWidth() * img.getHeight() * 4);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                buffer.put((byte) (new Color(img.getRGB(x, y), true).getRed() & 0xFF));
                buffer.put((byte) (new Color(img.getRGB(x, y), true).getGreen() & 0xFF));
                buffer.put((byte) (new Color(img.getRGB(x, y), true).getBlue() & 0xFF));
                buffer.put((byte) (new Color(img.getRGB(x, y), true).getAlpha() & 0xFF));
            }
        }
        buffer.flip();
        return buffer;
    }

    public static ByteBuffer asByteBuffer(byte[] b) {
        ByteBuffer buffer = BufferUtils.createByteBuffer(b.length);
        buffer.put(b);
        buffer.flip();
        buffer.order();
        return buffer;
    }

    public static FloatBuffer asFloatBuffer(float[] f) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(f.length);
        buffer.put(f);
        buffer.flip();
        buffer.order();
        return buffer;
    }

    public static DoubleBuffer asDoubleBuffer(double[] d) {
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(d.length);
        buffer.put(d);
        buffer.flip();
        buffer.order();
        return buffer;
    }

    public static IntBuffer asIntBuffer(int[] i) {
        IntBuffer buffer = BufferUtils.createIntBuffer(i.length);
        buffer.put(i);
        buffer.flip();
        buffer.order();
        return buffer;
    }
}
