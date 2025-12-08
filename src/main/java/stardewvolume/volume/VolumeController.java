package stardewvolume.volume;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class VolumeController {
    private DoubleProperty targetVolume;

    public VolumeController() {
        this.targetVolume = new SimpleDoubleProperty(0.5);
    }

    public void setTargetVolume(double volume) {
        targetVolume.set(Math.max(0.0, Math.min(1.0, volume)));
    }

    public double getTargetVolume() {
        return targetVolume.get();
    }

    public DoubleProperty targetVolumeProperty() {
        return targetVolume;
    }
}