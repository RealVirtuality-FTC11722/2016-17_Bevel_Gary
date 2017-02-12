package org.firstinspires.ftc.teamcode;

import android.hardware.Camera;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.IllegalFormatCodePointException;
import java.util.Vector;

/*
 * This OpMode was written for the VuforiaDemo Basics video. This demonstrates basic principles of
 * using VuforiaDemo in FTC.
 */
@Autonomous(name = "Autonomous Bevel Gary")
public class AutonomousBevelGary extends LinearOpMode {
    // Variables to be used for later
    private VuforiaLocalizer vuforiaLocalizer;
    private VuforiaLocalizer.Parameters parameters;
    private VuforiaTrackables beacons;
    private VuforiaTrackableDefaultListener listener_gears;
    private VuforiaTrackableDefaultListener listener_tools;
    private VuforiaTrackableDefaultListener listener_legos;
    private VuforiaTrackableDefaultListener listener_wheels;

    private OpenGLMatrix lastKnownLocation;
    private OpenGLMatrix phoneLocation;

    private static final String VUFORIA_KEY = "AfXDkbT/////AAAAGUWkW5XORUDZk0pzMnL5JlVLvMH8yBho/fstQbUOWSs+KpTGzK7G45wHLlm81SXcl71Youk9yLvlN8hblV/+U0s5aamvYKWA71dh8aiXVKYqoDyF5V70BbEXcfUXOcRphDBLUpnCLgVYPxr837L4Yc8RHPVlEYXAtbYKJAvjnMZurqHTSvQG4G/XV5QcFJaJPFyP9zC/sPlkGgdg/xDxYzkABnxDJFTlIKePvpgxCcednmCT6bG/hE5ZeuBxNtC7kWI0xqrG5L90Pq0UZ64Y87esm7DujazZ9YrRVkpNRXcM80kSm+27BrpPvubNeT1lxpRVAzsxZX5AXPAnrHUO3dMMx66HqXzp6X82OgLcHEL1"; // Insert your own key here


    private float robotX = 0;
    private float robotY = 0;
    private float robotAngle = 0;

    public DcMotor motorFL = null;
    public DcMotor motorFR = null;
    public DcMotor motorBL = null;
    public DcMotor motorBR = null;


    public Servo BPLeft = null;
    public Servo BPRight = null;
    public final static double LEFT_HOME = 0.65;
    public final static double RIGHT_HOME = 0.4;
    public final static double LED_HOME = 0.59;
    public Servo LED = null;
    public CRServo SpinningTube = null;


    public Boolean RedTeam = true;
    public Boolean BlueTeam = false;
    public Boolean Left = true;
    public Boolean Right = false;

    public Class sensor1 = Sensor1.class;

    TouchSensor alliance;  // Hardware Device Object
    TouchSensor position;  // Hardware Device Object

        public void runOpMode() throws InterruptedException {

    //        if (RedTeam) {
    //            BlueTeam = false;
    //        }
    //
    //        if (BlueTeam) {
    //            RedTeam = false;
    //        }
    //
    //        if (Left) {
    //            Right = false;
    //        }
    //
    //        if (Right) {
    //            Left = false;
    //        }

            motorFL = hardwareMap.dcMotor.get("motor_fl");
            motorFR = hardwareMap.dcMotor.get("motor_fr");
            motorBL = hardwareMap.dcMotor.get("motor_bl");
            motorBR = hardwareMap.dcMotor.get("motor_br");
            BPLeft = hardwareMap.servo.get("BPLeft");
            BPRight = hardwareMap.servo.get("BPRight");
            SpinningTube = hardwareMap.crservo.get("ballcollector");
            LED = hardwareMap.servo.get("LEDs");
            alliance = hardwareMap.touchSensor.get("alliance color");
            position = hardwareMap.touchSensor.get("team position");

            motorFL.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
            motorFR.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
            motorBL.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
            motorBR.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
            BPLeft.setDirection(Servo.Direction.FORWARD);
            BPRight.setDirection(Servo.Direction.FORWARD);
            LED.setDirection(Servo.Direction.FORWARD);
            SpinningTube.setDirection(CRServo.Direction.FORWARD);

            int counter = 0;

            setupVuforia();

            // We don't know where the robot is, so set it to the origin
            // If we don't include this, it would be null, which would cause errors later on
            lastKnownLocation = createMatrix(0, 0, 0, 0, 0, 0);

            BPLeft.setPosition(LEFT_HOME);
            BPRight.setPosition(RIGHT_HOME);
            LED.setPosition(LED_HOME);

            waitForStart();

            // Start tracking the targets
            beacons.activate();

            while (opModeIsActive()){
                if (alliance.isPressed()) {
                    telemetry.addData("Team: ", "Blue");

                    BlueTeam = true;
                    RedTeam = false;
                } else {
                    telemetry.addData("Team: ", "Red");

                    BlueTeam = false;
                    RedTeam = true;
                }
                if (position.isPressed()) {
                    telemetry.addData("Position: ", "Left");

                    Left = true;
                    Right = false;
                } else {
                    telemetry.addData("Position: ", "Right");

                    Left = false;
                    Right = true;
                }

                Drive();
            }

        }

        public void Drive() {
            // Ask the listener for the latest information on where the robot is
            //OpenGLMatrix latestLocation = listener.getUpdatedRobotLocation();

            // The listener will sometimes return null, so we check for that to prevent errors

            for (VuforiaTrackable beac : beacons) {
                OpenGLMatrix latestLocation = ((VuforiaTrackableDefaultListener) beac.getListener()).getUpdatedRobotLocation();
                if (latestLocation != null) {
                    lastKnownLocation = latestLocation;
                }
            }

            telemetry.addData(beacons.get(3).getName() + "Tracking ", listener_gears.isVisible());
            telemetry.addData(beacons.get(1).getName() + "Tracking ", listener_tools.isVisible());
            telemetry.addData(beacons.get(2).getName() + "Tracking ", listener_legos.isVisible());
            telemetry.addData(beacons.get(0).getName() + "Tracking ", listener_wheels.isVisible());
            telemetry.addData("LKL", formatMatrix(lastKnownLocation)); //Last Known Location Coordinates

            float[] coordinates = lastKnownLocation.getTranslation().getData();

            robotX = coordinates[0];
            robotY = coordinates[1];
            robotAngle = Orientation.getOrientation(lastKnownLocation, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle;

            // Send information about whether the target is visible, and where the robot is
            //telemetry.addData("Tracking " + target.getName(), listener.isVisible());
            //telemetry.addData("Last Known Location", formatMatrix(lastKnownLocation));
            telemetry.addData("X=" + robotX + " Y=" + robotY + " Angle=", robotAngle);


            // Send telemetry and idle to let hardware catch up
            telemetry.update();


            if (RedTeam && Left) {
                RedTeamLeft();
            }

            if (RedTeam && Right) {
                RedTeamRight();
            }

            if (BlueTeam && Left) {
                BlueTeamLeft();
            }

            if (BlueTeam && Right) {
                BlueTeamRight();
            }

        }

        public void RedTeamLeft(){
            // Turn the lights red
            LED.setPosition(0.67);

            // We need to go forward 1,492 millimeters
            ForBackDrive(1000,0.6);

            // We need to turn 90 degrees
            Turn(700,0.6);

            // We need to go forward 1200 millimeters with vuforia or 1790 millimeters without
            ForBackDrive(1000,0.6);

            // Press the correct coloured button

            if (Sensor1.redPercentage() > 0.4) {
                BPLeft.setPosition(0.96);
                sleep(3000);
                BPLeft.setPosition(0.65);
            } else if (Sensor2.redPercentage() > 0.4) {
                BPRight.setPosition(0.10);
                sleep(3000);
                BPRight.setPosition(0.4);
            } else if (Sensor1.bluePercentage() > 0.4 && Sensor2.bluePercentage() > 0.4) {
                BPLeft.setPosition(0.96);
                sleep(3000);
                BPLeft.setPosition(0.65);
            }


            // Make an ameezing light show
            LightShow("red");

            telemetry.update();

            idle();
        }

        public void RedTeamRight() {
            // Turn the lights red
            LED.setPosition(0.67);

            // We need to go forward 1,492 millimeters
            ForBackDrive(1000,0.6);

            // We need to turn 90 degrees
            Turn(700,0.6);

            // We need to go forward 1200 millimeters with vuforia or 1790 millimeters without
            ForBackDrive(1000,0.6);

            // Press the correct coloured button
            /*
            if (Sensor1.redPercentage > 0.4) {
                BPLeft.setPosition(0.96);
                sleep(3000);
                BPLeft.setPosition(0.65);
            } else if (Sensor2.redPercentage > 0.4) {
                BPRight.setPosition(0.10);
                sleep(3000);
                BPRight.setPosition(0.4);
            } else if (Sensor1.bluePercentage > 0.4 && Sensor2.bluePercentage > 0.4) {
                BPLeft.setPosition(0.96);
                sleep(3000);
                BPLeft.setPosition(0.65);
            }
             */

            // Make an ameezing light show
            LightShow("red");

            telemetry.update();

            idle();
        }

        public void BlueTeamLeft() {
            // Turn the lights blue
            LED.setPosition(0.5);

            // We need to go forward 1,492 millimeters
            ForBackDrive(1000,0.6);

            // We need to turn 90 degrees
            Turn(700,0.6);

            // We need to go forward 1200 millimeters with vuforia or 1790 millimeters without
            ForBackDrive(1000,0.6);

            // Press the correct coloured button
            /*
            if (Sensor1.bluePercentage > 0.4) {
                BPLeft.setPosition(0.96);
                sleep(3000);
                BPLeft.setPosition(0.65);
            } else if (Sensor2.bluePercentage > 0.4) {
                BPRight.setPosition(0.10);
                sleep(3000);
                BPRight.setPosition(0.4);
            } else if (Sensor1.redPercentage > 0.4 && Sensor2.redPercentage > 0.4) {
                BPLeft.setPosition(0.96);
                sleep(3000);
                BPLeft.setPosition(0.65);
            }
             */

            // Make an ameezing light show
            LightShow("blue");

            telemetry.update();

            idle();
        }

        public void BlueTeamRight() {
            // Turn the lights blue
            LED.setPosition(0.5);

            // We start by going forward for 1 second
            ForBackDrive(1000,0.6);

            // We change direction to turn
            Turn(700,0.6);

            // Make an ameezing light show
            LightShow("blue");

            telemetry.update();

            idle();
        }

        public void ForBackDrive(long time, double speed){
            motorFL.setPower(speed);
            motorFR.setPower(speed);
            motorBL.setPower(speed);
            motorBR.setPower(speed);
            sleep(time);
            motorFL.setPower(0);
            motorFR.setPower(0);
            motorBL.setPower(0);
            motorBR.setPower(0);
            sleep(1000);
        }

        public void Turn (long time, double speed) {
            if (BlueTeam) {
                motorFL.setPower(speed);
                motorFR.setPower(-speed);
                motorBL.setPower(speed);
                motorBR.setPower(-speed);
            } else if (RedTeam) {
                motorFL.setPower(-speed);
                motorFR.setPower(speed);
                motorBL.setPower(-speed);
                motorBR.setPower(speed);
            }
            sleep(time);
            motorFL.setPower(0);
            motorFR.setPower(0);
            motorBL.setPower(0);
            motorBR.setPower(0);
            sleep(1000);
        }

        public void LightShow (String team) {
            if (team == "red"){
                LED.setPosition(LED_HOME);
                sleep(50);
                LED.setPosition(0.67);
                sleep(50);
                LED.setPosition(LED_HOME);
                sleep(50);
                LED.setPosition(0.67);
                sleep(50);
                LED.setPosition(LED_HOME);
                sleep(50);
                LED.setPosition(0.67);
                sleep(50);
            }
            if (team == "blue") {
                LED.setPosition(LED_HOME);
                sleep(50);
                LED.setPosition(0.5);
                sleep(50);
                LED.setPosition(LED_HOME);
                sleep(50);
                LED.setPosition(0.5);
                sleep(50);
                LED.setPosition(LED_HOME);
                sleep(50);
                LED.setPosition(0.5);
                sleep(50);
            }
        }

        private void setupVuforia() {
            // Setup parameters to create localizer
            parameters = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId); // To remove the camera view from the screen, remove the R.id.cameraMonitorViewId
            parameters.vuforiaLicenseKey = VUFORIA_KEY;
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
            parameters.useExtendedTracking = false;
            vuforiaLocalizer = ClassFactory.createVuforiaLocalizer(parameters);
            // These are the vision targets that we want to use
            // The string needs to be the name of the appropriate .xml file in the assets folder
            Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 4);
            beacons = vuforiaLocalizer.loadTrackablesFromAsset("FTC_2016-17");
            beacons.get(0).setName("Wheels");
            beacons.get(1).setName("Tools");
            beacons.get(2).setName("Legos");
            beacons.get(3).setName("Gears");
            // Setup the target to be tracked
            beacons.get(0).setLocation(createMatrix(298, 1790, 146, 90, 0, 0));
            beacons.get(1).setLocation(createMatrix(-1790, 894, 146, 90, 0, 90));
            beacons.get(2).setLocation(createMatrix(-894, 1790, 146, 90, 0, 0));
            beacons.get(3).setLocation(createMatrix(-1790, -298, 146, 90, 0, 90));


            // Set phone location on robot
            phoneLocation = createMatrix(0, 225, 0, 90, 0, 0);
            // Setup listener and inform it of phone information
            listener_gears = (VuforiaTrackableDefaultListener) beacons.get(3).getListener();
            listener_gears.setPhoneInformation(phoneLocation, parameters.cameraDirection);
            listener_tools = (VuforiaTrackableDefaultListener) beacons.get(1).getListener();
            listener_tools.setPhoneInformation(phoneLocation, parameters.cameraDirection);
            listener_legos = (VuforiaTrackableDefaultListener) beacons.get(2).getListener();
            listener_legos.setPhoneInformation(phoneLocation, parameters.cameraDirection);
            listener_wheels = (VuforiaTrackableDefaultListener) beacons.get(0).getListener();
            listener_wheels.setPhoneInformation(phoneLocation, parameters.cameraDirection);
        }

        // Creates a matrix for determining the locations and orientations of objects
        // Units are millimeters for x, y, and z, and degrees for u, v, and w
        private OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w) {
            return OpenGLMatrix.translation(x, y, z).
                    multiplied(Orientation.getRotationMatrix(
                            AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w));
        }

        // Formats a matrix into a readable string
        private String formatMatrix(OpenGLMatrix matrix) {
            return matrix.formatAsTransform();
        }

    }