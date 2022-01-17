package tankrotationexample.game.moveable;



import tankrotationexample.game.GameObject;
import tankrotationexample.game.TRE;
import tankrotationexample.game.powerup.PowerUp;
import tankrotationexample.game.walls.BreakableWall;
import tankrotationexample.game.walls.Wall;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Objects;

import static javax.imageio.ImageIO.read;

/**
 *
 * @author anthony-pc
 */
public class Tank extends Movable{

    private int power = 1;
    private int health;
    private int lives;
    private final float ROTATIONSPEED = 3.0f;


    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;

    private ArrayList<Bullet> ammo;

    public Tank(int x, int y, int vx, int vy, float angle, BufferedImage img, int R) {
        super(x, y, img, vx, vy, angle, R);

        this.ammo = new ArrayList<Bullet>();
        this.health = 10;
        this.lives = 3;
    }

    public int getLives() {
        return lives;
    }

    public void resetHealth(){
        this.health = 10;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public ArrayList<Bullet> getAmmo() {
        return ammo;
    }

    public int getHealth(){
        return health;
    }

   public void setHealth(int health){
        this.health = health;
   }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void toggleShootPressed(){
        this.ShootPressed = true;
    }

    void unToggleShootPressed(){
        this.ShootPressed = false;
    }

    public void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        //&& TRE.getTick() % 20 == 0
        if (this.ShootPressed 
                && TRE.getTick() % 20 == 0) {
            try{
                Bullet bullet;
                bullet = new Bullet(this.getX(), this.getY(), 0, 0, (int) this.getAngle(), read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("bullet.png"))), 7);
                this.ammo.add(bullet);
            }
            catch(Exception e){
                System.out.println("No bullet image found!");
            }
        }

        this.ammo.forEach(bullet->bullet.update());

    }


    private void rotateLeft() {
        this.setAngle(this.getAngle() - this.ROTATIONSPEED);
    }

    private void rotateRight() {
        this.setAngle(this.getAngle() + this.ROTATIONSPEED);
    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(this.getX(), this.getY());
        rotation.rotate(Math.toRadians(this.getAngle()), this.getObjImage().getWidth() / 2.0, this.getObjImage().getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.getObjImage(), rotation, null);
        this.ammo.forEach(bullet->bullet.drawImage(g));
        for(int i = 0 ; i < ammo.size() ; i++){
            if(ammo.get(i).checkBorder()){
                ammo.remove(i);
            }
        }

    }
    public void checkCollision(ArrayList<GameObject> objects) {
        if (this instanceof Tank) {
            Tank temp = (Tank) this;
            for (int i = 0; i < objects.size(); i++) {
                if (objects.get(i) == this) {
                    continue;
                }
                // Check for bullet collision
                if (objects.get(i) instanceof Tank) {
                    Tank t = (Tank) (objects.get(i));
                    for (int j = 0; j < t.getAmmo().size(); j++) {
                        if (t.getAmmo().get(j).getHitBoxBounds().intersects(this.getHitBox())) {
                            t.getAmmo().remove(j);
                            temp.setHealth(temp.getHealth() - (t.getPower()));
                        }
                    }
                }
                if (objects.get(i) instanceof Wall) {
                    for (int j = 0; j < temp.getAmmo().size(); j++) {
                        if (temp.getAmmo().get(j).getHitBoxBounds().intersects(objects.get(i).getHitBoxBounds())) {
                            temp.getAmmo().remove(j);
                            if(objects.get(i) instanceof BreakableWall){
                                ((BreakableWall) objects.get(i)).reduceState();
                                if(((BreakableWall) objects.get(i)).getState() == 0){
                                    objects.remove(i);
                                }
                            }
                        }
                    }

                }


                if (this.getHitBoxBounds().intersects(objects.get(i).getHitBoxBounds())) {
                    if (objects.get(i) instanceof Tank) {
                    }
                    if (objects.get(i) instanceof Bullet) {
                        objects.remove(i);
                    }
                    if (objects.get(i) instanceof Wall) {
                        this.setX(this.getX());
                        this.setY(this.getY());
                        while(this.getHitBoxBounds().intersects(objects.get(i).getHitBoxBounds())){
                            temp.moveBackwards();
                            if(this.getHitBoxBounds().intersects(objects.get(i).getHitBoxBounds())){
                                temp.moveForwards();
                                temp.moveForwards();
                            }
                        }
                    }
                    if (objects.get(i) instanceof PowerUp) {
                        ((PowerUp) objects.get(i)).powerUp((Tank) this);
                        objects.remove(i);
                    }
                }
            }
        }
    }


}
