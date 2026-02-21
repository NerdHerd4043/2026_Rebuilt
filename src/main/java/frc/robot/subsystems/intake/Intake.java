package frc.robot.subsystems.intake;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.ctre.phoenix6.hardware.CANcoder;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.util.Units;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.intake.IntakeConstants.ExpansionFF;
import frc.robot.subsystems.intake.IntakeConstants.ExpansionPID;
import edu.wpi.first.epilogue.Logged;

@Logged
public class Intake extends SubsystemBase {
  private SparkMax intakeMotor = new SparkMax(IntakeConstants.intakeMotorID, MotorType.kBrushless);
  private SparkMax expansionMotor = new SparkMax(IntakeConstants.expansionMotorID, MotorType.kBrushless);


  //boolean toggle value
   private boolean isExtended = false;


  //init CANcoder for expansion motor
  private CANcoder expansionCoder = new CANcoder(IntakeConstants.expansionEncoderID);

  private ArmFeedforward feedforward = new ArmFeedforward(ExpansionFF.s,
      ExpansionFF.g,
      ExpansionFF.v);

  private ProfiledPIDController pidController = new ProfiledPIDController(ExpansionPID.p, ExpansionPID.i, ExpansionPID.d,
     new TrapezoidProfile.Constraints(6, 5));

  public Intake() {
    final SparkMaxConfig intakeMotorConfig = new SparkMaxConfig();
    final SparkMaxConfig expansionMotorConfig = new SparkMaxConfig();

    intakeMotorConfig.idleMode(IdleMode.kBrake);
    expansionMotorConfig.idleMode(IdleMode.kBrake);

    intakeMotorConfig.inverted(false);
    expansionMotorConfig.inverted(false);

    intakeMotor.configure(intakeMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    expansionMotor.configure(expansionMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    this.pidController.setGoal(getEncoderRadians());
  }

  public void intake() {
    intakeMotor.set(IntakeConstants.intakeSpeed);
  }

  public void stopIntake() {
    intakeMotor.stopMotor();
  }
  
  public void expand() {
    this.pidController.setGoal(IntakeConstants.outPos);
    isExtended = true;
  }

  public void contract() {
    this.pidController.setGoal(IntakeConstants.inPos);
    isExtended = false;
  }

  public void toggleintake(){
    if(isExtended){
    contract();
    }
    else{
    expand();
    }
    
  }

//expansion CANCoder functions
  public double getEncoder() {
    return expansionCoder.getAbsolutePosition().getValueAsDouble() * 360.0;
  }

  public double getEncoderRadians() {
    return Units.degreesToRadians(getEncoder());
  }

  //ticking function
  @Override
  public void periodic() {
    double ffOutput = -feedforward.calculate((pidController.getSetpoint()).position, this.pidController.getSetpoint().velocity);
    expansionMotor.setVoltage(ffOutput - this.pidController.calculate(getEncoderRadians()));
  }
}