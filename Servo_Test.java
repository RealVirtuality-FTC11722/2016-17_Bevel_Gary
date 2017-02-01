package org.firstinspires.ftc.teamcode;//package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Created by ArtemusMaximus on 12/29/2016.
 */
@TeleOp(name="Servo Test", group="Linear Opmode")
public class Servo_Test extends LinearOpMode {

    Servo PushButtonLeft         = null;
    Servo Servo35         = null;
    CRServo CR_Servo         = null;

    public final static double PushButtonLeft_MIN_RANGE  = 0.32;
    public final static double PushButtonLeft_MAX_RANGE  = 0.63;
    public double PushButtonLeft_POS =PushButtonLeft_MAX_RANGE;
    public double PushButtonLeft_Range =PushButtonLeft_MAX_RANGE-PushButtonLeft_MIN_RANGE;
    public double CR_Servo_POS =0;

    @Override
    public void runOpMode() throws InterruptedException {
        PushButtonLeft = hardwareMap.servo.get("BP_Left");
        Servo35 = hardwareMap.servo.get("Servo785");
        PushButtonLeft.setPosition(PushButtonLeft_POS);
        CR_Servo = hardwareMap.crservo.get("cr_servo");
        CR_Servo.setPower(CR_Servo_POS);
        waitForStart();
        while(opModeIsActive()){
            PushButtonLeft_POS = PushButtonLeft_MAX_RANGE - PushButtonLeft_Range * gamepad1.left_trigger;
            PushButtonLeft.setPosition(PushButtonLeft_POS);
            CR_Servo_POS = gamepad1.left_stick_y;
            Servo35.setPosition(gamepad1.right_trigger);
            CR_Servo.setPower(CR_Servo_POS);
            telemetry.addData("Trigger Left", gamepad1.left_trigger);
            telemetry.addData("Position", gamepad1.left_stick_y);
            telemetry.update();
            sleep(50);
            idle();
        }

    }
}


