package frc.robot.subsystems.drivebase;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drivebase.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
    private SparkMax intakeMotor = new SparkMax(IntakeConstants.intakeMotorID, MotorType.kBrushless);
    private SparkMax expantionMotor = new SparkMax(IntakeConstants.expantionMotorID, MotorType.kBrushless);

    public Intake() {
        final SparkMaxConfig intakeMotorConfig = new SparkMaxConfig();
        final SparkMaxConfig expantionMotorConfig = new SparkMaxConfig();

        intakeMotorConfig.idleMode(IdleMode.kBrake);
        expantionMotorConfig.idleMode(IdleMode.kBrake);

        intakeMotorConfig.inverted(false);
        expantionMotorConfig.inverted(false);

        intakeMotor.configure(intakeMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        expantionMotor.configure(expantionMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    }

    public void intake() {
        intakeMotor.set(IntakeConstants.intakeSpeed);
    }

    public void stopIntake() {
        intakeMotor.stopMotor();
    }

    public void expand() {

    }

    public void retraked() {
        
    }

}
