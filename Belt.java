package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

/**
 * Created by e652 on 2017-02-11.
 */

@TeleOp
public class Belt extends LinearOpMode {

    public CRServo TopConveyer = null;
    public CRServo SpinningTube = null;

    private boolean spinningForward = false;
    private boolean spinningBackward = false;

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        TopConveyer = hardwareMap.crservo.get("ConveyerBeltTop");
        SpinningTube = hardwareMap.crservo.get("BallCollector");

        TopConveyer.setDirection(CRServo.Direction.FORWARD);
        SpinningTube.setDirection(CRServo.Direction.FORWARD);


        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.right_bumper == false && gamepad1.left_bumper) {
                SpinningTube.setPower(1);
            }

            if (gamepad1.right_bumper && gamepad1.left_bumper == false) {
                SpinningTube.setPower(-1);
            }

            if (gamepad1.right_bumper == false && gamepad1.left_bumper == false) {
                SpinningTube.setPower(0);
            }

            if (gamepad1.y && spinningForward == false) {
                spinningForward = true;
                TopConveyer.setPower(1);
            } else if (gamepad1.y && spinningBackward == false) {
                spinningBackward = true;
                TopConveyer.setPower(-1);
            }

            if (gamepad1.y && spinningForward == true && spinningBackward == true) {
                spinningForward = false;
                spinningBackward = false;
                TopConveyer.setPower(0);
            }
        }
    }
}