package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by e652 on 2017-02-11.
 */

public class Sensor1 extends LinearOpMode {

    MultiplexColorSensor muxColor;
    int[] ports = {0, 3};

    public void runOpMode() throws InterruptedException{
        int milliSeconds = 48;
        muxColor = new MultiplexColorSensor(hardwareMap, "mux", "ada", ports, milliSeconds, MultiplexColorSensor.GAIN_16X);

        muxColor.startPolling();

        waitForStart();

        while (opModeIsActive()){
            for (int i=0; i<ports.length; i++) {
                int[] crgb = muxColor.getCRGB(ports[i]);

                telemetry.addLine("Sensor " + ports[i]);

                int RedPercent = crgb[1]/crgb[0];
                int GreenPercent = crgb[2]/crgb[0];
                int BluePercent = crgb[3]/crgb[0];

                if (RedPercent > BluePercent && RedPercent > GreenPercent) {
                    telemetry.addLine("Colour: Red");
                }

                if (BluePercent > RedPercent && BluePercent > GreenPercent) {
                    telemetry.addLine("Colour1: Blue");
                }

                telemetry.addData("Red Percent: ", crgb[1]/crgb[0]);
                telemetry.addData("Blue Percent: ", crgb[3]/crgb[0]);
                telemetry.addData("CRGB", "%5d %5d %5d %5d",
                        crgb[0], crgb[1], crgb[2], crgb[3]);
            }
            telemetry.update();
        }
    }

    public float redPercentage() {

        int[] crgb = muxColor.getCRGB(ports[0]);

        int RP = crgb[1]/(crgb[0] + 1);

        return RP;
    }

    public float bluePercentage() {

        int[] crgb = muxColor.getCRGB(ports[0]);

        int RP = crgb[3]/(crgb[0] + 1);

        return RP;
    }
}
