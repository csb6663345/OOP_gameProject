package core;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import managers.ResourceManager;
import entities.Player;
import floor.Floor;
import floor.Door;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    
    // ขนาดหน้าจอ
    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 768;
    
    // Game Thread
    private Thread gameThread;
    private boolean running = false;
    private final int FPS = 60;
    
    // Double Buffering
    private BufferedImage bufferImage;
    private Graphics2D g2d;
    
    // Resource Manager
    private ResourceManager resourceManager;
    
    // Game State
    private GameState currentState;
    
    // Player
    private Player player;
    
    // Floor
    private Floor currentFloor;
    private int floorNumber = 8;
    
    // Input
    private boolean keyLeft = false;
    private boolean keyRight = false;
    private boolean keyE = false;
    private boolean keyEPressed = false; // ป้องกันกดค้าง
    
    // Camera offset
    private int cameraX = 0;
    
    // Door Interaction
    private Door nearbyDoor = null;

    private boolean playerDying = false;
    private double deathTimer = 0;
    private static final double DEATH_DELAY = 3.0; // 3 วินาที
    
    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.addKeyListener(this);
        
        // สร้าง buffer image
        bufferImage = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        g2d = bufferImage.createGraphics();
        
        // โหลดรูปภาพทั้งหมด
        resourceManager = new ResourceManager();
        
        initGame();
    }
    
    private void initGame() {
        // สร้างผู้เล่น
        player = new Player(SCREEN_WIDTH / 6, SCREEN_HEIGHT - 450, resourceManager);
        
        // สร้างชั้นแรก
        currentFloor = new Floor(floorNumber, resourceManager);
        
        // ตั้งค่า Game State
        currentState = GameState.PLAYING;
    }
    
    public void startGame() {
        if (!running) {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }
    }
    
    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        
        while (running) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            
            if (delta >= 1) {
                update(1.0 / FPS);
                repaint();
                delta--;
            }
        }
    }
    
    private void update(double deltaTime) {
        if (currentState == GameState.PLAYING) {
            // ถ้ากำลังตาย หยุดเกม
            if (playerDying) {
                deathTimer += deltaTime;
                if (deathTimer >= DEATH_DELAY) {
                    currentState = GameState.GAME_OVER;
                    playerDying = false;
                    deathTimer = 0;
                }
                return; // หยุด update
            }

            // อัปเดตผู้เล่น
            player.update(deltaTime, keyLeft, keyRight);
            
            // อัปเดตกล้อง
            updateCamera();

            // อัพเดท Boss
            currentFloor.updateBosses(deltaTime, player.getX());
            
            // ตรวจสอบว่า Boss จับได้หรือไม่
            if (currentFloor.checkBossCaught(player.getX())) {
                playerDeath();
                return;
            }
            
            // ตรวจสอบประตูที่อยู่ใกล้
            checkNearbyDoor();
            
            // ตรวจสอบการกด E เพื่อเข้าประตู
            if (keyE && !keyEPressed && nearbyDoor != null) {
                keyEPressed = true;
                handleDoorInteraction(nearbyDoor);
            }
            
            if (!keyE) {
                keyEPressed = false;
            }
        }
    }
    
    private void updateCamera() {
        cameraX = (int) player.getX() - SCREEN_WIDTH / 6;
        if (cameraX < 0) cameraX = 0;
        if (cameraX > currentFloor.getWidth() - SCREEN_WIDTH) {
            cameraX = currentFloor.getWidth() - SCREEN_WIDTH;
        }
    }
    
    private void checkNearbyDoor() {
        nearbyDoor = null;
        
        float playerX = player.getX();
        
        // ตรวจสอบประตูซ้าย (Anomaly Door)
        Door leftDoor = currentFloor.getLeftDoor();
        if (Math.abs(playerX - leftDoor.getX()) < 500) {
            nearbyDoor = leftDoor;
        }
        
        // ตรวจสอบประตูขวา (Normal Door)
        Door rightDoor = currentFloor.getRightDoor();
        if (Math.abs(playerX - rightDoor.getX()) < 1000) {
            nearbyDoor = rightDoor;
        }
    }
    
    private void handleDoorInteraction(Door door) {
        if (door.isLeftDoor()) {
            // ถ้ามี Boss ให้ผ่านชั้นไปเลย (หนีรอด)
            if (currentFloor.hasActiveBoss()) {
                currentFloor.deactivateBosses();
                nextFloor();
                return;
            }
            
            // ประตูซ้าย - เลือกว่ามี Anomaly
            if (currentFloor.hasAnomaly()) {
                nextFloor();
            } else {
                resetToFloor8();
            }
        } else {
            // ประตูขวา - เลือกว่าไม่มี Anomaly
            if (!currentFloor.hasAnomaly()) {
                nextFloor();
            } else {
                resetToFloor8();
            }
        }
    }
    
    private void nextFloor() {
        floorNumber--;
        if (floorNumber < 1) {
            currentState = GameState.WIN;
        } else {
            currentFloor = new Floor(floorNumber, resourceManager);
            player.reset(SCREEN_WIDTH / 6, SCREEN_HEIGHT - 450);
            cameraX = 0;
        }
    }
    
    private void resetToFloor8() {
        floorNumber = 8;
        currentFloor = new Floor(floorNumber, resourceManager);
        player.reset(SCREEN_WIDTH / 6, SCREEN_HEIGHT - 450);
        cameraX = 0;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        if (currentState == GameState.PLAYING || playerDying) { 
            currentFloor.renderBackground(g2d, cameraX);
            currentFloor.renderBackObjects(g2d, cameraX);
            player.render(g2d, cameraX);
            currentFloor.renderFrontObjects(g2d, cameraX);
            drawHUD(g2d);
        
        if (nearbyDoor != null && !playerDying) {
            drawEPrompt(g2d);
        }

        } else if (currentState == GameState.WIN) {
            drawWinScreen(g2d);
        } else if (currentState == GameState.GAME_OVER) {
            drawGameOverScreen(g2d);
        }
        
        g.drawImage(bufferImage, 0, 0, null);
    }
    
    private void drawHUD(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Floor: " + floorNumber, 20, 50);
        
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("A/D or ←/→ to move", 20, SCREEN_HEIGHT - 60);
        g.drawString("E to enter door", 20, SCREEN_HEIGHT - 35);
    }
    
    private void drawEPrompt(Graphics2D g) {
        int promptX = SCREEN_WIDTH / 2 - 700;
        int promptY = SCREEN_HEIGHT / 2 - 200 ;
        
        // พื้นหลัง
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(promptX, promptY, 200, 80, 20, 20);
        
        // กรอบ
        g.setColor(Color.YELLOW);
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(promptX, promptY, 200, 80, 20, 20);
        
        // ข้อความ
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        String doorType = nearbyDoor.isLeftDoor() ? "ANOMALY" : "NORMAL";
        g.drawString("Press [E]", promptX + 45, promptY + 35);
        
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString(doorType + " Door", promptX + 45, promptY + 60);
    }
    
    private void drawWinScreen(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 80));
        String text = "YOU WIN!";
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, SCREEN_WIDTH / 2 - textWidth  , SCREEN_HEIGHT / 2);
        
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        text = "Press SPACE to play again";
        textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, SCREEN_WIDTH / 2 - textWidth , SCREEN_HEIGHT / 2 + 60);
    }
    
    private void drawGameOverScreen(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 80));
        String text = "GAME OVER";
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, SCREEN_WIDTH / 2 - textWidth + 50 , SCREEN_HEIGHT / 2);
        
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        text = "Press SPACE to restart";
        textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, SCREEN_WIDTH / 2 - textWidth - 30, SCREEN_HEIGHT / 2 + 60);
    }

    // ผู้เล่นตาย
    private void playerDeath() {
        playerDying = true;
        deathTimer = 0;
        player.setDead(true); // แสดงรูปตาย
        
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            keyLeft = true;
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            keyRight = true;
        }
        if (code == KeyEvent.VK_E) {
            keyE = true;
        }
        
        if (code == KeyEvent.VK_SPACE) {
            if (currentState == GameState.WIN || currentState == GameState.GAME_OVER) {
                floorNumber = 8;
                currentFloor = new Floor(floorNumber, resourceManager);
                player.reset(SCREEN_WIDTH / 6, SCREEN_HEIGHT - 450);
                player.setDead(false);
                cameraX = 0;
                currentState = GameState.PLAYING;
            }
        }
        
        if (code == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        
        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            keyLeft = false;
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            keyRight = false;
        }
        if (code == KeyEvent.VK_E) {
            keyE = false;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    
    private enum GameState {
        MENU, PLAYING, PAUSED, GAME_OVER, WIN
    }
}