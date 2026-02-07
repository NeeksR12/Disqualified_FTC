package org.firstinspires.ftc.teamcode;

public abstract class CrashAuto extends CrashOpMode {

    /**
     * Description: Allows the user to select the alliance
     * Pre-Condition: All hardware and objects are initialized
     * Post-Condition: Alliance is chosen
     */
    @Override
    protected void specificSetup() {
        selectAliance();
    }

}
