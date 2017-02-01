package org.firstinspires.ftc.teamcode;

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

/*
 * This OpMode was written for the VuforiaDemo Basics video. This demonstrates basic principles of
 * using VuforiaDemo in FTC.
 */
@Autonomous(name = "VuforiaDemo")
public class VuforiaDemo extends LinearOpMode
{
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


    public void runOpMode() throws InterruptedException
    {
        setupVuforia();

        // We don't know where the robot is, so set it to the origin
        // If we don't include this, it would be null, which would cause errors later on
        lastKnownLocation = createMatrix(0, 0, 0, 0, 0, 0);

        waitForStart();

        // Start tracking the targets
        beacons.activate();

        while(opModeIsActive())
        {
            // Ask the listener for the latest information on where the robot is
            //OpenGLMatrix latestLocation = listener.getUpdatedRobotLocation();

            // The listener will sometimes return null, so we check for that to prevent errors

            for (VuforiaTrackable beac : beacons) {
                OpenGLMatrix latestLocation = ((VuforiaTrackableDefaultListener) beac.getListener()).getUpdatedRobotLocation();
                if(latestLocation != null) {
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
            telemetry.addData("X="+robotX+" Y="+robotY+" Angle=",robotAngle);


            // Send telemetry and idle to let hardware catch up
            telemetry.update();
            idle();
        }
    }



    private void setupVuforia()
    {
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
    private OpenGLMatrix createMatrix(float x, float y, float z, float u, float v, float w)
    {
        return OpenGLMatrix.translation(x, y, z).
                multiplied(Orientation.getRotationMatrix(
                        AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, u, v, w));
    }

    // Formats a matrix into a readable string
    private String formatMatrix(OpenGLMatrix matrix)
    {
        return matrix.formatAsTransform();
    }
}