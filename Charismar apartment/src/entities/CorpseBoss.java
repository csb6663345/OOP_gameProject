package entities;

import managers.ResourceManager;
import objects.GameObject;
import java.awt.image.BufferedImage;

public class CorpseBoss extends Boss {
    private float originalX;
    private boolean triggered = false;

    private GameObject washingMachine;

    public CorpseBoss(float x, float y, ResourceManager resourceManager, float width, float height) {
        super(x, y, resourceManager,width,height);
        this.originalX = x;
        this.speed = 505;
        
        // Animation เดิน (3 frames)
        BufferedImage[] frames = {
            resourceManager.getImage("corpse_boss_2"),
            resourceManager.getImage("corpse_boss_3"),
            resourceManager.getImage("corpse_boss_4")
        };
        walkAnim = new Animation(frames, 8); // 8 FPS
        idleImage = resourceManager.getImage("corpse_boss_1");
    }
    
    public void setWashingMachine(GameObject washingMachine) {
        this.washingMachine = washingMachine;
    }

    
    @Override
    public void activate() {
        active = true;
        triggered = true;
        
        // เปลี่ยนเครื่องซักผ้ากลับเป็นปกติ
        if (washingMachine != null && 
            washingMachine.getImageKey().equals("washing_machine_anomalie1")) {
            washingMachine.setImageKey("washing_machine");
            System.out.println("✓ Washing machine returned to normal");
        }
        
        System.out.println(" CORPSE BOSS TRIGGERED!");
    }
    public void checkPlayerDistance(float playerX) {
        if (active || triggered) return;
        
        if (Math.abs(playerX - originalX) < 500) {
            activate();
        }
    }
    
    @Override
    public void deactivate() {
        super.deactivate();
        triggered = false;
    }
}