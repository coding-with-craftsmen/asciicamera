package nl.codecentric.cc.asciicamera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class App {


    public static final int PICTURE_HEIGHT = 90;
    public static final int PICTURE_WIDTH = 90;
    public static final int TERMINAL_COLOR_RED = 31;
    public static final int TERMINAL_COLOR_BLUE = 34;
    public static final int TERMINAL_COLOR_GREEN = 32;
    public static final int TERMINAL_COLOR_WHITE = 37;
    public static final int MAX_INTENSITY = 256 * 3;

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedImage picture;
        if (args != null && args.length > 0) {
            picture = loadPicture(args[0]);
        } else {
            picture = takePicture();
        }
        for (int y = 0; y < picture.getHeight(); y++) {
            for (int x = 0; x < picture.getWidth(); x++) {
                Color color = getColor(picture, x, y);
                System.out.print(String.format("\033[1;%dm%s", toTerminalColor(color), toTerminalCharacter(color)));
            }
            System.out.println();
        }
    }

    private static BufferedImage loadPicture(String fileName) throws IOException {
        File file = new File(fileName);
        InputStream inputStream = new FileInputStream(file);
        return ImageIO.read(inputStream);
    }

    private static int toTerminalColor(Color color) {
        if (isLeading(color.getRed(), color.getBlue(), color.getGreen())) {
            return TERMINAL_COLOR_RED;
        } else if (isLeading(color.getBlue(), color.getRed(), color.getGreen())) {
            return TERMINAL_COLOR_BLUE;
        } else if (isLeading(color.getGreen(), color.getRed(), color.getBlue())) {
            return TERMINAL_COLOR_GREEN;
        } else {
            return TERMINAL_COLOR_WHITE;
        }
    }

    private static String toTerminalCharacter(Color rgb) {
        int intensity = rgb.getRed() + rgb.getGreen() + rgb.getBlue();
        String character;

        if (intensity < MAX_INTENSITY / 4) {
            character = ".";
        } else if (intensity < (MAX_INTENSITY / 4) * 2) {
            character = "i";
        } else if (intensity < (MAX_INTENSITY / 4) * 3) {
            character = "e";
        } else {
            character = "@";
        }
        return character + character;
    }

    private static Color getColor(BufferedImage image, int x, int y) {
        int rgb = image.getRGB(x, y);
        return new Color(rgb, true);
    }

    private static BufferedImage takePicture() throws IOException {
        Process process = executeShellCommand(String.format("raspistill -w %d -h %d -n -t 5000 -e bmp -o -", PICTURE_WIDTH, PICTURE_HEIGHT));
        InputStream inputStream = process.getInputStream();
        return ImageIO.read(inputStream);
    }

    private static boolean isLeading(int c1, int c2, int c3) {
        return c1 > c2 && c1 > c3 && c1 * 3 > (c2 + c3) * 2;
    }


    private static Process executeShellCommand(String pCommand) {
        try {
            Runtime run = Runtime.getRuntime();
            Process pr = run.exec(pCommand);
            return pr;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException(ex);
        }
    }


}

