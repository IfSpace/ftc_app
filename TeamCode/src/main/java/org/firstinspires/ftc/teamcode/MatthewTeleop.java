/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Matthew Iterative OpMode", group="Iterative Opmode")
//@Disabled
public class MatthewTeleop extends OpMode
{
    // Declare OpMode members.
    InvadersRelicRecoveryBot robot = new InvadersRelicRecoveryBot();
    private ElapsedTime runtime = new ElapsedTime();


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        robot.init(this);
        telemetry.addData("Status", "Initialized");
        //robot.leftGrab.setDirection(Servo.Direction.FORWARD);
        //robot.rightGrab.setDirection(Servo.Direction.REVERSE);



    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }
    boolean fineMode = false;
    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }


    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Setup a variable for each drive wheel to save power level for telemetry
        double leftPower;
        double rightPower;
        double armPower;
        double armPos;
        robot.rightDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.leftDrive.setDirection(DcMotorSimple.Direction.FORWARD);


        // Choose to drive using either Tank Mode, or POV Mode
        // Comment out the method that's not used.  The default below is POV.

        // POV Mode uses left stick to go forward, and right stick to turn.
        // - This uses basic math to combine motions and is easier to drive straight.
        double drive = gamepad1.left_stick_y;
        double turn  =  gamepad1.right_stick_x;


        armPos = robot.liftMotor.getCurrentPosition();

        if(gamepad1.a == true){
            fineMode = false;
        }
        if (gamepad1.b == true){
            fineMode = true;
        }

        // Tank Mode uses one stick to control each wheel.
        // - This requires no math, but it is hard to drive forward slowly and keep straight.
         leftPower  = -gamepad1.left_stick_y ;
         rightPower = -gamepad1.right_stick_y ;

        //This is where we figure out if we should switch the direction of the controls or not.
        //This initial if statement keeps us from suddenly reversing when the arm passes 180. The direction change only takes effect once the driver has stopped moving the robot.
        //@ TODO: 11/5/2017 Figure out how to make it so that the controls don't change unless we are stopped. This will be tricky because of the way this loop works.
            if (armPos < 180) {
                if(fineMode == true){
                    leftPower = Range.clip(drive - turn, -0.3, 0.3);
                    rightPower = Range.clip(drive + turn, -0.3, 0.3);
                    robot.leftDrive.setPower(leftPower);
                    robot.rightDrive.setPower(rightPower);

                }
                else{
                    leftPower = Range.clip(drive - turn, -1.0, 1.0);
                    rightPower = Range.clip(drive + turn, -1.0, 1.0);
                    robot.leftDrive.setPower(leftPower);
                    robot.rightDrive.setPower(rightPower);

                }


            } else {
                if(fineMode == true){
                    robot.leftDrive.setPower(leftPower/2);
                    robot.rightDrive.setPower(rightPower/2);
                } else{
                    robot.leftDrive.setPower(leftPower);
                    robot.rightDrive.setPower(rightPower);
                }

            }


        robot.liftMotor.setPower(gamepad1.left_trigger);
        robot.liftMotor.setPower(-gamepad1.right_trigger);

        if (gamepad1.right_bumper){
           robot.CRGripper.setPower(-1);
        } else if(gamepad1.left_bumper){
            robot.CRGripper.setPower(1);
        }
        else{
            robot.CRGripper.setPower(0);
        }




        //robot.leftGrab.setPosition(gamepad1.left_trigger);
        //robot.rightGrab.setPosition(gamepad1.right_trigger);

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        //telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
