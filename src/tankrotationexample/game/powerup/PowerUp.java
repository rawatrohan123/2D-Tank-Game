package tankrotationexample.game.powerup;

import tankrotationexample.game.GameObject;
import tankrotationexample.game.moveable.Tank;

import java.awt.image.BufferedImage;

public abstract class PowerUp extends GameObject {

    public PowerUp(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    public abstract void powerUp(Tank t);

}
