package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "CrashAutoFS")
public class CrashAutoFS extends CrashAuto {

    @Override
    protected void opMode() {

        moveRobot(4, 0);

        stay(500);

        crash.currentTime = runtime.milliseconds();

        while (runtime.milliseconds() <= (crash.currentTime + 7000)) {
            farPowerAuto();
        }

        stay(500);

        moveRobot(11, 0);

    }
}
