package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "ArmMotorERW (Blocks to Java)")
public class ArmMotorERW extends LinearOpMode {

  private DcMotor arm;

  /**
   * This sample contains the bare minimum Blocks for any regular OpMode. The 3 blue
   * Comment Blocks show where to place Initialization code (runs once, after touching the
   * DS INIT button, and before touching the DS Start arrow), Run code (runs once, after
   * touching Start), and Loop code (runs repeatedly while the OpMode is active, namely not
   * Stopped).
   */
  @Override
  @Disabled
  public void runOpMode() {
    double Speed;

    arm = hardwareMap.get(DcMotor.class, "armAsDcMotor");

    // Put initialization blocks here.
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      while (opModeIsActive()) {
        // Put loop blocks here.
        if (gamepad1.b) {
          Speed = 0.1;
        } else {
          Speed = 1;
        }
        arm.setPower(gamepad1.left_stick_y * Speed);
        telemetry.addData("arm pow", gamepad1.left_stick_y);
        telemetry.update();
      }
    }
  }
}
