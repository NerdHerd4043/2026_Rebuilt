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

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.epilogue.Logged;

@Logged
public class Intake extends SubsystemBase {
  private SparkMax intakeMotor = new SparkMax(IntakeConstants.intakeMotorID, MotorType.kBrushless);
  private SparkFlex expansionMotor = new SparkFlex(IntakeConstants.expansionMotorID, MotorType.kBrushless);

  private enum ExpansionPositions {
    INTAKE, SHOOTING, REST
  };

  private ExpansionPositions expansionPosition = ExpansionPositions.REST;

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

  public void setToIntakePos() {
    expansionPosition = ExpansionPositions.INTAKE;
  }

  public void setToShootPos() {
    expansionPosition = ExpansionPositions.SHOOTING;
  }

  public void stopExpansion() {
    expansionMotor.set(0);
  }

  // auto commands
  public Command autoDropIntake() {

    Command pullIntake = new RunCommand(this::setToShootPos).withTimeout(0.25);
    Command waitCommand = Commands.waitSeconds(0.5);
    Command dropIntake = new RunCommand(this::setToIntakePos).withTimeout(5.25);
    return Commands.sequence(
        pullIntake,
        waitCommand,
        dropIntake);

  }

  // expansion CANCoder functions
  public double getEncoder() {
    return expansionCoder.getAbsolutePosition().getValueAsDouble() * 360.0;
  }

  public double getEncoderRadians() {
    return Units.degreesToRadians(getEncoder());
  }

  // ticking function
  @Override
  public void periodic() {
    if (expansionPosition == ExpansionPositions.INTAKE) {
      if (getEncoderRadians() < IntakeConstants.intakePos) {
        expansionMotor.set(IntakeConstants.expansionSpeed);
      } else if (getEncoderRadians() > IntakeConstants.intakePos) {
        expansionMotor.set(-IntakeConstants.expansionSpeed);
      } else {
        expansionMotor.stopMotor();
      }
    } else if (expansionPosition == ExpansionPositions.SHOOTING) {
      if (getEncoderRadians() < IntakeConstants.shootPos) {
        expansionMotor.set(IntakeConstants.expansionSpeed);
      } else if (getEncoderRadians() > IntakeConstants.shootPos) {
        expansionMotor.set(-IntakeConstants.expansionSpeed);
      } else {
        expansionMotor.stopMotor();
      }
    }
  }
}
