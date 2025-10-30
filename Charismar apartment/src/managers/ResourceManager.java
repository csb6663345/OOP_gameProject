package managers;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ResourceManager {
     private HashMap<String, BufferedImage> images;
    
    public ResourceManager() {
        images = new HashMap<>();
        loadAllImages();
    }
    
    private void loadAllImages() {
        // Player Animation - 6 frames
        loadImage("player_left_1", "resources/images/character/Security_guard/left_stand.png");
        loadImage("player_left_2", "resources/images/character/Security_guard/left_walk1.png");
        loadImage("player_left_3", "resources/images/character/Security_guard/left_walk2.png");
        
        loadImage("player_right_1", "resources/images/character/Security_guard/right_stand.png");
        loadImage("player_right_2", "resources/images/character/Security_guard/right_walk1.png");
        loadImage("player_right_3", "resources/images/character/Security_guard/right_walk2.png");
        
        loadImage("player_idle", "resources/images/character/Security_guard/right_stand.png");
        loadImage("player_dead", "resources/images/character/Security_guard/guard_die.png"); 
        
        // Background
        loadImage("floor_background", "resources/images/background/background_main.png");

        
        // Objects
        loadImage("extinguisher", "resources/images/character/object/extinguisher.png");
        loadImage("vase", "resources/images/character/object/vase.png");
        loadImage("vase_anomalie1", "resources/images/character/object/vase_anomalie1.png");
        loadImage("vase_anomalie2", "resources/images/character/object/vase_anomalie2.png");
        loadImage("washing_machine", "resources/images/character/object/washing_machine.png");
        loadImage("washing_machine_anomalie1", "resources/images/character/object/washing_machine_anomalie1.png");
        loadImage("washing_machine_anomalie2", "resources/images/character/object/washing_machine_anomalie2.png");
        loadImage("teen1", "resources/images/character/object/teen1.png");
        loadImage("teen1_anomalie1", "resources/images/character/object/teen1_anomalie1.png");
        loadImage("teen2", "resources/images/character/object/teen2.png");
        loadImage("teen2_anomalie1", "resources/images/character/object/teen2_anomalie1.png");
        
         // Boss 1 - Ghost
        loadImage("ghost_boss_1", "resources/images/character/boss/boss1_walk1.png"); 
        loadImage("ghost_boss_2", "resources/images/character/boss/boss1_walk2.png"); 
        
        // Boss 2 - Corpse
        loadImage("corpse_boss_1", "resources/images/character/object/washing_machine_anomalie1.png"); 
        loadImage("corpse_boss_2", "resources/images/character/boss/boss2_walk1.png"); 
        loadImage("corpse_boss_3", "resources/images/character/boss/boss2_walk2.png"); 
        loadImage("corpse_boss_4", "resources/images/character/boss/boss2_walk3.png"); 
    }
    
    private void loadImage(String key, String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                BufferedImage img = ImageIO.read(file);
                images.put(key, img);
                System.out.println("✓ Loaded: " + path);
            } else {
                System.out.println("✗ File not found: " + path);
                images.put(key, createPlaceholder(100, 100, Color.GRAY));
            }
        } catch (IOException e) {
            System.out.println("✗ Error loading: " + path);
            images.put(key, createPlaceholder(100, 100, Color.GRAY));
        }
    }
    
    private BufferedImage createPlaceholder(int width, int height, Color color) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, width - 1, height - 1);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("NO IMG", 20, height / 2);
        g.dispose();
        return img;
    }
    
    public BufferedImage getImage(String key) {
        return images.getOrDefault(key, createPlaceholder(50, 50, Color.MAGENTA));
    }
}

