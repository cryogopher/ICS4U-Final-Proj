package Spotlight.Inputs;

import Spotlight.Main.GamePanel;
import Spotlight.Sprites.Player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//KEYBOARD INPUTS
public class KeyInputs implements KeyListener {


    private GamePanel gamePanel;

    //turn into boolean probably makes more sense tbh

    //pass in a game panel, so that changes are made to the game panel
    public KeyInputs(GamePanel gamepanel){
        this.gamePanel = gamepanel;

    }


    public void keyTyped(KeyEvent e) {

    }


    //update the moving stuff later
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W ){
            System.out.println("w");
            gamePanel.getGame().getPlayer().setUp(true);

        }
        else if(e.getKeyCode() == KeyEvent.VK_A){
            System.out.println("a");
            gamePanel.getGame().getPlayer().setLeft(true);

        }
        else if(e.getKeyCode() == KeyEvent.VK_D){
            System.out.println("d");
            gamePanel.getGame().getPlayer().setRight(true);

        }
        else if(e.getKeyCode() == KeyEvent.VK_S){
            System.out.println("s");
            gamePanel.getGame().getPlayer().setDown(true);

        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE){
            System.out.println("s");
            gamePanel.getGame().getPlayer().setJump(true);

        }

        //switch to pause menu
        if (e.getKeyCode() == KeyEvent.VK_P) {
            if (gamePanel.getGamestate() == 2) {
                gamePanel.setGamestate(5); // Quit to menu from pause
            }
            else if(gamePanel.getGamestate() == 5) {
                gamePanel.setGamestate(2); // Quit to menu from pause
            }
        }
    }

    public void keyReleased(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_A) {
            gamePanel.getGame().getPlayer().setMoving(false);
            gamePanel.getGame().getPlayer().setLeft(false);

            //left = false;
        }else if(e.getKeyCode() == KeyEvent.VK_D) {
            gamePanel.getGame().getPlayer().setMoving(false);
            gamePanel.getGame().getPlayer().setRight(false);

            //right = false;
        }else if(e.getKeyCode() == KeyEvent.VK_W) {
            gamePanel.getGame().getPlayer().setMoving(false);
            gamePanel.getGame().getPlayer().setUp(false);


            //up = false;
        }else if(e.getKeyCode() == KeyEvent.VK_S) {
            gamePanel.getGame().getPlayer().setMoving(false);
            gamePanel.getGame().getPlayer().setDown(false);


            //down = false;
        }
        /*

         */
        else if(e.getKeyCode() == KeyEvent.VK_SPACE){
            System.out.println("s");
            gamePanel.getGame().getPlayer().setJump(false);

        }

    }
}
