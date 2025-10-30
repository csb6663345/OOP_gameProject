package entities;

import managers.ResourceManager;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Boss {
    protected float x, y;
    protected float width;
    protected float height;
    protected float speed;
    protected ResourceManager resourceManager;
    protected boolean active = false;
    protected boolean caught = false;
    
    // Animation
    protected Animation walkAnim;
    protected BufferedImage idleImage;
    
    public Boss(float x, float y, ResourceManager resourceManager, float width, float height) {
        this.x = x;
        this.y = y;
        this.resourceManager = resourceManager;
        this.width = width;
        this.height = height;
    }
    
    //Abstract Method - บังคับให้ subclass implement  แต่ละ Boss มีวิธีการ activate ต่างกัน

    public abstract void activate();
    
    //Hook Method - subclass สามารถ override ได้

    public void deactivate() {
        active = false;
        caught = false;
    }
    
    //Template Method - กำหนดโครงสร้างการ update
    public void update(double deltaTime) {
        if (!active) return;
        
        // Hook: ให้ subclass ทำ behavior พิเศษก่อน
        beforeUpdate(deltaTime);
        
        // Default behavior: เดินไปซ้าย
        x -= speed * deltaTime;
        
        // Update animation
        if (walkAnim != null) {
            walkAnim.update(deltaTime);
        }
        
        // Hook: ให้ subclass ทำ behavior พิเศษหลัง
        afterUpdate(deltaTime);
    }
    
    // Hook Methods - subclass override ได้ถ้าต้องการ

    protected void beforeUpdate(double deltaTime) {
        // Override ได้
    }
    
    protected void afterUpdate(double deltaTime) {
        // Override ได้
    }
    
    /**
     * Render Boss - สามารถ override ได้
     */
    public void render(Graphics2D g, int cameraX) {
        if (!active) return;
        
        int drawX = (int) (x - cameraX);
        int drawY = (int) y;
        
        BufferedImage img = walkAnim != null ? walkAnim.getCurrentFrame() : idleImage;
        g.drawImage(img, drawX, drawY, (int) width, (int) height, null);
    }
    
    /**
     *  Final Method - ห้าม override (พฤติกรรมเดียวกันทุก Boss)
     */
    public final boolean checkCatchPlayer(float playerX) {
        if (!active) return false;
        
        if (Math.abs(x - playerX) < 50) {
            caught = true;
            return true;
        }
        return false;
    }
    
    // Getters (final)
    public final boolean isActive() { return active; }
    public final boolean hasCaughtPlayer() { return caught; }
    public final float getX() { return x; }
    public final float getY() { return y; }
}