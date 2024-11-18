package entity;


import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.Background;
import main.KeyHandlers;
public class Player extends Entity{
    
    


    private int maxHealth = 3000; // Máu tối đa, giả sử là 3.000 để phù hợp với số trên hình ảnh
    private int currentHealth = 3000; // Máu hiện tại
    private int barWidth = 400; // Chiều rộng của thanh máu
    private int barHeight = 20; // Chiều cao của thanh máu
    





    private int animationFrame = 0; // Biến đếm khung hình hiện tại
    private int animationCounter = 0; // Biến đếm thời gian chuyển đổi khung hình
    private final int animationSpeed = 7; // Tốc độ chuyển đổi khung hình
    private boolean isMoving = false; // Biến để kiểm tra xem nhân vật có di chuyển hay không
    private boolean isUsingSkill = false;
    private boolean isUsingJump = false;
    private int skillCounter = 0; // Đếm thời gian để điều khiển hoạt ảnh skill
    private boolean isUsingSkillK = false;
    private int skillKCounter = 0; // Đếm thời gian để điều khiển hoạt ảnh skill qua phím K
    Background gp;
    KeyHandlers keyH;


    public Player(Background gp, KeyHandlers keyH){
        this.gp = gp;
        this.keyH = keyH;
        setDefaultValues();
        getPlayerImage();
    }
    public void setDefaultValues(){
        x = 0;
        y = 500;
        speed = 6;
        direction = "down";
    }
    // public void drawHealthBar(Graphics2D g2) {
    //     // Vẽ nền thanh máu (màu xám)
    //     g2.setColor(new Color(50, 50, 50)); // Màu nền xám tối
    //     g2.fillRoundRect(0, 0, 400, 30, 15, 15); // Vẽ nền thanh máu với bo góc
        
    //     // Vẽ viền cho thanh máu (màu đen)
    //     g2.setColor(Color.BLACK);
    //     g2.setStroke(new BasicStroke(2)); // Đặt độ dày viền
    //     g2.drawRoundRect(0, 0, 400, 30, 15, 15); // Vẽ viền thanh máu
        
    //     // Tính chiều rộng của thanh máu
    //     int healthBarWidth = (int) (400 * ((double) currentHealth / maxHealth)); 
        
    //     // Vẽ thanh máu hiện tại (màu đỏ)
    //     // Chuyển từ đỏ nhạt đến đỏ đậm tùy theo máu còn lại
    //     int red = 255;
    //     int green = (int) (255 * ((double) currentHealth / maxHealth)); 
    //     g2.setColor(new Color(red, green, 0)); // Màu chuyển dần từ đỏ sang vàng nhạt
    //     g2.fillRoundRect(0, 0, healthBarWidth, 30, 15, 15); // Vẽ thanh máu với bo góc
        
    //     // Đổ bóng cho thanh máu (cho hiệu ứng nổi bật)
    //     g2.setColor(new Color(0, 0, 0, 50)); // Màu đổ bóng nhẹ
    //     g2.fillRoundRect(3,3, 400, 30, 15, 15); // Đổ bóng nhẹ cho thanh máu
    // }


    public void drawHealthBar(Graphics2D g2) {
        // Vẽ viền ngoài
        g2.setColor(Color.WHITE); // Sử dụng màu trắng cho viền
        g2.fillRoundRect(0, 0, barWidth + 4, barHeight + 4, 10, 10); // Viền tròn
        
        // Vẽ nền thanh máu
        g2.setColor(new Color(50, 50, 50)); // Màu nền xám
        g2.fillRoundRect(0, 0, barWidth, barHeight, 8, 8); // Nền nhỏ hơn viền một chút
    
        // Vẽ thanh máu
        int healthBarWidth = (int) (barWidth * ((double) currentHealth / maxHealth));
        GradientPaint gradient = new GradientPaint(2, 2, Color.RED, 2 + healthBarWidth, 2 + barHeight, Color.YELLOW, false);
        g2.setPaint(gradient);
        g2.fillRoundRect(2, 2, healthBarWidth, barHeight, 8, 8); // Vẽ gradient từ đỏ sang vàng
    }


    
    public void getPlayerImage(){
        try {
            //standing
            stand1 = ImageIO.read(getClass().getResourceAsStream("/res/standard1.PNG"));
            stand2 = ImageIO.read(getClass().getResourceAsStream("/res/standard2.PNG"));
            stand3 = ImageIO.read(getClass().getResourceAsStream("/res/standard3.PNG"));
            stand4 = ImageIO.read(getClass().getResourceAsStream("/res/standard4.PNG"));
            stand5 = ImageIO.read(getClass().getResourceAsStream("/res/standard5.PNG"));
            

            // // move
            // //up + down
            // up_down_1 = ImageIO.read(getClass().getResourceAsStream("/res/standard1.PNG"));
            // up_down_2 = ImageIO.read(getClass().getResourceAsStream("/res/right1.PNG"));

            //right
            right1 = ImageIO.read(getClass().getResourceAsStream("/res/right1.PNG"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/res/right2.PNG"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/res/right3.PNG"));

            

            //jump_right
            jump1 = ImageIO.read(getClass().getResourceAsStream("/res/jump_right/jump1.PNG"));
            jump2 = ImageIO.read(getClass().getResourceAsStream("/res/jump_right/jump2.PNG"));
            jump3 = ImageIO.read(getClass().getResourceAsStream("/res/jump_right/jump3.PNG"));

            //jump_left 
            jump1_1 = ImageIO.read(getClass().getResourceAsStream("/res/jump_left/jump1.PNG"));
            jump2_1 = ImageIO.read(getClass().getResourceAsStream("/res/jump_left/jump2.PNG"));
            jump3_1 = ImageIO.read(getClass().getResourceAsStream("/res/jump_left/jump3.PNG"));



            
            
            //skill j
            j1 = ImageIO.read(getClass().getResourceAsStream("/res/quaytay1.PNG"));
            j2 = ImageIO.read(getClass().getResourceAsStream("/res/quaytay2.PNG"));
            j3 = ImageIO.read(getClass().getResourceAsStream("/res/quaytay3.PNG"));
            j4 = ImageIO.read(getClass().getResourceAsStream("/res/quaytay4.PNG"));
            j5 = ImageIO.read(getClass().getResourceAsStream("/res/quaytay5.PNG"));
            j6 = ImageIO.read(getClass().getResourceAsStream("/res/quaytay6.PNG"));
            
            //skill k
            k2 = ImageIO.read(getClass().getResourceAsStream("/res/skill2.PNG"));
            k3 = ImageIO.read(getClass().getResourceAsStream("/res/skill3.PNG"));
            k4 = ImageIO.read(getClass().getResourceAsStream("/res/skill4.PNG"));
            k5 = ImageIO.read(getClass().getResourceAsStream("/res/skill5.PNG"));
            k6 = ImageIO.read(getClass().getResourceAsStream("/res/skill6.PNG"));
            k7 = ImageIO.read(getClass().getResourceAsStream("/res/skill8.PNG"));
            k8 = ImageIO.read(getClass().getResourceAsStream("/res/skill7.PNG"));
            k9 = ImageIO.read(getClass().getResourceAsStream("/res/skill9.PNG"));
            
            //left
            left1 = ImageIO.read(getClass().getResourceAsStream("/res/left1.PNG"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/res/left2.PNG"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/res/left3.PNG"));


            //j_left
            j1_l = ImageIO.read(getClass().getResourceAsStream("/res/left/quaytay1_left.PNG"));
            j2_l = ImageIO.read(getClass().getResourceAsStream("/res/left/quaytay2_left.PNG"));
            j3_l = ImageIO.read(getClass().getResourceAsStream("/res/left/quaytay3_left.PNG"));
            j4_l = ImageIO.read(getClass().getResourceAsStream("/res/left/quaytay4_left.PNG"));
            j5_l = ImageIO.read(getClass().getResourceAsStream("/res/left/quaytay5_left.PNG"));
            j6_l = ImageIO.read(getClass().getResourceAsStream("/res/left/quaytay6_left.PNG"));

            //k_left
            k2_l = ImageIO.read(getClass().getResourceAsStream("/res/left/skill2_left.PNG"));
            k3_l = ImageIO.read(getClass().getResourceAsStream("/res/left/skill3_left.PNG"));
            k4_l = ImageIO.read(getClass().getResourceAsStream("/res/left/skill4_left.PNG"));
            k5_l = ImageIO.read(getClass().getResourceAsStream("/res/left/skill5_left.PNG"));
            k6_l = ImageIO.read(getClass().getResourceAsStream("/res/left/skill6_left.PNG"));
            k7_l = ImageIO.read(getClass().getResourceAsStream("/res/left/skill7_left.PNG"));
            k8_l = ImageIO.read(getClass().getResourceAsStream("/res/left/skill8_left.PNG"));
            k9_l = ImageIO.read(getClass().getResourceAsStream("/res/left/skill9_left.PNG"));

            //standing_left
            stand1_l = ImageIO.read(getClass().getResourceAsStream("/res/left/standard1_left.PNG"));
            stand2_l = ImageIO.read(getClass().getResourceAsStream("/res/left/standard2_left.PNG"));
            stand3_l = ImageIO.read(getClass().getResourceAsStream("/res/left/standard3_left.PNG"));
            stand4_l = ImageIO.read(getClass().getResourceAsStream("/res/left/standard4_left.PNG"));
            stand5_l = ImageIO.read(getClass().getResourceAsStream("/res/left/standard5_left.PNG"));




        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // public void getPlayerImage_left(){
    //     try {
    //         //standing
    //         stand1 = ImageIO.read(getClass().getResourceAsStream("/res/standard1.PNG"));
    //         stand2 = ImageIO.read(getClass().getResourceAsStream("/res/standard2.PNG"));
    //         stand3 = ImageIO.read(getClass().getResourceAsStream("/res/standard3.PNG"));
    //         stand4 = ImageIO.read(getClass().getResourceAsStream("/res/standard4.PNG"));
    //         stand5 = ImageIO.read(getClass().getResourceAsStream("/res/standard5.PNG"));
            

    //         // move
    //         //up + down
    //         up_down_1 = ImageIO.read(getClass().getResourceAsStream("/res/standard1.PNG"));
    //         up_down_2 = ImageIO.read(getClass().getResourceAsStream("/res/right1.PNG"));

    //         //right
    //         right1 = ImageIO.read(getClass().getResourceAsStream("/res/right1.PNG"));
    //         right2 = ImageIO.read(getClass().getResourceAsStream("/res/right2.PNG"));
    //         right3 = ImageIO.read(getClass().getResourceAsStream("/res/right3.PNG"));

    //         //left
    //         left1 = ImageIO.read(getClass().getResourceAsStream("/res/left1.PNG"));
    //         left2 = ImageIO.read(getClass().getResourceAsStream("/res/left2.PNG"));
    //         left3 = ImageIO.read(getClass().getResourceAsStream("/res/left3.PNG"));


            
            
    //         //skill j
    //         j1 = ImageIO.read(getClass().getResourceAsStream("/res/quaytay1.PNG"));
    //         j2 = ImageIO.read(getClass().getResourceAsStream("/res/quaytay2.PNG"));
    //         j3 = ImageIO.read(getClass().getResourceAsStream("/res/quaytay3.PNG"));
    //         j4 = ImageIO.read(getClass().getResourceAsStream("/res/quaytay4.PNG"));
    //         j5 = ImageIO.read(getClass().getResourceAsStream("/res/quaytay5.PNG"));
    //         j6 = ImageIO.read(getClass().getResourceAsStream("/res/quaytay6.PNG"));
            
    //         //skill k
    //         k2 = ImageIO.read(getClass().getResourceAsStream("/res/skill2.PNG"));
    //         k3 = ImageIO.read(getClass().getResourceAsStream("/res/skill3.PNG"));
    //         k4 = ImageIO.read(getClass().getResourceAsStream("/res/skill4.PNG"));
    //         k5 = ImageIO.read(getClass().getResourceAsStream("/res/skill5.PNG"));
    //         k6 = ImageIO.read(getClass().getResourceAsStream("/res/skill6.PNG"));
    //         k7 = ImageIO.read(getClass().getResourceAsStream("/res/skill8.PNG"));
    //         k8 = ImageIO.read(getClass().getResourceAsStream("/res/skill7.PNG"));
    //         k9 = ImageIO.read(getClass().getResourceAsStream("/res/skill9.PNG"));
            
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }

    public void updateHealth(int newHealth) {
        currentHealth = newHealth;
        // Gọi hàm vẽ lại thanh máu tại đây hoặc từ vòng lặp game
    }







    public void update() {
        // Kiểm tra xem phím "J" có được nhấn không
        if (keyH.jPressed) {
            isUsingSkill = true; // Bắt đầu sử dụng skill
            skillCounter = 0; // Reset bộ đếm thời gian cho skill
        }
        if (keyH.kPressed) {
            isUsingSkillK = true; // Bắt đầu sử dụng skill qua phím K
            skillKCounter = 0; // Reset bộ đếm thời gian cho skill K
        }
        if (keyH.wPressed) {
            isUsingJump = true;
            skillCounter = 0;
        }

    
        // Chỉ cho phép di chuyển nếu không sử dụng skill
        if (!isUsingSkill) {
            // Giả định ban đầu là nhân vật không di chuyển
            isMoving = false;
    
            // Kiểm tra nếu có phím nào được nhấn để xác định trạng thái di chuyển
            // if (keyH.upPressed) {
            //     direction = "up";
            //     y -= speed;
            //     isMoving = true;
            // } 
            // if (keyH.downPressed) {
            //     direction = "down";
            //     y += speed;
            //     isMoving = true;
            // } 
            if (keyH.leftPressed) {
                direction = "left";
                x -= speed;
                isMoving = true;
            } 
            else if (keyH.rightPressed) {
                direction = "right";
                x += speed;
                isMoving = true;
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        drawHealthBar(g2);
        // Tăng animationCounter mỗi lần vẽ
        animationCounter++;
        if (animationCounter > animationSpeed) {
            // Reset bộ đếm
            animationCounter = 0;
    
            // Xử lý khi đang sử dụng skill qua phím J
            if (isUsingSkill) {
                animationFrame = (animationFrame + 1) % 6; // Chuyển đổi giữa 6 khung hình skill J
                skillCounter++;
    
                if (skillCounter >= 6) {
                    isUsingSkill = false; // Dừng sử dụng skill J
                }
            } 
            // Xử lý khi đang sử dụng skill qua phím K
            else if (isUsingSkillK) {
                animationFrame = (animationFrame + 1) % 8; // Chuyển đổi giữa 8 khung hình skill K
                skillKCounter++;
    
                if (skillKCounter >= 8) {
                    isUsingSkillK = false; // Dừng sử dụng skill K
                }
            }
            else if (isUsingJump) {
                animationFrame = (animationFrame + 1) % 3;
                skillCounter++;
                if (skillCounter >= 3) { 
                    isUsingJump = false;
                }
            }
            // Nếu không đang sử dụng skill, tiếp tục xử lý di chuyển hoặc đứng yên
            else if (isMoving) {
                switch (direction) {
                    // case "up":
                    // case "down":
                    //     animationFrame = (animationFrame + 1) % 2; // Chuyển đổi giữa 2 khung hình
                    //     break;
                    case "left":
                    case "right":
                        animationFrame = (animationFrame + 1) % 3; // Chuyển đổi giữa 3 khung hình
                        break;
                }
            } else {
                animationFrame = (animationFrame + 1) % 5; // Chuyển đổi giữa 5 khung hình đứng yên
            }
        }
    
        // Xử lý hiển thị hình ảnh
        if (isUsingSkill) {
            // Kiểm tra hướng và chọn hình ảnh skill J tương ứng
            if (direction.equals("left")) {
                switch (animationFrame) {
                    case 0: image = j1_l; break;
                    case 1: image = j2_l; break;
                    case 2: image = j3_l; break;
                    case 3: image = j4_l; break;
                    case 4: image = j5_l; break;
                    case 5: image = j6_l; break;
                }
            } else {
                switch (animationFrame) {
                    case 0: image = j1; break;
                    case 1: image = j2; break;
                    case 2: image = j3; break;
                    case 3: image = j4; break;
                    case 4: image = j5; break;
                    case 5: image = j6; break;
                }
            }
        } 
        // Xử lý skill K tương tự
        else if (isUsingSkillK) {
            if (direction.equals("left")) {
                switch (animationFrame) {
                    case 0: image = k2_l; break;
                    case 1: image = k3_l; break;
                    case 2: image = k4_l; break;
                    case 3: image = k5_l; break;
                    case 4: image = k6_l; break;
                    case 5: image = k7_l; x -= 2 * speed; break;
                    case 6: image = k8_l; break;
                    case 7: image = k9_l; break;
                }
            } else {
                switch (animationFrame) {
                    case 0: image = k2; break;
                    case 1: image = k3; break;
                    case 2: image = k4; break;
                    case 3: image = k5; break;
                    case 4: image = k6; break;
                    case 5: image = k7; x += 2 * speed; break;
                    case 6: image = k8; break;
                    case 7: image = k9; break;
                }
            }
        } 
        else if (isUsingJump) {
            int initialY = y;
            // Di chuyển sang trái (left)
            if (direction.equals("left")) {
                switch (animationFrame) {
                    case 0: 
                        image = jump1_1;
                        // y += 10; // Tăng dần y lên một đoạn
                        x -= 1 * speed;       // Lùi lại 2*speed
                        break;
                    case 1: 
                        image = jump2_1;
                        // y = initialY - speed; // Y vẫn giữ ở mức cao
                        x -= 1 * speed;       // Lùi lại 2*speed
                        break;
                    case 2: 
                        image = jump3_1;
                        // y -= 10;         // Giảm về y ban đầu
                        x -= 1 * speed;       // Lùi lại 2*speed
                        break;
                }
            } 
            // Di chuyển sang phải (right)
            else {
                switch (animationFrame) {
                    case 0: 
                        image = jump1;
                        // y += 10; // Tăng dần y lên một đoạn
                        x += 1 * speed;       // Tiến lên 2*speed
                        break;
                    case 1: 
                        image = jump2;
                        // y = initialY - speed; // Y vẫn giữ ở mức cao
                        x += 1 * speed;       // Tiến lên 2*speed
                        break;
                    case 2: 
                        image = jump3;
                        // y -= 10;         // Giảm về y ban đầu
                        x += 1 * speed;       // Tiến lên 2*speed
                        break;
                }
            }
        }
        
        
        // Xử lý di chuyển hoặc đứng yên
        else if (!isMoving) {
            if (direction.equals("left")) {
                switch (animationFrame) {
                    case 0: image = stand1_l; break;
                    case 1: image = stand2_l; break;
                    case 2: image = stand3_l; break;
                    case 3: image = stand4_l; break;
                    case 4: image = stand5_l; break;
                }
            } else {
                switch (animationFrame) {
                    case 0: image = stand1; break;
                    case 1: image = stand2; break;
                    case 2: image = stand3; break;
                    case 3: image = stand4; break;
                    case 4: image = stand5; break;
                }
            }
        } else {
            // Xử lý di chuyển sang trái
            if (direction.equals("left")) {
                switch (animationFrame) {
                    case 0: image = left1; break;
                    case 1: image = left2; break;
                    case 2: image = left3; break;
                }
            }
            // Xử lý di chuyển sang phải
            else if (direction.equals("right")) {
                switch (animationFrame) {
                    case 0: image = right1; break;
                    case 1: image = right2; break;
                    case 2: image = right3; break;
                }
            }
        }
    
        // Vẽ hình ảnh tại vị trí x, y với kích thước phù hợp
        g2.drawImage(image, x, y, 2 * gp.titleSize, 2 * gp.titleSize, null);
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
