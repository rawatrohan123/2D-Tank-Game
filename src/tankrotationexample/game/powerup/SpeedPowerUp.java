package tankrotationexample.game.powerup;

import tankrotationexample.game.moveable.Tank;

import java.awt.image.BufferedImage;

public class SpeedPowerUp extends PowerUp{
    public SpeedPowerUp(int x, int y, BufferedImage img){
        super(x, y, img);
    }

    public void powerUp(Tank t){
        t.setR(t.getR() + 1);
    }

}
