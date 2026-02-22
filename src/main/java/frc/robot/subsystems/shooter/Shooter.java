package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.config.*;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

@Logged
public class Shooter extends SubsystemBase {
  private SparkFlex flyWheelMotor = new SparkFlex(ShooterConstants.flyWheelMotorID, MotorType.kBrushless);
  private SparkMax indexerMotor = new SparkMax(ShooterConstants.indexerMotorID, MotorType.kBrushless);

  private final SparkClosedLoopController pidController;
  private final RelativeEncoder encoder;

  private boolean stoped;

  public Shooter() {
    final SparkFlexConfig flyWheelMotorConfig = new SparkFlexConfig();
    final SparkMaxConfig indexerMotorConfig = new SparkMaxConfig();


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

    flyWheelMotorConfig.closedLoop.feedForward
      .kV(ShooterConstants.FlyWheelFF.v);

    flyWheelMotor.configure(flyWheelMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    indexerMotor.configure(indexerMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    this.pidController = flyWheelMotor.getClosedLoopController();
    this.encoder = flyWheelMotor.getEncoder();
  }

  public void spinUpFlyWheel() {
    this.stoped = false;
    this.pidController.setSetpoint(ShooterConstants.setPoint, ControlType.kVelocity);
    // flyWheelMotor.set(0.05);
  }

  public void slowFlyWheel() {
    if (!this.stoped) {
      this.pidController.setSetpoint(0, ControlType.kVoltage);
    }
  }

  // public void stopFlyWheel() {
  //   this.stoped = true;
  //   this.pidController.setSetpoint(0, ControlType.kVelocity);
  // }

  public Command shootOneBall(){

    Command runFlywheel = new RunCommand(this::spinUpFlyWheel).withTimeout(4);
    Command waitCommand = Commands.waitSeconds(0.2);
    Command runShooter = feedBalls();
    return Commands.sequence(
        runFlywheel,
        waitCommand,
        runShooter
    );

  }


  public Command feedBalls() {
    return this.run(() -> {if (!this.pidController.isAtSetpoint()) { indexerMotor.set(ShooterConstants.indexerFeedSpeed); }})
      .finallyDo(() -> indexerMotor.stopMotor());
  }

  public void periodic() {
    //SmartDashboard stuff
    SmartDashboard.putNumber("Flywheel speed (rpm)", this.encoder.getVelocity());
    SmartDashboard.putBoolean("Flywheel is at velocity setpoint", this.pidController.isAtSetpoint());
    SmartDashboard.putBoolean("Flywheel is stoped", this.stoped);
  }
}