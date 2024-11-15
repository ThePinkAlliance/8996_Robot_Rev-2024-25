package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoArmClawERW (Blocks to Java)")
@Disabled
public class ServoArmClawERW extends LinearOpMode {

  private Servo claw_servo;
  private Servo arm_servo;

  /**
   * This sample contains code to move an arm servo and a claw servo
   */
  @Override
  public void runOpMode() {
    double claw_servo_speed;
    double arm_servo_speed;

    claw_servo = hardwareMap.get(Servo.class, "claw_servo");
    arm_servo = hardwareMap.get(Servo.class, "arm_servoAsServo");

    // Put initialization blocks here.
    // Initialize Claw Servo Position
    claw_servo.setPosition(0);
    claw_servo_speed = 0.01;
    // Initialize Arm Servo Position
    arm_servo.setPosition(0);
    arm_servo_speed = 0.002;
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        // Put loop blocks here.
        // Use gamepaed X and B to open and close claw servo
        if (gamepad1.b) {
          claw_servo.setPosition(claw_servo.getPosition() + claw_servo_speed);
        }
        if (gamepad1.x) {
          claw_servo.setPosition(claw_servo.getPosition() - claw_servo_speed);
        }
        // Keep Claw servo position in valid range
        claw_servo.setPosition(Math.min(Math.max(claw_servo.getPosition(), 0), 0.4));
        // Use gamepaed A and X to raise and lower arm
        if (gamepad1.a) {
          arm_servo.setPosition(arm_servo.getPosition() + arm_servo_speed);
        }
        if (gamepad1.y) {
          arm_servo.setPosition(arm_servo.getPosition() - arm_servo_speed);
        }
        // Keep Arm servo position in valid range
        arm_servo.setPosition(Math.min(Math.max(arm_servo.getPosition(), 0), 1));
        telemetry.addData("arm servo position", arm_servo.getPosition());
        telemetry.addData("Claw Servo Position", claw_servo.getPosition());
        telemetry.update();
      }
    }
  }
}
