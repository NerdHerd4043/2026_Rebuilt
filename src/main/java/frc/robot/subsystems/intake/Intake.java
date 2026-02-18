package frc.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.ctre.phoenix6.hardware.CANcoder;
import edu.wpi.first.math.util.Units;


import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.epilogue.Logged;

@Logged
public class Intake extends SubsystemBase {
  private SparkFlex intakeMotor = new SparkFlex(IntakeConstants.intakeMotorID, MotorType.kBrushless);
  private SparkFlex expansionMotor = new SparkFlex(IntakeConstants.expansionMotorID, MotorType.kBrushless);

  //init CANcoder for expansion motor
  private CANcoder expansionCoder = new CANcoder(IntakeConstants.expansionCoderID);
  private double expansionCoderRadians;


  private SparkClosedLoopController pidController;

  public Intake() {
    final SparkFlexConfig intakeMotorConfig = new SparkFlexConfig();
    final SparkFlexConfig expansionMotorConfig = new SparkFlexConfig();

    intakeMotorConfig.idleMode(IdleMode.kBrake);
    expansionMotorConfig.idleMode(IdleMode.kBrake);

    intakeMotorConfig.inverted(false);
    expansionMotorConfig.inverted(false);

    // set PID coeffecients
    expansionMotorConfig.closedLoop.p(IntakeConstants.ExpansionPID.p)
      .i(IntakeConstants.ExpansionPID.i)
      .d(IntakeConstants.ExpansionPID.d)
      .maxOutput(IntakeConstants.ExpansionPID.maxOutput)
      .minOutput(IntakeConstants.ExpansionPID.minOutput);

    expansionMotorConfig.closedLoop.feedForward.kS(IntakeConstants.ExpansionFF.s)
      .kV(IntakeConstants.ExpansionFF.v);

    intakeMotor.configure(intakeMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    expansionMotor.configure(expansionMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    pidController = expansionMotor.getClosedLoopController();

  }

//ticking function
 @Override
  public void periodic() {


  this.expansionCoderRadians = getEncoderRadians();

  }

  public void intake() {
    intakeMotor.set(IntakeConstants.intakeSpeed);
  }

  public void stopIntake() {
    intakeMotor.stopMotor();
  }





  
  public void expand() {
    pidController.setSetpoint(IntakeConstants.expansionSetPoint, ControlType.kPosition);
  }

  public void contract() {
    pidController.setSetpoint(IntakeConstants.startingSetPoint, ControlType.kPosition);
  }



//expansion CANCoder functions
  public double getEncoder() {
    return expansionCoder.getAbsolutePosition().getValueAsDouble() * 360.0;
  }

  public double getEncoderRadians() {
    return Units.degreesToRadians(getEncoder());
  }


}
