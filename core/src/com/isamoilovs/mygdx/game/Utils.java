package com.isamoilovs.mygdx.game;

public class Utils {
    public static final float pi = 90;
    public static final float pi2 = 180;
    public static final float piD2 = 45;
    public static final float pi3D2 = 270;

    public static float getAngle(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.toDegrees((float) Math.atan2(dy, dx));
    }

    public static float makeRotation(float angleFrom, float angleTo, float rotationSpeed, float dt) {
        if (angleFrom < angleTo) {
            if (Math.abs(angleFrom - angleTo) < 180) {
                angleFrom += rotationSpeed * dt;
            } else {
                angleFrom -= rotationSpeed * dt;
            }
        }
        if (angleFrom > angleTo) {
            if (Math.abs(angleFrom - angleTo) < 180) {
                angleFrom -= rotationSpeed * dt;
            } else {
                angleFrom += rotationSpeed * dt;
            }
        }
        if (Math.abs(angleFrom - angleTo) < 1.5f) {
            angleFrom = angleTo;
        }
        return angleFrom;
    }

    public static float checkAngleValue(float ang) {
        while (ang < -180 || ang > 180) {
            if (ang > 180) {
                ang -= 360;
            }
            if(ang < -180) {
                ang += 360;
            }
        }
        return ang;
    }
}
