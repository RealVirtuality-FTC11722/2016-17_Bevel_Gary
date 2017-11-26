/*
2017-11-13 ArtemusMaximus
*/
package org.firstinspires.ftc.teamcode;


import android.graphics.Point;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * This file contains test code to operate motor with a limit based on encoder values
 */

@TeleOp(name="Motor & Encoder Test", group="Linear Opmode")

public class Lifter_test extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    public DcMotor motorLift = null;
    long PositionStart;
    long PositionMax;
    long Position1;
    long Position2;
    long Position3;
    long PositionEnd;


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        motorLift  = hardwareMap.dcMotor.get("motor_lift");
        motorLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        PositionStart = motorLift.getCurrentPosition();
        PositionMax = PositionStart +1680;
        Position1 = PositionStart + 100;
        Position2 = PositionStart + 200;
        Position3 = PositionStart + 300;
        PositionEnd = PositionMax;
        telemetry.addData("Initial Position", PositionStart);
        telemetry.update();

        // eg: Set the drive motor directions:
        // "Reverse" the motor that runs backwards when connected directly to the battery
        motorLift.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        //motorLift.

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Position",  motorLift.getCurrentPosition());
            telemetry.update();

            if (gamepad2.a = true) {
                PositionEnd = Position1;
            }
            if (gamepad2.x = true) {
                PositionEnd = Position2;
            }
            if (gamepad2.y = true) {
                PositionEnd = Position3;
            }
            //motorLift.setPower(gamepad2.left_stick_y);
            if (gamepad2.left_stick_y > 0 && motorLift.getCurrentPosition() <= PositionMax) {
                motorLift.setPower(gamepad2.left_stick_y);
            }else{
                if (gamepad2.left_stick_y < 0 && motorLift.getCurrentPosition() >= PositionStart) {
                    motorLift.setPower(gamepad2.left_stick_y);
                }else{
                    if (motorLift.getCurrentPosition() < PositionEnd) {
                        motorLift.setPower(0.3);
                    }
                    if (motorLift.getCurrentPosition() > PositionEnd){
                        motorLift.setPower(-0.3);
                    }
                }
            }
        }
    }
}