// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
//This is Shmirtz code by Chris
package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private final PWMVictorSPX frontRightMotor = new PWMVictorSPX(2);
  private final PWMVictorSPX backRightMotor = new PWMVictorSPX(1);
  private final PWMVictorSPX frontLeftMotor = new PWMVictorSPX(4);
  private final PWMVictorSPX backLeftMotor = new PWMVictorSPX(0);
  private final PWMVictorSPX intakeMotor = new PWMVictorSPX(6);
  private final PWMVictorSPX launcherMotor = new PWMVictorSPX(5);
  private final PWMSparkMax flyWheelMotor = new PWMSparkMax(8);
  private final XboxController controller = new XboxController(0);

  private double intakeSpeed = 0.2;
  private double launcherSpeed = 1;
  private double flyWheelSpeed = -1;
  private double driveSpeed = 0.5;
  private UsbCamera camera;
  
  @Override
  public void robotInit() {
    // back left motor follows the front right motor
    frontRightMotor.addFollower(backRightMotor);
    frontLeftMotor.addFollower(backLeftMotor);

    camera = CameraServer.startAutomaticCapture(0);

    Shuffleboard.getTab("camera").add(camera);
  }

  @Override
  public void robotPeriodic() {
    
  }

  @Override
  public void teleopPeriodic() {
    
    //Turbo
    if (controller.getBButton()){
      driveSpeed = 1;
    }
    else{
      driveSpeed = 0.5;
    }

    //Driving
    double leftYJoystick = controller.getLeftY() * -driveSpeed;
    double rightYJoystick = controller.getRightY() * driveSpeed;
    frontRightMotor.set(rightYJoystick);
    frontLeftMotor.set(leftYJoystick);

    //Intake
    double triggerAxis = controller.getLeftTriggerAxis() - controller.getRightTriggerAxis();
    if (triggerAxis > 0.5) {
      intakeMotor.set(-intakeSpeed);
    } else if (triggerAxis < -0.5) {
      intakeMotor.set(intakeSpeed);
    } else {
      intakeMotor.stopMotor();
    }

    //Launcher
    boolean leftBumper = controller.getLeftBumper();
    boolean rightBumper = controller.getRightBumper();
    if(leftBumper) {
      flyWheelMotor.set(flyWheelSpeed);
    } else {
      flyWheelMotor.stopMotor();
    }
    if(rightBumper) {
      launcherMotor.set(launcherSpeed);
    } else if(controller.getXButton()) {
      launcherMotor.set(-launcherSpeed);
    } else {
      launcherMotor.stopMotor();
    }
    
    //Here is the start of shinanigines!
    boolean partyMode = false;
    if(partyMode == true && controller.getYButtonPressed()) {
      int timeOfParty = 0;
      while(timeOfParty < 2000) {
        frontRightMotor.set(0.5);
        timeOfParty ++;
      }
      frontRightMotor.stopMotor();
      timeOfParty = 0;
      while(timeOfParty < 2000) {
        frontLeftMotor.set(0.5);
        timeOfParty ++;
      }
      frontLeftMotor.stopMotor();
    }

  }
}
