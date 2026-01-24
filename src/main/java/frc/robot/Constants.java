// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.RobotConfig;
import frc.robot.Constants.DriveConstants.ModuleLocations;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.math.system.plant.DCMotor;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean
 * constants. This class should not be used for any other purpose. All constants
 * should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {

public static final class RobotConfigInfo {

    public static final ModuleConfig moduleConfig = new ModuleConfig(
        DriveConstants.WHEEL_DIAMETER,
        2.75,
        0.7,
        DCMotor.getNEO(1),
        DriveConstants.DRIVE_REDUCTION,
        DriveConstants.currentLimit,
        1);

    public static final RobotConfig robotConfig = new RobotConfig(
        66.68, 3.682,
        moduleConfig,
        ModuleLocations.frontLeft,
        ModuleLocations.frontRight,
        ModuleLocations.backLeft,
        ModuleLocations.backRight);
  }


  //evil code that checks what mode the robot is running on
  //probably will rewrite later
  public static final Mode simMode = Mode.SIM;
  public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;

  public static enum Mode {
    /** Running on a real robot. */
    REAL,

    /** Running a physics simulator. */
    SIM,

    /** Replaying from a log file. */
    REPLAY
  }



  public static final class DriveConstants {
    public static final double deadband = 0.08;
    public static final int currentLimit = 40;
    public static final double slewRate = 20; // lower number for higher center of mass
    public static final int temp = 21;
    public static final double DRIVE_REDUCTION = 1.0 / 6.75;
    public static final double NEO_FREE_SPEED = 5820.0 / 60.0;
    public static final double WHEEL_DIAMETER = 0.1016;
    public static final double MAX_VELOCITY = NEO_FREE_SPEED * DRIVE_REDUCTION * WHEEL_DIAMETER * Math.PI;
    public static final double MAX_ANGULAR_VELOCITY = MAX_VELOCITY / (ModuleLocations.dist / Math.sqrt(2.0));

    public static final class SwervePID {
      public static final double p = 0.12;
      public static final double i = 0;
      public static final double d = 0.0015;
    }

    public static final class SwerveModules {

    }

    public static final class ModuleLocations {
      public static final double dist = Units.inchesToMeters(9.25);
      public static final double robotRaduius = Math.sqrt(2 * Math.pow(dist, 2));
      public static final Translation2d frontLeft = new Translation2d(dist, dist);
      public static final Translation2d frontRight = new Translation2d(dist, -dist);
      public static final Translation2d backLeft = new Translation2d(-dist, dist);
      public static final Translation2d backRight = new Translation2d(-dist, -dist);
    }
  }



}