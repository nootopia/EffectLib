package de.slikey.effectlib.util;

import org.bukkit.Color;
import org.bukkit.Material;

public class ParticleOptions {
    public Color color;
    public Color toColor;
    public float offsetX;
    public float offsetY;
    public float offsetZ;
    public float speed;
    public int amount;
    public float size;
    public Material material;
    public byte materialData;
    public Object data;

    public ParticleOptions() {

    }

    public ParticleOptions(float offsetX, float offsetY, float offsetZ, float speed, int amount, float size, Color color, Material material, byte materialData) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.amount = amount;
        this.size = size;
        this.color = color;
        this.material = material;
        this.materialData = materialData;
    }

    public ParticleOptions(float offsetX, float offsetY, float offsetZ, float speed, int amount, float size, Color color, Color toColor, Material material, byte materialData) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.amount = amount;
        this.size = size;
        this.color = color;
        this.toColor = toColor;
        this.material = material;
        this.materialData = materialData;
    }
}
