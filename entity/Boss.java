package entity;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    private int animationSpeed = 10;  // Tốc độ chuyển đổi khung hình
    private boolean isMoving = false; // Trạng thái di chuyển
    private boolean isUsingSkill1 = false; // Trạng thái sử dụng skill 1
    private boolean isUsingSkill2 = false; // Trạng thái sử dụng skill 2
    private boolean isUsingSkill3 = false;
    private long lastSkill3Time = 0; // Thời gian sử dụng chiêu 3 lần gần nhất
    private static final long COOLDOWN_SKILL3 = 3000; // Thời gian hồi của chiêu 3 (3 giây)

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
            isUsingSkill1 =false;
        }
    }, 1500); // 1000 ms = 1 giây
    }

    public void useSkill2() {
        // Bắt đầu chiêu 2
        isUsingSkill2 = true;
        skillStartTime = System.currentTimeMillis(); // Ghi lại thời gian bắt đầu chiêu 2
        
    
        // Đợi 1 giây (hoặc thời gian của hoạt ảnh) rồi quay lại chiêu 1
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isUsingSkill2 = false;
            }
        }, 1500); // 1000 ms = 1 giây
    }

    public void useSkill3() {
        isUsingSkill3 = true; // Kích hoạt chiêu 3
        skillStartTime = System.currentTimeMillis();
    
        // Sau 1 giây, kết thúc chiêu 3
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isUsingSkill3 = false; // Dừng chiêu 3
            }
        }, 1500); // 1000 ms = 1 giây
    }
    
    public void randomSkillCloseRange() {
        ArrayList<Integer> skills = new ArrayList<>();
        skills.add(1); // Chiêu 1
        skills.add(2); // Chiêu 2
    
        // Shuffle the skills list using Fisher-Yates
        Collections.shuffle(skills);
    
        // Chọn chiêu đầu tiên trong danh sách
        int skill = skills.get(0);
        if (skill == 1) {
            useSkill1();
        } else if (skill == 2) {
            useSkill2();
        }
    }

    
    public void update() {
       // Tăng animationCounter mỗi lần gọi update
    animationCounter++;

    // Khi đạt đến animationSpeed, chuyển sang khung hình tiếp theo
    if (animationCounter >= animationSpeed) {
        animationCounter = 0; // Reset counter


            if (isMoving) {
                animationFrame = (animationFrame + 1) % 5; // 6 khung hình cho di chuyển
            } else if (isUsingSkill1) {
                animationFrame = (animationFrame + 1) % 6; // 6 khung hình cho skill 1
            } else if (isUsingSkill2) {
                animationFrame = (animationFrame + 1) % 3; // 6 khung hình cho skill 2
            } else if (isUsingSkill3) {
                animationFrame = (animationFrame + 1) % 6; // 6 khung hình cho skill 3
            }

            // Tính khoảng cách tới người chơi
            int distanceToPlayer = Math.abs(x - player.getX());
            long currentTime = System.currentTimeMillis();

            if (distanceToPlayer > 50) {
                // Xa người chơi: chọn giữa di chuyển hoặc chiêu 3
                if (Math.random() < 0.5) {
                    moveTowardsPlayer(); // Di chuyển về phía người chơi
                } else if (currentTime - lastSkill3Time >= COOLDOWN_SKILL3) {
                    useSkill3(); // Sử dụng chiêu 3 (nếu chưa cooldown)
                    lastSkill3Time = currentTime;
                }
            } else {
                // Gần người chơi: trộn chiêu 1 và chiêu 2
                isMoving = false;
                randomSkillCloseRange();
            }
        }

        // Di chuyển Boss nếu cần
        if (isMoving) {
            if (direction.equals("left")) {
                x -= speed; // Di chuyển sang trái
            } else if (direction.equals("right")) {
                x += speed; // Di chuyển sang phải
            }
        }
    }

    
    
    
    

    public void moveTowardsPlayer() {
        isMoving = true;
    
        // Cập nhật hướng di chuyển
        if (player.getX() < x) {
            direction = "left";
        } else {
            direction = "right";
        }
    
        // Dừng di chuyển nếu đã đủ gần
        int distanceToPlayer = Math.abs(x - player.getX());
        if (distanceToPlayer <= 50) {
            isMoving = false;
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
        y = 380;
        speed = 2;
    }

    public void getBossImage() {
        try {
            // Load all images (same as before)
            BOSS_move_right_1 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_move_1_right.png"));
            BOSS_move_right_2 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_move_2_right.png"));
            BOSS_move_right_3 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_move_3_right.png"));
            BOSS_move_right_4 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_move_4_right.png"));
            BOSS_move_right_5 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_move_5_right.png"));

            BOSS_move_left_1 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_move_1_left.png"));
            BOSS_move_left_2 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_move_2_left.png"));
            BOSS_move_left_3 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_move_3_left.png"));
            BOSS_move_left_4 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_move_4_left.png"));
            BOSS_move_left_5 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_move_5_left.png"));

            BOSS_skill1_right_1 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack1_1_right.png"));
            BOSS_skill1_right_2 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack1_2_right.png"));
            BOSS_skill1_right_3 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack1_3_right.png"));
            BOSS_skill1_right_4 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack1_4_right.png"));
            BOSS_skill1_right_5 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack1_5_right.png"));
            BOSS_skill1_right_6 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack1_6_right.png"));

            BOSS_skill1_left_1 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack1_1_left.png"));
            BOSS_skill1_left_2 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack1_2_left.png"));
            BOSS_skill1_left_3 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack1_3_left.png"));
            BOSS_skill1_left_4 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack1_4_left.png"));
            BOSS_skill1_left_5 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack1_5_left.png"));
            BOSS_skill1_left_6 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack1_6_left.png"));

            BOSS_SKILL2_right_1 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack2_1_right.png"));
            BOSS_SKILL2_right_2 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack2_2_right.png"));
            BOSS_SKILL2_right_3 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack2_3_right.png"));
            //BOSS_SKILL2_right_4 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_right/s4.png"));
            //BOSS_SKILL2_right_5 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_right/s5.png"));
            // BOSS_SKILL2_right_6 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_right/s6.png"));

            BOSS_SKILL2_left_1 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack2_1_left.png"));
            BOSS_SKILL2_left_2 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack2_2_left.png"));
            BOSS_SKILL2_left_3 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack2_3_left.png"));
            //BOSS_SKILL2_left_4 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_left/s4.png"));
            //BOSS_SKILL2_left_5 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_left/s5.png"));
            // BOSS_SKILL2_left_6 = ImageIO.read(getClass().getResourceAsStream("/res/BOSS/skill_2_left/s6.png"));

            BOSS_SKILL3_right_1 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack3_1_right.png"));
            BOSS_SKILL3_right_2 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack3_2_right.png"));
            BOSS_SKILL3_right_3 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack3_3_right.png"));
            BOSS_SKILL3_right_4 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack3_4_right.png"));
            BOSS_SKILL3_right_5 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack3_5_right.png"));
            BOSS_SKILL3_right_6 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/right/su_attack3_6_right.png"));

            BOSS_SKILL3_left_1 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack3_1_left.png"));
            BOSS_SKILL3_left_2 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack3_2_left.png"));
            BOSS_SKILL3_left_3 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack3_3_left.png"));
            BOSS_SKILL3_left_4 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack3_4_left.png"));
            BOSS_SKILL3_left_5 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack3_5_left.png"));
            BOSS_SKILL3_left_6 = ImageIO.read(getClass().getResourceAsStream("/res/BLACK/left/su_attack3_6_left.png"));
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
                }
            } else if (direction.equals("left")) {
                switch (animationFrame) {
                    case 0: image = BOSS_move_left_1; break;
                    case 1: image = BOSS_move_left_2; break;
                    case 2: image = BOSS_move_left_3; break;
                    case 3: image = BOSS_move_left_4; break;
                    case 4: image = BOSS_move_left_5; break;
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
                    case 4: image = BOSS_skill1_right_5; x+=1;break;
                    case 5: image = BOSS_skill1_right_6; break;
                }
            } else if (direction.equals("left")) {
                switch (animationFrame) {
                    case 0: image = BOSS_skill1_left_1; x-=1;break;
                    case 1: image = BOSS_skill1_left_2; x-=1;break;
                    case 2: image = BOSS_skill1_left_3; x-=1;break;
                    case 3: image = BOSS_skill1_left_4; x-=1;break;
                    case 4: image = BOSS_skill1_left_5; x-=1;break;
                    case 5: image = BOSS_skill1_left_6; break;

                }
            }
        } else if (isUsingSkill2) {
            // Kỹ năng 2
            if (direction.equals("right")) {
                switch (animationFrame) {
                    case 0: image = BOSS_SKILL2_right_1; break;
                    case 1: image = BOSS_SKILL2_right_2; break;
                    case 2: image = BOSS_SKILL2_right_3; break;
                }
            } else if (direction.equals("left")) {
                switch (animationFrame) {
                    case 0: image = BOSS_SKILL2_left_1; break;
                    case 1: image = BOSS_SKILL2_left_2; break;
                    case 2: image = BOSS_SKILL2_left_3; break;
                }
            }
            else if (isUsingSkill3) {
                // Hiển thị chiêu 3 (tầm xa)
                if (direction.equals("right")) {
                    switch (animationFrame) {
                        case 0: image = BOSS_SKILL3_right_1; break;
                        case 1: image = BOSS_SKILL3_right_2; break;
                        case 2: image = BOSS_SKILL3_right_3; break;
                        case 3: image = BOSS_SKILL3_right_4; break;
                        case 4: image = BOSS_SKILL3_right_5; break;
                        case 5: image = BOSS_SKILL3_right_6; break;

                    }
                } else if (direction.equals("left")) {
                    switch (animationFrame) {
                        case 0: image = BOSS_SKILL3_left_1; break;
                        case 1: image = BOSS_SKILL3_left_2; break;
                        case 2: image = BOSS_SKILL3_left_3; break;
                        case 3: image = BOSS_SKILL3_left_4; break;
                        case 4: image = BOSS_SKILL3_left_5; break;
                        case 5: image = BOSS_SKILL3_left_6; break;
                    }
                }
            }
        } 
        else if (isStanding) {
            // Đứng yên
            if (direction.equals("right")) {
                switch (animationFrame) {
                    case 0: image = BOSS_standing_right_1; break;
                    //case 1: image = BOSS_standing_right_2; break;
                    //case 2: image = BOSS_standing_right_3; break;
                   // case 3: image = BOSS_standing_right_4; break;
                }
            } else if (direction.equals("left")) {
                switch (animationFrame) {
                    case 0: image = BOSS_standing_left_1; break;
                    //case 1: image = BOSS_standing_left_2; break;
                    //case 2: image = BOSS_standing_left_3; break;
                    //case 3: image = BOSS_standing_left_4; break;
                }
            }
        }
    
        g2.drawImage(image, x, y, 4 * gp.titleSize, 4 * gp.titleSize, null);
    }
    

}

