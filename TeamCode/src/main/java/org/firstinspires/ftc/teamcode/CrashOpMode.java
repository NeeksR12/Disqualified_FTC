package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public abstract class CrashOpMode extends LinearOpMode {

    //Hardware
    protected CrashHardware crash = new CrashHardware();
    protected ElapsedTime runtime, inertiaBuildUp;


    // Main OpMode
    @Override
    public void runOpMode() {

        // Hardware initialization
        crash.init(hardwareMap, telemetry);

        // Init phase
        setup();

        // Wait for start
        waitForStart();
        runtime.reset();

        // Play
        opMode();

    }

    // Class method
    /**
     * Description: Performs all setup and actions required in the init phase of the OpMode
     * Pre-Condition: All objects/hardware is declared and initialized
     * Post-Condition: The OpMode's init phase is performed
     */
    private void setup() {
        runtime = new ElapsedTime();
        inertiaBuildUp = new ElapsedTime();
        crash.activeTime = new ElapsedTime();

        // Pre-Aliance selection setup
        specificSetup();


    }

    // Abstract Methods
    /**
     * To contain anything that must occur specifically in the initialization phase of the specific
     * OpMode before alliance selection
     */
    protected abstract void specificSetup();

    /**
     * To contain the code to execute after the "play" button is pressed
     */
    protected abstract void opMode();

    // Actual active methods
    /**
     * Description: This method contains all the statements that manually control the corehex and
     * the servo
     * Pre-Condition: All objects/hardware have been initialized
     * Post-Condition: The given command will execute the appropriate corehex or servo motion
     */
    protected void manualCoreHexAndServoControl() {
        // Manual control for the Core Hex intake
        if (gamepad1.cross) {
            crash.coreHex.setPower(0.5);
        } else if (gamepad1.triangle) {
            crash.coreHex.setPower(-0.5);
        }
        // Manual control for the hopper's servo
        if (gamepad1.dpad_left) {
            crash.servo.setPower(1);
        } else if (gamepad1.dpad_right) {
            crash.servo.setPower(-1);
        }
    }

    /**
     * Description: This method contains all the statements that control the flywheel, including
     * manual and automatic launches at set velocities
     * Pre-Condition: All objects/hardware have been initialized
     * Post-Condition: The given command will execute the appropriate flywheel and related hardware
     * as required
     */
    protected void setFlywheelVelocity() {
        if (gamepad1.start) {
            crash.flywheel.setPower(-0.5);
        }
        else if (gamepad1.left_bumper) {
            farPowerAuto();

            if (gamepad1.leftBumperWasPressed()) {
                alignToTag(crash.activeTag, (crash.activeTag - 22) * crash.alignmentCorrection
                        * crash.camera.isTagDetected(crash.activeTag));
            }

        }
        else if (gamepad1.right_bumper) {
            bankShotAuto();

            if (gamepad1.rightBumperWasPressed()) {
                alignToTag(crash.activeTag, 0);
            }

        }
        else if (gamepad1.b) {
            ((DcMotorEx) crash.flywheel).setVelocity(crash.bankVelocity);
        }
        else if (gamepad1.x) {
            ((DcMotorEx) crash.flywheel).setVelocity(crash.maxVelocity);
        }
        else {
            ((DcMotorEx) crash.flywheel).setVelocity(0);
            crash.coreHex.setPower(0);

            if (!gamepad1.dpad_right && !gamepad1.dpad_left) {
                crash.servo.setPower(0);
            }
        }
    }

    /**
     * Description: Launches an artifact from the close location by running all pieces involved for
     * the duration the button is held
     * Pre-Condition: All objects/hardware have been initialized
     * Post-Condition: Robot runs all motors and hardware to launch from the close location
     */
    protected void bankShotAuto() {
        ((DcMotorEx) crash.flywheel).setVelocity(crash.bankVelocity);

        if (((DcMotorEx) crash.flywheel).getVelocity() >= crash.bankVelocity - 50) {
            crash.coreHex.setPower(1);
            crash.servo.setPower(-1);
            //inertiaBuildUp.reset();
        }
        /*
        else if (inertiaBuildUp.milliseconds() > 1000) {
            crash.coreHex.setPower(-1);
        }
         */
        else
            crash.coreHex.setPower(-1);
    }

    /**
     * Description: Launches an artifact from the far location by running all pieces involved for
     * the duration the button is held
     * Pre-Condition: All objects/hardware have been initialized
     * Post-Condition: Robot runs all motors and hardware to launch from the far location
     */
    protected void farPowerAuto() {
        ((DcMotorEx) crash.flywheel).setVelocity(crash.farVelocity);

        if (((DcMotorEx) crash.flywheel).getVelocity() >= crash.farVelocity - 30) {
            crash.coreHex.setPower(1);
            crash.servo.setPower(-1);
            inertiaBuildUp.reset();
        }
        else if (inertiaBuildUp.milliseconds() > 1000) {
            crash.coreHex.setPower(-1);
        }
    }

    /**
     * Description: Rotates the intake in or out
     * Pre-Condition: All objects/hardware have been initialized
     * Post-Condition: Intake is turned or not turned depending on buttons pressed or not pressed
     */
    protected void intakeArtifact() {
        // Intake artifact
        if (gamepad1.right_trigger > 0) {
            crash.intake.setPower(-1.0);
        }
        // Reverse
        else if (gamepad1.left_trigger > 0) {
            crash.intake.setPower(1.0);
        }
        else {
            crash.intake.setPower(0.0);
        }
    }

    /**
     * Description: Turns robot at a specified degree value clockwise from where it is facing
     * Pre-Condition: Param must be a double and all objects/hardware have been initialized
     * Post-Condition: Robot turns the specified degrees
     * @param degrees The angle the robot is turning
     * @param power The power for wheels (0 < power <= 1)
     */
    protected void turnRobot(double degrees, double power) {

        // Checking power
        if (power > 1) {
            power = 0.4;
        }

        //Variable
        double targetPosition = (degrees * crash.DRIVE_ENCODER_DEGREE_RATIO) +
                crash.drivetrain.leftFrontDrive.getCurrentPosition();

        // Turning
        while (Math.abs(crash.drivetrain.leftFrontDrive.getCurrentPosition() - targetPosition) > 10) {
            crash.drivetrain.moveDrivetrain(0, 0, power *
                    (degrees/Math.abs(degrees)));
            telemetry.addData("Target position", targetPosition);
            telemetry.addData("Current position",
                    crash.drivetrain.leftFrontDrive.getCurrentPosition());
            telemetry.update();
        }
    }

    /**
     * Description: Moves the robot a distance facing forward at an angle from where the robot is
     * facing
     * Pre-Condition: Both params are doubles and all objects/hardware have been initialized
     * Post-Condition: The robot moves the specified distance at the specified angle
     * @param distanceInches The distance in inches the robot moves
     * @param degrees The degree value of where the robot moves
     */
    protected void moveRobot(double distanceInches, double degrees) {

        degrees = AngleUnit.normalizeDegrees(degrees);
        double theta; // Angle in radians
        double targetPosition;

        // leftFrontDrive target
        if (0 <= degrees && degrees <= 90 || -180 <= degrees && degrees <= -90) {
            theta = Math.toRadians(degrees);

            targetPosition = ((distanceInches * (Math.cos(theta) * crash.INCHES_TO_ENCODER)
                    + (distanceInches * Math.sin(theta)) * crash.INCHES_TO_ENCODER)) +
                    crash.drivetrain.leftFrontDrive.getCurrentPosition();

            while (Math.abs(crash.drivetrain.leftFrontDrive.getCurrentPosition() - targetPosition) > 10) {
                crash.drivetrain.moveDrivetrain((0.4 * Math.cos(theta)), (0.4 * Math.sin(theta)), 0);
                telemetry.addData("Target position", targetPosition);
                telemetry.addData("Current position", crash.drivetrain.leftFrontDrive.getCurrentPosition());
                telemetry.update();
            }
        }

        // rightFrontDrive target
        else {
            theta = Math.toRadians(degrees);

            targetPosition = ((distanceInches * (Math.cos(theta) * crash.INCHES_TO_ENCODER)
                    - (distanceInches * Math.sin(theta)) * crash.INCHES_TO_ENCODER)) +
                    crash.drivetrain.rightFrontDrive.getCurrentPosition();

            while (Math.abs(crash.drivetrain.rightFrontDrive.getCurrentPosition() - targetPosition) > 10) {
                crash.drivetrain.moveDrivetrain((0.4 * Math.cos(theta)), (0.4 * Math.sin(theta)), 0);
                telemetry.addData("Target position", targetPosition);
                telemetry.addData("Current position", crash.drivetrain.rightFrontDrive.getCurrentPosition());
                telemetry.update();
            }
        }
    }

    /**
     * Description: Sends a halt command to the drive train and ceases all robot function for the
     * specified amount of time
     * Pre-Condition: All objects/hardware have been initialized
     * Post-Condition: The robot stays still doing nothing for the specified amount of time
     * @param milliseconds The time in milliseconds for the robot to wait
     */
    protected void stay(int milliseconds) {
        crash.activeTime.reset();
        while (crash.activeTime.milliseconds() < milliseconds) {
            crash.drivetrain.moveDrivetrain(0, 0, 0);
            telemetry.addData("Current Time", crash.activeTime.milliseconds());
            telemetry.addData("Goal Time", milliseconds);
            telemetry.update();
        }
    }

    /**
     * Description: Maintains the driving controls for crash, whether it is driving robot or field
     * oriented and resetting robot yaw
     * Pre-Condition: All objects/hardware have been initialized
     * Post-Condition: Robot drive state is updated as according to input
     */
    public void driveControls() {
        if (gamepad1.dpad_up && !crash.lastUp) {
            crash.drivingField = !crash.drivingField;
            if (crash.drivingField)
                crash.drivetrain.imu.resetYaw();
        }
        if (gamepad1.dpad_down && !crash.lastDown) {
            crash.drivetrain.imu.resetYaw();
        }

        crash.lastUp = gamepad1.dpad_up;
        crash.lastDown = gamepad1.dpad_down;
    }

    /**
     * Description: Aligns the robot to a specific tag at the specified angle
     * Pre-Condition: All objects/hardware have been initialized
     * Post-Condition: Robot aligns itself at the specified angle from the tag
     * @param tagId The integer value associated with the desired tag
     * @param degrees The additional degree value wanted
     */
    public void alignToTag(int tagId, double degrees) {
        turnRobot(-crash.camera.getBearingFromId(tagId) + degrees, 0.1);
    }

    /**
     * Description: Allows for a user to select the alliance of the robot during INIT
     * Pre-Condition: All objects and hardware must be initialized
     * Post-Condition: Alliance selection choice is recorded to the active tag
     */
    public void selectAliance() {
        while (opModeInInit()) {

            // Tag selection
            if (gamepad1.x) {
                crash.activeTag = crash.BLUE_TAG_ID;
            }
            if (gamepad1.b) {
                crash.activeTag = crash.RED_TAG_ID;
            }

            // Telemetry
            telemetry.addLine("=== APRILTAG SELECTION ===");
            telemetry.addLine("Press BEFORE Start");
            telemetry.addLine("X = BLUE (ID 20)");
            telemetry.addLine("B = RED  (ID 24)");
            telemetry.addLine("------------------------");
            telemetry.addData("SELECTED TAG ID", crash.activeTag);

            if (crash.activeTag == crash.BLUE_TAG_ID) {
                telemetry.addLine("SELECTED COLOR: BLUE");
            } else {
                telemetry.addLine("SELECTED COLOR: RED");
            }

            telemetry.update();

            // Blackboard
            blackboard.put("Alliance tag ID", crash.activeTag);

        }

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

}
