package intothedeep.opmode;

import static intothedeep.subsystem.Common.mTelemetry;
import static intothedeep.subsystem.Common.robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import intothedeep.subsystem.Common;
import intothedeep.subsystem.Robot;
import intothedeep.util.LoopUtil;

@Disabled
public abstract class AbstractAutoPedro extends LinearOpMode {

    protected final void update() {
        robot.readSensors();
        robot.run();
        robot.printTelemetry();
        mTelemetry.addData("Loop time (hertz)", LoopUtil.getLoopTimeInHertz());
        mTelemetry.update();
    }

    @Override
    public final void runOpMode() {
        robot = new Robot(hardwareMap);
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

    protected void onInit() {}
    protected void configure() {}
    protected abstract Pose getStartPose();
    protected abstract void onRun();
}