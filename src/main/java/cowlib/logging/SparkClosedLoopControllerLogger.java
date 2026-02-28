// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package cowlib.logging;

import com.revrobotics.spark.SparkClosedLoopController;

import edu.wpi.first.epilogue.CustomLoggerFor;
import edu.wpi.first.epilogue.logging.ClassSpecificLogger;
import edu.wpi.first.epilogue.logging.EpilogueBackend;

/** Add your docs here. */
@CustomLoggerFor(SparkClosedLoopController.class)
public class SparkClosedLoopControllerLogger extends ClassSpecificLogger<SparkClosedLoopController> {
  public SparkClosedLoopControllerLogger() {
    super(SparkClosedLoopController.class);
  }

  @Override
  protected void update(EpilogueBackend backend, SparkClosedLoopController controller) {
    backend.log("Setpoint", controller.getSetpoint());
    backend.log("I Accumulator", controller.getIAccum());
    backend.log("Control Type", controller.getControlType());
    backend.log("Slot", controller.getSelectedSlot());
    backend.log("At Setpoint", controller.isAtSetpoint());
  }
}
