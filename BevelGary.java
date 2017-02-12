package org.firstinspires.ftc.teamcode;


        import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
        import com.qualcomm.robotcore.hardware.CRServo;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.DcMotorSimple;
        import com.qualcomm.robotcore.hardware.Servo;
        import com.qualcomm.robotcore.util.ElapsedTime;


/**
 * Created by e652 on 2017-01-21.
 */

@TeleOp(name="TeleOp Bevel Gary", group="Linear Opmode")  // @Autonomous(...) is the other common choice

public class BevelGary extends LinearOpMode {

    /* Declare OpMode members. */
    private ElapsedTime runtime = new ElapsedTime();
    public DcMotor motorFL = null;
    public DcMotor motorFR = null;
    public DcMotor motorBL = null;
    public DcMotor motorBR = null;
    public Servo BPLeft = null;
    public Servo BPRight = null;
    public DcMotor LeftWheel = null;
    public DcMotor RightWheel = null;
    public CRServo SpinningTube = null;
    public CRServo Conveyor = null;
    public final static double LEFT_HOME = 0.65;
    public final static double RIGHT_HOME = 0.4;
    public final static double LED_HOME = 0.59;
    public Servo LED = null;

    private boolean spinningForward = false;
    private boolean spinningBackward = false;
    
    /*public final static double PushButtonLeft_MIN_RANGE  = 0.32;
    public final static double PushButtonLeft_MAX_RANGE  = 0.63;
    public double PushButtonLeft_POS =PushButtonLeft_MAX_RANGE;
    public double PushButtonLeft_Range =PushButtonLeft_MAX_RANGE-PushButtonLeft_MIN_RANGE;*/

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        /* eg: Initialize the hardware variables. Note that the strings used here as parameters
         * to 'get' must correspond to the names assigned during the robot configuration
         * step (using the FTC Robot Controller app on the phone).
         */
        motorFL  = hardwareMap.dcMotor.get("motor_fl");
        motorFR  = hardwareMap.dcMotor.get("motor_fr");
        motorBL  = hardwareMap.dcMotor.get("motor_bl");
        motorBR  = hardwareMap.dcMotor.get("motor_br");
        BPLeft = hardwareMap.servo.get("BPLeft");
        BPRight = hardwareMap.servo.get("BPRight");
        LeftWheel  = hardwareMap.dcMotor.get("LeftWheel");
        RightWheel  = hardwareMap.dcMotor.get("RightWheel");
        SpinningTube = hardwareMap.crservo.get("ballcollector");
        Conveyor = hardwareMap.crservo.get("Conveyor");
        LED = hardwareMap.servo.get("LEDs");


        // eg: Set the drive motor directions:
        // "Reverse" the motor that runs backwards when connected directly to the battery
        motorFL.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        motorFR.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        motorBL.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        motorBR.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        BPLeft.setDirection(Servo.Direction.FORWARD);
        BPRight.setDirection(Servo.Direction.FORWARD);
        LeftWheel.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        RightWheel.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors*/
        SpinningTube.setDirection(CRServo.Direction.FORWARD);
        Conveyor.setDirection(CRServo.Direction.REVERSE);
        LED.setDirection(Servo.Direction.FORWARD);

        BPLeft.setPosition(LEFT_HOME);
        BPRight.setPosition(RIGHT_HOME);
        LED.setPosition(LED_HOME);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {


            // eg: Run wheels in tank mode (note: The joystick goes negative when pushed forwards)
            //leftMotor.setPower(-gamepad1.left_stick_y);
            //rightMotor.setPower(-gamepad1.right_stick_y);

            double r = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
            //double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
            double rightX = gamepad1.right_stick_x;
            final double v1 = r * Math.cos(robotAngle) - rightX;
            final double v2 = r * Math.sin(robotAngle) + rightX;
            final double v3 = r * Math.sin(robotAngle) - rightX;
            final double v4 = r * Math.cos(robotAngle) + rightX;

            motorFL.setPower(v3);
            motorFR.setPower(v2);
            motorBL.setPower(v1);
            motorBR.setPower(v4);

            if(gamepad1.x){
                LED.setPosition(0.5);
            }

            if(gamepad1.b){
                LED.setPosition(0.67);
            }

            if(gamepad1.a){
                LED.setPosition(LED_HOME);
            }

            float LeftT;
            float RightT;

            if(gamepad1.left_trigger < 0.65){
                LeftT = 0.65f;
            } else if(gamepad1.left_trigger > 0.96){
                LeftT = 0.96f;
            } else {
                LeftT = gamepad1.left_trigger;
            }

            if(gamepad1.right_trigger < 0.4){
                RightT = 0.4f;
            } else if(gamepad1.right_trigger > 0.10){
                RightT = 0.10f;
            } else {
                RightT = gamepad1.right_trigger;
            }

            BPLeft.setPosition(LeftT);
            BPRight.setPosition(RightT);

            LeftWheel.setPower(gamepad2.right_trigger);
            RightWheel.setPower(gamepad2.right_trigger);

            Conveyor.setPower(gamepad2.left_trigger);

            if(gamepad2.left_bumper) {
                SpinningTube.setPower(1);
            } else if (gamepad2.right_bumper) {
                SpinningTube.setPower(-1);
            } else {
                SpinningTube.setPower(0);
            }

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            idle();
        }
    }
}