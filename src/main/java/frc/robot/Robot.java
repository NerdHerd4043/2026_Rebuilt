// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.epilogue.Epilogue;
import edu.wpi.first.epilogue.Logged;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;



@Logged
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;

  public Robot() {
    DataLogManager.start();
    DriverStation.startDataLog(DataLogManager.getLog());
    Epilogue.bind(this);




//  // Record metadata
//     Logger.recordMetadata("ProjectName", BuildConstants.MAVEN_NAME);
//     Logger.recordMetadata("BuildDate", BuildConstants.BUILD_DATE);
//     Logger.recordMetadata("GitSHA", BuildConstants.GIT_SHA);
//     Logger.recordMetadata("GitDate", BuildConstants.GIT_DATE);
//     Logger.recordMetadata("GitBranch", BuildConstants.GIT_BRANCH);
//     Logger.recordMetadata(
//         "GitDirty",
//         switch (BuildConstants.DIRTY) {
//           case 0 -> "All changes committed";
//           case 1 -> "Uncommitted changes";
//           default -> "Unknown";
//         });

//     // Set up data receivers & replay source
//     switch (Constants.currentMode) {
//       case REAL:
//         // Running on a real robot, log to a USB stick ("/U/logs")
//         Logger.addDataReceiver(new WPILOGWriter());
//         Logger.addDataReceiver(new NT4Publisher());
//         break;

//       case SIM:
//         // Running a physics simulator, log to NT
//         Logger.addDataReceiver(new NT4Publisher());
//         break;

//       case REPLAY:
//         // Replaying a log, set up replay source
//         setUseTiming(false); // Run as fast as possible
//         String logPath = LogFileUtil.findReplayLog();
//         Logger.setReplaySource(new WPILOGReader(logPath));
//         Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
//         break;
//     }

//     // Initialize URCL
//     //Logger.registerURCL(URCL.startExternal()); //its erorring and failing to simulate -Michael

//     StatusLogger.disableAutoLogging(); // Disable REVLib's built-in logging

//     // Start AdvantageKit logger
//     Logger.start();


    //INSTANCE IT
    m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      CommandScheduler.getInstance().schedule(m_autonomousCommand);
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}
}
