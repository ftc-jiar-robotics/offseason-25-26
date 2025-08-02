package robot.intothedeep.opmodes.auto;

import static robot.intothedeep.subsystem.Common.mTelemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import robot.intothedeep.subsystem.Common;
import robot.intothedeep.subsystem.Robot;

@Disabled
public abstract class AbstractAutoPedro extends LinearOpMode {

    protected final void update() {
        robot.readSensors();
        Common.robot.run();
        robot.printTelemetry();
        mTelemetry.addData("Loop time (hertz)", LoopUtil.getLoopTimeInHertz());
        mTelemetry.update();
    }

    @Override
    public final void runOpMode() {
        Common.robot = new Robot(hardwareMap);
        mTelemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        robot.actionScheduler.setUpdate(this::update);

        onInit();
        configure();

        if (isStopRequested()) return;

        waitForStart();

        resetRuntime();
        robot.drivetrain.setPose(getStartPose());

        onRun();
        Common.AUTO_END_POSE_PEDRO = robot.drivetrain.getPose();
    }

    protected void onInit() {
        Constants.setConstants(FConstants.class, LConstants.class);
    }
    protected void configure() {}
    protected abstract Pose getStartPose();
    protected abstract void onRun();
}