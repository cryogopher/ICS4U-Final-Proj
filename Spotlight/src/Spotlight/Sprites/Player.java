//Daniel Lin, Jan. 19/24
//class for the PLAYER
//stores all the information, including animation, movement, hitbox detection etc.

package Spotlight.Sprites;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Sprite{

    //directions, lives, speed, sprites for animation
    private boolean left, right, up, down, jump;
    private float speed = 3;
    private BufferedImage[][] animations;
    private int lives = 3;


    //how often the animation updates, index(action)
    private int animationTick, animationIndex, animationSpeed = 10;

    private int playerDirection = -1;
    //-1 = idle, 0 = forward, 1= backward, 2 up, 3 down

    private int playerAction = 0;
    //0 = idle, 1 = walking, 2 = running, 3 jumping

    //moving
    private boolean moving = false;
    private int[][] levelData;

    //gravity, and movement in the air
    private double gravity = 0.4;
    private boolean airborne = false;
    private double jumpSpeed = 9;
    private double airSpeed = 0;


    //constructor
    //x,y, sprite position, width, height, size of the sprite
    public Player(float x, float y, int width, int height){
        super(x , y, width, height);
        loadAnimations();
        //take from sprite class

        //get data from the leve
        levelData = getLevelData();
        //animations here after
    }

    //method that resets position if killed for example
    public void resetPosition(float startX, float startY) {
        this.x = startX;
        this.y = startY;
        updateHitbox();
    }

    //method that updates everything, position, animation
    public void update(){
        updatePosition();
        updateAnimation();
        setAnimation();
        updateHitbox();

        //System.out.println(airborne);
    }

    //paint component method basically, updates what is on screen
    public void render(Graphics g, int xOffset){
        g.drawImage(animations[playerAction][animationIndex], (int)x -xOffset, (int)y, width, height,null);
        drawHitbox(g, xOffset );
        //hit box only work for a bit lol
    }

    //method that imports the sprites
    //stores the sprites in a 2d Array, each column is what action, ie. walking, running
    //the rows are the number of sprites in each action
    private void loadAnimations(){

        //import sprites, and then load animations (put them into an array)
        try{
            //import sprite for player
            BufferedImage player1 = ImageIO.read(getClass().getResource("/spritesheet.png"));

            //2d array storing the sprites
            animations = new BufferedImage[4][6];
            for(int i = 0; i < animations.length; i ++){
                for(int j = 0; j < animations[i].length; j++)
                    animations[i][j] = player1.getSubimage(j*64, i*64, 64, 64);

            }
        }
        catch(IOException e){
            System.out.println("did not import");
        }

    }


    //method that constantly updates the method
    private void updateAnimation(){
        animationTick++;
        //every thirty, update the index (next element in array)
        if(animationTick >= animationSpeed){
            animationTick = 0;
            animationIndex++;

            //if it reaches the end of the index, reset
            if(animationIndex >= 5){
                animationIndex = 0;
            }
        }
    }

    //method that:
    //set current animation depending on the movement
    private void setAnimation(){
        if(moving){
            //System.out.println(hitBox.y);
            if(jump){
                playerAction = 3;
                speed = 3;
            }
            else if(left){
                playerAction = 1;
                speed = 3;
            }
            else if(right){
                playerAction = 2;
                speed = 3;
            }
            else{
                playerAction = 0;
            }
        }

    }

    //setter (direction)
    public void setDirection(int direction){
        this.playerDirection = direction;
        moving = true;
    }

    //setter (is moving)
    public void setMoving(boolean moving){
        this.moving = moving;
    }


    //method THAT HANDLES ALL MOVEMENT FOR THE PLAYER
    //update the position of the player depending on their direction
    private void updatePosition(){
        moving = false;
        float xSpeed = 0, ySpeed = 0;

        //check if on ground
        //CHECK THE CORNERS OF THE HITBOX
        if(!airborne){
            //CHECK IF IT INTERSECTS ABOVE (the +3) so the dumb cat doesnt clip into the ground
            if(!isWall(hitBox.x, hitBox.y + hitBox.height+3, levelData)){
                if(!isWall(hitBox.x + hitBox.width, hitBox.y + hitBox.height+3, levelData)){
                    airborne = true;
                }
            }

        }
        //jump
        if(jump){
            jump();
        }
        //move left
        if (left){
            xSpeed -= speed;
        }
        //move right
        if (right){
            xSpeed += speed;
        }

        if(airborne){
            //if on the floor, and you jump
            if(collisionDetection(hitBox.x, hitBox.y + (float)airSpeed, width,height, levelData)){
                y += airSpeed;
                //gravity pulls u down duh
                airSpeed += gravity;

                //update xposition while airborne if there is any
                updateXPos(xSpeed);
            }else{

                //given a collision:
                //System.out.println(YPos(hitBox, (float) airSpeed));
                System.out.println("HIT THE FLOOR");

                //if airspeed>0 (if the cat is on the floor)
                if(airSpeed > 0){
                    //reset after touching floor
                    airborne = false;
                    airSpeed = 0;

                    hitBox.y = (int) (YPos(hitBox, (float) airSpeed));
                }
                else{
                    //otherwise if not airborne
                    airSpeed = 0;
                    updateXPos(xSpeed);
                }

            }
        }
        else{
            updateXPos(xSpeed);
        }

        moving = true;
    }

    private void jump() {
        if(airborne) return;
        airborne = true;
        airSpeed = -jumpSpeed;
    }


    //method that:
    //update the player position based on the speed
    private void updateXPos(float xSpeed){
        if (collisionDetection(hitBox.x + xSpeed, hitBox.y, width, height, levelData)) {
            this.x += xSpeed;
            moving = true;
        }else{
            x = XPos(hitBox, xSpeed);
        }
    }


    //method that:
    //always put one pixel space inbetween hitbox and tile, so the game doesnt get u stuck and run inside walls
    public static float XPos(Rectangle hitBox, float xSpeed){
        int currentTile = (int)(hitBox.x/24);
        if(xSpeed > 0){
            int tileXpos = currentTile*24;
            return tileXpos-10;
        }
        else{
            return currentTile * 24 -10;
        }
    }

    //method that:
    //after hitting the floor, MAKE SURE the cat does not clip through the floor
    public static float YPos(Rectangle hitBox, float airSpeed){
        int currentTile = (hitBox.y/24);
        if(airSpeed > 0){
            //if falling or touching floor
            int tileYpos = currentTile*24;
            System.out.println(tileYpos);
            return tileYpos - 11;
        }
        else{
            //jump
            return currentTile * 24;
        }

    }

    //method that checks if the player is colliding
    //taking in the position of player, + the width/height of the hitbox
    //checks by seeing if the corners of the hitbox are in contact with a tile
    private boolean collisionDetection(float x, float y, int width, int height, int[][]levelData){
        //BUG FIXED. USE THE HITBOX DIMENSIONS NOT THE SPRITE :(((
        if(!isWall(x,y, levelData)){ //check left corner
            if(!isWall(x+ hitBox.width ,y+hitBox.height, levelData)) { //check bottom right corner
                if(!isWall(x+ hitBox.width, y, levelData)){ //check top left
                    if(!isWall(x, y + hitBox.height, levelData)){ //check left
                        return true;
                    }
                }

            }
        }
        return false;
    }

    //method that checks it the current sprite is a wall
    //take x,y, player position, and compare it with the level data to see if it is a wall
    private boolean isWall(float x, float y, int[][]levelData){
        //is a wall if out of bounds
        int maxWidth = levelData[0].length * 24;

        //System.out.println("x: " + x  + "y: " + y);
        if(x < 0 || x >= maxWidth || y < 0 || y >= 600){
            //System.out.println("out of bounds");
            return true;
        }

        //array index; find by taking x and y pos of player, and then dividing by size of the title, to find out what tile they
        //are on
        float arrayY = (y/24);
        float arrayX =  (x/24);

        //find the value in the levelData
        int value = levelData[(int)arrayY][(int)arrayX];

        //System.out.println(arrayY +"  " +arrayX);
        //System.out.println(value);

        if(value > 315 || value != 4 || value < 0) return true;
        return false;
        //is a wall
        //value = 4 is the blank sprite
    }

    //same method as in the level class, imports level data, this is used for collision
    private int[][] getLevelData() {
        int[][] lvlData = new int[25][200];
        try{
            BufferedImage img = ImageIO.read(getClass().getResource("/leveldata.png"));
            for (int j = 0; j < img.getHeight(); j++)
                for (int i = 0; i < img.getWidth(); i++) {
                    //find the red gbg value of each pixel
                    Color color = new Color(img.getRGB(i, j));
                    int value = color.getRed();

                    //if the value goes out of avaliable sprites, then set it to the first one
                    if (value >= 315){
                        value = 0;
                    }
                    if(value == 0){
                        value = 4;
                    }
                    lvlData[j][i] = value;
                }

        }
        catch(IOException e){
            System.out.println("did not import");
        }
        return lvlData;
    }


    //method that decreases a life
    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }

    //method that checks if player still has lives
    public boolean isDead() {
        return lives <= 0; //check if the player has no lives left
    }

    //getters:
    public boolean isLeft(){
        return left;
    }
    public boolean isRight(){
        return right;
    }
    public boolean isUp(){return up;}
    public boolean isDown(){
        return down;
    }
    public int getLives(){return lives;}

    //setters
    public void setUp(boolean up){
        this.up = up;
    }
    public void setLeft(boolean left){
        this.left = left;
    }
    public void setRight(boolean right){
        this.right = right;
    }
    public void setDown(boolean down){
        this.down = down;
    }
    public void setLives(int lives){
        this.lives = 3;
    }
    public void setJump(boolean jump){
        this.jump = jump;
    }

    public void setAnimationIndex(int index){
        animationIndex = index;
    }

}
