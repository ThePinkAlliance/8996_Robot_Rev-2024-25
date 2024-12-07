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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="HighBasketScore", group="Auto")

public class HighBasketScore extends LinearOpMode {

    private ElapsedTime     runtime = new ElapsedTime();
    private Servo claw_servo = null;
    private DcMotor         extendArmMotor = null;
    private DcMotor         raiseArmMotor  = null;
    private DcMotor         rotateArmMotor = null;
    public DcMotor frontRightMotor = null;
    public DcMotor backRightMotor = null;
    public DcMotor rotate_arm_motor = null;
    public DcMotor frontLeftMotor= null;
    public DcMotor backLeftMotor= null;
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
    static final double     LIFT_SPEED             = 0.7;
    static final double     EXTEND_SPEED           = 0.7;
    static final double     ROTATE_SPEED           = 0.7;
    static final double     MAX_EXTENSION = 45; //max length robot can extend arm to from initial length
    static final double     straightup_angle = 12; //max angle robot can rotate arm to from initial orientation  TODO: adjust this to actual value once known
    static final double     MAX_LIFT = 14; //max distance robot can lift the arm to from initial position TODO: adjust this to actual value once known
    @Override
    public void runOpMode() {
        // Initialize the drive system variables.
        claw_servo = hardwareMap.get(Servo.class, "claw_servo");
        raiseArmMotor = hardwareMap.get(DcMotor.class, "raise_arm_motor");
        extendArmMotor = hardwareMap.get(DcMotor.class, "extend_arm_motor");
        rotateArmMotor = hardwareMap.get(DcMotor.class, "rotate_arm_motor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
        rotate_arm_motor = hardwareMap.get(DcMotor.class, "rotate_arm_motor");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        rotateArmMotor.setDirection(DcMotor.Direction.REVERSE);
        extendArmMotor.setDirection(DcMotor.Direction.REVERSE);
        backRightMotor.setDirection(DcMotor.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotor.Direction.REVERSE);
        // Wait for the game to start (driver presses START)
        //Step0: Engage servo and grab sample. (look at mainTeleop for how to do this)
        //Step1: Rotate Arm to 90-degrees
        //Step2: raise arm to Max lift
        //Step3: Extend Arm to max
        //Step4: Move forward 8 to 16 inches (use encoderDrive function to do this)
        //Step5: Rotate arm to 120-degrees position
        //Step6: Release sample
        //Step7: turn robot cw 120-degrees (use encoderDrive function to do this)
        //Step8: Move robot roughly 8 to 10 tiles
        //Step9: DONE! YAAAAAAAAAY!!!!!!!!
        // Wait for the game to start (driver presses START)
        waitForStart();
        claw_servo.setPosition(45);
        sleep(1000);
        encoderMove(rotateArmMotor,ROTATE_SPEED,straightup_angle,10.0); //rotate arm to straightup angle
        encoderMove(raiseArmMotor, LIFT_SPEED,MAX_LIFT,6.0); //raise arm lift to maxlift
        /*encoderMove(extendArmMotor, EXTEND_SPEED,MAX_EXTENSION, 5.0);*/ //extend arm to max_extension
        encoderDrive(0.6, 14.5, 14.5, 10); // drive forward to basket 15 inches
        //encoderDriveV2(0.3, 15, 0, 0, 15, 10); //moves the robot to the right
        encoderDrive(0.6, 5, -5, 10); // rotate the robot to face the basket
        encoderMove(rotateArmMotor,ROTATE_SPEED,3, 10.0); // rotate arm to be over the basket
        claw_servo.setPosition(0); //drop the sample in the basket
        encoderMove(rotateArmMotor, ROTATE_SPEED,-12, 10.0); //rotate arm away from basket
        encoderDrive(0.6,-6,-6,10.0); //move slightly back from basket before lowering lift
        encoderMove(raiseArmMotor,LIFT_SPEED, -(MAX_LIFT / 2), 10.0); //lower lift to be about half the max height
        encoderDrive(0.6,45,-45,10.0); //rotate 235-degrees toward ascent zone
        encoderDrive(0.6,52,52,10); //drive a little bit
        encoderDrive(0.8,-10,10,10); //turn a bit to touch the bar
        encoderMove(rotateArmMotor, ROTATE_SPEED,15, 10.0); //rotate arm so that it touches the bar
        encoderDrive(0.6,6,6,10); //move a little bit if arm did not touch bar
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
    public void encoderMove(DcMotor motor, double speed, double inches, double timeoutS) {
        int target;

        if (opModeIsActive()) {
            target = motor.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
            motor.setTargetPosition(target);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            runtime.reset();
            motor.setPower(Math.abs(speed));

            while (opModeIsActive() && runtime.seconds() < timeoutS && motor.isBusy()) {
                telemetry.addData("Running to", "%7d", target);
                telemetry.addData("Currently at", "%7d", motor.getCurrentPosition());
                telemetry.update();
            }

            motor.setPower(0);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            sleep(250); // optional pause
        }
    }

    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = frontLeftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newLeftTarget = backLeftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = frontRightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newRightTarget = backRightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);

            frontLeftMotor.setTargetPosition(newLeftTarget);
            backLeftMotor.setTargetPosition(newLeftTarget);
            frontRightMotor.setTargetPosition(newRightTarget);
            backRightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            frontLeftMotor.setPower(Math.abs(speed));
            backLeftMotor.setPower(Math.abs(speed));
            frontRightMotor.setPower(Math.abs(speed));
            backRightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (backLeftMotor.isBusy() && frontRightMotor.isBusy() && frontLeftMotor.isBusy() && backRightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Running to",  " %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Currently at",  " at %7d :%7d",
                        frontLeftMotor.getCurrentPosition(), frontRightMotor.getCurrentPosition(),
                        backLeftMotor.getCurrentPosition(), backRightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            frontLeftMotor.setPower(0);
            backLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backRightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move.
        }
    }

    public void encoderDriveV2(double speed,
                             double frontleftInches, double frontrightInches,
                             double backleftInches, double backrightInches,
                             double timeoutS) {
        int newbackLeftTarget;
        int newbackRightTarget;
        int newfrontRightTarget;
        int newfrontLeftTarget;

        // Ensure that the OpMode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newbackLeftTarget = frontLeftMotor.getCurrentPosition() + (int)(backleftInches * COUNTS_PER_INCH);
            newbackRightTarget = backLeftMotor.getCurrentPosition() + (int)(backleftInches * COUNTS_PER_INCH);
            newfrontLeftTarget = frontLeftMotor.getCurrentPosition() + (int)(frontleftInches * COUNTS_PER_INCH);
            newfrontRightTarget = frontRightMotor.getCurrentPosition() + (int)(frontrightInches * COUNTS_PER_INCH);

            frontLeftMotor.setTargetPosition(newfrontLeftTarget);
            backLeftMotor.setTargetPosition(newbackLeftTarget);
            frontRightMotor.setTargetPosition(newfrontRightTarget);
            backRightMotor.setTargetPosition(newbackRightTarget);

            // Turn On RUN_TO_POSITION
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            frontLeftMotor.setPower(Math.abs(speed));
            backLeftMotor.setPower(Math.abs(speed));
            frontRightMotor.setPower(Math.abs(speed));
            backRightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (backLeftMotor.isBusy() && frontRightMotor.isBusy() && frontLeftMotor.isBusy() && backRightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Currently at",  " at %7d :%7d",
                        frontLeftMotor.getCurrentPosition(), frontRightMotor.getCurrentPosition(),
                        backLeftMotor.getCurrentPosition(), backRightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            frontLeftMotor.setPower(0);
            backLeftMotor.setPower(0);
            frontRightMotor.setPower(0);
            backRightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            frontLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backLeftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backRightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move.
        }
    }
}
