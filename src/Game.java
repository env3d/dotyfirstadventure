
import env3d.Env;
import env3d.android.EnvMobileGame;

public class Game extends EnvMobileGame {

    private Ball b;
    private Room currentRoom;
    private double camX, camY, camZ;
    private int cam;
    private boolean camChange = false;
    private boolean fs;

    public void setup() {
        currentRoom = new Room(100, 200, 80, "BALLS!!");
        b = new Ball(25, 1, 25, currentRoom);
        cam = 3;
        fs = false;
        env.setRoom(currentRoom);
        env.setDefaultControl(false);
        //env.setCameraXYZ(50,120,40);
        //env.setCameraPitch(-90);
        env.setDefaultControl(false);
        env.setCameraXYZ(b.getX(), 3, b.getZ());
        env.addObject(b);
        
        env.setShowAd(true);
    }

    public void loop() {

        if (env.getKeyDown("up") || env.getTiltY() < 7) {
            b.accellerate();
        }
        
        if (env.getKeyDown("down") || env.getTiltY() > 8) {
            b.decellerate();
        }
        
        if (env.getKeyDown("left") || env.getTiltX() < -1) {
            b.turnLeft();
        }
        
        if (env.getKeyDown("right") || env.getTiltX() > 1) {
            b.turnRight();
        }

        if (env.getKeyDown("a") && !camChange) {
            camChange = true;
        }
        
        if (!env.getKeyDown("a") && camChange) {
            camChange = false;
            if (cam == 1) {
                cam = 2;
            } else if (cam == 2) {
                cam = 3;
            } else if (cam == 3) {
                cam = 1;
            }
        }
        
        if (env.getKeyDown("b") || env.getTiltZ() > 10) {
            if (!b.getFalling() && !b.getJumping()) {
                b.resetJumpSpeed();
                b.setJumping(true);
            }
        }
//        if (env.getKeyDown(Keyboard.KEY_N)) {
//            env.removeObject(b);
//            b = new Ball(50, 1, 50, currentRoom);
//            env.addObject(b);
//            env.setCameraXYZ(b.getX(), 3, b.getZ());
//        }

        if (cam == 1) {
            followCamera();
        } else if (cam == 2) {
            followCamera2();
        } else if (cam == 3) {
            followCamera3();
        }

        b.move();
        b.jump();
        b.checkFalling();
        b.checkWall();
        String str1 = "Ball X: " + Math.round(b.getX()) + " Camera X: " + Math.round(camX) + " xDifference: " + Math.round((camX - (int) (b.getX())));
        String str2 = "Ball Z: " + Math.round(b.getZ()) + " Camera Z: " + Math.round(camZ) + " zDifference: " + Math.round((camZ - (int) (b.getZ())));
        String str3 = "Camera: " + cam;
        
        
        env.setDisplayStr(str1, 2, env.getScreenHeight());
        env.setDisplayStr(str2, 2, env.getScreenHeight()-20);
        env.setDisplayStr(str3, 2, env.getScreenHeight()-40);
        env.setDisplayStr("Tilt X"+env.getTiltX(), 2, env.getScreenHeight()-60);
        env.setDisplayStr("Tilt Y"+env.getTiltY(), 2, env.getScreenHeight()-80);
        env.setDisplayStr("Tilt Z"+env.getTiltZ(), 2, env.getScreenHeight()-100);

    }

    /**
     * Sets the camera to follow b
     */
    private void followCamera() {
        camX = env.getCameraX();
        camY = env.getCameraY();
        camZ = env.getCameraZ();
        env.setCameraXYZ((float) (b.getX() + 4 * Math.sin(b.getRotY() / 180 * Math.PI)),
                b.getY() + 1,
                (float) (b.getZ() + 4 * Math.cos(b.getRotY() / 180 * Math.PI)));
        env.setCameraYaw(b.getRotY());
        env.setCameraPitch(-10);
        env.setDefaultControl(false);
        if (env.getCameraX() <= 0) {
            env.setCameraXYZ(camX, b.getY() + 1, (float) (b.getZ() + 4 * Math.cos(b.getRotY() / 180 * Math.PI)));
        }
        if (env.getCameraX() >= currentRoom.getWidth()) {
            env.setCameraXYZ(camX, b.getY() + 1, (float) (b.getZ() + 4 * Math.cos(b.getRotY() / 180 * Math.PI)));
        }
        if (env.getCameraZ() <= 0) {
            env.setCameraXYZ((float) (b.getX() + 4 * Math.sin(b.getRotY() / 180 * Math.PI)), b.getY() + 1, camZ);
        }
        if (env.getCameraZ() >= currentRoom.getDepth()) {
            env.setCameraXYZ((float) (b.getX() + 4 * Math.sin(b.getRotY() / 180 * Math.PI)), b.getY() + 1, camZ);
        }
    }

    private void followCamera2() {
        camX = env.getCameraX();
        camY = b.getY() + 1;
        camZ = env.getCameraZ();

        double xdiff = camX - b.getX();
        double zdiff = camZ - b.getZ();
        double ydiff = camY - b.getY();

        double dist = (double) Math.sqrt(xdiff * xdiff + zdiff * zdiff);

        if (xdiff >= 0 && zdiff > 0) {
            env.setCameraYaw(Math.toDegrees(Math.atan(xdiff / zdiff)));
        } else if (xdiff < 0 && zdiff >= 0) {
            env.setCameraYaw(270 - (Math.toDegrees(Math.atan(zdiff / xdiff))));
        } else if (xdiff <= 0 && zdiff < 0) {
            env.setCameraYaw(Math.toDegrees(Math.atan(xdiff / zdiff)) + 180);
        } else if (xdiff > 0 && zdiff <= 0) {
            env.setCameraYaw(90 - (Math.toDegrees(Math.atan(zdiff / xdiff))));
        }

        if (dist > 8) {
            camX = (camX - ((float) (b.getSpeed() * Math.sin(Math.PI * env.getCameraYaw() / 180))));
            camZ = (camZ - ((float) (b.getSpeed() * Math.cos(Math.PI * env.getCameraYaw() / 180))));
        } else {
            camX = (camX - ((float) ((b.getSpeed() * dist / 8) * Math.sin(Math.PI * env.getCameraYaw() / 180))));
            camZ = (camZ - ((float) ((b.getSpeed() * dist / 8) * Math.cos(Math.PI * env.getCameraYaw() / 180))));
        }

        env.setCameraXYZ(camX, camY, camZ);
        env.setCameraPitch(-10);
        env.setDefaultControl(false);
    }

    private void followCamera3() {
        camX = env.getCameraX();
        camY = env.getCameraY();
        camZ = env.getCameraZ();

        double xdiff = camX - b.getX();
        double zdiff = camZ - b.getZ();
        double ydiff = camY - b.getY();
        double dist = (double) Math.sqrt(xdiff * xdiff + zdiff * zdiff);

        if (xdiff >= 0 && zdiff > 0) {
            env.setCameraYaw(Math.toDegrees(Math.atan(xdiff / zdiff)));
        } else if (xdiff < 0 && zdiff >= 0) {
            env.setCameraYaw(270 - (Math.toDegrees(Math.atan(zdiff / xdiff))));
        } else if (xdiff <= 0 && zdiff < 0) {
            env.setCameraYaw(Math.toDegrees(Math.atan(xdiff / zdiff)) + 180);
        } else if (xdiff > 0 && zdiff <= 0) {
            env.setCameraYaw(90 - (Math.toDegrees(Math.atan(zdiff / xdiff))));
        }

        if (dist > 8) {
            camX = (camX - ((float) (b.getSpeed() * Math.sin(Math.PI * env.getCameraYaw() / 180))));
            camZ = (camZ - ((float) (b.getSpeed() * Math.cos(Math.PI * env.getCameraYaw() / 180))));
        } else {
            camX = (camX - ((float) ((b.getSpeed() * dist / 8) * Math.sin(Math.PI * env.getCameraYaw() / 180))));
            camZ = (camZ - ((float) ((b.getSpeed() * dist / 8) * Math.cos(Math.PI * env.getCameraYaw() / 180))));
        }

        env.setCameraXYZ(camX, camY, camZ);
        env.setCameraPitch(-Math.toDegrees(Math.atan(ydiff / dist)));
        env.setDefaultControl(false);
    }

    public static void main(String args[]) {
        Game g = new Game();
        g.start();
    }
}
