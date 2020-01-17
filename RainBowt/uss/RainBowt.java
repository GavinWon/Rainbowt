package uss;

import robocode.*;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.*;


/**
 * @author Gavin Josh Sam
 */
public class RainBowt extends AdvancedRobot
{
    private AdvancedEnemyBot enemy = new AdvancedEnemyBot();

    private RobotPart[] parts = new RobotPart[3]; // make three parts

    private final static int RADAR = 0;

    private final static int GUN = 1;

    private final static int TANK = 2;

    // holds direction to move in
    private byte moveDirection = 1;

    // holds strafe turn angle
    private double angle = 0;

    // holds the random value for when to switch
    private double strafeDistance = randStrafe();

    // once ramming continue ramming
    private boolean ram = false;

    // stored location values for calculating distance
    private double storeX = 0;

    private double storeY = 0;

    private Random rand = new Random();

    private Color[] colors = new Color[] { Color.red, Color.orange,
        Color.yellow, Color.green, Color.blue, Color.pink };

    private double bulletsFired = 1;

    private double bulletsHit = 1;

    private ArrayList<Bullet> bulletTime = new ArrayList<Bullet>( 60 );

    private int bulletTimeLen = 60;


    // computes the absolute bearing between two points
    /**
     * absolute bearing
     * 
     * @param x1
     *            double
     * @param y1
     *            double
     * @param x2
     *            double
     * @param y2
     *            double
     * @return bearing
     */

    public double absoluteBearing( double x1, double y1, double x2, double y2 )
    {
        double xo = x2 - x1;
        double yo = y2 - y1;
        double hyp = Point2D.distance( x1, y1, x2, y2 );
        double arcSin = Math.toDegrees( Math.asin( xo / hyp ) );
        double bearing = 0;

        if ( xo > 0 && yo > 0 )
        { // both pos: lower-Left
            bearing = arcSin;
        }
        else if ( xo < 0 && yo > 0 )
        { // x neg, y pos: lower-right
            bearing = 360 + arcSin; // arcsin is negative here, actually 360 -
                                    // ang
        }
        else if ( xo > 0 && yo < 0 )
        { // x pos, y neg: upper-left
            bearing = 180 - arcSin;
        }
        else if ( xo < 0 && yo < 0 )
        { // both neg: upper-right
            bearing = 180 - arcSin; // arcsin is negative here, actually 180 +
                                    // ang
        }

        return bearing;
    }


    // normalizes a bearing to between +180 and -180
    /**
     * normalize bearing
     * 
     * @param angle
     *            double
     * @return angle
     */
    public double normalizeBearing( double angle )
    {
        while ( angle > 180 )
        {
            angle -= 360;
        }
        while ( angle < -180 )
        {
            angle += 360;
        }
        return angle;
    }


    public double distance( double x1, double y1, double x2, double y2 )
    {
        return Math.abs(
            Math.sqrt( Math.pow( x1 - x2, 2 ) + Math.pow( y1 - y2, 2 ) ) );
    }


    public Color randColor()
    {
        return colors[rand.nextInt( 5 )];
    }


    public double randStrafe()
    {
        return Math.round( Math.random() * 160 ) + 30;
    }


    /**
     * run
     */
    public void run()
    {
        parts[RADAR] = new Radar();
        parts[GUN] = new Gun();
        parts[TANK] = new Tank();

        // initialize each part
        for ( int i = 0; i < parts.length; i++ )
        {
            parts[i].init();
        }
        bulletTime.clear();
        // run through each part then execute
        for ( int i = 0; true; i = ( i + 1 ) % parts.length )
        {
            parts[i].move();
            if ( i == 0 )
            {
                execute();
            }
        }
    }


    /**
     * on scanned robot
     * 
     * @param e
     *            scanned robot event
     */
    // ----------------------------------------------------------------------------------------------------------------
    // updates enemy bot
    public void onScannedRobot( ScannedRobotEvent e )
    {
        // passes if have no enemy, this one is closer than current or this is
        // current enemy
        if ( enemy.none() || e.getDistance() < enemy.getDistance() - 120
            || e.getName().equals( enemy.getName() ) )
        {
            double energyChange = enemy.getEnergy() - e.getEnergy();
            enemy.update( e, this );
            if ( energyChange >= 0.1 && energyChange <= 3 )
            {
                // out.println( "enemy fired" );
                enemyFiredBullet( energyChange, getTime() );
            }
        }
    }


    /**
     * robot part
     */

    public void onHitByBullet( HitByBulletEvent e )
    {
        // change direction if hit
        storeX = getX();
        storeY = getY();
        strafeDistance = randStrafe();
        moveDirection *= -1;
        // out.println( "HIT!! gotta switch" );
    }


    public void onHitRobot( HitRobotEvent e )
    {
        ram = true;
    }


    public void onHitWall( HitWallEvent e )
    {
        moveDirection *= -1;
    }


    public void onWin( WinEvent e )
    {
        stop();
        while ( true )
        {
            setTurnRadarRight( 3 );
            setColors( randColor(),
                randColor(),
                randColor(),
                randColor(),
                randColor() );
            execute();
        }
    }


    public void onRoundEnded( RoundEndedEvent e )
    {
        out.println( "Bullet Accuracy: " + bulletsHit / bulletsFired );
    }


    public void onRobotDeath( RobotDeathEvent e )
    {
        if ( e.getName().equals( enemy.getName() ) )
        {
            // out.println( "killed my enemy" );
            enemy.reset();
            // out.println(enemy.none());
            // out.println(getRadarTurnRemaining());
            setTurnRadarRight( 300 );
        }

    }


    public void onBulletHit( BulletHitEvent e )
    {
        bulletsFired += 1;
        bulletsHit += 1;
    }


    public void onBulletMissed( BulletMissedEvent e )
    {
        bulletsFired += 1;
    }


    // ---------------------------------------------------------------------------------------------------
    public void enemyFiredBullet( double enDrop, long time )
    {
        double fireSpeed = robocode.Rules.getBulletSpeed( enDrop );
        double dis = distance( enemy.getX(), enemy.getY(), getX(), getY() );
        double dodgeTime = ( time + ( dis / fireSpeed ) );
        bulletTime.add( new Bullet( dodgeTime, moveDirection ) );
        boolean avoid = false;
        double move = 0;
        for ( int i = 0; i < bulletTime.size(); i++ )
        {
            if ( bulletTime.get( i ).getTime() - time < 0.3 )
            {
                avoid = true;
                move = bulletTime.get(i).getMoveDirection();
                bulletTime.remove( i );
                i++;
            }
        }
        if ( avoid )
        {
            moveDirection *= -1 * move;
            //moveDirection *= -1;
            //out.println( "switching to avoid" );
        }
    }


    public interface RobotPart
    {
        /**
         * init
         */
        public void init();


        /**
         * move
         */
        public void move();
    }


    /**
     * radar
     */
    public class Radar implements RobotPart
    {
        /**
         * init
         */
        public void init()
        {
            // simple initiation routine
            setAdjustRadarForGunTurn( true );
            turnRadarRight( 360 );
            execute();
            // out.println( "radar init" );
        }


        /**
         * move
         */
        public void move()
        {
            // narrow beam radar lock
            if ( getRadarTurnRemaining() == 0 )
                setTurnRadarRight( 400 );
            setTurnRadarRight( normalizeBearing(
                getHeading() - getRadarHeading() + enemy.getBearing() ) );
        }
    }


    /**
     * gun
     */
    public class Gun implements RobotPart
    {
        /**
         * init
         */
        public void init()
        {
            setAdjustGunForRobotTurn( true );
        }


        /**
         * move
         */
        public void move()
        {
            // predictive shooting
            if ( enemy.none() )
                return;

            double firePower = Math.max( 800 / enemy.getDistance(), 2.8 );
            //double firePower = bulletsHit/bulletsFired * 8;
            //out.println( (bulletsHit/bulletsFired) * 3 * 2 );
            /*if ( ram )
            {
                firePower = 3;
            }*/
            if ( getEnergy() < 25 || enemy.getEnergy() < 0.4)
            {
                firePower = 300/enemy.getDistance();
            }
            double bulletSpeed = 20 - firePower * 3;

            long time = (long)( enemy.getDistance() / bulletSpeed );

            double futureX = enemy.getFutureX( time );
            double futureY = enemy.getFutureY( time );
            double absDeg = absoluteBearing( getX(), getY(), futureX, futureY );

            setTurnGunRight( normalizeBearing( absDeg - getGunHeading() ) );
            if ( getGunHeat() == 0 && Math.abs( getGunTurnRemaining() ) < 10 )
            {
                setFire( firePower );
            }

        }
    }


    /**
     * tank
     */
    public class Tank implements RobotPart
    {
        /**
         * init
         */
        public void init()
        {
            setColors( Color.red, Color.blue, Color.red );
            storeX = getX();
            storeY = getY();
        }


        /**
         * move
         */
        public void move()
        {
            /*if ( enemy.getDistance() < 100 || ram )
            {
                ram = true;
                moveRam();
            }

            else*/
            {
                setTurnRight( normalizeBearing(
                    enemy.getBearing() + 90 - ( angle * moveDirection ) ) );

                // switch after distance
                /*
                 * if ( distance( storeX, storeY, getX(), getY() ) >
                 * strafeDistance ) { storeX = getX(); storeY = getY();
                 * moveDirection *= -1; strafeDistance = randStrafe(); //
                 * out.println(strafeDistance); }
                 */

                // enemy too far
                if ( enemy.getDistance() > 350 )
                {
                    angle = 30;
                    // out.println("far");
                }

                // enemy too close
                else if ( enemy.getDistance() < 340 )
                {
                    angle = -30;
                    // out.println("close");
                }
                // enemy in wanted range
                else
                {
                    // angle = Math.random() * 10 - 5;
                    angle = 0;
                    // out.println("mid");
                }

                /*
                 * setColors( randColor(), randColor(), randColor(),
                 * randColor(), randColor() );
                 */

                setAhead( 50 * moveDirection );
            }
        }


        public void moveRam()
        {
            setTurnRight( enemy.getBearing() );
            setAhead( 50 );
            // out.println( "RAMMING!!" );
            if ( enemy.getDistance() > 125 )
            {
                ram = false;
            }
        }
    }
}
