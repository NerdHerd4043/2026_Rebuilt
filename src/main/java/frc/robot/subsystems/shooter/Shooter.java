package frc.robot.subsystems.shooter;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  private SparkFlex flyWheelMotor = new SparkFlex(ShooterConstants.flyWheelMotorID, MotorType.kBrushless);
  private SparkFlex indexerMotor = new SparkFlex(ShooterConstants.indexerMotorID, MotorType.kBrushless);

  private final SparkClosedLoopController pidController;

  private boolean stoped;

  public Shooter() {
    final SparkFlexConfig flyWheelMotorConfig = new SparkFlexConfig();
    final SparkFlexConfig indexerMotorConfig = new SparkFlexConfig();

    // configs
    flyWheelMotorConfig.idleMode(IdleMode.kCoast);
    indexerMotorConfig.idleMode(IdleMode.kBrake);

    flyWheelMotorConfig.inverted(true);
    indexerMotorConfig.inverted(true);

    // set PID coeffecients
    flyWheelMotorConfig.closedLoop.p(ShooterConstants.FlyWheelPID.p)
      .i(ShooterConstants.FlyWheelPID.i)
      .d(ShooterConstants.FlyWheelPID.d)
      .maxOutput(ShooterConstants.FlyWheelPID.maxOutput)
      .minOutput(ShooterConstants.FlyWheelPID.minOutput);

    flyWheelMotorConfig.closedLoop.feedForward.kS(ShooterConstants.FlyWheelFF.s)
      .kV(ShooterConstants.FlyWheelFF.v);

    flyWheelMotor.configure(flyWheelMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    indexerMotor.configure(indexerMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    pidController =  flyWheelMotor.getClosedLoopController();
  }

  public void spinUpFlyWheel() {
    this.stoped = false;
      pidController.setSetpoint(ShooterConstants.setPoint, ControlType.kVelocity);
  }

  public void slowFlyWheel() {
    if (!this.stoped) {
      pidController.setSetpoint(0, ControlType.kVoltage);
    }
  }

  public void stopFlyWheel() {
    this.stoped = true;
    pidController.setSetpoint(0, ControlType.kVelocity);
  }

  public Command feedBalls() {
    return this.run(() -> {if (pidController.isAtSetpoint()) { indexerMotor.set(ShooterConstants.indexerFeedSpeed); }})
      .finallyDo(() -> indexerMotor.stopMotor());
  }

  public void periodic() {

  }
}