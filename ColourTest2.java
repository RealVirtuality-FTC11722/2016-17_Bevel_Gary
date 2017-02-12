package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.Arrays;

/**
 * Created by e652 on 2017-02-11.
 */
@Autonomous
public class ColourTest2 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        MultiplexColorSensor mux = new MultiplexColorSensor(hardwareMap, "mux", "ada", new int[]{0, 3}, 10, 0);

        waitForStart();

        while(opModeIsActive()){
            int i = mux.getCRGB(0)[1];
            int i2 = mux.getCRGB(0)[2];
            int i3 = mux.getCRGB(0)[3];
            int i4 = mux.getCRGB(0)[0];
            telemetry.addData("Colour red(R): ", i);
            telemetry.addData("Colour green(G): ", i2);
            telemetry.addData("Colour blue(B): ", i3);
            telemetry.addData("Colour unknown(C): ", i4);

            telemetry.addData("Colour2", Arrays.toString(mux.getCRGB(3)));
            telemetry.update();
            idle();
        }
    }
}
