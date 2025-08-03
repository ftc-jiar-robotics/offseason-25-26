package intothedeep.opmode.path;


import static intothedeep.subsystem.Common.robot;

import com.acmerobotics.dashboard.config.Config;

import com.pedropathing.follower.Follower;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;

@Config
public class SpecPaths {
    Follower f = robot.drivetrain;
    ScoreSpecimen S_S = new ScoreSpecimen();

    public Point desiredScorePoint = new Point(39.000, 70.000, Point.CARTESIAN);
    public Point desiredSpecControl1 = new Point(22.000, 3.000, Point.CARTESIAN);
    public Point desiredSpecControl2 = new Point(15.000, 72.000, Point.CARTESIAN);

    public static class ScoreSpecimen {
        public Point[] scorePoints = {
                new Point(39.000, 70.000, Point.CARTESIAN),
                new Point(39.000, 72.000, Point.CARTESIAN),
                new Point(39.000, 74.000, Point.CARTESIAN),
                new Point(39.000, 76.000, Point.CARTESIAN)
        };

        public Point[] controlPoints1 = {
                new Point(22.000, 3.000, Point.CARTESIAN),
                new Point(22.000, 5.000, Point.CARTESIAN),
                new Point(22.000, 7.000, Point.CARTESIAN),
                new Point(22.000, 9.000, Point.CARTESIAN),
        };

        public Point[] controlPoints2 = {
                new Point(15.000, 72.000, Point.CARTESIAN),
                new Point(15.000, 74.000, Point.CARTESIAN),
                new Point(15.000, 76.000, Point.CARTESIAN),
                new Point(15.000, 78.000, Point.CARTESIAN),
        };
    }

    public void setScorePoint(int cycle) {
        desiredScorePoint = S_S.scorePoints[cycle];
        desiredSpecControl2 = S_S.controlPoints2[cycle];
        desiredSpecControl1 = S_S.controlPoints1[cycle];
    }

    public PathChain line1 = f.pathBuilder()
            .addPath(
                    new BezierLine(
                            new Point(8.291, 65.000, Point.CARTESIAN),
                            new Point(39.000, 76.000, Point.CARTESIAN)
                    )
            )
            .setConstantHeadingInterpolation(Math.toRadians(0))
            .build();

    public Path pushingIntermediary =
            new Path(
                    new BezierCurve(
                            new Point(39.000, 76.000, Point.CARTESIAN),
                            new Point(30.000, 74.000, Point.CARTESIAN),
                            new Point(17.500, 41.000, Point.CARTESIAN),
                            new Point(52.000, 35.000, Point.CARTESIAN)
                    )
            );

    public Path pushingIntermediary2 =
            new Path(
                    new BezierCurve(
                            new Point(52.000, 35.000, Point.CARTESIAN),
                            new Point(60.000, 32.500, Point.CARTESIAN),
                            new Point(60.000, 23.000, Point.CARTESIAN)
                    )
            );

    public PathChain line2 = f.pathBuilder()
            .addPath(pushingIntermediary) // 0
            .setConstantHeadingInterpolation(Math.toRadians(0))
            .addPath(pushingIntermediary2) // 1
            .setConstantHeadingInterpolation(Math.toRadians(0))
            .build();

    public Path sample1 =
            new Path(
                    new BezierLine(
                            new Point(60.000, 23.000, Point.CARTESIAN),
                            new Point(20.000, 23.000, Point.CARTESIAN)
                    )
            );

    public Path goToSample2 =
            new Path(
                    new BezierCurve(
                            new Point(20.000, 23.000, Point.CARTESIAN),
                            new Point(62.000, 28.400, Point.CARTESIAN),
                            new Point(60.000, 13.000, Point.CARTESIAN)
                    )
            );

    public Path sample2 =
            new Path(
                    new BezierLine(
                            new Point(60.000, 13.000, Point.CARTESIAN),
                            new Point(20.000, 12.500, Point.CARTESIAN)
                    )
            );

    public Path goToSample3 =
            new Path(
                    new BezierCurve(
                            new Point(20.000, 12.500, Point.CARTESIAN),
                            new Point(57.000, 18.000, Point.CARTESIAN),
                            new Point(60.000, 7.500, Point.CARTESIAN)
                    )
            );

    public Path sample3 =
            new Path(
                    new BezierLine(
                            new Point(60.000, 7.500, Point.CARTESIAN),
                            new Point(8.300, 7.500, Point.CARTESIAN)
                    )
            );

    public PathChain line3 = f.pathBuilder()
            .addPath(sample1) // 0
            .setConstantHeadingInterpolation(Math.toRadians(0))
            .addPath(goToSample2) // 1
            .setConstantHeadingInterpolation(Math.toRadians(0))
            .addPath(sample2) // 2
            .setConstantHeadingInterpolation(Math.toRadians(0))
            .addPath(goToSample3) // 3
            .setConstantHeadingInterpolation(Math.toRadians(0))
            .addPath(sample3) // 4
            .setConstantHeadingInterpolation(Math.toRadians(0))
            .build();

    public Path grabSpec =
            new Path(
                    new BezierCurve(
                            new Point(robot.drivetrain.getPose()),
                            new Point(17.250, 63.000, Point.CARTESIAN),
                            new Point(32.000, 51.000, Point.CARTESIAN),
                            new Point(18.000, 24.250, Point.CARTESIAN),
                            new Point(9.000, 27.500, Point.CARTESIAN)
                    )
            );

    public PathChain firstSpec = line1;
    public PathChain getToSamples = line2;
    public PathChain giveSamples = line3;
}
