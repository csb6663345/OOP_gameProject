package anomaly.types;

import objects.GameObject;
import managers.ResourceManager;
import java.util.List;
import java.util.Random;
import anomaly.*;

public class ObjectReplacedAnomaly implements Anomaly {
    private List<GameObject> objects;
    private ResourceManager resourceManager;
    private GameObject targetObject;
    private String originalImageKey;
    private boolean active = false;
    private Random random;
    
    public ObjectReplacedAnomaly(List<GameObject> objects, ResourceManager resourceManager, Random random) {
        this.objects = objects;
        this.resourceManager = resourceManager;
        this.random = random;
    }
    
    @Override
    public void apply() {
        if (objects.isEmpty()) return;
        
        targetObject = objects.get(random.nextInt(objects.size()));
        originalImageKey = targetObject.getImageKey();
        
        // สุ่ม object ใหม่
        String[] objectTypes = {"extinguisher", "vase", "washing_machine", "teen1", "teen2"};
        String newType;
        
        do {
            newType = objectTypes[random.nextInt(objectTypes.length)];
        } while (newType.equals(originalImageKey)); // ห้ามเหมือนเดิม
        
        targetObject.setImageKey(newType);
        active = true;
    }
    
    @Override
    public void reset() {
        if (targetObject != null) {
            targetObject.setImageKey(originalImageKey);
        }
        active = false;
    }
    
    @Override
    public AnomalyType getType() {
        return AnomalyType.OBJECT_REPLACED;
    }
    
    @Override
    public String getDescription() {
        return "Object replaced: " + originalImageKey + " -> " + (targetObject != null ? targetObject.getImageKey() : "unknown");
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
}