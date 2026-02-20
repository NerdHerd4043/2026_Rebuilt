package frc.robot.subsystems.shooter;

public class ShooterConstants {
    public static final int flyWheelMotorID = 31;
    public static final int indexerMotorID = 32;
    public static final double flyWheelMaxSpeed = 1.0;
    public static final double indexerFeedSpeed = 0.25;
    public static final double setPoint = 5000;

    public static final class FlyWheelPID {
        public static final double p = 0.00035;
        public static final double i = 0;
        public static final double d = 0.01;

        public static final double maxOutput = 1;
        public static final double minOutput = -1;
    }

    public static final class FlyWheelFF {
        public static final double s = 0.0;
        public static final double v = 0.00015;
    }
}
