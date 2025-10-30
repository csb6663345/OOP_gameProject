package objects;

import managers.ResourceManager;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;

public class GameObject {
    private int x, y;
    private String imageKey;
    private ResourceManager resourceManager;
    private boolean visible = true;
    private Color tintColor = null;
    private float scale = 1.0f;
    
    // เพิ่มตัวแปรขนาดพื้นฐาน (base size)
    private int baseWidth;
    private int baseHeight;
    
    // Constructor แบบเดิม (ใช้ขนาดเริ่มต้น 250x250)
    public GameObject(int x, int y, String imageKey, ResourceManager resourceManager) {
        this(x, y, imageKey, resourceManager, 250, 250);
    }
    
    // Constructor ใหม่ - กำหนดขนาดได้
    public GameObject(int x, int y, String imageKey, ResourceManager resourceManager, int width, int height) {
        this.x = x;
        this.y = y;
        this.imageKey = imageKey;
        this.resourceManager = resourceManager;
        this.baseWidth = width;
        this.baseHeight = height;
    }
    
    public void render(Graphics2D g, int cameraX) {
        if (!visible) return;
        
        BufferedImage img = resourceManager.getImage(imageKey);
        
        // ใช้ baseWidth/baseHeight แทน 250
        int width = (int)(baseWidth * scale);
        int height = (int)(baseHeight * scale);
        
        // ถ้ามีสีย้อม
        if (tintColor != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.drawImage(img, x - cameraX, y, width, height, null);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
            g2.setColor(tintColor);
            g2.fillRect(x - cameraX, y, width, height);
            g2.dispose();
        } else {
            g.drawImage(img, x - cameraX, y, width, height, null);
        }
    }
    
    // Getters and Setters
    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    
    public String getImageKey() { return imageKey; }
    public void setImageKey(String imageKey) { this.imageKey = imageKey; }
    
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    
    public Color getTintColor() { return tintColor; }
    public void setTintColor(Color tintColor) { this.tintColor = tintColor; }
    
    public float getScale() { return scale; }
    public void setScale(float scale) { this.scale = scale; }
    
    // เพิ่ม getters/setters สำหรับขนาด
    public int getBaseWidth() { return baseWidth; }
    public int getBaseHeight() { return baseHeight; }
    public void setBaseWidth(int width) { this.baseWidth = width; }
    public void setBaseHeight(int height) { this.baseHeight = height; }
    
    // Method สำหรับเปลี่ยนขนาดพื้นฐาน
    public void setSize(int width, int height) {
        this.baseWidth = width;
        this.baseHeight = height;
    }
}