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

@Autonomous(name="ArmEncoderAuto", group="Auto")

public class ArmEncoderAuto extends LinearOpMode {

    private ElapsedTime     runtime = new ElapsedTime();
    private DcMotor         extendArmMotor = null;
    private DcMotor         raiseArmMotor  = null;
    private DcMotor         rotateArmMotor = null;
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

        // Wait for the game to start (driver presses START)
        waitForStart();
        encoderMove(rotateArmMotor,ROTATE_SPEED,MAX_ANGLE,10.0);
        encoderMove(extendArmMotor,EXTEND_SPEED, MAX_EXTENSION, 10.0);
        encoderMove(raiseArmMotor, LIFT_SPEED,MAX_LIFT,10.0);
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

}
}
