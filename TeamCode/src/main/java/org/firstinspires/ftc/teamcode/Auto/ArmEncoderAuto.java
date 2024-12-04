/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

/*
 * This OpMode illustrates the concept of driving a path based on encoder counts.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: RobotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forward, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backward for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This method assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@Autonomous(name="ArmEncoderAuto", group="Auto")

public class ArmEncoderAuto extends LinearOpMode {

    /* Declare OpMode members. */
    private DcMotor         leftFrontDrive   = null;
    private DcMotor         rightFrontDrive  = null;
    private DcMotor         leftBackDrive   = null;
    private DcMotor         rightBackDrive   = null;
    private ElapsedTime     runtime = new ElapsedTime();
    private DcMotor         extendArmMotor = null;
    public DcMotor          raiseArmMotor  = null;
    public DcMotor          rotateArmMotor = null;
    // Calculate the COUNTS_PER_INCH for your specific drive train.
    // Go to your motor vendor website to determine your motor's COUNTS_PER_MOTOR_REV
    // For external drive gearing, set DRIVE_GEAR_REDUCTION as needed.
    // For example, use a value of 2.0 for a 12-tooth spur gear driving a 24-tooth spur gear.
    // This is gearing DOWN for less speed and more torque.
    // For gearing UP, use a gear ratio less than 1.0. Note this will affect the direction of wheel rotation.
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     LIFT_SPEED             = 0.6;
    static final double     EXTEND_SPEED           = 0.6;
    static final double     ROTATE_SPEED           = 0.6;
    static final double     MAX_EXTENSION = 80; //max length robot can extend arm to from initial length
    static final double     MAX_ANGLE = 25; //max angle robot can rotate arm to from initial orientation  TODO: adjust this to actual value once known
    static final double     MAX_LIFT = 20; //max distance robot can lift the arm to from initial position TODO: adjust this to actual value once known
    @Override
    public void runOpMode() {
        // Initialize the drive system variables.
        raiseArmMotor = hardwareMap.get(DcMotor.class, "raise_arm_motor");
        extendArmMotor = hardwareMap.get(DcMotor.class, "extend_arm_motor");
        rotateArmMotor = hardwareMap.get(DcMotor.class, "rotate_arm_motor");
        rotateArmMotor.setDirection(DcMotor.Direction.REVERSE);
        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Starting at",  "%7d :%7d",
                raiseArmMotor.getCurrentPosition(),
                extendArmMotor.getCurrentPosition());
        telemetry.update();
        // Wait for the game to start (driver presses START)
        waitForStart();
        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
//        encoderRaiseArm(LIFT_SPEED,  8, 5.0);  // S1: Forward 47 Inches with 5 Sec timeout
        //
        encoderRotateArm(ROTATE_SPEED,MAX_ANGLE,10.0);
        encoderExtendArm(EXTEND_SPEED, MAX_EXTENSION, 10.0);
        encoderRaiseArm(LIFT_SPEED,MAX_LIFT,10.0);
        telemetry.addData("Path", "Complete");
        telemetry.update();
        sleep(1000);  // pause to display final telemetry message.
    }

    /*
     *  Method to perform a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the OpMode running.
     */

    public void encoderRaiseArm(double speed,
                             double inches,
                             double timeoutS) {
        int raiseArmTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            raiseArmTarget = raiseArmMotor.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);

            raiseArmMotor.setTargetPosition(raiseArmTarget);

            // Turn On RUN_TO_POSITION
            raiseArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            raiseArmMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    raiseArmMotor.isBusy()) {

                // Display it for the driver.
                telemetry.addData("Running to",  " %7d", raiseArmTarget);
                telemetry.addData("Currently at",  " at %7d",
                        raiseArmMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            raiseArmMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            raiseArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move.
        }

    }
    public void encoderExtendArm(double speed,
                                double inches,
                                double timeoutS) {
        int extendArmTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            extendArmTarget = extendArmMotor.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);

            extendArmMotor.setTargetPosition(extendArmTarget);

            // Turn On RUN_TO_POSITION
            extendArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            extendArmMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    extendArmMotor.isBusy()) {

                // Display it for the driver.
                telemetry.addData("Running to",  " %7d", extendArmTarget);
                telemetry.addData("Currently at",  " at %7d",
                        extendArmMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            extendArmMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            extendArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move.
        }

    }
    public void encoderRotateArm(double speed,
                                 double inches,
                                 double timeoutS) {
        int rotateArmTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            rotateArmTarget = rotateArmMotor.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);

            rotateArmMotor.setTargetPosition(rotateArmTarget);

            // Turn On RUN_TO_POSITION
            rotateArmMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            rotateArmMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    rotateArmMotor.isBusy()) {

                // Display it for the driver.
                telemetry.addData("Running to",  " %7d", rotateArmTarget);
                telemetry.addData("Currently at",  " at %7d",
                        rotateArmMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            rotateArmMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            rotateArmMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move.
        }

    }
}
