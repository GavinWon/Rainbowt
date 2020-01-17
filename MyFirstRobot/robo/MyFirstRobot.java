package robo;

import robocode.*;

/**
 * MyFirstRobot
 * 
 * @author TODO Your Name
 * @version TODO Date
 * 
 * @author Period - TODO Your Period
 * @author Assignment - PartsBot
 * 
 * @author Sources - Fleming N. Larsen
 */
public class MyFirstRobot extends Robot
{
    /**
     * Where all the fun is
     * 
     * @see robocode.Robot#run()
     */
    public void run()
    {
        turnLeft( getHeading() % 90 );
        turnGunRight( 90 );
        while ( true )
        {
            ahead( 1000 );
            turnRight( 90 );
        }
    }

    /**
     * Fire on opponent when scanned
     * 
     * @see robocode.Robot#onScannedRobot(robocode.ScannedRobotEvent)
     * 
     * @param e ScannedRobotEvent
     */
    public void onScannedRobot( ScannedRobotEvent e )
    {
        fire( 1 );
    }
}
