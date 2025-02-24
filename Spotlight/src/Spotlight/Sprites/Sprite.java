package Spotlight.Sprites;


import java.awt.*;
//super class, contains the position of the various entities
//pretty much just an abstract class (is there even a difference with declaring it with abstract)
public class Sprite {

    //floats are faster? I dont think it makes a difference

    //position of the sprite
    protected float x,y;

    //sprite dimensions
    protected int width, height;
    //rectangle for the hitbox
    protected Rectangle hitBox;

    //constructor method, creates a sprite from x, y, width, height
    public Sprite(float x, float y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        //makes a new hitbox, adjusted to match sprites
        hitBox = new Rectangle((int) x, (int) y, width-35, height-35);
    }

    //method that updates the hitbox (moves with as the sprite moves)
    public void updateHitbox(){
        hitBox.x = (int) x+10;
        hitBox.y = (int) y+15;
        //moving around the hit box so it matches
    }

    //draws a hitbox over the sprites, just for debugging
    protected void drawHitbox(Graphics g, int xOffset){
        //debug
        g.setColor(Color.pink);
        g.drawRect(hitBox.x-xOffset, hitBox.y, hitBox.width, hitBox.height);
    }

    //getter
    public Rectangle getHitBox(){ return hitBox;}
}
