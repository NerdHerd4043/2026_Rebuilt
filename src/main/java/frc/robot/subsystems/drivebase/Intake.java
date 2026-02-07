package frc.robot.subsystems.drivebase;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drivebase.Constants.IntakeConstants;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Intake extends SubsystemBase {
    private SparkFlex intakeMotor = new SparkFlex(IntakeConstants.intakeMotorID, MotorType.kBrushless);
    private SparkFlex expansionMotor = new SparkFlex(IntakeConstants.expansionMotorID, MotorType.kBrushless);

    public Intake() {
        final SparkFlexConfig intakeMotorConfig = new SparkFlexConfig();
        final SparkFlexConfig expansionMotorConfig = new SparkFlexConfig();

        intakeMotorConfig.idleMode(IdleMode.kBrake);
        expansionMotorConfig.idleMode(IdleMode.kBrake);

        intakeMotorConfig.inverted(false);
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

    }

    public void contract() {
        
    }

}
