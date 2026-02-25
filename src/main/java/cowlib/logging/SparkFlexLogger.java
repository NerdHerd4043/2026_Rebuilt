// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package cowlib.logging;

import com.revrobotics.spark.SparkFlex;

import edu.wpi.first.epilogue.CustomLoggerFor;
import edu.wpi.first.epilogue.logging.ClassSpecificLogger;
import edu.wpi.first.epilogue.logging.EpilogueBackend;

/** Add your docs here. */
@CustomLoggerFor(SparkFlex.class)
public class SparkFlexLogger extends ClassSpecificLogger<SparkFlex> {
  public SparkFlexLogger() {
    super(SparkFlex.class);
  }

  @Override
  protected void update(EpilogueBackend backend, SparkFlex motor) {
    backend.log("Applied Output (Duty Cycle)", motor.getAppliedOutput());
    backend.log("Output Current (A)", motor.getOutputCurrent());
    backend.log("Temperature (C)", motor.getMotorTemperature());
  }
}
