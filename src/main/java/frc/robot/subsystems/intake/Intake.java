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

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.epilogue.Logged;

@Logged
public class Intake extends SubsystemBase {
  private SparkMax intakeMotor = new SparkMax(IntakeConstants.intakeMotorID, MotorType.kBrushless);
  private SparkFlex expansionMotor = new SparkFlex(IntakeConstants.expansionMotorID, MotorType.kBrushless);


  //boolean toggle value
   private boolean isExtended = false;


  //init CANcoder for expansion motor
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

  public void stopIntake() {
    intakeMotor.stopMotor();
  }

  public void expand() {
    isExtended = true;
  }

  public void contract() {
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
    
  }
}