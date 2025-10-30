package entities;

import managers.ResourceManager;
import java.awt.image.BufferedImage;

public class GhostBoss extends Boss {
    private float spawnX; // ตำแหน่งเกิด
    
    public GhostBoss(float x, float y, ResourceManager resourceManager, float width, float height) {
        super(x, y, resourceManager, width, height);
        this.spawnX = x;
        this.speed = 510; 
        
        // Animation เดิน (2 frames)
        BufferedImage[] frames = {
            resourceManager.getImage("ghost_boss_1"),
            resourceManager.getImage("ghost_boss_2")
        };
        walkAnim = new Animation(frames, 6); // 6 FPS
        idleImage = resourceManager.getImage("ghost_boss_1");
    }
    
    @Override
    public void activate() {
        active = true;
        x = spawnX; // Reset ตำแหน่งเกิด
        System.out.println("GHOST BOSS APPEARED!");
    }
}