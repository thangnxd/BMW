package main;

import javax.swing.JFrame;
public  class  Main{
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("BLACK_MYTH_WUKONG");
      
        Background gamePanel = new Background();
        window.add(gamePanel);
        window.pack();

        window.setVisible(true);
        window.setLocationRelativeTo(null);
        gamePanel.startGameThread();
       

    }
}