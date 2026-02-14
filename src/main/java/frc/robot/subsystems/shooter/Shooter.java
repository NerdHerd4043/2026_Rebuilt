package frc.robot.subsystems.shooter;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.shooter.ShooterConstants;
import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

@Logged
public class Shooter extends SubsystemBase {

  // subscribers and variables code block
  private final DoubleSubscriber maxSpeedSub;
  private double flyWheelMaxSpeed = ShooterConstants.flyWheelMaxSpeed; // Default max speed

  private SparkFlex flyWheelMotor = new SparkFlex(ShooterConstants.flyWheelMotorID, MotorType.kBrushless);
  private SparkFlex indexerMotor = new SparkFlex(ShooterConstants.indexerMotorID, MotorType.kBrushless);

  private final SparkFlexConfig flyWheelMotorConfig = new SparkFlexConfig();
  private final SparkFlexConfig indexerMotorConfig = new SparkFlexConfig();

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

    flyWheelMotor.configure(flyWheelMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    indexerMotor.configure(indexerMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void periodic() {

    // update flywheel speed
    flyWheelMaxSpeed = maxSpeedSub.get();

  }

  public void spinUpflyWheel() {
    flyWheelMotor.set(ShooterConstants.flyWheelMaxSpeed);
  }

  public void slowflyWheel() {
    flyWheelMotor.stopMotor();
  }

  // there is proble a beter way to do this but i dont know how -Michael
  public void emergencyStop() {
    flyWheelMotorConfig.idleMode(IdleMode.kBrake);
    flyWheelMotor.configure(flyWheelMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    flyWheelMotor.stopMotor();

    flyWheelMotorConfig.idleMode(IdleMode.kCoast);
    flyWheelMotor.configure(flyWheelMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public Command feedBalls() {

    return this.run(() -> indexerMotor.set(ShooterConstants.indexerFeedSpeed))
        .finallyDo(() -> indexerMotor.stopMotor());
  }
}
