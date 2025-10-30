package anomaly.types;

import anomaly.Anomaly;
import anomaly.AnomalyType;
import objects.GameObject;
import java.util.List;
import java.util.Random;

public class Teen2Anomaly implements Anomaly {
    private List<GameObject> objects;
    private GameObject targetTeen;
    private String originalImageKey;
    private boolean active = false;
    private Random random;
    
    public Teen2Anomaly(List<GameObject> objects, Random random) {
        this.objects = objects;
        this.random = random;
    }
    
    @Override
    public void apply() {
        // หา teen2 ทั้งหมดในฉาก
        List<GameObject> teen2List = new java.util.ArrayList<>();
        for (GameObject obj : objects) {
            if (obj.getImageKey().equals("teen2")) {
                teen2List.add(obj);
            }
        }
        
        if (teen2List.isEmpty()) return;
        
        // สุ่มเลือก teen2 คนใดคนหนึ่ง
        targetTeen = teen2List.get(random.nextInt(teen2List.size()));
        originalImageKey = targetTeen.getImageKey();
        
        // เปลี่ยนเป็น teen2_anomaly
        targetTeen.setImageKey("teen2_anomalie1");
        active = true;
        
        System.out.println("✓ Teen2 Anomaly: teen2 -> teen2_anomaly");
    }
    
    @Override
    public void reset() {
        if (targetTeen != null) {
            targetTeen.setImageKey(originalImageKey);
        }
        active = false;
    }
    
    @Override
    public AnomalyType getType() {
        return AnomalyType.OBJECT_REPLACED;
    }
    
    @Override
    public String getDescription() {
        return "Teen2 changed to anomaly state";
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
}