package tankrotationexample.game.powerup;

import tankrotationexample.game.moveable.Tank;

import java.awt.image.BufferedImage;

public class BulletPowerUp extends PowerUp{
    public BulletPowerUp(int x, int y, BufferedImage img){
        super(x, y, img);
    }

    public void powerUp(Tank t){
        t.setPower(t.getPower() + 1);
    }
}
