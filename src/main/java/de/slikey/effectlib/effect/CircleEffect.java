package de.slikey.effectlib.effect;

import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.VectorUtils;

public class CircleEffect extends Effect {

    /**
     * Whether or not to orient to the direction of the source location
     */
    public boolean orient = false;

    /**
     * Rotation of the torus.
     */
    public double xRotation, yRotation, zRotation = 0;

    /**
     * Turns the circle by this angle each iteration around the x-axis
     */
    public double angularVelocityX = Math.PI / 200;

    /**
     * Turns the circle by this angle each iteration around the y-axis
     */
    public double angularVelocityY = Math.PI / 170;

    /**
     * Turns the circle by this angle each iteration around the z-axis
     */
    public double angularVelocityZ = Math.PI / 155;

    /**
     * Radius of circle above head
     */
    public float radius = 0.4F;

    /**
     * Used to make a partial circle
     */
    public double maxAngle = Math.PI * 2;

    /**
     * Start at the same location each step, use this
     * along with maxAngle and wholeCircle to form persistent semicircles
     */
    public boolean resetCircle = false;

    /**
     * Current step. Works as a counter
     */
    protected float step = 0;

    /**
     * Subtracts from location if needed
     */
    public double xSubtract, ySubtract, zSubtract;

    /**
     * Should it rotate?
     */
    public boolean enableRotation = true;

    /**
     * Amount of particles per circle
     */
    public int particles = 20;

    /**
     * To make a whole circle each iteration
     */
    public boolean wholeCircle = false;

    public CircleEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        particle = Particle.HAPPY_VILLAGER;
        period = 2;
        iterations = 50;
    }

    @Override
    public void reset() {
        step = 0;
    }

    @Override
    public void onRun() {
        Location location = getLocation();

        if (location == null) {
            cancel();
            return;
        }

        location.subtract(xSubtract, ySubtract, zSubtract);
        double inc = maxAngle / particles;
        int steps = wholeCircle ? particles : 1;

        double angle;
        Vector v;

        for (int i = 0; i < steps; i++) {
            angle = step * inc;
            v = new Vector();

            v.setX(Math.cos(angle) * radius);
            v.setZ(Math.sin(angle) * radius);

            VectorUtils.rotateVector(v, xRotation, yRotation, zRotation);
            VectorUtils.rotateAroundAxisX(v, location.getPitch() * MathUtils.degreesToRadians);
            VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);

            if (enableRotation) {
                VectorUtils.rotateVector(v, angularVelocityX * step, angularVelocityY * step, angularVelocityZ * step);
            }

            if (orient) v = VectorUtils.rotateVector(v, location);

            display(particle, location.clone().add(v));
            step++;
        }

        if (resetCircle) step = 0;
    }

}
