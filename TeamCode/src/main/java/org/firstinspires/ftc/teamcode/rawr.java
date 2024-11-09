package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp(name = "rawr (Blocks to Java)")
public class rawr extends LinearOpMode {

  private ColorSensor color_sensor;

  /**
   * This function is executed when this OpMode is selected from the Driver Station.
   */
  @Override
  public void runOpMode() {
    double count = 0;

    color_sensor = hardwareMap.get(ColorSensor.class, "color_sensorAsColorSensor");

    // Put initialization blocks here.
    waitForStart();
    if (opModeIsActive()) {
      // Put run blocks here.
      telemetry.addLine("testttt");
      telemetry.update();
      while (opModeIsActive()) {
        telemetry.addData("Red: ", color_sensor.red());
        telemetry.addData("Green: ", color_sensor.green());
        telemetry.addData("Blue: ", color_sensor.blue());
        count += 1;
        telemetry.addData("count: ", count);
        telemetry.update();
      }
    }
  }
}
