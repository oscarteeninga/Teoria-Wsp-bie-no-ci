package main;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

public class Transmiter {

    public BufferedImage bitmap;
    public int width = 100;
    public int height = 100;

    public Transmiter() {
        bitmap = new BufferedImage(width, height, TYPE_3BYTE_BGR);
    }

    public static void main(String args[]) {
        Transmiter trans = new Transmiter();
        int rgb = 100;
        for (;;) {
            trans.bitmap.setRGB(50, 50, Math.abs(100-rgb));
            trans.bitmap.createGraphics();
        }

    }
}
