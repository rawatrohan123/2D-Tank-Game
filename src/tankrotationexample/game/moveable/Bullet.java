package tankrotationexample.game.moveable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends Movable
{
    //private Rectangle hitBox;

    Bullet(int x, int y, int vx, int vy, int angle, BufferedImage img, int R){
        super(x, y, img, vx, vy, angle, R);
        //this.hitBox = new Rectangle(x, y, this.img.getWidth(), this.img.getHeight());
    }

    public void update(){
        moveForwards();
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.getX(), this.getY());
        rotation.rotate(Math.toRadians(this.getAngle()), this.getObjImage().getWidth() / 2.0, this.getObjImage().getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.getObjImage(), rotation, null);
    }



}
