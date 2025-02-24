//Daniel Lin, Jan. 19/24
//class that holds all the information for the enemies
package Spotlight.Sprites;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class Enemy extends Sprite {

    //same as player; [] for images, animation variables to control how fast it cycles through
    private int animationTick, animationIndex, animationSpeed = 10;
    private LinkedList<BufferedImage> animations;
    private static ArrayList<Enemy> enemies = new ArrayList<>();
    //arraylist to hold all the enemies in the level

    //variables for patrolling
    private float patrolStartX;
    private float patrolEndX;
    private boolean movingRight = true;


    //constructor
    public Enemy(float x, float y, int width, int height) {
        super(x, y, width, height);

        //patrol boundaries
        this.patrolStartX = x-100;
        this.patrolEndX = x+100;
        hitBox = new Rectangle((int) x, (int) y, 10, 24);
        loadAnimations();
    }

    //method that constantly updates the animation
    private void updateAnimation() {
        animationTick++;
        //every thirty, update the index (next element in array)
        if (animationTick >= animationSpeed) {
            animationTick = 0;

            BufferedImage firstFrame = animations.removeFirst();
            animations.addLast(firstFrame);
        }
    }

    //getter
    public static ArrayList<Enemy> getEnemies(){
        return enemies;
    }

    //method update the animation and hitbox
    public void update() {
        updateAnimation();
        updateHitbox();
        if (movingRight) {
            x += 1; // Move right
            if (x >= patrolEndX) {
                movingRight = false; // Change direction
            }
        } else {
            x -= 1; // Move left
            if (x <= patrolStartX) {
                movingRight = true; // Change direction
            }
        }

    }

    //method that draws the image and hitbox
    public void render(Graphics g, int xOffset) {
        g.drawImage(animations.getFirst(), (int) x-xOffset, (int) y, 24, 24, null);
        //drawHitbox(g, xOffset);

    }



    public int getAnimationindex() {
        return animationIndex;
    }

    //method that imports the sprites
    //stores the sprites in a 2d Array, each column is what action, ie. walking, running
    //the rows are the number of sprites in each action
    private void loadAnimations() {
        //import sprites, and then load animations (put them into an array)
        try {
            //import sprite for player
            BufferedImage player1 = ImageIO.read(getClass().getResource("/dogWalk.png"));

            //2d array storing the sprites
            animations = new LinkedList<>();
            for (int i = 0; i < 6; i++) {
                BufferedImage wtv = player1.getSubimage(i * 48, 0, 48, 48);
                animations.add(wtv);

            }
        } catch (IOException e) {
            System.out.println("did not import");
        }

    }
}
