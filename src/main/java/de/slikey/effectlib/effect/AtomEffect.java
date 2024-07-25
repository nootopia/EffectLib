package de.slikey.effectlib.effect;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.RandomUtils;
import de.slikey.effectlib.util.VectorUtils;

public class AtomEffect extends Effect {

    /**
     * ParticleType of the nucleus
     */
    public Particle particleNucleus = Particle.DRIPPING_WATER;
    public Color colorNucleus = null;

    /**
     * ParticleType of orbitals
     */
    public Particle particleOrbital = Particle.DRIPPING_LAVA;
    public Color colorOrbital = null;

    /**
     * Radius of the atom
     */
    public double radius = 3;

    /**
     * Radius of the nucleus as a fraction of the atom-radius
     */
    public float radiusNucleus = 0.2f;

    /**
     * Particles to be spawned in the nucleus per iteration
     */
    public int particlesNucleus = 10;

    /**
     * Particles to be spawned per orbital per iteration
     */
    public int particlesOrbital = 10;

    /**
     * Orbitals around the nucleus
     */
    public int orbitals = 3;

    /**
     * Rotation around the Y-axis
     */
    public double rotation = 0;

    /**
     * Whether or not to orient to the direction of the source location
     */
    public boolean orient = false;

    /**
     * Velocity of the orbitals
     */
    public double angularVelocity = Math.PI / 80D;

    /**
     * Internal counter
     */
    protected int step = 0;

    public AtomEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 2;
        iterations = 200;
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

        Vector v;
        double angle;
        double xRotation;

        for (int i = 0; i < particlesNucleus; i++) {
            v = RandomUtils.getRandomVector().multiply(radius * radiusNucleus);
            if (orient) v = VectorUtils.rotateVector(v, location);

            location.add(v);
            display(particleNucleus, location, colorNucleus);
            location.subtract(v);
        }

        for (int i = 0; i < particlesOrbital; i++) {
            angle = step * angularVelocity;
            for (int j = 0; j < orbitals; j++) {
                xRotation = (Math.PI / orbitals) * j;
                v = new Vector(Math.cos(angle), Math.sin(angle), 0).multiply(radius);

                VectorUtils.rotateAroundAxisX(v, xRotation);
                VectorUtils.rotateAroundAxisY(v, rotation);
                if (orient) v = VectorUtils.rotateVector(v, location);

                location.add(v);
                display(particleOrbital, location, colorOrbital);
                location.subtract(v);
            }
            step++;
        }
    }

}
