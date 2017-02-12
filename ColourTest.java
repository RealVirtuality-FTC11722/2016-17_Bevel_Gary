package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Arrays;

/**
 * Created by e652 on 2017-02-11.
 */
@Autonomous
public class ColourTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        MultiplexColorSensor mux = new MultiplexColorSensor(hardwareMap, "mux", "ada", new int[]{0, 3}, 10, 0);

        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("Colour", Arrays.toString(mux.getCRGB(0)));
            telemetry.addData("Colour2", Arrays.toString(mux.getCRGB(3)));
            telemetry.update();
            idle();
        }
    }
}
