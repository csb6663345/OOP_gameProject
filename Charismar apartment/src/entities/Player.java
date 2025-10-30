package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import managers.ResourceManager;

public class Player {
    private float x, y;
    private float width = 450;
    private float height = 450;
    private float speed = 1500;
    private boolean dead = false;
    private BufferedImage deadImage;
    private ResourceManager resourceManager;

    
    // Animation
    private Animation walkLeftAnim;
    private Animation walkRightAnim;
    private BufferedImage idleImage;
    private boolean isMoving = false;
    private boolean facingLeft = false;
    
    public Player(float x, float y, ResourceManager resourceManager) {
        this.x = x;
        this.y = y;
        this.resourceManager = resourceManager;
        
        // สร้าง animation
        BufferedImage[] leftFrames = {
            resourceManager.getImage("player_left_1"),
            resourceManager.getImage("player_left_2"),
            resourceManager.getImage("player_left_3")
        };
        walkLeftAnim = new Animation(leftFrames, 8); // 8 FPS
        
        BufferedImage[] rightFrames = {
            resourceManager.getImage("player_right_1"),
            resourceManager.getImage("player_right_2"),
            resourceManager.getImage("player_right_3")
        };
        walkRightAnim = new Animation(rightFrames, 8);
        
        idleImage = resourceManager.getImage("player_idle");
        deadImage = resourceManager.getImage("player_dead");

    }
    
    public void update(double deltaTime, boolean moveLeft, boolean moveRight) {
        isMoving = false;
        
        if (moveLeft) {
            x -= speed * deltaTime;
            facingLeft = true;
            isMoving = true;
            walkLeftAnim.update(deltaTime);
        }
        if (moveRight) {
            x += speed * deltaTime;
            facingLeft = false;
            isMoving = true;
            walkRightAnim.update(deltaTime);
        }
        
        // Reset animation เมื่อหยุด
        if (!isMoving) {
            walkLeftAnim.reset();
            walkRightAnim.reset();
        }
        
        if (x < 200) x = 200;

        // 8000 คือความกว้างของแผนที่ใน Floor.java
        if (x > 8000 - width) {
            x = 8000 - width;
        }


    }
    
    public void render(Graphics2D g, int cameraX) {
        int drawX = (int) (x - cameraX - width / 2);
        int drawY = (int) y;
        
        BufferedImage img;
        
        if (dead) {
            img = deadImage; //รูปตาย
        } else if (isMoving) {
            img = facingLeft ? walkLeftAnim.getCurrentFrame() : walkRightAnim.getCurrentFrame();
        } else {
            img = idleImage;
        }
        
        g.drawImage(img, drawX, drawY, (int) width, (int) height, null);
    }
    
    public void reset(float x, float y) {
        this.x = x;
        this.y = y;
        isMoving = false;
        walkLeftAnim.reset();
        walkRightAnim.reset();
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
