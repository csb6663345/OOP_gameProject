package anomaly.types;

import objects.GameObject;
import java.util.List;
import java.util.Random;
import anomaly.*;

public class PositionChangeAnomaly implements Anomaly {
    private List<GameObject> objects;
    private GameObject targetObject;
    private int originalX, originalY;
    private boolean active = false;
    private Random random;
    
    public PositionChangeAnomaly(List<GameObject> objects, Random random) {
        this.objects = objects;
        this.random = random;
    }
    
    @Override
    public void apply() {
        if (objects.isEmpty()) return;
        
        targetObject = objects.get(random.nextInt(objects.size()));
        originalX = targetObject.getX();
        originalY = targetObject.getY();
        
        // เลื่อนตำแหน่ง
        int offsetX = -100 + random.nextInt(200);
        int offsetY = -50 + random.nextInt(100);
        
        targetObject.setX(originalX + offsetX);
        targetObject.setY(originalY + offsetY);
        active = true;
    }
    
    @Override
    public void reset() {
        if (targetObject != null) {
            targetObject.setX(originalX);
            targetObject.setY(originalY);
        }
        active = false;
    }
    
    @Override
    public AnomalyType getType() {
        return AnomalyType.POSITION_CHANGE;
    }
    
    @Override
    public String getDescription() {
        return "Object position changed: " + (targetObject != null ? targetObject.getImageKey() : "unknown");
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
}