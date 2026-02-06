package frc.robot.subsystems.drivebase;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
    private SparkMax flyWhealMotor = new SparkMax(ShooterConstants.flyWhealMotorID, MotorType.kBrushless);
    private SparkMax indexerMotor = new SparkMax(ShooterConstants.indexerMotorID, MotorType.kBrushless);

    public Shooter() {
        final SparkMaxConfig flyWhealMotorConfig = new SparkMaxConfig();
        final SparkMaxConfig indexerMotorConfig = new SparkMaxConfig();
        
        flyWhealMotorConfig.idleMode(IdleMode.kCoast);
        indexerMotorConfig.idleMode(IdleMode.kBrake);

        flyWhealMotorConfig.inverted(false);
        indexerMotorConfig.inverted(false);

        flyWhealMotor.configure(flyWhealMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        indexerMotor.configure(indexerMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void spinUpFlyWheal() {
        flyWhealMotor.set(ShooterConstants.flyWhealMaxSpeed);
    }

    public void slowFlyWheal() {
        flyWhealMotor.set(0.0);
    }

    public void stopFlyWheal() {
        flyWhealMotor.stopMotor();
    }

    public Command feedBalls(double speed) {
        
        return this.run(()-> indexerMotor.set(speed)).finallyDo(()-> indexerMotor.stopMotor());
    }
}
