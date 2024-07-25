package de.slikey.effectlib.effect;

/**
 * Idea by coco5843
 */

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.RandomUtils;

public class DiscoBallEffect extends Effect {

    /**
     * Radius of the sphere
     */
    public float sphereRadius = 0.6F;

    /**
     * Min and max sizes of the lines
     */
    public int max = 15;

    /**
     * Particle of the sphere and of the lines
     */
    public Particle sphereParticle = Particle.FLAME;
    public Particle lineParticle = Particle.DUST;

    public Color sphereColor = null;
    public Color lineColor = null;

    /**
     * Max number of lines
     */
    public int maxLines = 7;

    /**
     * Max number of particles per line
     */
    public int lineParticles = 100;
    public int sphereParticles = 50;

    /**
     * Direction of the lines
     */
    public Direction direction = Direction.DOWN;

    public DiscoBallEffect(EffectManager manager) {
        super(manager);
        type = EffectType.REPEATING;
        period = 7;
        iterations = 500;
    }

    public void onRun() {
        Location location = getLocation();

        if (location == null) {
            cancel();
            return;
        }

        //Lines
        int mL = RandomUtils.random.nextInt(maxLines - 2) + 2;

        double x;
        double y;
        double z;

        Location loc;
        Location target;

        Vector v;
        Vector link;
        Vector vector;

        float length;
        float ratio;

        for (int m = 0; m < mL * 2; m++) {
            x = RandomUtils.random.nextInt(max - max * (-1)) + max * (-1);
            y = RandomUtils.random.nextInt(max - max * (-1)) + max * (-1);
            z = RandomUtils.random.nextInt(max - max * (-1)) + max * (-1);

            if (direction == Direction.DOWN) y = RandomUtils.random.nextInt(max * 2 - max) + max;
            else if (direction == Direction.UP) y = RandomUtils.random.nextInt(max * (-1) - max * (-2)) + max * (-2);

            target = location.clone().subtract(x, y, z);

            link = target.toVector().subtract(location.toVector());
            length = (float) link.length();
            link.normalize();

            ratio = length / lineParticles;
            v = link.multiply(ratio);
            loc = location.clone().subtract(v);
            for (int i = 0; i < lineParticles; i++) {
                loc.add(v);
                display(lineParticle, loc, lineColor);
            }
        }

        //Sphere
        for (int i = 0; i < sphereParticles; i++) {
            vector = RandomUtils.getRandomVector().multiply(sphereRadius);
            location.add(vector);
            display(sphereParticle, location, sphereColor);
            location.subtract(vector);
        }
    }

    public enum Direction {

        UP, DOWN, BOTH;
    }

}
