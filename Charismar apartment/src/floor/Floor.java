package floor;

import managers.ResourceManager;
import objects.GameObject;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import anomaly.AnomalyManager;
import entities.GhostBoss;
import entities.CorpseBoss;

import java.awt.Graphics2D;

import core.GamePanel;

public class Floor {
    private int floorNumber;
    private int width = 8250;
    private boolean hasAnomaly;
    private ResourceManager resourceManager;
    
    private Door leftDoor;
    private Door rightDoor;
    private java.util.List<GameObject> objects;
    private AnomalyManager anomalyManager;
    
    private GhostBoss ghostBoss;
    private CorpseBoss corpseBoss;
    private boolean ghostBossSpawned = false;
    private boolean corpseBossActive = false;

    public Floor(int floorNumber, ResourceManager resourceManager) {
        this.floorNumber = floorNumber;
        this.resourceManager = resourceManager;
        this.hasAnomaly = Math.random() < 0.6;
        
        leftDoor = new Door(80, 400, true);
        rightDoor = new Door(width - 80, 400, false);
        
        objects = new java.util.ArrayList<>();
        initializeObjects();

        // สร้าง Anomaly
        anomalyManager = new AnomalyManager(resourceManager);
        hasAnomaly = anomalyManager.generateAnomaly(objects, floorNumber);

        // สร้าง Boss
        initializeBosses();
    }


    private void initializeBosses() {
        // Boss 1 - ปรากฎหลังลิฟต์ (กลางแผนที่)
        ghostBoss = new GhostBoss(width / 2 + 500, 150, resourceManager, 600, 600); // ขนาดกำหนดเอง

        
        // Boss 2 - ผูกกับเครื่องซักผ้า
        // หาตำแหน่งเครื่องซักผ้า
        GameObject washingMachineObj = null;
        for (GameObject obj : objects) {
            if (obj.getImageKey().contains("washing_machine")) {
                washingMachineObj = obj;
                corpseBoss = new CorpseBoss(obj.getX()+100, obj.getY(), resourceManager,500,500);
                corpseBoss.setWashingMachine(washingMachineObj);
                break;
            }
        }
        
        // ถ้าไม่มีเครื่องซักผ้า สร้างที่ตำแหน่งเริ่มต้น
        if (corpseBoss == null) {
            corpseBoss = new CorpseBoss(width - 1000, 350, resourceManager,300,300);
        }
        
        // สุ่มว่า Ghost Boss จะโผล่หรือไม่ (20% โอกาส)
        if (Math.random() < 0.2) {
            ghostBossSpawned = true;
        }
        
        // ตรวจสอบว่ามี washing_machine_anomaly1 หรือไม่
        for (GameObject obj : objects) {
            if (obj.getImageKey().equals("washing_machine_anomalie1")) {
                corpseBossActive = true;
                break;
            }
        }
        
    }

    // อัพเดท Boss
    public void updateBosses(double deltaTime, float playerX) {
        // Ghost Boss - โผล่หลังลิฟต์
        if (ghostBossSpawned && !ghostBoss.isActive()) {
            // โผล่หลังจากผู้เล่นเดินผ่านจุดกลาง
            if (playerX > width / 2) {
                ghostBoss.activate();
            }
        }
        
        if (ghostBoss.isActive()) {
            ghostBoss.update(deltaTime);
        }
        
        // Corpse Boss - ออกจากเครื่องซักผ้า
        if (corpseBossActive) {
            corpseBoss.checkPlayerDistance(playerX);
        }
        
        if (corpseBoss.isActive()) {
            corpseBoss.update(deltaTime);
        }
    }
    
    // ตรวจสอบว่า Boss จับผู้เล่นได้หรือไม่
    
    public boolean checkBossCaught(float playerX) {
        if (ghostBoss.checkCatchPlayer(playerX)) {
            return true;
        }
        if (corpseBoss.checkCatchPlayer(playerX)) {
            return true;
        }
        return false;
    }
    


    
    private void initializeObjects() {
        objects.add(new GameObject(750, 120, "extinguisher", resourceManager,300,300));
        objects.add(new GameObject(70, 500, "vase", resourceManager,250,250));
        objects.add(new GameObject(7400, 350, "washing_machine", resourceManager,500,500));
        objects.add(new GameObject(3900, 350, "teen1", resourceManager,350,350));
        objects.add(new GameObject(4000, 350, "teen2", resourceManager,350,350));
    }
    
   // วาดพื้นหลัง (Layer 1 - ด้านหลังสุด)

    public void renderBackground(Graphics2D g, int cameraX) {
        BufferedImage bg = resourceManager.getImage("floor_background");
        

        for (int i = 0; i < width; i += bg.getWidth()) {
            g.drawImage(bg, 
                i - cameraX,                    // X position
                0,                              // Y position (เริ่มจากด้านบน)
                bg.getWidth(),                    // Width เดิม
                GamePanel.SCREEN_HEIGHT,        // Height เต็มจอ 
                null);
        }
        
        // หมายเลขชั้น
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("F" + floorNumber, width / 11 - cameraX, 300);
        g.drawString("F" + floorNumber, width - 1235 - cameraX, 300);
    }
    
    // วาด Objects และประตูด้านหลังผู้เล่น (Layer 2)
    public void renderBackObjects(Graphics2D g, int cameraX) {
        // Objects ทั้งหมด
        for (GameObject obj : objects) {
            obj.render(g, cameraX);
        }

        // วาด Boss
        ghostBoss.render(g, cameraX);
        corpseBoss.render(g, cameraX);
    }
    
    // วาด Objects ด้านหน้าผู้เล่น (Layer 4 - ถ้ามี)

    public void renderFrontObjects(Graphics2D g, int cameraX) {
        // ถ้ามี object ที่ต้องวาดทับผู้เล่น ใส่ที่นี่
        // ตอนนี้ยังไม่มี
    }
    
    // วาดทั้งหมดในครั้งเดียว (สำหรับใช้แบบเดิม)

    public void render(Graphics2D g, int cameraX) {
        renderBackground(g, cameraX);
        renderBackObjects(g, cameraX);
        renderFrontObjects(g, cameraX);
    }
    
    public boolean hasAnomaly() { return hasAnomaly; }
    public int getWidth() { return width; }
    public Door getLeftDoor() { return leftDoor; }
    public Door getRightDoor() { return rightDoor; }
    

    // Getter สำหรับ Boss
    public boolean hasActiveBoss() {
        return ghostBoss.isActive() || corpseBoss.isActive();
    }
    
    public void deactivateBosses() {
        ghostBoss.deactivate();
        corpseBoss.deactivate();
    }
}
