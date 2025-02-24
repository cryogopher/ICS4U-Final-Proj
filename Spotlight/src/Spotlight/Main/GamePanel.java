//Daniel Lin, Jan. 19/24
//CLASS THAT DRAWS EVERYTHING ON SCREEN

package Spotlight.Main;

import Spotlight.Inputs.KeyInputs;
import Spotlight.Inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;


//DRAWS EVERYTHING
//draws the actual things on the frame
//also key input
public class GamePanel extends JPanel implements ActionListener {


    //game, level, gamestates
    private Game game;
    private Level level;
    private int gamestate = 0;
    //gs 0 = menu, 1 = credits/instructions,  2 game, 3 loss, 4 win

    //buttons to press
    private Button startButton;
    private Button creditsButton;
    private Button backButton;
    //bg images for the game states
    BufferedImage menu, loss, win, credits;

    //initialize
    public GamePanel(Game game){
        this.game = game;
        this.level = game.getLevel();

        //add input
        addKeyListener(new KeyInputs(this));
        addMouseListener(new MouseInputs());
        //moved to seperate classes so things are less cluttered, instead of using addKeyListener (This)

        //set dimensions for the panel
        Dimension dimension = new Dimension(1200, 600);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
        setMaximumSize(dimension);

        //honestly idek what this does, henry told me to use this instead of threading
        new Timer(1000 / 70, this).start();

        try {
            //import images
            menu = ImageIO.read(getClass().getResource("/menu.png"));
            loss = ImageIO.read(getClass().getResource("/gameover.png"));
            win = ImageIO.read(getClass().getResource("/win.png"));
            credits = ImageIO.read(getClass().getResource("/credits.png"));
        }
        catch(IOException e){
            System.out.println("did not import");
        }

        Font font = new Font("Monospaced", Font.BOLD, 24);

        //buttons
        startButton = new Button("Start Game");
        creditsButton = new Button("Credits/Instructions");
        backButton = new Button("Back");
        startButton.setFont(font);
        creditsButton.setFont(font);
        backButton.setFont(font);



        //set button sizes
        startButton.setBounds(760, 80, 360, 100);
        creditsButton.setBounds(760, 225, 360, 100);
        backButton.setBounds(780, 400, 250, 75);

        //add to layout
        this.setLayout(null);
        this.add(startButton);
        this.add(creditsButton);
        this.add(backButton);

        //input for buttons
        startButton.addActionListener(e ->{
            gamestate = 2;
            game.restartGame();
            game.setWin();
        } ); //start the game
        creditsButton.addActionListener(e -> gamestate = 1); //to credits/instructions
        backButton.addActionListener(e -> gamestate = 0); //back to menu
    }


    //paint component; update what's on screen
    public Game getGame(){
        return game;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        //gamestates
        if(gamestate == 0){
            //menu
            g.drawImage(menu,0,0,null);
            startButton.setVisible(true);
            creditsButton.setVisible(true);
            backButton.setVisible(false);
        }
        else if(gamestate == 1){
            //credits/instructions
            startButton.setVisible(false);
            creditsButton.setVisible(false);
            backButton.setVisible(true);
            g.drawImage(credits,0,0,null);
        }

        else if(gamestate == 2){
            //playing -> DRAW EVERYTHING IN "GAME"
            game.render(g);
            game.update();
            startButton.setVisible(false);
            creditsButton.setVisible(false);
            backButton.setVisible(false);
            if(game.getPlayer().isDead()){
                gamestate = 3;
            }
            if(game.isWin()){
                gamestate = 4;
            }
        }

        else if(gamestate == 3){
            //loss
            g.drawImage(loss,0,0,null);
            backButton.setVisible(true);
        }

        else if(gamestate == 4){
            //win
            g.drawImage(win,0,0,null);
            backButton.setVisible(true);
        }
        else{
            //pause menu
            startButton.setVisible(false);
            creditsButton.setVisible(false);
            backButton.setVisible(true);
            g.drawString("PAUSE MENU. 'P' to continue. 'BACK' for menu ", 500, 300);
        }

    }

    //Setter
    public int setGamestate(int i){
        return gamestate = i;
    }

    //Getter
    public int getGamestate(){
        return gamestate;
    }

    //repaint
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }


}
