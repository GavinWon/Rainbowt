package uss;

import robocode.*;

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
public class EnemyBot
{
    private double bearing;
    private double distance;
    private double energy;
    private double heading;
    private double velocity;
    private String name;

    /**
     * calls reset
     */
    public EnemyBot()
    {
        reset();
    }


    /**
     * returns bearing
     * 
     * @return bearing
     */
    public double getBearing()
    {

        return bearing;
    }


    /**
     * returns distance
     * 
     * @return distance
     */
    public double getDistance()
    {

        return distance;
    }


    /**
     * returns energy
     * 
     * @return energy
     */
    public double getEnergy()
    {

        return energy;
    }


    /**
     * returns heading
     * 
     * @return heading
     */
    public double getHeading()
    {

        return heading;
    }


    /**
     * returns velocity
     * 
     * @return velocity
     */
    public double getVelocity()
    {

        return velocity;
    }


    /**
     * returns name
     * 
     * @return name
     */
    public String getName()
    {

        return name;
    }


    /**
     * updates all parameters
     * 
     * @param srEvt
     *            scannedRobotEvent
     */
    public void update( ScannedRobotEvent srEvt )
    {
        bearing = srEvt.getBearing();
        distance = srEvt.getDistance();
        energy = srEvt.getEnergy();
        heading = srEvt.getHeading();
        velocity = srEvt.getVelocity();
        name = srEvt.getName();
    }


    /**
     * resets all parameters
     */
    public void reset()
    {
        bearing = 0.0;
        distance = 0.0;
        energy = 0.0;
        heading = 0.0;
        velocity = 0.0;
        name = "";
    }


    /**
     * checks if name is empty
     * 
     * @return true if name is empty
     */
    public boolean none()
    {
        return name.length() == 0;
    }
}