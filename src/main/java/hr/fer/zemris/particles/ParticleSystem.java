package hr.fer.zemris.particles;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.graphics.common.Camera;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ParticleSystem {
    private List<Particle> particles;
    private int period;
    private int amount;

    private int framesToNextGeneration;

    public ParticleSystem(int amount, int period) {
        this.period = period;
        this.amount = amount;
        this.framesToNextGeneration = 0;

        particles = new ArrayList<>();
    }

    public void render(GL2 gl, Camera camera) {
        particles.forEach(particle -> particle.render(gl, camera));
    }

    public void update() {
        particles.forEach(Particle::update);
        particles = particles.stream().filter(Particle::isAlive).collect(Collectors.toList());
        framesToNextGeneration--;
        if (framesToNextGeneration <= 0) {
            for (int i = 0; i < this.amount; i++) {
                particles.add(generateParticle());
            }
            framesToNextGeneration = this.period;
        }
    }

    public abstract Particle generateParticle();

    public void addParticle(Particle particle) {
        this.particles.add(particle);
    }
}
