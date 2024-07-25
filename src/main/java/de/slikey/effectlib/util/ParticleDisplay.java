package de.slikey.effectlib.util;

import java.util.List;

import de.slikey.effectlib.util.versions.ParticleDisplay_21;
import org.bukkit.Color;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.slikey.effectlib.EffectManager;

public abstract class ParticleDisplay {

    protected static EffectManager manager;

    private static boolean hasColorTransition = false;
    private static boolean hasColorDataType = false;

    public abstract void display(Particle particle, ParticleOptions options, Location center, double range, List<Player> targetPlayers);

    protected void spawnParticle(Particle particle, ParticleOptions options, Location center, double range, List<Player> targetPlayers) {
        try {
            if (targetPlayers == null) {
                double squared = range * range;
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    if (!manager.isVisiblePlayer(player, center, squared)) continue;

                    if (hasColorDataType && particle == Particle.valueOf("ENTITY_EFFECT")) {
                        player.spawnParticle(particle, center, options.amount, options.offsetX, options.offsetY, options.offsetZ, options.speed, options.color);
                    } else {
                        player.spawnParticle(particle, center, options.amount, options.offsetX, options.offsetY, options.offsetZ, options.speed, options.data);
                    }

                    displayFakeBlock(player, center, options);
                }
                return;
            }

            for (final Player player : targetPlayers) {
                if (manager.isPlayerIgnored(player)) continue;
                player.spawnParticle(particle, center, options.amount, options.offsetX, options.offsetY, options.offsetZ, options.speed, options.data);
                displayFakeBlock(player, center, options);
            }

        } catch (Exception ex) {
            if (manager != null) manager.onError(ex);
        }
    }

    protected void displayFakeBlock(final Player player, Location center, ParticleOptions options) {
        // Implemented in 1.13+
    }

    @SuppressWarnings({"deprecation"})
    protected void displayItem(Particle particle, ParticleOptions options, Location center, double range, List<Player> targetPlayers) {
        Material material = options.material;
        if (material == null || material.isAir()) return;

        ItemStack item = new ItemStack(material);
        item.setDurability(options.materialData);
        options.data = item;
        spawnParticle(particle, options, center, range, targetPlayers);
    }

    protected void displayLegacyColored(Particle particle, ParticleOptions options, Location center, double range, List<Player> targetPlayers) {
        // Colored particles can't have a speed of 0.
        Color color = options.color;
        if (color == null) color = Color.RED;
        if (options.speed == 0) options.speed = 1;
        // Amount = 0 is a special flag that means use the offset as color
        options.amount = 0;

        float offsetX = (float) color.getRed() / 255;
        float offsetY = (float) color.getGreen() / 255;
        float offsetZ = (float) color.getBlue() / 255;

        // The redstone particle reverts to red if R is 0!
        if (offsetX < Float.MIN_NORMAL) offsetX = Float.MIN_NORMAL;

        options.offsetX = offsetX;
        options.offsetY = offsetY;
        options.offsetZ = offsetZ;

        spawnParticle(particle, options, center, range, targetPlayers);
    }

    public void setManager(EffectManager manager) {
        this.manager = manager;
    }

    public static ParticleDisplay newInstance() {
        ParticleDisplay display;

        try {
            Particle.valueOf("DUST");
            display = new ParticleDisplay_21();
            hasColorTransition = true;
            hasColorDataType = true;
        } catch (Throwable not21) {
            display = null;
            manager.getLogger().warning("Unsupported server version, please update to 1.21.");
        }

        return display;
    }

    public static boolean hasColorTransition() {
        return hasColorTransition;
    }

}
