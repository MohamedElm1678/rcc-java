/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  	// Motor controllers
	Spark leftMaster;
	Spark leftSlave; // Comment (ctl + /) this line if using PWM Y-Wire, or only using 1 motor on left side
	Spark rightMaster;
	Spark rightSlave; // See above
	
	SpeedControllerGroup leftGearbox;
	SpeedControllerGroup rightGearbox;
	DifferentialDrive drive;
	
	double autoWaitTime;
	double autoDriveTime;

  // Controller

    RobotDrive myRobot;  // class that handles basic drive operations
    Joystick leftStick;  // set to ID 1 in DriverStation
    Joystick rightStick; // set to ID 2 in DriverStation

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
  
    // Define the motor controllers for the left gearbox
		leftMaster = new Spark(0); // Make sure these are the same as the PWM wires in the RoboRIO, and that you are using SPARKs, if different motors change definition
		leftSlave = new Spark(1);
		leftGearbox = new SpeedControllerGroup(leftMaster, leftSlave);
		
		// Right gearbox
		rightMaster = new Spark(2);
		rightSlave = new Spark(3);
		rightGearbox = new SpeedControllerGroup(rightMaster, rightSlave);
		
		drive = new DifferentialDrive(leftGearbox, rightGearbox);
    SmartDashboard.putData("Auto choices", m_chooser);

    // Controllers / driving
      myRobot = new RobotDrive(0, 1);
      myRobot.setExpiration(0.1);
    
      leftStick = new Joystick(0);
      rightStick = new Joystick(1);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
      double timeElapsed = 15 - DriverStation.getInstance().getMatchTime(); // The DriverStation gives an approximate time until the end of the period
          
          if (timeElapsed >= autoWaitTime) {
            if (timeElapsed <= autoWaitTime + autoDriveTime) {
              drive.tankDrive(0.2, 0.2); // Left and Right speeds, 20% power
            }
          }
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    myRobot.setSafetyEnabled(true);
          // change parameters below if they are using different controller layout
        	myRobot.tankDrive(leftStick.getY(), rightStick.getY());
    }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
