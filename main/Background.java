package main;

import entity.Boss;
import entity.Player;  // Make sure to import the Boss class
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Background extends JPanel implements Runnable {
    private BufferedImage backgroundImage;
    // Screen setting
    final int originalTileSize = 20;
    final int scale = 3;
    public final int titleSize = originalTileSize * scale;

    final int maxScreenCol = 23;
    final int maxScreenRow = 13;

    final int screenWidth = titleSize * maxScreenCol;
    final int screenHeight = titleSize * maxScreenRow;
    private final int FPS = 60;
    KeyHandlers keyH = new KeyHandlers();
    
    // Player and Boss instantiation
    public Player player;
    public Boss boss;

    public Background() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        
        // Load background image
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream(("/res/background_1.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize player and boss
        player = new Player(this, keyH);
        boss = new Boss(player, this);  // Pass player to the boss constructor
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Draw the default background
        if (backgroundImage != null) {
            // Draw background image resized to fit the JPanel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        
        // Create Graphics2D object for drawing
        Graphics2D g2 = (Graphics2D) g;
        
        // Draw player and boss
        player.draw(g2);
        boss.draw(g2);  // Draw the boss after the player
        
        g2.dispose();
    }

    private Thread gameThread;

    // Method to start the game loop thread
    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);  // 'this' is the Runnable object
            gameThread.start();  // Start the game loop
        }
    }

    // Game loop logic from the Runnable interface
    @Override
    public void run() {
        // Main game loop
        while (true) {
            double drawInterval = 1000000000 / FPS;
            double nextDrawTime = System.nanoTime() + drawInterval;

            update();  // Update player and boss
            repaint(); // Repaint screen
            
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;
                Thread.sleep((long) remainingTime);  // Sleep to maintain FPS
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Update player and boss state
    public void update() {
        player.update();  // Update player
        boss.update();    // Update boss
    }
}
