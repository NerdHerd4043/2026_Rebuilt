package frc.robot.subsystems.intake;

public class IntakeConstants {

  public static final int intakeMotorID = 33;
  public static final int expansionMotorID = 34;
  public static final int expansionEncoderID = 40;

  public static final double intakeSpeed = 0.50;

  public static final double expansionSpeed = 0.3;

  // TODO: remeasure intakePos
  public static final double intakePos = 1.8;
  // TODO: measure shootPos
  public static final double shootPos = 0.98;
  public static final double restPos = 0;

  public static final class ExpansionPID {
    public static final double p = 0;
    public static final double i = 0;
    public static final double d = 0;

    public static final double maxOutput = 1;
    public static final double minOutput = -1;
  }

  public static final class ExpansionFF {
    public static final double s = 0;
    public static final double g = 0;
    public static final double v = 0;
  }

}
