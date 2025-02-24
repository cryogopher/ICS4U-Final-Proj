package Spotlight.Main;

import javax.swing.JFrame;

//Just a panel, tbh idk why this is a separate class
//the frame, where the panel is placed

public class GameFrame{


    //create a frame with the panel inside
    public GameFrame(GamePanel gamePanel) {
        JFrame jframe = new JFrame();

        //self explanatory
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.pack();
        //set resizeable false -> does not matterx
        jframe.setVisible(true);
        gamePanel.setFocusable(true);
    }

}