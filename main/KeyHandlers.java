package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandlers implements KeyListener{
    public boolean upPressed, downPressed, leftPressed, rightPressed, jPressed, kPressed, wPressed;
    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
 
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W){
            wPressed = true;
        }
        if (code == KeyEvent.VK_A){
            leftPressed = true;
        }
        // if (code == KeyEvent.VK_S){
        //     downPressed = true;
        // }
        if (code == KeyEvent.VK_D){
            rightPressed = true;
        }
        if (code == KeyEvent.VK_J){
            jPressed = true;
        }
        if (code == KeyEvent.VK_K){
            kPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W){
            wPressed = false;
        }
        if (code == KeyEvent.VK_A){
            leftPressed = false;
        }
        // if (code == KeyEvent.VK_S){
        //     downPressed = false;
        // }
        if (code == KeyEvent.VK_D){
            rightPressed = false;
        }
        if (code == KeyEvent.VK_J){
            jPressed = false;
        }
        if (code == KeyEvent.VK_K){
            kPressed = false;
        }
    
    }
    
    
}
