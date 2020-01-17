package uss;

import robocode.*;

/**
 * Record the advanced state of an enemy bot.
 * 
 * @author Josh
 * @version 5/9/18
 * 
 * @author Period - 6
 * @author Assignment - AdvancedEnemyBot
 * 
 * @author Sources - None
 */
public class AdvancedEnemyBot extends EnemyBot
{
    private double x;
    private double y;

    /**
     * calls reset
     */

    public AdvancedEnemyBot()
    {
        reset();
    }


    /**
     * returns x
     * 
     * @return x
     */

    public double getX()
    {
        return x;
    }


    /**
     * returns y
     * 
     * @return y
     */

    public double getY()
    {
        return y;
    }


    /**
     * updates all parameters
     * 
     * @param e
     *            ScannedRobotEvent
     * @param robot
     *            Robot
     */

    public void update( ScannedRobotEvent e, Robot robot )
    {
        super.update( e );
        double absBearingDeg = ( robot.getHeading() + e.getBearing() );
        if ( absBearingDeg < 0 )
        {
            absBearingDeg += 360;
        }
        x = robot.getX()
            + Math.sin( Math.toRadians( absBearingDeg ) ) * e.getDistance();
        y = robot.getY()
            + Math.cos( Math.toRadians( absBearingDeg ) ) * e.getDistance();
    }


    /**
     * returns future x
     * 
     * @return future x
     * @param when
     *            long
     */
    public double getFutureX( long when )
    {
        return x
            + Math.sin( Math.toRadians( getHeading() ) ) * getVelocity() * when;
    }


    /**
     * returns y
     * 
     * @return future y
     * @param when
     *            long
     */
    public double getFutureY( long when )
    {
        return y
            + Math.cos( Math.toRadians( getHeading() ) ) * getVelocity() * when;
    }
    
    public void updateX (double newX)
    {
        x = newX;
    }

    public void updateY (double newY)
    {
        y = newY;
    }

    /**
     * resets
     */
    public void reset()
    {
        super.reset();
        x = 0;
        y = 0;
    }
}