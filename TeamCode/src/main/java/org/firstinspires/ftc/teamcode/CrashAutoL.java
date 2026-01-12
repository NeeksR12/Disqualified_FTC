package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "CrashAutoL")
public class CrashAutoL extends CrashAuto {

    @Override
    protected void opMode() {

        stay(500);

        moveRobot(15, 0);
    }
}