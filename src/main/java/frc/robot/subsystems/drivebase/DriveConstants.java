package frc.robot.subsystems.drivebase;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.subsystems.drivebase.SwerveModuleConfig;

public class DriveConstants {

  // Zeroed rotation values for each module, see setup instructions
  public static final Rotation2d frontLeftZeroRotation = new Rotation2d(0.0);
  public static final Rotation2d frontRightZeroRotation = new Rotation2d(0.0);
  public static final Rotation2d backLeftZeroRotation = new Rotation2d(0.0);
  public static final Rotation2d backRightZeroRotation = new Rotation2d(0.0);

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
    public static final SwerveModuleConfig frontRight = new SwerveModuleConfig(6, 5, 21, false);
    public static final SwerveModuleConfig frontLeft = new SwerveModuleConfig(7, 8, 22, true);
    public static final SwerveModuleConfig backLeft = new SwerveModuleConfig(1, 2, 23, false);
    public static final SwerveModuleConfig backRight = new SwerveModuleConfig(3, 4, 24, true);
  }

  public static final class ModuleLocations {
    public static final double dist = Units.inchesToMeters(9.25);
    public static final double robotRadius = Math.sqrt(2 * Math.pow(dist, 2));
    public static final Translation2d frontLeft = new Translation2d(dist, dist);
    public static final Translation2d frontRight = new Translation2d(dist, -dist);
    public static final Translation2d backLeft = new Translation2d(-dist, dist);
    public static final Translation2d backRight = new Translation2d(-dist, -dist);
  }

}
