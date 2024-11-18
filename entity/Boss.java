package entity;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import main.Background;

public class Boss extends Entity {
    private static final int ATTACK_DISTANCE = 20;  // Khoảng cách tấn công (20 pixels)
    private long lastMoveTime = 0;  // Thời gian của lần di chuyển/đứng yên cuối cùng
    private boolean isStanding = false;  // Kiểm tra trạng thái đứng yên
    private static final long MOVE_DURATION = 3000;  // Thời gian chạy (3 giây)
    private static final long STAND_DURATION = 1000;  // Thời gian đứng yên (1 giây)
    private long moveDurationCounter = 0;  // Bộ đếm thời gian cho di chuyển
    private long standDurationCounter = 0;  // Bộ đếm thời gian cho đứng yên
    private int animationFrame = 0;  // Khung hình hiện tại
    private int animationCounter = 0; // Bộ đếm cho các khung hình
    private int animationSpeed = 5;  // Tốc độ chuyển đổi khung hình
    private boolean isMoving = false; // Trạng thái di chuyển
    private boolean isUsingSkill1 = false; // Trạng thái sử dụng skill 1
    private boolean isUsingSkill2 = false; // Trạng thái sử dụng skill 2
    private String direction = "right";  // Hướng di chuyển của boss (right/left)
    private long skillStartTime = 0;

    private int barWidth = 400;  // Chiều rộng của thanh máu
    private int barHeight = 20;

    private int currentHealth = 100;  // Máu hiện tại
    private int maxHealth = 100;      // Máu tối đa


    private BufferedImage skill1Image, skill2Image;
    private Background gp;
    private Player player;



    public void drawHealthBar(Graphics2D g2) {
        // Tính toán vị trí của thanh máu ở góc trên cùng bên phải
        int healthBarX = 1380 - barWidth - 10; // 1380 là chiều rộng của frame, 10 là khoảng cách từ cạnh phải
        int healthBarY = 10; // Khoảng cách từ cạnh trên cùng
    
        // Vẽ viền ngoài
        g2.setColor(Color.WHITE); // Sử dụng màu trắng cho viền
        g2.fillRoundRect(healthBarX, healthBarY, barWidth + 4, barHeight + 4, 10, 10); // Viền tròn
    
        // Vẽ nền thanh máu
        g2.setColor(new Color(50, 50, 50)); // Màu nền xám
        g2.fillRoundRect(healthBarX, healthBarY, barWidth, barHeight, 8, 8); // Nền nhỏ hơn viền một chút
    
        // Vẽ thanh máu
        int healthBarWidth = (int) (barWidth * ((double) currentHealth / maxHealth)); // Chiều rộng thanh máu tùy theo tỷ lệ máu hiện tại
        GradientPaint gradient = new GradientPaint(2, 2, Color.RED, 2 + healthBarWidth, 2 + barHeight, Color.YELLOW, false);
        g2.setPaint(gradient);
        g2.fillRoundRect(healthBarX + 2, healthBarY + 2, healthBarWidth, barHeight, 8, 8); // Vẽ gradient từ đỏ sang vàng
    }
    
    
    public void updateHealth(int newHealth) {
        currentHealth = newHealth;
        // Gọi hàm vẽ lại thanh máu tại đây hoặc từ vòng lặp game
    }    
    
    public void useSkill1() {
    // Bắt đầu chiêu 1
    isUsingSkill1 = true;
    skillStartTime = System.currentTimeMillis(); // Ghi lại thời gian bắt đầu chiêu 1
    

    // Đợi 1 giây (hoặc thời gian của hoạt ảnh) rồi chuyển sang chiêu 2
    new Timer().schedule(new TimerTask() {
        @Override
        public void run() {
            useSkill2(); // Sau 1 giây, sử dụng chiêu 2
        }
    }, 1000); // 1000 ms = 1 giây
    }

    public void useSkill2() {
        // Bắt đầu chiêu 2
        isUsingSkill2 = true;
        skillStartTime = System.currentTimeMillis(); // Ghi lại thời gian bắt đầu chiêu 2
        
    
        // Đợi 1 giây (hoặc thời gian của hoạt ảnh) rồi quay lại chiêu 1
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                useSkill1(); // Sau 1 giây, quay lại chiêu 1
            }
        }, 1000); // 1000 ms = 1 giây
    }

    public void update() {
        animationCounter++;
        
        // Kiểm tra thời gian chuyển khung hình
        if (animationCounter >= animationSpeed) {
            animationCounter = 0;
            
            // Di chuyển Boss về phía người chơi
            moveTowardsPlayer();
            
            // Cập nhật trạng thái đứng yên hay di chuyển
            if (isMoving) {
                isStanding = false;  // Boss đang di chuyển, không đứng yên
                switch (direction) {
                    case "left":
                        animationFrame = (animationFrame + 1) % 6;  // 6 frame khi di chuyển sang trái
                        break;
                    case "right":
                        animationFrame = (animationFrame + 1) % 6;  // 6 frame khi di chuyển sang phải
                        break;
                }
            } else {
                isStanding = true;  // Boss đang đứng yên
                animationFrame = (animationFrame + 1) % 4;  // 4 frame khi đứng yên
            }
            
            // Tính khoảng cách giữa Boss và người chơi
            int distanceToPlayer = Math.abs(x - player.getX()); // Khoảng cách theo trục X
            
            // Kiểm tra nếu Boss gần người chơi (20 pixel)
            if (distanceToPlayer <= 40) {
                // Nếu Boss đã gần người chơi, kiểm tra và sử dụng chiêu
                if (isUsingSkill1 || isUsingSkill2) {
                    return;  // Nếu đã đang sử dụng chiêu, không tiếp tục
                }
                
                // Nếu không đang sử dụng kỹ năng, bắt đầu chiêu 1
                useSkill1(); // Dùng chiêu 1
            }
        }
        
        // Di chuyển Boss theo hướng đã xác định
        if (isMoving) {
            if (direction.equals("left")) {
                x -= speed; // Di chuyển sang trái
            } else if (direction.equals("right")) {
                x += speed; // Di chuyển sang phải
            }
        } else {
            // Nếu Boss không di chuyển (đứng yên), giữ nguyên vị trí
            // Không cần phải làm gì thêm nếu Boss đứng yên
        }
    }
    
    
    
    

    public void moveTowardsPlayer() {
        long currentTime = System.currentTimeMillis();
        
        if (isStanding) {
            // Nếu Boss đang đứng yên, kiểm tra xem đã đủ thời gian đứng yên chưa
            if (currentTime - standDurationCounter >= STAND_DURATION) {
                // Nếu đủ thời gian đứng yên, bắt đầu di chuyển
                isStanding = false;
                lastMoveTime = currentTime;  // Cập nhật thời gian khi bắt đầu di chuyển
            }
        } else {
            // Nếu Boss đang di chuyển, kiểm tra xem đã đủ thời gian di chuyển chưa
            if (currentTime - lastMoveTime >= MOVE_DURATION) {
                // Nếu đủ thời gian di chuyển, bắt đầu đứng yên
                isStanding = true;
                standDurationCounter = currentTime;  // Cập nhật thời gian khi bắt đầu đứng yên
            } else {
                // Nếu Boss vẫn đang di chuyển, tiến về phía người chơi
                int playerX = player.getX();
                int playerY = player.getY();
    
                // Nếu Boss ở bên phải người chơi, di chuyển sang trái, ngược lại di chuyển sang phải
                if (x > playerX) {
                    direction = "left";
                    isMoving = true;
                } else if (x < playerX) {
                    direction = "right";
                    isMoving = true;
                } else {
                    isMoving = false;  // Nếu Boss đã ở đúng vị trí với người chơi, dừng di chuyển
                }
            }
        }
    }
    

    // public void useRandomSkill() {
    //     // Chọn ngẫu nhiên giữa kỹ năng 1 và kỹ năng 2 để sử dụng
    //     if (!isUsingSkill1 && !isUsingSkill2 && Math.random() < 0.8) {
    //         // Xác suất thấp để sử dụng chiêu mỗi lần update
    //         if (Math.random() < 0.5) {
    //             isUsingSkill1 = true; // Sử dụng kỹ năng 1
    //             isUsingSkill2 = false;
    //         } else {
    //             isUsingSkill2 = true; // Sử dụng kỹ năng 2
    //             isUsingSkill1 = false;
    //         }
    //     }
    // }
    

    public Boss(Player player, Background gp) {
        this.gp = gp;
        this.player = player;
        setDefaultValues();
        getBossImage();
    }

    public void setDefaultValues() {
        x = 500; // Starting position of the boss
        y = 500;
        speed = 6;
    }

    public void getBossImage() {
        try {
            // Load all images (same as before)
            BOSS_standing_right_1 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/standing_right/s1.png"));
            BOSS_standing_right_2 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/standing_right/s2.png"));
            BOSS_standing_right_3 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/standing_right/s3.png"));
            BOSS_standing_right_4 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/standing_right/s4.png"));

            BOSS_standing_left_1 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/standing_left/s1.png"));
            BOSS_standing_left_2 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/standing_left/s2.png"));
            BOSS_standing_left_3 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/standing_left/s3.png"));
            BOSS_standing_left_4 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/standing_left/s4.png"));

            BOSS_move_right_1 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/move_right/s1.png"));
            BOSS_move_right_2 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/move_right/s2.png"));
            BOSS_move_right_3 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/move_right/s3.png"));
            BOSS_move_right_4 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/move_right/s4.png"));
            BOSS_move_right_5 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/move_right/s5.png"));
            BOSS_move_right_6 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/move_right/s6.png"));

            BOSS_move_left_1 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/move_left/s1.png"));
            BOSS_move_left_2 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/move_left/s2.png"));
            BOSS_move_left_3 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/move_left/s3.png"));
            BOSS_move_left_4 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/move_left/s4.png"));
            BOSS_move_left_5 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/move_left/s5.png"));
            BOSS_move_left_6 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/move_left/s6.png"));

            BOSS_skill1_right_1 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_1_right/s1.png"));
            BOSS_skill1_right_2 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_1_right/s2.png"));
            BOSS_skill1_right_3 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_1_right/s3.png"));
            BOSS_skill1_right_4 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_1_right/s4.png"));
            BOSS_skill1_right_5 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_1_right/s5.png"));
            BOSS_skill1_right_6 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_1_right/s6.png"));

            BOSS_skill1_left_1 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_1_left/s1.png"));
            BOSS_skill1_left_2 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_1_left/s2.png"));
            BOSS_skill1_left_3 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_1_left/s3.png"));
            BOSS_skill1_left_4 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_1_left/s4.png"));
            BOSS_skill1_left_5 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_1_left/s5.png"));
            BOSS_skill1_left_6 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_1_left/s6.png"));

            BOSS_SKILL2_right_1 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_right/s1.png"));
            BOSS_SKILL2_right_2 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_right/s2.png"));
            BOSS_SKILL2_right_3 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_right/s3.png"));
            BOSS_SKILL2_right_4 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_right/s4.png"));
            BOSS_SKILL2_right_5 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_right/s5.png"));
            // BOSS_SKILL2_right_6 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_right/s6.png"));

            BOSS_SKILL2_left_1 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_left/s1.png"));
            BOSS_SKILL2_left_2 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_left/s2.png"));
            BOSS_SKILL2_left_3 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_left/s3.png"));
            BOSS_SKILL2_left_4 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_left/s4.png"));
            BOSS_SKILL2_left_5 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_left/s5.png"));
            // BOSS_SKILL2_left_6 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_left/s6.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        drawHealthBar(g2);
        if (isMoving) {
            // Di chuyển - điều chỉnh frame tương ứng
            if (direction.equals("right")) {
                switch (animationFrame) {
                    case 0: image = BOSS_move_right_1; break;
                    case 1: image = BOSS_move_right_2; break;
                    case 2: image = BOSS_move_right_3; break;
                    case 3: image = BOSS_move_right_4; break;
                    case 4: image = BOSS_move_right_5; break;
                    case 5: image = BOSS_move_right_6; break;
                }
            } else if (direction.equals("left")) {
                switch (animationFrame) {
                    case 0: image = BOSS_move_left_1; break;
                    case 1: image = BOSS_move_left_2; break;
                    case 2: image = BOSS_move_left_3; break;
                    case 3: image = BOSS_move_left_4; break;
                    case 4: image = BOSS_move_left_5; break;
                    case 5: image = BOSS_move_left_6; break;
                }
            }
        } else if (isUsingSkill1) {
            // Kỹ năng 1
            if (direction.equals("right")) {
                switch (animationFrame) {
                    case 0: image = BOSS_skill1_right_1; x+=1; break;
                    case 1: image = BOSS_skill1_right_2; x+=1;break;
                    case 2: image = BOSS_skill1_right_3; x+=1;break;
                    case 3: image = BOSS_skill1_right_4; x+=1;break;
                    case 4: image = BOSS_skill1_right_5; break;
                }
            } else if (direction.equals("left")) {
                switch (animationFrame) {
                    case 0: image = BOSS_skill1_left_1; x-=1;break;
                    case 1: image = BOSS_skill1_left_2; x-=1;break;
                    case 2: image = BOSS_skill1_left_3; x-=1;break;
                    case 3: image = BOSS_skill1_left_4; x-=1;break;
                    case 4: image = BOSS_skill1_left_5; break;
                }
            }
        } else if (isUsingSkill2) {
            // Kỹ năng 2
            if (direction.equals("right")) {
                switch (animationFrame) {
                    case 0: image = BOSS_SKILL2_right_1; break;
                    case 1: image = BOSS_SKILL2_right_2; break;
                    case 2: image = BOSS_SKILL2_right_3; break;
                    case 3: image = BOSS_SKILL2_right_4; break;
                    case 4: image = BOSS_SKILL2_right_5; break;
                }
            } else if (direction.equals("left")) {
                switch (animationFrame) {
                    case 0: image = BOSS_SKILL2_left_1; break;
                    case 1: image = BOSS_SKILL2_left_2; break;
                    case 2: image = BOSS_SKILL2_left_3; break;
                    case 3: image = BOSS_SKILL2_left_4; break;
                    case 4: image = BOSS_SKILL2_left_5; break;
                }
            }
        } 
        else if (isStanding) {
            // Đứng yên
            if (direction.equals("right")) {
                switch (animationFrame) {
                    case 0: image = BOSS_standing_right_1; break;
                    case 1: image = BOSS_standing_right_2; break;
                    case 2: image = BOSS_standing_right_3; break;
                    case 3: image = BOSS_standing_right_4; break;
                }
            } else if (direction.equals("left")) {
                switch (animationFrame) {
                    case 0: image = BOSS_standing_left_1; break;
                    case 1: image = BOSS_standing_left_2; break;
                    case 2: image = BOSS_standing_left_3; break;
                    case 3: image = BOSS_standing_left_4; break;
                }
            }
        }
    
        g2.drawImage(image, x, y, 2 * gp.titleSize, 2 * gp.titleSize, null);
    }
    

}

