package frc.robot.subsystems.shooter;

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
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj2.command.Commands;

@Logged
public class Shooter extends SubsystemBase {
  private SparkFlex flyWheelMotor = new SparkFlex(ShooterConstants.flyWheelMotorID, MotorType.kBrushless);
  private SparkMax indexerMotor = new SparkMax(ShooterConstants.indexerMotorID, MotorType.kBrushless);
  private SparkFlex disrupterMotor = new SparkFlex(ShooterConstants.disrupterMotorID, MotorType.kBrushless);

  private final SparkClosedLoopController pidController;
  private final RelativeEncoder encoder;

  public Shooter() {
    // Flywheel motor config
    // @formatter:off
    final SparkFlexConfig flyWheelMotorConfig = new SparkFlexConfig();
    flyWheelMotorConfig
        .idleMode(IdleMode.kCoast)
        .inverted(true)
        .closedLoop
          .p(ShooterConstants.FlyWheelPID.p)
          .i(ShooterConstants.FlyWheelPID.i)
          .d(ShooterConstants.FlyWheelPID.d)
          .maxOutput(ShooterConstants.FlyWheelPID.maxOutput)
          .minOutput(ShooterConstants.FlyWheelPID.minOutput)
            .feedForward
            .kV(ShooterConstants.FlyWheelFF.v);
    // @formatter:on
    flyWheelMotor.configure(flyWheelMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    final SparkMaxConfig indexerMotorConfig = new SparkMaxConfig();
    indexerMotorConfig
        .idleMode(IdleMode.kBrake)
        .inverted(true);
    indexerMotor.configure(indexerMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    final SparkFlexConfig disrupterMotorConfig = new SparkFlexConfig();
    disrupterMotorConfig
        .idleMode(IdleMode.kCoast)
        .inverted(true);
    disrupterMotor.configure(disrupterMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    this.pidController = flyWheelMotor.getClosedLoopController();
    this.encoder = flyWheelMotor.getEncoder();
  }

  private Command spinUpFlywheel(double setpoint) {
    return this.runOnce(() -> {
      this.pidController.setSetpoint(setpoint, ControlType.kVelocity);
    });
  }

  public Command flywheelSlow() {
    return this.spinUpFlywheel(ShooterConstants.lowSetPoint);
  }

  public Command stopFlywheel() {
    return this.runOnce(() -> {
      this.pidController.setSetpoint(0, ControlType.kVoltage);
    });
  }

  public Command shootOneBall() {

    Command runFlywheel = this.flywheelSlow();
    Command waitCommand = Commands.waitSeconds(1.5);
    Command runShooter = feedBalls();
    return Commands.sequence(
        runFlywheel,
        waitCommand,
        runShooter);

  }

  // public Command ShootAndFeed() {
  // return Commands.parallel(Shoot, Feed);
  // }

  public Command feedBalls() {
    return this.runEnd(() -> {
      indexerMotor.set(ShooterConstants.indexerFeedSpeed);
      disrupterMotor.set(ShooterConstants.disrupterSpeed);
    }, () -> {
      indexerMotor.stopMotor();
      disrupterMotor.stopMotor();
    });
  }

  public Command Feed = feedBalls();
  public Command Shoot = spinUpFlywheel(ShooterConstants.lowSetPoint);

  public Command reverseIndexer() {
    return this.runEnd(() -> {
      if (!this.pidController.isAtSetpoint()) {
        indexerMotor.set(-ShooterConstants.indexerFeedSpeed);
      }
    }, () -> indexerMotor.stopMotor());
  }

  public void periodic() {
    // SmartDashboard stuff
    SmartDashboard.putNumber("Flywheel speed (rpm)", this.encoder.getVelocity());
    SmartDashboard.putBoolean("Flywheel is at velocity setpoint", this.pidController.isAtSetpoint());
    if (this.encoder.getVelocity() == 2600) {
      SmartDashboard.putBoolean("Flywheel is at velocity setpoint", true);
    } else {
      SmartDashboard.putBoolean("Flywheel is at velocity setpoint", false);
    }
  }
}