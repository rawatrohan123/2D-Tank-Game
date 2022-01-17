package tankrotationexample.game.moveable;

import tankrotationexample.GameConstants;
import tankrotationexample.game.GameObject;

import java.awt.image.BufferedImage;

public abstract class Movable extends GameObject {
    private int vx;
    private int vy;
    private float angle;
    private int R = 2;

    Movable(int x, int y, BufferedImage objImage, int vx, int vy, float angle, int R) {
        super(x, y, objImage);
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.R = R;
    }


    public abstract void update();

    public int getR() {
        return R;
    }

    public void setR(int r) {
        R = r;
    }

    public int getVx() {
        return vx;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public int getVy() {
        return vy;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void stopMovement(){
        this.setX(this.getX());
        this.setY(this.getY());
        vx = 0;
        vy = 0;
    }
    public boolean checkBorder() {
        if (this.getX() < 30) {
            this.setX(30);
            return true;
        }
        if (this.getX() >= GameConstants.WORLD_WIDTH - 88) {
            this.setX(GameConstants.WORLD_WIDTH - 88);
            return true;
        }
        if (this.getY() < 40) {
            this.setY(40);
            return true;
        }
        if (this.getY() >= GameConstants.WORLD_HEIGHT - 80) {
            this.setY(GameConstants.WORLD_HEIGHT - 80);
            return true;
        }
        return false;
    }
    public void moveBackwards() {
        vx = (int) Math.round(this.getR() * Math.cos(Math.toRadians(this.getAngle())));
        vy = (int) Math.round(this.getR() * Math.sin(Math.toRadians(this.getAngle())));
        this.setX(this.getX() - vx);
        this.setY(this.getY() - vy);
        checkBorder();
        this.getHitBox().setLocation(this.getX(), this.getY());
    }

    public void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        this.setX(this.getX() + vx);
        this.setY(this.getY() + vy);
        checkBorder();
        this.getHitBox().setLocation(this.getX(), this.getY());
    }

    @Override
    public String toString() {
        return "x=" + this.getX() + ", y=" + this.getY() + ", angle=" + angle;
    }

}



