package anomaly;

public interface Anomaly {
    void apply();
    void reset();
    AnomalyType getType();
    String getDescription();
    boolean isActive();
}
