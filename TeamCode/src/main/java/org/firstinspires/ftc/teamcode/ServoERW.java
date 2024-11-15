package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "ServoERW (Blocks to Java)")
@Disabled
public class ServoERW extends LinearOpMode {

  private Servo left_hand;

  /**
   * This sample OpMode shows how to operate a conventional servo at a smooth, controlled rate.
   */
  @Override
  public void runOpMode() {
    double ServoPosition;
    double ServoSpeed;

    left_hand = hardwareMap.get(Servo.class, "left_handAsServo");

    // Set servo to mid position
    ServoPosition = 0.5;
    ServoSpeed = 0.01;
    waitForStart();
    while (opModeIsActive()) {
      // Use gamepad X and B to open close servo
      if (gamepad1.x) {
        ServoPosition += ServoSpeed;
      }
      if (gamepad1.b) {
        ServoPosition += -ServoSpeed;
      }
      // Keep Servo position in valid range
      ServoPosition = Math.min(Math.max(ServoPosition, 0), 1);
      left_hand.setPosition(ServoPosition);
      telemetry.addData("Servo", ServoPosition);
      telemetry.update();
      sleep(20);
    }
  }
}
