package anomaly.types;

import objects.GameObject;
import managers.ResourceManager;
import java.util.List;
import java.util.Random;
import anomaly.*;

public class ObjectExtraAnomaly implements Anomaly {
    private List<GameObject> objects;
    private ResourceManager resourceManager;
    private GameObject extraObject;
    private boolean active = false;
    private Random random;
    
    public ObjectExtraAnomaly(List<GameObject> objects, ResourceManager resourceManager, Random random) {
        this.objects = objects;
        this.resourceManager = resourceManager;
        this.random = random;
    }
    
    @Override
    public void apply() {
        if (objects.isEmpty()) return;
        
        // สุ่มประเภท object
        String[] objectTypes = {"extinguisher", "vase", "washing_machine", "teen1", "teen2"};
        String type = objectTypes[random.nextInt(objectTypes.length)];
        
        // สุ่มตำแหน่ง
        int x = 300 + random.nextInt(1400);
        int y = 200 + random.nextInt(200);
        
        // สร้าง object ใหม่
        extraObject = new GameObject(x, y, type, resourceManager);
        objects.add(extraObject);
        active = true;
    }
    
    @Override
    public void reset() {
        if (extraObject != null) {
            objects.remove(extraObject);
        }
        active = false;
    }
    
    @Override
    public AnomalyType getType() {
        return AnomalyType.OBJECT_EXTRA;
    }
    
    @Override
    public String getDescription() {
        return "Extra object appeared: " + (extraObject != null ? extraObject.getImageKey() : "unknown");
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
}