package intothedeep.opmode;
import static intothedeep.subsystem.Common.robot;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.pedropathing.pathgen.PathChain;

import intothedeep.opmode.path.SpecPaths;
import intothedeep.subsystem.Arm;
import intothedeep.subsystem.Claw;
import intothedeep.subsystem.Extendo;
import intothedeep.subsystem.Intake;
import intothedeep.subsystem.Robot;
import intothedeep.subsystem.RobotActions;
import pedroPathing.DrivePoseLoggingAction;
import roadrunner.Actions;
import roadrunner.FollowPathAction;

@Autonomous(name = "5+0 Pedro")
public class Specimen5plus0Pedro extends AbstractAutoPedro {
    private SpecPaths S_P;


    @Override
    protected void onInit() {
        super.onInit();

        robot.arm.setArmAngle(Arm.ArmAngle.WALL_PICKUP);
        robot.arm.setWristAngle(Arm.WristAngle.GRAB_OFF_WALL);
        robot.arm.setArmstendoAngle(Arm.Extension.RETRACTED);
        robot.claw.setAngle(Claw.ClawAngles.SPECIMEN_CLAMPED);
        robot.intake.setTargetV4BAngle(Intake.V4BAngle.VERTICAL);
        robot.extendo.setTargetExtension(Extendo.Extension.RETRACTED);
        robot.setCurrentState(Robot.State.WALL_PICKUP);

        robot.intake.run(robot.extendo.getTargetAngle());
        robot.extendo.run(false);
        robot.arm.run();
        robot.claw.run();

        S_P = new SpecPaths();
    }
    @Override
    protected Pose getStartPose() {
        return new Pose(8.291, 65.000, Math.toRadians(0), true);
    }

    @Override
    protected void onRun() {
        scoreFirstSpecimen();
        getToSamples();
        giveSamples();
//        scoreSpecimen(0);
//        grabSpecimen();
//        scoreSpecimen(1);
//        grabSpecimen();
//        scoreSpecimen(2);
//        grabSpecimen();
//        scoreSpecimen(3);
    }


    private void scoreFirstSpecimen() {
        S_P.firstSpec.getPath(0).setPathEndTValueConstraint(0.96);

        robot.actionScheduler.addAction(
                new SequentialAction(
                        new DrivePoseLoggingAction(f, "start_first_specimen", true),
                        new ParallelAction(
                                new Actions.CallbackAction(new SequentialAction(
                                        new DrivePoseLoggingAction(f, "before_setup_first_specimen"),
                                        RobotActions.setupSpecimen(),
                                        new DrivePoseLoggingAction(f, "after_setup_first_specimen")
                                ), S_P.firstSpec, 0, 0),
                                new Actions.CallbackAction(new SequentialAction(
                                        new DrivePoseLoggingAction(f, "before_max_power_first_specimen"),
                                        new InstantAction(() -> f.setMaxPower(0.90)),
                                        new DrivePoseLoggingAction(f, "after_max_power_first_specimen")
                                ), S_P.firstSpec, 0.85, 0),
                                new FollowPathAction(f, S_P.firstSpec)
                        ),
                        new DrivePoseLoggingAction(f, "setup_first_specimen"),
                        RobotActions.scoreSpecimen()
                )
        );

        robot.actionScheduler.runBlocking();
        f.setMaxPower(1);
    }

    private void getToSamples() {
        S_P.getToSamples.getPath(0).setPathEndTValueConstraint(0.9);

        robot.actionScheduler.addAction(
                new SequentialAction(
                        new DrivePoseLoggingAction(f, "start_go_to_samples"),
                        new ParallelAction(
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(0.92)), S_P.getToSamples, 0.8, 1),
                                new FollowPathAction(f, S_P.getToSamples)
                        ),
                        new DrivePoseLoggingAction(f, "finish_go_to_samples")
                )
        );

        robot.actionScheduler.runBlocking();
        f.setMaxPower(1);
    }

    private void giveSamples() {
        S_P.giveSamples.getPath(0).setPathEndTValueConstraint(0.85);
        S_P.giveSamples.getPath(2).setPathEndTValueConstraint(0.85);

        robot.actionScheduler.addAction(
                new SequentialAction(
                        new DrivePoseLoggingAction(f, "start_push_samples"),
                        new ParallelAction(
                                new Actions.CallbackAction(RobotActions.setupWallPickup(), S_P.giveSamples, 0, 0),
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(0.7)), S_P.giveSamples, 0.65, 0),
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(1)), S_P.giveSamples, 0, 1),
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(0.7)), S_P.giveSamples, 0.8, 1),
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(1)), S_P.giveSamples, 0, 2),
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(0.7)), S_P.giveSamples, 0.65, 2),
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(1)), S_P.giveSamples, 0, 3),
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(0.7)), S_P.giveSamples, 0.8, 3),
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(1)), S_P.giveSamples, 0, 4),
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(0.7)), S_P.giveSamples, 0.65, 4),
                                new FollowPathAction(f, S_P.giveSamples, true)
                        ),
                        new DrivePoseLoggingAction(f, "finish_push_samples")
                )
        );

        robot.actionScheduler.runBlocking();
        f.setMaxPower(1);
    }

    private void scoreSpecimen(int cycle) {
        S_P.setScorePoint(cycle);

        Path scoreSpec =
                new Path(
                        new BezierCurve(
                                new Point(robot.drivetrain.getPose()),
                                S_P.desiredSpecControl1,
                                S_P.desiredSpecControl2,
                                S_P.desiredScorePoint
                        )
                );

        PathChain scoreSpecChain = f.pathBuilder()
                .addPath(scoreSpec)
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(10))
                .setPathEndTValueConstraint(0.96)
                .build();

        robot.actionScheduler.addAction(
                new SequentialAction(
                        new DrivePoseLoggingAction(f, "start_spec_cycle_"+(cycle+2)),
                        new ParallelAction(
                                new Actions.CallbackAction(RobotActions.setupSpecimen(), scoreSpecChain, 0, 0),
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(0.92)), scoreSpecChain, 0.85, 0),
                                new FollowPathAction(f, scoreSpecChain)
                        ),
                        RobotActions.scoreSpecimen(),
                        new DrivePoseLoggingAction(f, "score_spec_cycle_"+(cycle+2))
                )
        );

        robot.actionScheduler.runBlocking();
        f.setMaxPower(1);
    }

    private void grabSpecimen() {
        PathChain grabSpecChain = f.pathBuilder()
                .addPath(S_P.grabSpec)
                .setLinearHeadingInterpolation(Math.toRadians(10), Math.toRadians(0))
                .build();

        robot.actionScheduler.addAction(
                new SequentialAction(
                        new DrivePoseLoggingAction(f, "grab_spec_start"),
                        new ParallelAction(
                                new Actions.CallbackAction(RobotActions.setupWallPickup(), grabSpecChain, 0.2, 0),
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(0.8)), grabSpecChain, 0.9125, 0),
                                new FollowPathAction(f, grabSpecChain, true)
                        ),
                        new DrivePoseLoggingAction(f, "finish_spec_cycle")
                )
        );

        robot.actionScheduler.runBlocking();
        f.setMaxPower(1);
    }
}
