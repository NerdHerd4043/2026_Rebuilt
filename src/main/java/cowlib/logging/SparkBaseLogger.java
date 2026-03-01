// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package cowlib.logging;

import com.revrobotics.spark.SparkBase;

import edu.wpi.first.epilogue.CustomLoggerFor;
import edu.wpi.first.epilogue.logging.ClassSpecificLogger;
import edu.wpi.first.epilogue.logging.EpilogueBackend;

/** Add your docs here. */
@CustomLoggerFor(SparkBase.class)
public class SparkBaseLogger extends ClassSpecificLogger<SparkBase> {
  public SparkBaseLogger() {
    super(SparkBase.class);
  }

  @Override
  protected void update(EpilogueBackend backend, SparkBase motor) {
    backend.log("Applied Output (Duty Cycle)", motor.getAppliedOutput());
    backend.log("Output Current (A)", motor.getOutputCurrent());
    backend.log("Temperature (C)", motor.getMotorTemperature());
    backend.log("Motor Type", motor.getMotorType());
    backend.log("Velocity (RPM)", motor.getEncoder().getVelocity());
  }
}
