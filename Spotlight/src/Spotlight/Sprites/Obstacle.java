//class that stores the data for the obstacles (spikes)
package Spotlight.Sprites;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Obstacle extends Sprite {
    //image for the sprite
    private BufferedImage sprite;
    private static ArrayList<Obstacle> obstacles = new ArrayList<>();

    //constructor:
    //position (x,y), width of the image
    public Obstacle(float x, float y, int width, int height) {
        super(x, y, width, height);
        hitBox = new Rectangle((int) x, (int) y, 24, 24);
        loadImage();
    }


    //getter
    public static ArrayList<Obstacle> getObstacles(){
        return obstacles;
    }

    //method that updates the position of the spikes, ie. when the scren is scrolling
    public void update() {
        updateHitbox();
    }

    //method that draws the sprites
    public void render(Graphics g, int xOffset) {
        g.drawImage(sprite, (int) x-xOffset, (int) y, 40, 40, null);
        //drawHitbox(g, xOffset);
    }

    //method that imports the sprite
    //store
    private void loadImage() {
        //import sprites, and then load animations (put them into an array)
        try {
            //import sprite for player
            sprite = ImageIO.read(getClass().getResource("/spike.png"));
            //2d array storing the sprites
            sprite = sprite.getSubimage( 0, 0, 16, 16);
        } catch (IOException e) {
            System.out.println("did not import");
        }

    }

}
