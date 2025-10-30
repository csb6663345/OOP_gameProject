package anomaly.types;

import objects.GameObject;
import java.util.List;
import java.util.Random;
import anomaly.*;

public class ObjectMissingAnomaly implements Anomaly {
    private List<GameObject> objects;
    private GameObject targetObject;
    private boolean wasVisible;
    private boolean active = false;
    private Random random;
    
    public ObjectMissingAnomaly(List<GameObject> objects, Random random) {
        this.objects = objects;
        this.random = random;
    }
    
    @Override
    public void apply() {
        if (objects.isEmpty()) return;
        
        // สุ่มเลือก object
        targetObject = objects.get(random.nextInt(objects.size()));
        wasVisible = targetObject.isVisible();
        
        // ซ่อน object
        targetObject.setVisible(false);
        active = true;
    }
    
    @Override
    public void reset() {
        if (targetObject != null) {
            targetObject.setVisible(wasVisible);
        }
        active = false;
    }
    
    @Override
    public AnomalyType getType() {
        return AnomalyType.OBJECT_MISSING;
    }
    
    @Override
    public String getDescription() {
        return "Object is missing: " + (targetObject != null ? targetObject.getImageKey() : "unknown");
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
}