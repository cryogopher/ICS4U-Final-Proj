//Daniel Lin, Jan. 19/24
//CLASS THAT CONTAINS all the data for the levels

package Spotlight.Main;

import Spotlight.Sprites.Enemy;
import Spotlight.Sprites.Obstacle;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


//data for the levels (include a 2d array here)
public class Level extends JFrame {
    //pass into the game
    private Game game;
    //image importation for the level
    private BufferedImage[] levelImages;
    private BufferedImage levelBuffer;

    //constructor
    public Level(Game game){
        Enemy.getEnemies().clear();

        this.game = game;
        loadSprites();
        createLevelBuffer();

        //import the images while ur at it :)
    }

    //just like in player class, draw the tiles
    public void render(Graphics g, int xOffset){
        //number of rows: 20, columns: 10
        if(levelBuffer != null){
            g.drawImage(levelBuffer, -xOffset,0, null);
        }
    }


    //EXPLANATION FOR LEVEL MAP:
    //Its pretty much the 2d array, but except of doing it manually in console, you edit the pixels
    //externally, and then read every individual pixel
    //u take the red value of the rgb value, and this gives you 255 indexes to cycle through, each assigned to
    //a different sprite on the sprite sheet.
    //Should allow for greater flexibility, since you can assign the R value to the platforms for example, and then
    //the B values for if there is an obstacle or wtv on the platform


    //method that creates a bufferedimage out of the data, instead of redrawing it with for loops constantly
    private void createLevelBuffer() {
        int width = 4800;
        int height = 600;
        levelBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = levelBuffer.getGraphics();
        int[][] levelData = getLevelData();
        int[][] enemyData = getEnemyData();
        for (int i = 0; i < 25; i++) { //columns
            for (int j = 0; j < 200; j++) { //rows
                int index = levelData[i][j];
                int enemyIndex = levelData[i][j];
                g.drawImage(levelImages[index], j * 24, i * 24, 24, 24, null);

            }
        }

    }

    //method that loads the sprites for the level
    public void loadSprites(){
        try{
            BufferedImage sprites = ImageIO.read(getClass().getResource("/oak_woods_tileset.png"));

            //array storing the sprites
            //315 big cuz the sprite sheet is (21*15 lol)
           levelImages = new BufferedImage[315];

           //assign each index a sprite
            for(int i = 0; i < 15; i ++){
                for(int j = 0; j < 21; j++){
                    int index = i*21 + j;
                    levelImages[index] = sprites.getSubimage(j*24, i*24, 24, 24);
                }

            }
        }
        catch(IOException e){
            System.out.println("did not import");
        }
    }


    //method that stores the data of the image onto a 2d array, that is the size of the game frame
    //works the same way as just doing it manually,
    //get rgb value of each pixel, then assigns it based off the method above, that makes a 2d array of sprites
    public int[][] getLevelData() {
        int[][] lvlData = new int[25][200];
        try{
            BufferedImage img = ImageIO.read(getClass().getResource("/leveldata.png"));

            for (int j = 0; j < img.getHeight(); j++)
                for (int i = 0; i < img.getWidth(); i++) {
                    Color color = new Color(img.getRGB(i, j));
                    int value = color.getRed();
                    //if the value goes out of avaliable sprites, then set it to blank
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

    //same method for reading the level data, but now looks for green rgb value
    //assigns enemies and obstacles instead of level sprites
    public int[][] getEnemyData(){
        int[][] lvlData = new int[25][200];
        try{
            BufferedImage img = ImageIO.read(getClass().getResource("/leveldata.png"));
            for (int j = 0; j < img.getHeight(); j++)
                for (int i = 0; i < img.getWidth(); i++) {
                    Color color = new Color(img.getRGB(i, j));
                    int value = color.getGreen();
                    //if the value goes out of avaliable sprites, then set it to blank
                            //0= no enemy, 1 = enemy
                    if(value == 100){
                        Enemy.getEnemies().add(new Enemy(i*24, j * 24, 48, 48));
                    }
                    else if(value == 200){
                        Obstacle.getObstacles().add(new Obstacle(i*24, j * 24, 48, 48));
                    }
                    else{
                        value = 0;
                    }
                    lvlData[j][i] = value;
                }

            }
            catch(IOException e){
                System.out.println("did not import");
            }
            return lvlData;

    }


    //change later

    //include a 2d array here

    //draw entity methods

    //draw enemy method
}
