//Daniel Lin, Jan. 19/24
//Platformer game for ICS4U final
//Game about a cat trying to reach the end of the level
//Don't touch the dogs or spikes u die

package Spotlight.Main;
import Spotlight.Sprites.Enemy;
import Spotlight.Sprites.Obstacle;
import Spotlight.Sprites.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;


//main, where all the into is stored for the game frame and panel
public class Game {

    //panel, frame
    private GamePanel gamePanel;
    private GameFrame gameFrame;

    //construct player
    private Player player;
    private Level level;

    //private int FPS = 60;
    //private Thread thread; henry says no more threading :(

    //calculate width and height of each tile, and how many rows and columns (not used, just for referecne)
    private int tileWidth = 24;
    private int tileHeight = 24;
    private int numRows = 50;
    private int numColumns = 25;

    //if player wins
    private boolean win = false;

    //for the scrolling
    private int xOffset;
    //where the panel will start scrolling
    //(<300 for the left side, >900 for the right side)
    private int leftBorder = 300;
    private int rightBorder = 900;
    private int tileLength = 200; //the map is 200 tiles long

    //how many tiles are offscreen
    private int maxTileOffset = 150; //200-50
    //dimensions of what is not being shown
    private int maxLevelOffset = maxTileOffset * 24;

    //background images and the lives
    BufferedImage bg1, bg2, bg3, lives;

    //initialize everything
    public Game(){
        //initialize player
        player = new Player(200,200, 80, 80);
        level = new Level(this);
        //initializeEnemies();

        //init. game panel
        gamePanel = new GamePanel(this);

        //put game panel in the game
        gameFrame = new GameFrame(gamePanel);
        gamePanel.requestFocus();

        //import background images
        try {
            bg1 = ImageIO.read(getClass().getResource("/background_layer_1.png"));
            bg2 = ImageIO.read(getClass().getResource("/background_layer_2.png"));
            bg3 = ImageIO.read(getClass().getResource("/background_layer_3.png"));
            lives = ImageIO.read(getClass().getResource("/catlives.png"));

        }
        catch(IOException e){
            System.out.println("did not import");
        }
    }

    //Method that MANUALLY creates enemies if NOT using the level data reader
    private void initializeEnemies() {
        //create enemies *not using this
        Enemy.getEnemies().add(new Enemy(100, 300, 64, 64));
        Enemy.getEnemies().add(new Enemy(1500, 300, 64, 64));
        Enemy.getEnemies().add(new Enemy(3500, 300, 64, 64));
    }


    //method that CHECKS if the player is near the edges of the screen
    //check if the screen should start scrolling
    private void checkCloseToBorder() {
        int playerX = (int) player.getHitBox().x;
        int diff = playerX - xOffset;
        //find the difference between payer position and when it starts scrolling

        //scroll left if too far right
        if (diff > rightBorder)
            xOffset += diff - rightBorder;
        //scroll right if too far left
        else if (diff < leftBorder)
            xOffset += diff - leftBorder;
        if (xOffset > maxLevelOffset)
            xOffset = maxLevelOffset;
        //otherwise chill :)
        else if (xOffset < 0)
            xOffset = 10;
    }

    //method that updates the position of everything
    //ALSO checks for the position of things, and collision
    //update position of EVERYTHING, from each classes' respective update method
    public void update(){
        player.update();
        checkCloseToBorder();
        for (Enemy enemy : Enemy.getEnemies()) {
            enemy.update();

            //check for collision with player and enemies
            if (enemy.getHitBox().intersects(player.getHitBox())) {
               System.out.println("die"); // Player touched an enemy

                //if player still has lives, reset position take a life away
                if(!player.isDead()){
                    player.loseLife();
                    player.resetPosition(200, 200);
                }
            }
        }

        //check for collision with obstacles
        for(Obstacle obstacle : Obstacle.getObstacles()){
            obstacle.update();
            if (obstacle.getHitBox().intersects(player.getHitBox())) {
                System.out.println("die"); // Player touched an enemy

                if(!player.isDead()){
                    player.loseLife();
                    player.resetPosition(200, 200);
                }
            }
        }

        //if the player reaches the end of the screen, win
        if(player.getHitBox().x > 4700){
            win = true;
        }


        //System.out.println(xOffset);



    }

    //method that restarts the game, redeclares everything, used when "start game" is pressed
    public void restartGame(){
        //initialize player
        player = new Player(200,200, 80, 80);
        level = new Level(this);
        //initializeEnemies();

        //init. game panel
        gamePanel = new GamePanel(this);


    }

    //RENDER PLAYER NOT LEVEL
    //method that renders everything, including background and all the other "render" methods in the other classes
    public void render(Graphics g){
        g.drawImage(bg1,0 ,0, 1200, 600, null);
        g.drawImage(bg2,0 ,0, 1200, 600, null);
        g.drawImage(bg3, 0 ,0, 1200, 600, null);


        level.render(g, xOffset);
        player.render(g, xOffset);
        for (Enemy enemy : Enemy.getEnemies()) {
            enemy.render(g, xOffset);
        }
        for(int i = 0; i < player.getLives(); i++){
            g.drawImage(lives,0+ (i*40) ,0, 60, 60, null);
        }
        for (Obstacle obstacle: Obstacle.getObstacles()) {
            obstacle.render(g, xOffset);
        }

    }

    //setter
    public boolean setWin(){
        return win = false;
    }

    //getters
    public Player getPlayer(){
        return player;
    }
    public Level getLevel(){
        return level;
    }
    public boolean isWin(){
        return win;
    }

    //Run the actual program
    public static void main(String[] args){
        new Game();
    }
}
