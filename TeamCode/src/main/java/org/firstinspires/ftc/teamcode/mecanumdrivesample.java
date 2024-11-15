package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "mecanumdrivesample (Blocks to Java)")
@Disabled
public class mecanumdrivesample extends LinearOpMode {

  private DcMotor frontRightMotor;
  private DcMotor backRightMotor;
  private DcMotor frontLeftMotor;
  private DcMotor backLeftMotor;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    float y;
    double x;
    float rx;
    double denominator;

    frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
    backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
    frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
    backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");

    // Reverse the right side motors.  This may be wrong for your setup.
    // If your robot moves backwards when commanded to go forwards, reverse the left side instead.
    frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
    backRightMotor.setDirection(DcMotor.Direction.REVERSE);
    waitForStart();
    while (opModeIsActive()) {
      // Remember, Y stick value is reversed
      y = -gamepad1.left_stick_y;
      // Factor to counteract imperfect strafing
      x = gamepad1.left_stick_x * 1.1;
      rx = gamepad1.right_stick_x;
      // Denominator is the largest motor power (absolute value) or 1.
      // This ensures all powers maintain the same ratio, but only if one is outside of the range [-1, 1].
      denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
      // Make sure your ID's match your configuration
      frontLeftMotor.setPower((y + x + rx) / denominator);
      backLeftMotor.setPower(((y - x) + rx) / denominator);
      frontRightMotor.setPower(((y - x) - rx) / denominator);
      backRightMotor.setPower(((y + x) - rx) / denominator);
    }
  }
}
