package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "mecanumdrivemiscERW (Blocks to Java)")
public class mecanumdrivemiscERW extends LinearOpMode {

  private Servo claw_servo;
  private DcMotor frontRightMotor;
  private DcMotor backRightMotor;
  private DcMotor rotate_arm_motor;
  private DcMotor frontLeftMotor;
  private DcMotor backLeftMotor;
  private DcMotor raise_arm_motor;
  private DcMotor extend_arm_motor;

  double claw_servo_speed;

  /**
   * This function is executed when this Op Mode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    claw_servo = hardwareMap.get(Servo.class, "claw_servo");
    frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
    backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
    rotate_arm_motor = hardwareMap.get(DcMotor.class, "rotate_arm_motor");
    frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
    backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
    raise_arm_motor = hardwareMap.get(DcMotor.class, "raise_arm_motor");
    extend_arm_motor = hardwareMap.get(DcMotor.class, "extend_arm_motor");

    // Put initialization blocks here
    // Initiaize Claw Servo Position
    claw_servo.setPosition(0);
    claw_servo_speed = 0.01;
    // Initialize Arm servo position
    // Reverse the right side motors.  This may be wrong for your setup.
    // If your robot moves backwards when commanded to go forwards, reverse the left side instead.
    frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
    backRightMotor.setDirection(DcMotor.Direction.REVERSE);
    waitForStart();
    while (opModeIsActive()) {
      // Put loop blocks here
      move_claw();
      move_robot();
      extend_arm();
      raise_arm();
      rotate_arm();
      telemetry.addData("arm pos", 123);
      telemetry.addData("claw pos", claw_servo.getPosition());
      telemetry.update();
    }
  }

  /**
   * Describe this function...
   */
  private void move_claw() {
    // Use gamepad X and B to open and close claw servo
    if (gamepad2.b) {
      claw_servo.setPosition(claw_servo.getPosition() + claw_servo_speed);
    }
    if (gamepad2.x) {
      claw_servo.setPosition(claw_servo.getPosition() - claw_servo_speed);
    }
    // Keep claw servo position in valid range
    claw_servo.setPosition(Math.min(Math.max(claw_servo.getPosition(), 0), 0.4));
  }

  /**
   * Describe this function...
   */
  private void rotate_arm() {
    // Use gamepad A and Y to rasie and lower arm
    if (gamepad2.a) {
      rotate_arm_motor.setPower(1);
    } else if (gamepad2.y) {
      rotate_arm_motor.setPower(-1);
    } else {
      rotate_arm_motor.setPower(0);
    }
    // Keep arm servo position in valid range
  }

  /**
   * Describe this function...
   */
  private void move_robot() {
    float y;
    double x;
    float rx;
    double denominator;

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

  /**
   * Describe this function...
   */
  private void raise_arm() {
    // Use gamepad DpadUp to rasie and DpadDown to lower arm
    if (gamepad2.dpad_up) {
      raise_arm_motor.setPower(1);
    } else if (gamepad2.dpad_down) {
      raise_arm_motor.setPower(-1);
    } else {
      raise_arm_motor.setPower(0);
    }
    // Keep arm servo position in valid range
  }

  /**
   * Describe this function...
   */
  private void extend_arm() {
    // Use gamepad DpadRight to extend and DpadLeft retract arm
    if (gamepad2.dpad_left) {
      extend_arm_motor.setPower(1);
    } else if (gamepad2.dpad_right) {
      extend_arm_motor.setPower(-1);
    } else {
      extend_arm_motor.setPower(0);
    }
    // Keep arm servo position in valid range
  }
}
