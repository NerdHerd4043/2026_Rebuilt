package frc.robot.subsystems.drivebase;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.drivebase.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {
    private SparkMax flyWhealMotor = new SparkMax(ShooterConstants.flyWhealMotorID, MotorType.kBrushless);
    private SparkMax indexerMotor = new SparkMax(ShooterConstants.indexerMotorID, MotorType.kBrushless);

    private final SparkMaxConfig flyWhealMotorConfig = new SparkMaxConfig();
    private final SparkMaxConfig indexerMotorConfig = new SparkMaxConfig();

    public Shooter() {
        flyWhealMotorConfig.idleMode(IdleMode.kCoast);
        indexerMotorConfig.idleMode(IdleMode.kBrake);

        flyWhealMotorConfig.inverted(true);
        indexerMotorConfig.inverted(true);

        flyWhealMotor.configure(flyWhealMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        indexerMotor.configure(indexerMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void spinUpFlyWheal() {
        flyWhealMotor.set(ShooterConstants.flyWhealMaxSpeed);
    }

    public void slowFlyWheal() {
        flyWhealMotor.stopMotor();
    }

    //there is proble a beter way to do this but i dont know how -Michael
    public void emergencyStop() {
        flyWhealMotorConfig.idleMode(IdleMode.kBrake);
        flyWhealMotor.configure(flyWhealMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        flyWhealMotor.stopMotor();

        flyWhealMotorConfig.idleMode(IdleMode.kCoast);
        flyWhealMotor.configure(flyWhealMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public Command feedBalls() {
        
        return this.run(()-> indexerMotor.set(ShooterConstants.feedSpeed)).finallyDo(()-> indexerMotor.stopMotor());
    }
}
