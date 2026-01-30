package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "CrashMain")
public class CrashMain extends CrashOpMode{

    // Setup
    @Override
    protected final void specificSetup() {}

    /**
     * Description: Calls all methods needed to operate Crash in TeleOp
     * Pre-Condition: All objects/hardware have been initialized
     * Post-Condition: Gamepad controls as needed
     */
    @Override
    protected void opMode() {
        while (opModeIsActive()) {

            if (crash.drivingField)
                crash.drivetrain.driveField(-gamepad1.left_stick_y,
                    gamepad1.left_stick_x, gamepad1.right_stick_x);
            else
                crash.drivetrain.moveDrivetrain(-gamepad1.left_stick_y,
                        gamepad1.left_stick_x, gamepad1.right_stick_x);

            driveControls();

            setFlywheelVelocity();
            manualCoreHexAndServoControl();

            intakeArtifact();

            telemetry.addData("Flywheel Velocity", ((DcMotorEx) crash.flywheel).getVelocity());
            telemetry.addData("Flywheel Power", crash.flywheel.getPower());
            telemetry.addData("\nDrive state", crash.drivingField ? "Field Oriented" :
                    "Robot Oriented");
            telemetry.update();
        }
    }


}
