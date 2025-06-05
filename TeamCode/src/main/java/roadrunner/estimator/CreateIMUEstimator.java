package roadrunner.estimator;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public interface CreateIMUEstimator extends Estimator {
    void startIMUThread(LinearOpMode opMode);
}
