package tankrotationexample.game.walls;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends Wall{
    private int state = 2;


    public BreakableWall(int x, int y, BufferedImage wallImage) {
        super(x, y, wallImage);
    }

    public void reduceState(){
        state--;
    }

    public int getState(){
        return state;
    }


    @Override
    public void drawImage(Graphics g){
        if(state > 0){
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(this.getObjImage(), this.getX(), this.getY(), null);
        }

    }
}
