package tankrotationexample.game.walls;
import tankrotationexample.game.GameObject;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Wall extends GameObject {
    public Wall(int x, int y, BufferedImage wallImage) {
        super(x, y, wallImage);
    }
}
