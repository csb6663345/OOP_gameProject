package entities;

import java.awt.image.BufferedImage;

public class Animation {
     private BufferedImage[] frames;
    private int currentFrame;
    private double frameTimer;
    private double frameDuration; // วินาทีต่อเฟรม
    
    public Animation(BufferedImage[] frames, double fps) {
        this.frames = frames;
        this.frameDuration = 1.0 / fps;
        this.currentFrame = 0;
        this.frameTimer = 0;
    }
    
    public void update(double deltaTime) {
        frameTimer += deltaTime;
        if (frameTimer >= frameDuration) {
            currentFrame = (currentFrame + 1) % frames.length;
            frameTimer = 0;
        }
    }
    
    public BufferedImage getCurrentFrame() {
        return frames[currentFrame];
    }
    
    public void reset() {
        currentFrame = 0;
        frameTimer = 0;
    }
}

