package anomaly.types;

import anomaly.Anomaly;
import anomaly.AnomalyType;
import objects.GameObject;
import java.util.List;
import java.util.Random;

public class VaseColorAnomaly implements Anomaly {
    private List<GameObject> objects;
    private GameObject targetVase;
    private String originalImageKey;
    private boolean active = false;
    private Random random;
    
    public VaseColorAnomaly(List<GameObject> objects, Random random) {
        this.objects = objects;
        this.random = random;
    }
    
    @Override
    public void apply() {
        //  หาแจกันทั้งหมดในฉาก
        List<GameObject> vases = new java.util.ArrayList<>();
        for (GameObject obj : objects) {
            if (obj.getImageKey().equals("vase") || 
                obj.getImageKey().equals("vase_anomalie1") || 
                obj.getImageKey().equals("vase_anomalie2")) {
                vases.add(obj);
            }
        }
        
        if (vases.isEmpty()) return;
        
        // สุ่มเลือกแจกัน 1 ใบ
        targetVase = vases.get(random.nextInt(vases.size()));
        originalImageKey = targetVase.getImageKey();
        
        // สุ่มเปลี่ยนสี (ไม่ซ้ำสีเดิม)
        String[] vaseColors = {"vase_anomalie1", "vase_anomalie2", "vase"};  // vase = ฟ้า (เดิม)
        String newColor;
        
        do {
            newColor = vaseColors[random.nextInt(vaseColors.length)];
        } while (newColor.equals(originalImageKey));
        
        targetVase.setImageKey(newColor);
        active = true;
        
        System.out.println("✓ Vase color changed: " + originalImageKey + " -> " + newColor);
    }
    
    @Override
    public void reset() {
        if (targetVase != null) {
            targetVase.setImageKey(originalImageKey);
        }
        active = false;
    }
    
    @Override
    public AnomalyType getType() {
        return AnomalyType.OBJECT_REPLACED;  // หรือสร้าง VASE_COLOR_CHANGE ใหม่
    }
    
    @Override
    public String getDescription() {
        return "Vase color changed: " + originalImageKey + " -> " + 
               (targetVase != null ? targetVase.getImageKey() : "unknown");
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
}