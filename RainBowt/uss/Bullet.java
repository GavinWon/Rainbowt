package uss;

/**
 * Record the state of an enemy bot.
 * 
 * @author Josh
 * @version 5/9/18
 * 
 * @author Period - 6
 * @author Assignment - EnemyBot
 * 
 * @author Sources - None
 */
public class Bullet
{
    private double time;
    private byte moveDirection;

    /**
     * calls reset
     */
    public Bullet(double t, byte m)
    {
        time = t;
        moveDirection = m;
    }
    
    public double getTime() {
        return time;
    }
    
    public byte getMoveDirection() {
        return moveDirection;
    }
    
    public void update(double t, byte m) {
        time = t;
        moveDirection = m;
    }
}