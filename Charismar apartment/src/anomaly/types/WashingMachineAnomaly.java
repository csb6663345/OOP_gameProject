package anomaly.types;

import anomaly.Anomaly;
import anomaly.AnomalyType;
import objects.GameObject;
import java.util.List;
import java.util.Random;

public class WashingMachineAnomaly implements Anomaly {
    private List<GameObject> objects;
    private GameObject targetMachine;
    private String originalImageKey;
    private boolean active = false;
    private Random random;
    
    public WashingMachineAnomaly(List<GameObject> objects, Random random) {
        this.objects = objects;
        this.random = random;
    }
    
    @Override
    public void apply() {
        // หา washing_machine ทั้งหมดในฉาก
        List<GameObject> machineList = new java.util.ArrayList<>();
        for (GameObject obj : objects) {
            if (obj.getImageKey().equals("washing_machine")) {
                machineList.add(obj);
            }
        }
        
        if (machineList.isEmpty()) return;
        
        // สุ่มเลือกเครื่องซักผ้าเครื่องใดเครื่องหนึ่ง
        targetMachine = machineList.get(random.nextInt(machineList.size()));
        originalImageKey = targetMachine.getImageKey();
        
        // สุ่มเลือก anomaly 1 หรือ 2
        String[] anomalyTypes = {"washing_machine_anomalie1", "washing_machine_anomalie2"};
        String selectedAnomaly = anomalyTypes[random.nextInt(anomalyTypes.length)];
        
        targetMachine.setImageKey(selectedAnomaly);
        active = true;
        
        System.out.println("✓ Washing Machine Anomaly: washing_machine -> " + selectedAnomaly);
    }
    
    @Override
    public void reset() {
        if (targetMachine != null) {
            targetMachine.setImageKey(originalImageKey);
        }
        active = false;
    }
    
    @Override
    public AnomalyType getType() {
        return AnomalyType.OBJECT_REPLACED;
    }
    
    @Override
    public String getDescription() {
        return "Washing Machine changed to anomaly state: " + 
               (targetMachine != null ? targetMachine.getImageKey() : "unknown");
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
}