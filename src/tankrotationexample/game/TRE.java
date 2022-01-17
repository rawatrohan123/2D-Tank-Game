/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.game.moveable.Tank;
import tankrotationexample.game.powerup.BulletPowerUp;
import tankrotationexample.game.powerup.HealthPowerUp;
import tankrotationexample.game.powerup.SpeedPowerUp;
import tankrotationexample.game.walls.BreakableWall;
import tankrotationexample.game.walls.UnBreakableWall;
import tankrotationexample.game.walls.Wall;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;



import static javax.imageio.ImageIO.read;

/**
 *
 * @author anthony-pc
 */
public class TRE extends JPanel implements Runnable {

    private BufferedImage world;
    private ArrayList<Tank> tanks;
    private ArrayList<Wall> walls;
    private ArrayList<GameObject> objects;
    private Launcher lf;
    private static long tick = 0;


    public TRE(Launcher lf){
        this.lf = lf;
        tanks = new ArrayList<>();
        walls = new ArrayList<>();
        objects = new ArrayList<>();
    }

    public static long getTick(){
        return tick;
    }


    @Override
    public void run(){
       try {
           boolean gameOver = false;
           boolean player1Win = false;
           this.resetGame();
           while (true) {
                this.tick++;
                for(int i = 0 ; i < this.tanks.size() ; i++){
                    this.tanks.get(i).update();
                    if(tanks.get(i).getHealth() <= 0){
                        tanks.get(i).setLives(tanks.get(i).getLives()-1);
                        tanks.get(i).resetHealth();
                    }
                    if(tanks.get(i).getLives() == 0){
                        if(i == 1){
                            player1Win = true;
                        }
                        gameOver = true;
                    }
                }



                this.repaint();   // redraw game
               tanks.get(0).checkCollision(objects);
               tanks.get(1).checkCollision(objects);
                Thread.sleep(1000 / 144); //sleep for a few milliseconds
                /*
                 * simulate an end game event
                 * we will do this with by ending the game when drawn 2000 frames have been drawn
                 */

                if(gameOver){
                    if(player1Win == true)
                        System.out.println("Player 1 Wins!");
                    else{
                        System.out.println("Player 2 Wins!");
                    }
                    this.lf.setFrame("end");
                    return;
                }
            }
       } catch (InterruptedException ignored) {
           System.out.println(ignored);
       }
    }


    /**
     * Reset game to its initial state.
     */
    public void resetGame(){
        this.tick = 0;
        this.tanks.get(0).setX(300);
        this.tanks.get(0).setY(300);
        this.tanks.get(0).setAngle(0);
        this.tanks.get(1).setX(GameConstants.WORLD_WIDTH - 300);
        this.tanks.get(1).setAngle(180);
        this.tanks.get(1).setY(GameConstants.WORLD_HEIGHT - 400);

    }


    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void gameInitialize() {
        this.world = new BufferedImage(GameConstants.WORLD_WIDTH,
                                       GameConstants.WORLD_HEIGHT,
                                       BufferedImage.TYPE_INT_RGB);

        BufferedImage t1img = null;
        BufferedImage t2img = null;
        BufferedImage breakableWall = null;
        BufferedImage unBreakableWall = null;
        BufferedImage healthPowerUp = null;
        BufferedImage speedPowerUp = null;
        BufferedImage bulletPowerUp = null;
        BufferedImage backgroundImg = null;
        try {
            /*
             * note class loaders read files from the out folder (build folder in Netbeans) and not the
             * current working directory.
             */
            t1img = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("tank1.png")));
            t2img = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("tank1.png")));
            breakableWall = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Wall1.gif")));
            unBreakableWall = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Wall2.gif")));
            healthPowerUp = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("healthpowerup.png")));
            bulletPowerUp = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("bulletpowerup.png")));
            speedPowerUp = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("speedpowerup.png")));
            backgroundImg = read(Objects.requireNonNull(TRE.class.getClassLoader().getResource("Background.bmp")));
            Resources.setBackgroundImg(backgroundImg);


            InputStreamReader isr = new InputStreamReader(TRE.class.getClassLoader().getResourceAsStream("maps/map3.txt"));
            BufferedReader mapReader = new BufferedReader(isr);

            String row = mapReader.readLine();
            if(row == null){
                throw new IOException("no data in file");
            }
            String[] mapInfo = row.split("\t");
            int numCols = Integer.parseInt(mapInfo[0]);
            int numRows = Integer.parseInt(mapInfo[1]);

            for(int curRow = 0 ; curRow < numRows ; curRow++){
                row = mapReader.readLine();
                mapInfo = row.split("\t");
                for(int curCol = 0 ; curCol < numCols ; curCol++){
                    switch (mapInfo[curCol]){
                        case "2":
                            this.objects.add(new BreakableWall(curCol * 32, curRow * 32, breakableWall));
                            break;
                        case "3":
                            this.objects.add(new UnBreakableWall(curCol * 32, curRow * 32, unBreakableWall));
                            break;
                        case "4":
                            this.objects.add(new HealthPowerUp(curCol * 32, curRow * 32, healthPowerUp));
                            break;
                        case "5":
                            this.objects.add(new BulletPowerUp(curCol * 32, curRow * 32, bulletPowerUp));
                            break;
                        case "6":
                            this.objects.add(new SpeedPowerUp(curCol * 32, curRow * 32, speedPowerUp));
                            break;
                        case "9":
                            this.objects.add(new UnBreakableWall(curCol * 32, curRow * 32, unBreakableWall));
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        this.tanks.add(new Tank(300, 300, 0, 0, 180, t1img, 2));
        this.tanks.add(new Tank(GameConstants.GAME_SCREEN_WIDTH - 75, GameConstants.GAME_SCREEN_HEIGHT - 75, 0, 0,0 , t2img, 2));
        objects.add(this.tanks.get(0));
        objects.add(this.tanks.get(1));
        TankControl tc1 = new TankControl(this.tanks.get(0), KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        TankControl tc2 = new TankControl(this.tanks.get(1), KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.setBackground(Color.BLACK);
        this.lf.getJf().addKeyListener(tc1);
        this.lf.getJf().addKeyListener(tc2);
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        //buffer.setColor(Color.BLACK);
        //buffer.fillRect(0,0,GameConstants.GameConstants.WORLD_WIDTH,GameConstants.WORLD_HEIGHT);
        buffer.drawImage(Resources.getBackgroundImg(), 0, 0, null);
        //this.walls.forEach(wall->wall.drawImage(buffer));
        for(int i = 0 ; i < this.objects.size() ; i++){
            this.objects.get(i).drawImage(buffer);
            //this.tanks.get(i).drawImage(buffer);

        }

        int p1X = tanks.get(0).getX();
        int p2X = tanks.get(1).getX();
        int p1Y = tanks.get(0).getY();
        int p2Y = tanks.get(1).getY();
        if (p1X < GameConstants.GAME_SCREEN_WIDTH / 4) {
            p1X = GameConstants.GAME_SCREEN_WIDTH / 4;
        }
        if (p2X < GameConstants.GAME_SCREEN_WIDTH / 4) {
            p2X = GameConstants.GAME_SCREEN_WIDTH / 4;
        }
        if (p1X > GameConstants.WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 4) {
            p1X = GameConstants.WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 4;
        }
        if (p2X > GameConstants.WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 4) {
            p2X = GameConstants.WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 4;
        }
        if (p1Y < GameConstants.GAME_SCREEN_HEIGHT / 2) {
            p1Y = GameConstants.GAME_SCREEN_HEIGHT / 2;
        }
        if (p2Y < GameConstants.GAME_SCREEN_HEIGHT / 2) {
            p2Y = GameConstants.GAME_SCREEN_HEIGHT / 2;
        }
        if (p1Y > GameConstants.WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT / 2) {
            p1Y =  GameConstants.WORLD_HEIGHT- GameConstants.GAME_SCREEN_HEIGHT / 2;
        }
        if (p2Y >  GameConstants.WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT / 2) {
            p2Y =  GameConstants.WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT / 2;
        }
        BufferedImage left_split_screen = world.getSubimage(p1X - GameConstants.GAME_SCREEN_WIDTH / 4, p1Y - GameConstants.GAME_SCREEN_HEIGHT / 2, GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage right_split_screen = world.getSubimage(p2X - GameConstants.GAME_SCREEN_WIDTH / 4, p2Y - GameConstants.GAME_SCREEN_HEIGHT / 2, GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);
        //BufferedImage leftHalf = world.getSubimage( 0, 0, GameConstants.GAME_GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_GameConstants.GAME_SCREEN_HEIGHT);
        //BufferedImage rightHalf = world.getSubimage(0,0, GameConstants.GAME_GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_GameConstants.GAME_SCREEN_HEIGHT);
        //g2.fillRect(25, 40, 2 * tanks.get(0).getHealth(), 20);
        //g2.fillRect(GameConstants.GAME_GameConstants.GAME_SCREEN_WIDTH / 2 + 25, 40, 2 * tanks.get(1).getHealth(), 20);
        //BufferedImage leftHalf = world.getSubimage( (GameConstants.GAME_GameConstants.GAME_SCREEN_WIDTH )/4 - tanks.get(1).getX(), (GameConstants.GAME_GameConstants.GAME_SCREEN_HEIGHT)/2 - tanks.get(1).getY(), GameConstants.GAME_GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_GameConstants.GAME_SCREEN_HEIGHT);
        //BufferedImage rightHalf = world.getSubimage(tanks.get(1).getX() + GameConstants.GAME_GameConstants.GAME_SCREEN_WIDTH/2, tanks.get(1).getY(), GameConstants.GAME_GameConstants.GAME_SCREEN_WIDTH/2, GameConstants.GAME_GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage miniMap = world.getSubimage(0,0, GameConstants.WORLD_WIDTH, GameConstants.WORLD_HEIGHT);
        g2.drawImage(left_split_screen,0,0,null);
        g2.drawImage(right_split_screen,GameConstants.GAME_SCREEN_WIDTH/2 + 4,0,null);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.setColor(Color.WHITE);
        g2.drawString("Player1 lives: " + tanks.get(0).getLives(), 10, 30);
        g2.drawString("Player2 lives: " + tanks.get(1).getLives(), GameConstants.GAME_SCREEN_WIDTH - 250, 30);
        g2.drawString("[", 10, 58);
        g2.drawString("[", GameConstants.GAME_SCREEN_WIDTH  - 250, 58);
        g2.drawString("]", 230, 58);
        //GameConstants.GAME_SCREEN_WIDTH / 2 + 450
        g2.drawString("]", + GameConstants.GAME_SCREEN_WIDTH - 30, 58);
        g2.setColor(Color.green);
        g2.fillRect(25, 40, 20 * tanks.get(0).getHealth(), 20);
        g2.fillRect( GameConstants.GAME_SCREEN_WIDTH - 235, 40, 20 * tanks.get(1).getHealth(), 20);
        //g2.drawString("Player2 lives: " + this.Player2_num_lives, GameConstants.GAME_SCREEN_WIDTH / 2 + 10, 28);
        g2.scale(0.2, 0.2);
        g2.drawImage(miniMap, GameConstants.GAME_SCREEN_WIDTH + GameConstants.GAME_SCREEN_WIDTH/4 + miniMap.getWidth()/2 , 0, null);
        g2.scale(1, 1);

    }

}
