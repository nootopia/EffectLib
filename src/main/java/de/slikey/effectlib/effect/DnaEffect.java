package de.slikey.effectlib.effect;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectType;
import de.slikey.effectlib.EffectManager;
import de.slikey.effectlib.util.MathUtils;
import de.slikey.effectlib.util.VectorUtils;

public class DnaEffect extends Effect {

    /**
     * ParticleType of spawned particle
     */
    public Particle particleHelix = Particle.FLAME;
    public Color colorHelix = null;

    /**
     * Particle of base 1
     */
    public Particle particleBase1 = Particle.FISHING;
    public Color colorBase1 = null;

    /**
     * Particle of base 2
     */
    public Particle particleBase2 = Particle.DUST;
    public Color colorBase2 = null;

    /**
     * Radials to turn per step
     */
    public double radials = Math.PI / 30;

    /**
     * Radius of dna-double-helix
     */
    public float radius = 1.5F;

    /**
     * Particles to spawn per interation
     */
    public int particlesHelix = 3;

    /**
     * Particles per base
     */
    public int particlesBase = 15;

    /**
     * Length of the dna-double-helix
     */
    public float length = 15;

    /**
     * Growth per particle
     */
    public float grow = 0.2F;

    /**
     * Particles between every base
     */
    public float baseInterval = 10;

    /**
     * Current step. Works as counter
     */
    protected int step = 0;

    public DnaEffect(EffectManager effectManager) {
        super(effectManager);
        type = EffectType.REPEATING;
        period = 1;
        iterations = 500;
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

        double angle;
        Vector v;
        Particle particle;
        Color color;

        for (int j = 0; j < particlesHelix; j++) {
            if (step * grow > length) step = 0;

            for (int i = 0; i < 2; i++) {
                angle = step * radials + Math.PI * i;
                v = new Vector(Math.cos(angle) * radius, step * grow, Math.sin(angle) * radius);
                drawParticle(location, v, particleHelix, colorHelix);
            }

            if (step % baseInterval == 0) {
                for (int i = -particlesBase; i <= particlesBase; i++) {
                    if (i == 0) continue;

                    particle = particleBase1;
                    color = colorBase1;
                    if (i < 0) {
                        particle = particleBase2;
                        color = colorBase2;
                    }
                    angle = step * radials;
                    v = new Vector(Math.cos(angle), 0, Math.sin(angle)).multiply(radius * i / particlesBase).setY(step * grow);
                    drawParticle(location, v, particle, color);
                }
            }
            step++;
        }
    }

    protected void drawParticle(Location location, Vector v, Particle particle, Color color) {
        VectorUtils.rotateAroundAxisX(v, (location.getPitch() + 90) * MathUtils.degreesToRadians);
        VectorUtils.rotateAroundAxisY(v, -location.getYaw() * MathUtils.degreesToRadians);

        location.add(v);
        display(particle, location, color);
        location.subtract(v);
    }

}
