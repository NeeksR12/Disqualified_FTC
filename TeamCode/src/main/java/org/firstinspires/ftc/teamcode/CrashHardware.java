package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class CrashHardware {

    //Declarations of hardware
    public DcMotor flywheel, coreHex, intake;
    public CRServo servo;

    // Setting velocity targets (Ticks per second)
    public final int bankVelocity = 1450;
    public final int farVelocity = 1950;
    public final int maxVelocity = 2200;

    // Encoder ratios (Constants)
    public final double DRIVE_ENCODER_DEGREE_RATIO = 12.25;
    public final double WHEEL_DIAMETER_MM = 75;
    public final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER_MM * Math.PI;
    public final double INCHES_TO_MM = 25.4;
    public final double GEAR_RATIO = 4 * 5;
    public final double ENCODER_TO_MOTOR_REVOLUTION_RATIO = 28;
    public final double INCHES_TO_ENCODER = INCHES_TO_MM / WHEEL_CIRCUMFERENCE *
            GEAR_RATIO * ENCODER_TO_MOTOR_REVOLUTION_RATIO;

    // April tags
    public final int BLUE_TAG_ID = 20;
    public final int RED_TAG_ID = 24;
    public int activeTag = 20;


    // Variable fields
    public boolean lastUp = false;
    public boolean lastDown = false;
    public boolean drivingField = false;

    // Elapsed time for performing tasks
    public ElapsedTime activeTime;

    // Drivetrain object
    public MecanumDrive drivetrain = new MecanumDrive();

    // Camera Object
    public AprilTagWebcam camera = new AprilTagWebcam();

    /**
     * Description: Initializes all hardware for Crash
     * Pre-Condition: Must be given a hardware map with the correct names and a telemetry object
     * Post-Condition: The software variables are linked up to the correct hardware components
     * @param hwMap The hardware map
     */
    public void init(HardwareMap hwMap, Telemetry tel) {
        // Initializing drivetrain
        drivetrain.init(hwMap);

        // Initializing camera
        camera.init(hwMap, tel);

        // Hardware
        flywheel = hwMap.get(DcMotor.class, "flywheel");
        coreHex = hwMap.get(DcMotor.class, "coreHex");
        intake = hwMap.get(DcMotor.class, "intake");
        servo = hwMap.get(CRServo.class, "servo");

        // Motor behavior
        flywheel.setDirection(DcMotor.Direction.REVERSE);
        coreHex.setDirection(DcMotor.Direction.REVERSE);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Servo stop
        servo.setPower(0);
    }

}