package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.ColorSensor;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "ColorSensorTest", group = "Auto")
public class ColorSensorTest extends LinearOpMode {

    private DcMotor leftMotor;
    private DcMotor rightMotor;
    private ColorSensor colorSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        // Initialize hardware
        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        colorSensor = hardwareMap.get(ColorSensor.class, "colorSensor");

        // Reverse one motor to ensure the robot moves correctly
        leftMotor.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the start button
        waitForStart();

        while (opModeIsActive()) {
            // Check if the sensor detects blue
            if (colorSensor.blue() > colorSensor.red() && colorSensor.blue() > colorSensor.green()) {
                telemetry.addData("Color Detected", "Blue");
                telemetry.update();

                // Perform a 90-degree turn
                turn90Degrees();
                break; // Exit loop after turning
            } else {
                telemetry.addData("Color Detected", "Not Blue");
                telemetry.update();
            }
        }
    }

    private void turn90Degrees() {
        // Set power for turning
        leftMotor.setPower(0.5);
        rightMotor.setPower(-0.5);

        // Adjust the duration to achieve a 90-degree turn (depends on your robot)
        sleep(500); // Example duration, adjust for your robot's turning speed

        // Stop the motors
        leftMotor.setPower(0);
        rightMotor.setPower(0);
    }
}
