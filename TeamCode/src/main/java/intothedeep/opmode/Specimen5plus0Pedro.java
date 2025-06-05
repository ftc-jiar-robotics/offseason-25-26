package intothedeep.opmode;

import static intothedeep.opmode.path.SpecPaths.builder;
import static intothedeep.subsystem.Common.robot;

import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.pedropathing.pathgen.PathChain;

import intothedeep.opmode.path.SpecPaths;
import intothedeep.subsystem.RobotActions;
import pedroPathing.DrivePoseLoggingAction;
import roadrunner.Actions;
import roadrunner.FollowPathAction;

@Autonomous(name = "5+0 Pedro")
public class Specimen5plus0Pedro extends AbstractAutoPedro {
    private final SpecPaths S_P = new SpecPaths();
    private final Follower f = robot.drivetrain;

    @Override
    protected Pose getStartPose() {
        return null;
    }

    @Override
    protected void onRun() {
        scoreFirstSpecimen();
        getToSamples();
        giveSamples();
        scoreSpecimen(0);
        grabSpecimen();
        scoreSpecimen(1);
        grabSpecimen();
        scoreSpecimen(2);
        grabSpecimen();
        scoreSpecimen(3);
    }


    private void scoreFirstSpecimen() {
        S_P.firstSpec.getPath(0).setPathEndTValueConstraint(0.96);

        robot.actionScheduler.addAction(
                new SequentialAction(
                        new DrivePoseLoggingAction(f, "start_first_specimen", true),
                        new ParallelAction(
                                new Actions.CallbackAction(RobotActions.setupSpecimen(), S_P.firstSpec, 0, 0),
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(0.92)), S_P.firstSpec, 0.85, 0),
                                new FollowPathAction(f, S_P.firstSpec)
                        ),
                        new DrivePoseLoggingAction(f, "setup_first_specimen"),
                        RobotActions.scoreSpecimen()
                )
        );

        robot.actionScheduler.runBlocking();
    }

    private void getToSamples() {
        robot.actionScheduler.addAction(
                new SequentialAction(
                        new DrivePoseLoggingAction(f, "start_go_to_samples"),
                        new ParallelAction(
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(0.92)), S_P.getToSamples, 0.9, 1),
                                new FollowPathAction(f, S_P.getToSamples)
                        ),
                        new DrivePoseLoggingAction(f, "finish_go_to_samples")
                )
        );

        robot.actionScheduler.runBlocking();
    }

    private void giveSamples() {
        robot.actionScheduler.addAction(
                new SequentialAction(
                        new DrivePoseLoggingAction(f, "start_push_samples"),
                        new ParallelAction(
                                new Actions.CallbackAction(RobotActions.setupWallPickup(), S_P.giveSamples, 0, 0),
                                new Actions.CallbackAction(new InstantAction(() -> f.setMaxPower(0.8)), S_P.giveSamples, 0.85, 4),
                                new FollowPathAction(f, S_P.giveSamples, true)
                        ),
                        new DrivePoseLoggingAction(f, "finish_push_samples")
                )
        );


        robot.actionScheduler.runBlocking();
    }

    private void scoreSpecimen(int cycle) {
        S_P.setScorePoint(cycle);

        PathChain scoreSpecChain = builder
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
    }

    private void grabSpecimen() {
        PathChain grabSpecChain = builder
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
    }
}
