package tankrotationexample.game;

import java.awt.image.BufferedImage;

public class Resources {
    private static BufferedImage backgroundImg;

    public static BufferedImage getBackgroundImg() {
        return backgroundImg;
    }
    public static void setBackgroundImg(BufferedImage img){
        Resources.backgroundImg = img;
    }
}
