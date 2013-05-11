import env3d.EnvObject;
import env3d.advanced.EnvNode;

public class Ball extends EnvNode
{
    private boolean rolling, turning, jumping, falling;
    private double speed, prev_y, jumpSpeed, gravity, floor, rotY;
    private int time;
    private Room currentRoom;

  
    public Ball(double x, double y, double z, Room currentRoom)
    {
        this.currentRoom = currentRoom;
        setX(x);
        setY(y);
        setZ(z);
        setScale(1);
        speed = 0.002;
        gravity = 0.006;
        jumpSpeed = 0.6;
        time = 0;
        floor = 1;
        rotY = getRotateY();
        setTexture("textures/doty_talk.png");
    }
  
    /**
    * The method gets called every frame
    */
    public void move() {
            setX(getX()-((float)(speed*Math.sin(Math.PI*rotY/180))));
            setZ(getZ()-((float)(speed*Math.cos(Math.PI*rotY/180))));
            setRotateX(getRotateX()-speed*10);
    }
        
    public void accellerate() {
        if (speed == 0) {
            speed=0.0002;
        } else {            
            speed=1.2*speed;
            if (speed > 0 && speed*speed > 0.25) {
                speed = 0.5;
            } else if (speed < 0 && speed*speed > 0.25) {
                speed = -0.5;
            }
        }
    }
    
    public void decellerate() {
        speed=0.9*speed;
        if (-0.0001 < speed && speed < 0.0001) {
            speed = 0;
        }
    }
    
    public void turnLeft() {
        rotY+=2;
        setRotateY(getRotateY()+2);
    }
    
    public void turnRight() {
        rotY-=2;
        setRotateY(getRotateY()-2);
    }
        
    
    /**
     * makes the object jump up in the air
     * @param jumpSpeed (changes the height of the jump)
     * @param jumpStart (the location of where the object initiated the jump)
     */
    public void jump() {
        if (jumping && !falling) {
            setY(floor+((jumpSpeed*time))+(0.5*(-gravity)*time*time));
            time++;
            if (jumpSpeed*time < gravity*time*time) {
                time = 0;
                jumping = false;
                falling = true;
            }
        }
    }    
    /**
     * checks to see if the object should be falling, if so, then it falls until it hits the floor
     */
    public void checkFalling() {
        if (getY() > floor) {
            if (!jumping) {
                prev_y = getY();
                setY(getY()+(-gravity)*time);
                time++;
                if (getY() < floor && jumpSpeed > 0.01) {
                    setY(floor);
                    jumpSpeed=jumpSpeed*4/5;
                    time = 0;
                    jumping = true;
                    falling = false;
                } else if (getY() < floor) {
                    setY(floor);
                    resetJumpSpeed();
                    time = 0;
                    jumping = false;
                    falling = false;
                }
            }
        }
    }
    
    public void checkWall() {
        if (getZ() <= getScale()) {
            //speed-=speed*2;
            rotY = (rotY-(180-((360-rotY)*2))) % 360;
            setRotateY(rotY);
        } else if (getZ() >= currentRoom.getDepth()-getScale()) {
            //speed-=speed*2;
            rotY = (rotY-(180-((360-rotY)*2))) % 360;
            setRotateY(rotY);
        }
        if (getX() <= getScale()) {
            //speed-=speed*2;
            rotY = (rotY-(180-((90-rotY)*2))) % 360;
            setRotateY(rotY);
        } else if (getX() >= currentRoom.getWidth()-getScale()) {
            //speed-=speed*2;
            rotY = (rotY-(180-((270-rotY)*2))) % 360;
            setRotateY(rotY);
        }
    }

    public void resetJumpSpeed() {
        jumpSpeed = 0.3;
    }
    public void setJumping(boolean delta) {
        jumping = delta;
    }
    public boolean getJumping() {
        return jumping;
    }
    public boolean getFalling() {
        return falling;
    }
    
    public boolean isRolling()
    {
        return rolling;
    }
    public double getRotY() {
        return rotY;
    }
    
    public boolean isTurning()
    {
        return turning;
    }
    
    public void switchRolling() 
    {
        rolling = !rolling;
    }
    
    public void switchTurning()
    {
        turning = !turning;
    }
    public double getSpeed() {
        return speed;
    }
    
}

