package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.Background;

public class Boss extends Entity {
    private boolean isSkill1Executed = false; // Cờ kiểm soát để đảm bảo Skill1 chỉ hiển thị một lần

    // Cooldown và hoạt ảnh chiêu thức
private int skill1Cooldown = 300; // Cooldown cho Skill1 (5 giây = 300 lần cập nhật)
private int skill2Cooldown = 300; // Cooldown cho Skill2
private int skill1CooldownCounter = 0; // Bộ đếm cooldown Skill1
private int skill2CooldownCounter = 0; // Bộ đếm cooldown Skill2

private final int skillAnimationSpeed = 15; // Tốc độ chậm hơn cho chiêu thức (15 lần cập nhật đổi 1 khung)
private int skillAnimationCounter = 0; // Bộ đếm hoạt ảnh chiêu thức

// Trạng thái Boss
private int maxHealth = 5000; // Máu tối đa của Boss
private int currentHealth = 5000; // Máu hiện tại của Boss
private int speed = 4; // Tốc độ di chuyển của Boss
private String direction = "right"; // Hướng di chuyển ban đầu ("right" hoặc "left")

// Biến hoạt ảnh chung
private int animationFrame = 0; // Khung hình hiện tại
private int animationCounter = 0; // Bộ đếm để kiểm soát tốc độ chuyển đổi khung hình
private final int animationSpeed = 7; // Tốc độ chuyển đổi khung hình (mỗi 7 lần cập nhật đổi 1 khung)

// Trạng thái chiêu thức
private boolean isUsingSkill1 = false; // Trạng thái đang sử dụng Skill1
private boolean isUsingSkill2 = false; // Trạng thái đang sử dụng Skill2
private int skillCounter1 = 0; // Bộ đếm thời gian cho Skill1
private int skillCounter2 = 0; // Bộ đếm thời gian cho Skill2
    private BufferedImage skill1Image, skill2Image;
    Background gp;
    private Player player;

    public Boss(Player player, Background gp) {
        this.gp = gp;
        this.player = player;
        setDefaultValues();
        getBossImage();
    }

    public void setDefaultValues() {
        x = 500; // Starting position of the boss
        y = 500;
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        // Cập nhật vị trí di chuyển của Boss
        if (player.getX() > this.x) {
            direction = "right";
            x += speed;
        } else if (player.getX() < this.x) {
            direction = "left";
            x -= speed;
        }
    
        // Kiểm tra điều kiện ra chiêu
        if (Math.abs(player.getX() - this.x) < 200) { // Nếu người chơi ở gần
            // Kiểm tra Skill1
            if (!isUsingSkill1 && skill1CooldownCounter >= skill1Cooldown) {
                useSkill1(); // Sử dụng Skill1
            }
    
            // Kiểm tra Skill2
            if (!isUsingSkill2 && skill2CooldownCounter >= skill2Cooldown) {
                useSkill2(); // Sử dụng Skill2
            }
        }
    
        // Tăng bộ đếm cooldown nếu kỹ năng không được sử dụng
        if (skill1CooldownCounter < skill1Cooldown) {
            skill1CooldownCounter++;
        }
        if (skill2CooldownCounter < skill2Cooldown) {
            skill2CooldownCounter++;
        }
    
        // Đếm thời gian kỹ năng đang được sử dụng
        if (isUsingSkill1) {
            animationFrame = (animationFrame + 1) % 6; // Chuyển đổi khung hình Skill1
            skillCounter1++;
            if (skillCounter1 >= 6) { // Khi Skill1 kết thúc
                isUsingSkill1 = false; // Kết thúc trạng thái Skill1
                skill1CooldownCounter = 0; // Reset cooldown
                isSkill1Executed = false; // Đặt lại cờ để cho phép sử dụng Skill1 lần tiếp theo
            }
        }
    
        if (isUsingSkill2) {
            skillCounter2++;
            if (skillCounter2 > 1) { // Giả sử Skill2 kéo dài 1.5 giây (~100 lần cập nhật)
                isUsingSkill2 = false; // Kết thúc kỹ năng
                skill2CooldownCounter = 0; // Bắt đầu cooldown
            }
        }
    
        // Cập nhật hoạt ảnh
        animationCounter++;
        if (animationCounter >= animationSpeed) {
            animationFrame++;
            animationCounter = 0;
            if (animationFrame > 3) { // Quay lại khung hình đầu tiên
                animationFrame = 0;
            }
        }
    }
    

    public BufferedImage getCurrentImage() {
        BufferedImage image = null;

        if (isUsingSkill1) {
            // If Boss is using skill1
            if (direction.equals("right")) {
                // Boss uses skill1 facing right
                image = getSkill1RightImage();
            } else if (direction.equals("left")) {
                // Boss uses skill1 facing left
                image = getSkill1LeftImage();
            }
        } else {
            // Regular animation for moving or standing
            if (direction.equals("left")) {
                if (isMoving()) {
                    image = getMoveLeftImage();
                } else {
                    image = getStandingLeftImage();
                }
            } else if (direction.equals("right")) {
                if (isMoving()) {
                    image = getMoveRightImage();
                } else {
                    image = getStandingRightImage();
                }
            }
        }

        return image;
    }

    private BufferedImage getSkill1RightImage() {
        // Tăng bộ đếm animation chiêu
        skillAnimationCounter++;
        if (skillAnimationCounter >= skillAnimationSpeed) {
            skillCounter1++;
            skillAnimationCounter = 0; // Reset bộ đếm khi đủ tốc độ
        }
    
        // Lấy khung hình phù hợp
        switch (skillCounter1 % 6) {
            case 0: return BOSS_skill1_right_1;
            case 1: return BOSS_skill1_right_2;
            case 2: return BOSS_skill1_right_3;
            case 3: return BOSS_skill1_right_4;
            case 4: return BOSS_skill1_right_5;
            case 5: return BOSS_skill1_right_6;
            default: return BOSS_skill1_right_1;
        }
    }
    
    private BufferedImage getSkill1LeftImage() {
        // Tăng bộ đếm animation chiêu
        skillAnimationCounter++;
        if (skillAnimationCounter >= skillAnimationSpeed) {
            skillCounter1++;
            skillAnimationCounter = 0; // Reset bộ đếm khi đủ tốc độ
        }
    
        // Lấy khung hình phù hợp
        switch (skillCounter1 % 6) {
            case 0: return BOSS_skill1_left_1;
            case 1: return BOSS_skill1_left_2;
            case 2: return BOSS_skill1_left_3;
            case 3: return BOSS_skill1_left_4;
            case 4: return BOSS_skill1_left_5;
            case 5: return BOSS_skill1_left_6;
            default: return BOSS_skill1_left_1;
        }
    }
    

    private BufferedImage getMoveRightImage() {
        switch (animationFrame) {
            case 0: return BOSS_move_right_1;
            case 1: return BOSS_move_right_2;
            case 2: return BOSS_move_right_3;
            case 3: return BOSS_move_right_4;
            case 4: return BOSS_move_right_5;
            case 5: return BOSS_move_right_6;
            default: return BOSS_move_right_1;
        }
    }

    private BufferedImage getMoveLeftImage() {
        switch (animationFrame) {
            case 0: return BOSS_move_left_1;
            case 1: return BOSS_move_left_2;
            case 2: return BOSS_move_left_3;
            case 3: return BOSS_move_left_4;
            case 4: return BOSS_move_left_5;
            case 5: return BOSS_move_left_6;
            default: return BOSS_move_left_1;
        }
    }

    private BufferedImage getStandingRightImage() {
        switch (animationFrame) {
            case 0: return BOSS_standing_right_1;
            case 1: return BOSS_standing_right_2;
            case 2: return BOSS_standing_right_3;
            case 3: return BOSS_standing_right_4;
            default: return BOSS_standing_right_1;
        }
    }

    private BufferedImage getStandingLeftImage() {
        switch (animationFrame) {
            case 0: return BOSS_standing_left_1;
            case 1: return BOSS_standing_left_2;
            case 2: return BOSS_standing_left_3;
            case 3: return BOSS_standing_left_4;
            default: return BOSS_standing_left_1;
        }
    }

    public void useSkill1() {
        isUsingSkill1 = true;
        skillCounter1 = 0; // Reset bộ đếm thời gian sử dụng kỹ năng
    }
    
    public void useSkill2() {
        isUsingSkill2 = true;
        skillCounter2 = 0; // Reset bộ đếm thời gian sử dụng kỹ năng
    }
    

    public boolean isMoving() {
        return player.getX() != x;
    }

    public void draw(Graphics2D g) {
        g.drawImage(getCurrentImage(), x, y, 2 * gp.titleSize, 2 * gp.titleSize, null);
    }
}
