package frc.robot.subsystems.drivebase;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private SparkMax intakeMotor = new SparkMax(0, MotorType.kBrushless);
    private SparkMax expantionMotor = new SparkMax(0, MotorType.kBrushless);

    public Intake() {
        final SparkMaxConfig intakeMotorConfig = new SparkMaxConfig();
        final SparkMaxConfig expantionMotorConfig = new SparkMaxConfig();

    }

}
