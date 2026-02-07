package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "CrashMain")
public class CrashMain extends CrashOpMode{

    /**
     * Description: Takes the stored alliance tag value from auto, if not allows for selection
     * Pre-Condition: All objects/hardware used are initialized
     * Post-Condition: Alliance is chosen for main
     */
    @Override
    protected final void specificSetup() {

        // Retrieving the stored alliance
        java.lang.Object temp = blackboard.get("Alliance tag ID");

        // Setting the stored alliance
        if (temp instanceof Integer) {
            crash.activeTag = (Integer) temp; // Alliance from auto

            // Telemetry
            if (crash.activeTag == crash.BLUE_TAG_ID) {
                telemetry.addLine("SELECTED COLOR: BLUE");
            }
            else {
                telemetry.addLine("SELECTED COLOR: RED");
            }

            telemetry.addData("Status", "Initialized");
            telemetry.update();
        }
        else
            selectAliance();

    }

    /**
     * Description: Calls all methods needed to operate Crash in TeleOp
     * Pre-Condition: All objects/hardware have been initialized
     * Post-Condition: Gamepad controls as needed
     */
    @Override
    protected void opMode() {
        while (opModeIsActive()) {

            // Update april tag
            crash.camera.update();

            // Driving
            if (crash.drivingField)
                crash.drivetrain.driveField(-gamepad1.left_stick_y,
                    gamepad1.left_stick_x, gamepad1.right_stick_x);
            else
                crash.drivetrain.moveDrivetrain(-gamepad1.left_stick_y,
                        gamepad1.left_stick_x, gamepad1.right_stick_x);

            // Field or Robot
            driveControls();

            // Other hardware
            setFlywheelVelocity();
            manualCoreHexAndServoControl();

            // Intake
            intakeArtifact();

            // Telemetry
            telemetry.addData("Flywheel Velocity", ((DcMotorEx) crash.flywheel).getVelocity());
            telemetry.addData("Flywheel Power", crash.flywheel.getPower());
            telemetry.addData("\nDrive state", crash.drivingField ? "Field Oriented" :
                    "Robot Oriented");
            telemetry.update();
        }
    }


}
