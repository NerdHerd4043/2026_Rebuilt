package frc.robot.subsystems.shooter;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkRelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Shooter extends SubsystemBase {

  // subscribers and variables code block
  private final DoubleSubscriber maxSpeedSub;
  private double flyWheelMaxSpeed = ShooterConstants.flyWheelMaxSpeed; // Default max speed

  private SparkFlex flyWheelMotor = new SparkFlex(ShooterConstants.flyWheelMotorID, MotorType.kBrushless);
  private SparkFlex indexerMotor = new SparkFlex(ShooterConstants.indexerMotorID, MotorType.kBrushless);

  private final SparkFlexConfig flyWheelMotorConfig = new SparkFlexConfig();
  private final SparkFlexConfig indexerMotorConfig = new SparkFlexConfig();

  private final SparkClosedLoopController pidController;
  private final SparkRelativeEncoder encoder;

  private boolean stop;

  public Shooter() {

    // initiate shuffleboard tab
    ShuffleboardTab configTab = Shuffleboard.getTab("ShooterConfiguration");

    configTab.add("flyWheelMaxSpeed", ShooterConstants.flyWheelMaxSpeed)
        .withWidget(BuiltInWidgets.kTextView) // Or kNumberSlider
        .getEntry();

    // networktables slop
    var inst = NetworkTableInstance.getDefault();

    // Subscribes to /datatable/MaxSpeed
    maxSpeedSub = inst.getDoubleTopic("/Shuffleboard/ShooterConfiguration/flyWheelMaxSpeed")
        .subscribe(ShooterConstants.flyWheelMaxSpeed);

    // configs
    flyWheelMotorConfig.idleMode(IdleMode.kCoast);
    indexerMotorConfig.idleMode(IdleMode.kBrake);

    flyWheelMotorConfig.inverted(true);
    indexerMotorConfig.inverted(true);

    // set PID coeffecients
    flyWheelMotorConfig.closedLoop.p(ShooterConstants.FlyWheelPID.p);
    flyWheelMotorConfig.closedLoop.i(ShooterConstants.FlyWheelPID.i);
    flyWheelMotorConfig.closedLoop.d(ShooterConstants.FlyWheelPID.d);
    flyWheelMotorConfig.closedLoop.maxOutput(ShooterConstants.FlyWheelPID.maxOutput);
    flyWheelMotorConfig.closedLoop.minOutput(ShooterConstants.FlyWheelPID.minOutput);

    flyWheelMotorConfig.closedLoop.feedForward.kS(ShooterConstants.FlyWheelFF.s);
    flyWheelMotorConfig.closedLoop.feedForward.kV(ShooterConstants.FlyWheelFF.v);

    flyWheelMotor.configure(flyWheelMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    indexerMotor.configure(indexerMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    pidController =  flyWheelMotor.getClosedLoopController();
    encoder = (SparkRelativeEncoder) flyWheelMotor.getEncoder();

  }

  public void periodic() {

    // update flywheel speed
    flyWheelMaxSpeed = maxSpeedSub.get();

  }

  public void spinUpFlyWheel() {
    stop = false;
      pidController.setSetpoint(0, ControlType.kVelocity);

  }

  public void slowFlyWheel() {
    if (!stop) {
      pidController.setSetpoint(0, ControlType.kVoltage);
    }
  }

  public void stopFlyWheel() {
    stop = true;
    pidController.setSetpoint(0, ControlType.kVelocity);

  }

  public Command feedBalls() {

    return this.run(() -> indexerMotor.set(ShooterConstants.indexerFeedSpeed))
        .finallyDo(() -> indexerMotor.stopMotor());
  }
}
