package frc.robot.subsystems.drivebase;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drivebase.Constants.ShooterConstants;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Shooter extends SubsystemBase {
    private SparkFlex flyWheelMotor = new SparkFlex(ShooterConstants.flyWheelMotorID, MotorType.kBrushless);
    private SparkFlex indexerMotor = new SparkFlex(ShooterConstants.indexerMotorID, MotorType.kBrushless);

    private final SparkFlexConfig flyWheelMotorConfig = new SparkFlexConfig();
    private final SparkFlexConfig indexerMotorConfig = new SparkFlexConfig();

    public Shooter() {
        flyWheelMotorConfig.idleMode(IdleMode.kCoast);
        indexerMotorConfig.idleMode(IdleMode.kBrake);

        flyWheelMotorConfig.inverted(true);
        indexerMotorConfig.inverted(true);

        flyWheelMotor.configure(flyWheelMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        indexerMotor.configure(indexerMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void spinUpflyWheel() {
        flyWheelMotor.set(ShooterConstants.flyWheelMaxSpeed);
    }

    public void slowflyWheel() {
        flyWheelMotor.stopMotor();
    }

    //there is proble a beter way to do this but i dont know how -Michael
    public void emergencyStop() {
        flyWheelMotorConfig.idleMode(IdleMode.kBrake);
        flyWheelMotor.configure(flyWheelMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        flyWheelMotor.stopMotor();

        flyWheelMotorConfig.idleMode(IdleMode.kCoast);
        flyWheelMotor.configure(flyWheelMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public Command feedBalls() {
        
        return this.run(()-> indexerMotor.set(ShooterConstants.feedSpeed)).finallyDo(()-> indexerMotor.stopMotor());
    }
}
