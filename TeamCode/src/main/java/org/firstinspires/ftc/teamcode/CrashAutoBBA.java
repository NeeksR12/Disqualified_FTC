package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "CrashAutoBBA")
public class CrashAutoBBA extends CrashAuto{

    @Override
    protected void opMode() {
        moveRobot(38, 180);

        stay(1000);

        crash.activeTime.reset();

        while (crash.activeTime.milliseconds() <= 7000) {
            bankShotAuto();
        }

        moveRobot(35, 45);
    }
}
