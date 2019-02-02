package hr.fer.zemris.particles.example;

import com.jogamp.opengl.util.texture.Texture;
import hr.fer.zemris.graphics.common.Vertex3D;
import hr.fer.zemris.particles.ParticleSystem;
import hr.fer.zemris.particles.TextureParticle;

import java.util.Random;

public class SnowParticleSystem extends ParticleSystem {
    public static final Random rand = new Random();

    private float particleSpeed;
    private int particleLifetime;
    private float disorderFactor;

    private final Vertex3D systemPosition;
    private final float systemSize;

    private Texture snowTexture;
    private float particleSize;

    private float xForce;
    private float zForce;

    public class SnowParticle extends TextureParticle {
        private final float speed;

        public SnowParticle(Vertex3D position, int lifetime, float speed) {
            super(position, lifetime, snowTexture, particleSize, particleSize);
            this.speed = speed;
        }

        @Override
        public void update() {
            position = new Vertex3D(position.x + xForce, position.y + speed, position.z + zForce);
            width = (float) lifetime/particleLifetime * particleSize;
            height = (float) lifetime/particleLifetime * particleSize;
            super.update();
        }
    }

    public SnowParticleSystem(int amount, int period, float averageParticleSpeed, int averageLifetime,
                              float disorderFactor, float particleSize, Vertex3D systemPosition, float systemSize) {
        super(amount, period);

        this.particleSpeed = averageParticleSpeed;
        this.particleLifetime = averageLifetime;
        this.disorderFactor = disorderFactor;

        this.particleSize = particleSize;

        this.systemPosition = systemPosition;
        this.systemSize = systemSize;
    }

  public void addZForce(float force) {
    this.zForce += force;
  }

  public void addXForce(float force) {
    this.xForce += force;
  }

  @Override
  public void update() {
    super.update();
    if (Math.abs(this.zForce - 0f) > 0.001f)
      this.zForce = this.zForce - Math.signum(this.zForce)*0.05f;
    if (Math.abs(this.xForce - 0f) > 0.001f)
      this.xForce = this.xForce - Math.signum(this.xForce)*0.05f;
  }

  public void setSnowTexture(Texture snowTexture) {
        this.snowTexture = snowTexture;
    }

    @Override
    public SnowParticle generateParticle() {
        Vertex3D position = new Vertex3D(
                systemPosition.x + getDisorderedFloat(systemSize, 1f),
                systemPosition.y,
                systemPosition.z + getDisorderedFloat(systemSize, 1f));
        float speed = Math.max(0.001f, particleSpeed + getDisorderedFloat(particleSpeed, disorderFactor));
        int lifetime = Math.max(5, particleLifetime + getDisorderedInt(particleLifetime, disorderFactor));
        return new SnowParticle(position, lifetime, speed);
    }

    private float getDisorderedFloat(float value, float factor) {
        return (rand.nextFloat() - 0.5f) * factor * value;
    }

    private int getDisorderedInt(int value, float factor) {
        return (int)((rand.nextFloat() - 0.5f) * factor * value);
    }

    public Vertex3D getSystemPosition() {
        return systemPosition;
    }

    public float getSystemSize() {
        return systemSize;
    }
}
