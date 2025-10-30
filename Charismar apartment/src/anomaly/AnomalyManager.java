package anomaly;

import objects.GameObject;
import managers.ResourceManager;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import anomaly.types.*; 

public class AnomalyManager {
    private ResourceManager resourceManager;
    private Random random;
    private List<Anomaly> possibleAnomalies;
    private Anomaly currentAnomaly;
    private boolean hasAnomaly;
    
    public AnomalyManager(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.random = new Random();
        this.possibleAnomalies = new ArrayList<>();
    }

    public boolean generateAnomaly(List<GameObject> objects, int floorNumber) {
        // สุ่มว่ามี Anomaly หรือไม่ (60% มี)
        double anomalyChance = 0.6;
        
        // ชั้นล่างๆ มีโอกาสมาก anomaly มากขึ้น
        if (floorNumber <= 3) {
            anomalyChance = 0.7;
        }
        
        hasAnomaly = random.nextDouble() < anomalyChance;
        
        if (!hasAnomaly || objects.isEmpty()) {
            currentAnomaly = null;
            return false;
        }
        
        //สุ่มเลือก Anomaly
        currentAnomaly = createRandomAnomaly(objects, floorNumber);
        
        if (currentAnomaly != null) {
            currentAnomaly.apply();
            System.out.println("✓ Anomaly created: " + currentAnomaly.getDescription());
        }
        
        return hasAnomaly;
    }
    
    //สร้าง Anomaly แบบสุ่ม
   
    private Anomaly createRandomAnomaly(List<GameObject> objects, int floorNumber) {
        possibleAnomalies.clear();
        
        // เพิ่ม Anomaly 
        possibleAnomalies.add(new ObjectMissingAnomaly(objects, random));
        possibleAnomalies.add(new ObjectExtraAnomaly(objects, resourceManager, random));     
        possibleAnomalies.add(new PositionChangeAnomaly(objects, random));
        possibleAnomalies.add(new ObjectReplacedAnomaly(objects, resourceManager, random));
        
       // Anomaly แจกัน
        if (hasObjectType(objects, "vase")) {
            possibleAnomalies.add(new VaseColorAnomaly(objects, random));
            possibleAnomalies.add(new VaseColorAnomaly(objects, random)); // เพิ่มโอกาส
        }
        
        // Anomaly Teen1
        if (hasObjectType(objects, "teen1")) {
            possibleAnomalies.add(new Teen1Anomaly(objects, random));
            possibleAnomalies.add(new Teen1Anomaly(objects, random)); // เพิ่มโอกาส
        }
        
        // Anomaly Teen2
        if (hasObjectType(objects, "teen2")) {
            possibleAnomalies.add(new Teen2Anomaly(objects, random));
            possibleAnomalies.add(new Teen2Anomaly(objects, random)); // เพิ่มโอกาส
        }
        
        // Anomaly Washing Machine
        if (hasObjectType(objects, "washing_machine")) {
            possibleAnomalies.add(new WashingMachineAnomaly(objects, random));
            possibleAnomalies.add(new WashingMachineAnomaly(objects, random));
            possibleAnomalies.add(new WashingMachineAnomaly(objects, random));
            possibleAnomalies.add(new WashingMachineAnomaly(objects, random));
            possibleAnomalies.add(new WashingMachineAnomaly(objects, random)); // เพิ่มโอกาส
        }

        return possibleAnomalies.get(random.nextInt(possibleAnomalies.size()));
    }

    //ตรวจสอบว่ามี object ประเภทนี้ในฉากหรือไม่

    private boolean hasObjectType(List<GameObject> objects, String imageKey) {
        for (GameObject obj : objects) {
            if (obj.getImageKey().equals(imageKey)) {
                return true;
            }
        }
        return false;
    }

    
    //รีเซ็ต Anomaly
    public void resetAnomaly() {
        if (currentAnomaly != null && currentAnomaly.isActive()) {
            currentAnomaly.reset();
        }
        currentAnomaly = null;
        hasAnomaly = false;
    }
    
    public boolean hasAnomaly() {
        return hasAnomaly;
    }
    
    public Anomaly getCurrentAnomaly() {
        return currentAnomaly;
    }
}