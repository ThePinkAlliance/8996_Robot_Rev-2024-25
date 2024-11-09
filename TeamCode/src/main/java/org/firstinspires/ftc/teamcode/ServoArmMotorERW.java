package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "ServoArmMotorERW (Blocks to Java)")
public class ServoArmMotorERW extends LinearOpMode {

  private DcMotor arm;

  /**
   * This sample contains the bare minimum Blocks for any regular OpMode. The 3 blue
   * Comment Blocks show where to place Initialization code (runs once, after touching the
   * DS INIT button, and before touching the DS Start arrow), Run code (runs once, after
   * touching Start), and Loop code (runs repeatedly while the OpMode is active, namely not
   * Stopped).
   */
  @Override
  public void runOpMode() {
    double ServoPosition;
    double ServoSpeed;
    double Arm_Motor_Speed;

    arm = hardwareMap.get(DcMotor.class, "armAsDcMotor");

    // Put initialization blocks here.
    // Initialize Servo Position
    ServoPosition = 0.5;
    ServoSpeed = 0.01;
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        // Put loop blocks here.
        // Use gamepaed X and B to open and close servo
        if (gamepad1.b) {
          ServoPosition += ServoSpeed;
        }
        if (gamepad1.x) {
          ServoPosition += -ServoSpeed;
        }
        // Use gamepaed A to slow arm motor speed
        if (gamepad1.a) {
          Arm_Motor_Speed = 0.4;
        } else {
          Arm_Motor_Speed = 1;
        }
        arm.setPower(gamepad1.left_stick_y * Arm_Motor_Speed);
        telemetry.addData("arm motor speed", gamepad1.left_stick_y);
        telemetry.addData("Servo Position", ServoPosition);
        telemetry.update();
      }
    }
  }
}
