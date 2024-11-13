package main;
import entity.Player;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
public class Background extends JPanel implements Runnable{
    private BufferedImage backgroundImage;
    // Screen setting
    final int originalTileSize = 20;
    final int scale = 3;
    public final int titleSize = originalTileSize * scale;

    final int maxScreenCol = 20;
    final int maxScreenRow = 12;

    final int screenWidth = titleSize * maxScreenCol;
    final int screenHeight = titleSize * maxScreenRow;
    private final int FPS = 60;
    KeyHandlers keyH = new KeyHandlers();
    public Background(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream(("/res/background_1.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Vẽ phần nền mặc định
        if (backgroundImage != null) {
            // Vẽ ảnh với kích thước của JPanel
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        Graphics2D g2 = (Graphics2D)g;
        player.draw(g2);
        g2.dispose();
        
    }
    

    private Thread gameThread;
    Player player = new Player(this, keyH);
    int playerY = 400;
    int playerSpeed = 4;

    // Phương thức để khởi động luồng trò chơi
    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);  // 'this' là đối tượng Runnable
            gameThread.start();  // Bắt đầu luồng
        }
    }

    // Phương thức từ giao diện Runnable
    @Override
    public void run() {
        // Vòng lặp trò chơi chính
        while (true) {
            double drawInterval = 1000000000 / FPS;
            double nextDrawTime = System.nanoTime() + drawInterval;
           
            update();
            repaint();
            
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;
                // if(remainingTime < 0){
                //     remainingTime = 0;
                // }
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        player.update();
    }
    
        
}
