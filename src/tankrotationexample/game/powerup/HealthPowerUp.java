package tankrotationexample.game.powerup;

import tankrotationexample.game.moveable.Tank;

import java.awt.image.BufferedImage;

public class HealthPowerUp extends PowerUp{
    public HealthPowerUp(int x, int y, BufferedImage img){
        super(x, y, img);
    }

    public void powerUp(Tank t){
        if(t.getHealth() != 10){
            t.setHealth(t.getHealth() + 1);
        }
    }


}
