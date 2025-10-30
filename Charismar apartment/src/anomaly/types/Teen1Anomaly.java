package anomaly.types;

import anomaly.Anomaly;
import anomaly.AnomalyType;
import objects.GameObject;
import java.util.List;
import java.util.Random;

public class Teen1Anomaly implements Anomaly {
    private List<GameObject> objects;
    private GameObject targetTeen;
    private String originalImageKey;
    private boolean active = false;
    private Random random;
    
    public Teen1Anomaly(List<GameObject> objects, Random random) {
        this.objects = objects;
        this.random = random;
    }
    
    @Override
    public void apply() {
        // หา teen1 ทั้งหมดในฉาก
        List<GameObject> teen1List = new java.util.ArrayList<>();
        for (GameObject obj : objects) {
            if (obj.getImageKey().equals("teen1")) {
                teen1List.add(obj);
            }
        }
        
        if (teen1List.isEmpty()) return;
        
        // สุ่มเลือก teen1 คนใดคนหนึ่ง
        targetTeen = teen1List.get(random.nextInt(teen1List.size()));
        originalImageKey = targetTeen.getImageKey();
        
        // เปลี่ยนเป็น teen1_anomaly
        targetTeen.setImageKey("teen1_anomalie1");
        active = true;
        
        System.out.println("✓ Teen1 Anomaly: teen1 -> teen1_anomaly");
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
        return "Teen1 changed to anomaly state";
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
}