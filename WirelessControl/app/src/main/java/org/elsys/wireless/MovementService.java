package org.elsys.wireless;

/**
 * Created by marto on 11/5/15.
 */
public class MovementService {
    public static NetworkService netService;

    public static boolean forward = false;
    public static boolean left = false;
    public static boolean right = false;
    public static boolean back = false;
    public static boolean stop = true;
    public static boolean straight = true;
    private static int lastSteerAngle;
    private static int lastSpeed;

    public static void init() {
        netService = new NetworkService();
    }

    public static void forward() {
        if (!forward && !back) {
            System.out.println("Command: forward");
            if(netService.isConnected()) {
                netService.write("forward\n");
                forward = true;
                stop = false;
            }
        }
    }

    public static void left() {
        if (!left && !right) {
            System.out.println("Command: left");
            if(netService.isConnected()) {
                netService.write("left\n");
                left = true;
                straight = false;
            }
        }
    }

    public static void right() {
        if (!left && !right) {
            System.out.println("Command: right");
            if(netService.isConnected()) {
                netService.write("right\n");
                right = true;
                straight = false;
            }
        }
    }

    public static void back() {
        if (!forward && !back) {
            System.out.println("Command: back");
            if(netService.isConnected()) {
                netService.write("back\n");
                back = true;
                stop = false;
            }
        }
    }

    public static void stop() {
        if (!stop) {
            if (netService.isConnected()) {
                System.out.println("Command: stop");
                netService.write("stop\n");
                forward = false;
                back = false;
                stop = true;
            }
        }
    }

    public static void straight() {
        if (!straight) {
            if (netService.isConnected()) {
                System.out.println("Command: straight");
                netService.write("straight\n");
                left = false;
                right = false;
                straight = true;
            }
        }
    }

    public static void setSpeed(int speed) {
        speed *= 2.55;
        if (speed > 255) speed = 255;
        if (speed < 1) speed = 1;
        if (lastSpeed != speed) {
            System.out.println("Command: speed: " + speed);
            lastSpeed = speed;
            if (netService.isConnected()) {
                netService.write("speed," + speed + "\n");
                if (forward) {
                    MovementService.stop();
                    MovementService.forward();
                }
                if (back) {
                    MovementService.stop();
                    MovementService.back();
                }
            }
        }
    }

    public static void steerByAngle(float delta) {
        // Tires are straight at 34
        double angle = 34;
        // Turn left
        if (delta < 0) {
            // 3.9 is multiplicator for left side angle
            angle -= 3.9 * delta;
            if (angle > 65) angle = 65;
        }
        // Turn right
        if (delta > 0) {
            // 4.1 is multiplicator for right side angle
            angle -= 4.1 * delta;
            if (angle < 1) angle = 1;
        }

        if (lastSteerAngle != (int) angle) {
            System.out.println("Command: steerByAngle: " + (int) angle);
            lastSteerAngle = (int) angle;
            if (netService.isConnected()) {
                netService.write("steerByAngle," + (int) angle + "\n");
            }
        }
    }

    public static void headlight(boolean on) {
        System.out.println("Command: headlights " + (on ? "on" : "off"));
        if (netService.isConnected()) {
            netService.write("headlights," + (on ? "on" : "off") + "\n");
        }
    }
}
