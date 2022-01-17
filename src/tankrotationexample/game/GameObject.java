package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class GameObject{
    private int x;
    private int y;
    private BufferedImage objImage;
    private Rectangle hitBox;

    public Rectangle getHitBox() {
        return hitBox;
    }

    public Rectangle getHitBoxBounds(){
        return hitBox.getBounds();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BufferedImage getObjImage() {
        return objImage;
    }

    public void setObjImage(BufferedImage objImage) {
        this.objImage = objImage;
    }

    public GameObject(int x, int y, BufferedImage objImage) {
        this.x = x;
        this.y = y;
        this.objImage = objImage;
        this.hitBox = new Rectangle(x, y, this.objImage.getWidth(), this.objImage.getHeight());
    }



    //public abstract void drawImage(Graphics g);
    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.getObjImage(), this.getX(), this.getY(), null);
    }

}
