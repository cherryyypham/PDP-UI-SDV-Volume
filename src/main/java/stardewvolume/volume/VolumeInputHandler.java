package stardewvolume.volume;

public class VolumeInputHandler {
    private VolumeModel engine;
    private boolean isMousePressed;

    public VolumeInputHandler(VolumeModel engine) {
        this.engine = engine;
        this.isMousePressed = false;
    }

    public void handleMousePress() {
        if (engine.isPlaying()) {
            isMousePressed = true;
            engine.onClickPress();
        }
    }

    public void handleMouseRelease() {
        isMousePressed = false;
    }

    public boolean isMousePressed() {
        return isMousePressed;
    }
}