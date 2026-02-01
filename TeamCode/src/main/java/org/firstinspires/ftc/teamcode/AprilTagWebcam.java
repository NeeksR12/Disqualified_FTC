package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;

import android.util.Size;

import java.util.ArrayList;
import java.util.List;

public class AprilTagWebcam {

    private AprilTagProcessor aprilTagProcessor;
    private VisionPortal visionPortal;
    private List<AprilTagDetection> aprilTagDetection = new ArrayList<>();

    // Store references
    private Telemetry telemetry;

    public double degreeCorrection;

    /**
     * Description: Initializes all of the required April Tag Processors and Vision Portal
     * Pre-Condition: Must be given a hardware map with the correct names and a telemetry object
     * Post-Condition: The camera is initialized
     * @param hwMap The hardware map
     * @param tel The telemetry
     */
    public void init(HardwareMap hwMap, Telemetry tel) {
        this.telemetry = tel;


        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary())
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hwMap.get(WebcamName.class, "Webcam 1"));
        builder.setCameraResolution(new Size(640, 480));
        builder.addProcessor(aprilTagProcessor);

        visionPortal = builder.build();
    }

    /**
     * Description: Updates what the camera is seeing to the detection
     * Pre-Condition: Objects are declared and initialized
     * Post-Condition: Camera is updated
     */
    public void update() {
        aprilTagDetection = aprilTagProcessor.getDetections();
    }

    /**
     * Description: Checks for a specific tag
     * Pre-Condition: All objects are declared and initialized
     * Post-Condition: Returns tag detection info if found or else returns null
     * @param id The tag ID number
     * @return The tag information (null if unsuccessful)
     */
    public AprilTagDetection getTagBySpecificId(int id) {
        for (AprilTagDetection detection : aprilTagDetection) {
            if (detection.id == id) {
                return detection;
            }
        }
        return null;
    }

    /**
     * Description: Adds the camera information to the telemetry
     * Pre-Condition: All objects and hardware are declared and initialized
     * Post-Condition: The information is sent to the telemetry
     * @param detectedId The ID that is detected
     */
    public void displayDetectionTelemetry(AprilTagDetection detectedId) {
        if (detectedId == null) {
            degreeCorrection = 0;
            telemetry.addData("Tag", "Not detected");
            return;
        }

        if (detectedId.metadata != null) {
            telemetry.addData("Tag ID", detectedId.id);
            telemetry.addData("Range (in)", "%.1f", detectedId.ftcPose.range);
            telemetry.addData("Bearing (deg)", "%.1f", detectedId.ftcPose.bearing);
            telemetry.addData("Yaw (deg)", "%.1f", detectedId.ftcPose.yaw);

            degreeCorrection = -detectedId.ftcPose.bearing;
        }
        else {
            telemetry.addLine(String.format("\n==== (ID %d) Unknown", detectedId.id));
            telemetry.addLine(String.format("Center %6.0f %6.0f (pixels)", detectedId.center.x, detectedId.center.y));
        }

    }

    /**
     * Description: Closes the vision portal and stops the webcam
     * Pre-Condition: All hardware and objects are initialized
     * Post-Condition: If the vision portal is not already null, it is closed
     */
    public void stop() {
        if (visionPortal != null) {
            visionPortal.close();
        }
    }

    /**
     * Description: Returns the distance from a detected tag
     * Pre-Condition: All hardware and objects are declared and initialized
     * Post-Condition: Distance is returned in inches
     * @param tagId The ID being detected
     * @return The distance in inches
     */
    public double getDistanceFromId(int tagId) {
        return getTagBySpecificId(tagId).ftcPose.range;
    }

    /**
     * Description: Returns the distance from a detected tag
     * Pre-Condition: All hardware and objects are declared and initialized
     * Post-Condition: Distance is returned in inches
     * @param tagId The ID being detected
     * @return The bearing in degrees
     */
    public double getBearingFromId(int tagId) {
        return getTagBySpecificId(tagId).ftcPose.bearing;
    }

    /**
     * Description: Returns the distance from a detected tag
     * Pre-Condition: All hardware and objects are declared and initialized
     * Post-Condition: Distance is returned in inches
     * @param tagId The ID being detected
     * @return The yaw in degrees
     */
    public double getYawFromId(int tagId) {
        return getTagBySpecificId(tagId).ftcPose.yaw;
    }

}