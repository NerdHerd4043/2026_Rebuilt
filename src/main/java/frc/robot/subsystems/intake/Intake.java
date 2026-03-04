package frc.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.ctre.phoenix6.hardware.CANcoder;

import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.epilogue.Logged;

@Logged
public class Intake extends SubsystemBase {
  private SparkMax intakeMotor = new SparkMax(IntakeConstants.intakeMotorID, MotorType.kBrushless);
  private SparkFlex expansionMotor = new SparkFlex(IntakeConstants.expansionMotorID, MotorType.kBrushless);

  public enum ExpansionPositions {
    REST, EXTENDED
  };

  public ExpansionPositions expansionPosition = ExpansionPositions.REST;

  // init CANcoder for expansion motor
  private CANcoder expansionCoder = new CANcoder(IntakeConstants.expansionEncoderID);

  public Intake() {
    final SparkMaxConfig intakeMotorConfig = new SparkMaxConfig();
    final SparkFlexConfig expansionMotorConfig = new SparkFlexConfig();

    intakeMotorConfig.idleMode(IdleMode.kBrake);
    expansionMotorConfig.idleMode(IdleMode.kBrake);

    intakeMotorConfig.inverted(true);
    expansionMotorConfig.inverted(false);

    intakeMotor.configure(intakeMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    expansionMotor.configure(expansionMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

  }

  public void intake() {
    intakeMotor.set(IntakeConstants.intakeSpeed);
  }

  public void reveseIntake() {
    intakeMotor.set(-IntakeConstants.intakeSpeed);
  }

  public void stopIntake() {
    intakeMotor.stopMotor();
  }

  public void stopExpansion() {
    expansionMotor.stopMotor();
  }

  // auto commands
  // TODO: fix timings?
  public Command autoDropIntake() {
    Command pullIntake = this.runEnd(this::moveExpansionUp, this.expansionMotor::stopMotor).withTimeout(0.3);
    Command waitCommand = Commands.waitSeconds(0.5);
    Command dropIntake = this.runEnd(this::moveExpansionDown, this.expansionMotor::stopMotor).withTimeout(0.25);
    Command autoDrop = Commands.sequence(
        this.runOnce(() -> {
          this.expansionPosition = ExpansionPositions.EXTENDED;
        }),
        pullIntake,
        waitCommand,
        dropIntake);

    return Commands.either(
        autoDrop,
        Commands.none(),
        () -> this.expansionPosition != ExpansionPositions.EXTENDED);
  }

  // expansion CANCoder functions
  public double getEncoder() {
    return expansionCoder.getAbsolutePosition().getValue().in(Units.Degrees);
  }

  public double getEncoderRadians() {
    return expansionCoder.getAbsolutePosition().getValue().in(Units.Radians);
  }

  private void moveExpansionUp() {
    this.expansionMotor.set(-IntakeConstants.expansionSpeed);
  }

  private void moveExpansionDown() {
    this.expansionMotor.set(IntakeConstants.expansionSpeed);
  }

  public Command raiseExpansion() {
    return this.runEnd(() -> {
      if (this.getEncoderRadians() > IntakeConstants.shootPos) {
        this.moveExpansionUp();
      }
    }, () -> {
      this.expansionMotor.stopMotor();
    });
  }

  public Command lowerExpansion() {
    return this.runEnd(() -> {
      if (this.getEncoderRadians() < IntakeConstants.intakePos) {
        this.moveExpansionDown();
      }
    }, () -> {
      this.expansionMotor.stopMotor();
    });
  }

  public Command intakeCommand = Commands.runEnd(() -> {
    this.intake();
    this.lowerExpansion();
  }, () -> {
    stopIntake();
  });

  // ticking function
  @Override
  public void periodic() {
    // switch (expansionPosition) {
    // case INTAKE:
    // if (getEncoderRadians() < IntakeConstants.intakePos) {
    // expansionMotor.set(IntakeConstants.expansionSpeed);
    // } else if (getEncoderRadians() > IntakeConstants.intakePos) {
    // expansionMotor.set(-IntakeConstants.expansionSpeed);
    // } else {
    // expansionMotor.stopMotor();
    // }
    // break;
    // case SHOOTING:
    // if (getEncoderRadians() < IntakeConstants.shootPos) {
    // expansionMotor.set(IntakeConstants.expansionSpeed);
    // } else if (getEncoderRadians() > IntakeConstants.shootPos) {
    // expansionMotor.set(-IntakeConstants.expansionSpeed);
    // } else {
    // expansionMotor.stopMotor();
    // }
    // break;
    // default:
    // }

    SmartDashboard.putNumber("Intake expansion encoder (degrees)", getEncoder());
    SmartDashboard.putNumber("Intake expansion encoder (radians)", getEncoderRadians());
  }
}
