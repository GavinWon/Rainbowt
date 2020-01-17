package robo;

import robocode.*;
// import java.awt.Color;

/**
 * BearingBot
 * 
 * @author TODO Your Name
 * @version TODO Date
 * 
 * @author Period - TODO Your Period
 * @author Assignment - PartsBot
 * 
 * @author Sources - TODO list collaborators
 */
public class BearingBot extends Robot
{
    /**
     * run: BearingBot's default behavior
     * 
     * 1. Scans for his enemy (just whip your radar around in the while (true)
     * loop of your run() method)
     */
    public void run()
    {
        // After trying out your robot, try uncommenting the import at the top,
        // and the next line:
        // setColors(Color.red,Color.blue,Color.green);
        setAdjustRadarForGunTurn(true);
        while ( true )
        {
            turnRadarRight(360);
        }
    }

    /**
     * onScannedRobot: What to do when you see another robot
     * 
     * 2. Turns toward him (right or left, depending; use the bearing reported
     * by the ScannedRobotEvent object passed to the onScannedRobot() method) 3.
     * Fires at him 4. Lastly, rams into him (Hint: use the getDistance() method
     * of the onScannedRobot() object passed to the onScannedRobot() method. The
     * sample robot "RamFire" further demonstrates the virtues of ramming.)
     * 
     * @param e an object passed to the onScannedRobot
     */
    public void onScannedRobot( ScannedRobotEvent e )
    {
        turnRight(e.getBearing());
        fire(1);
        ahead(e.getDistance());
    }
}
