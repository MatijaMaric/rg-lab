package hr.fer.zemris.particles;

import com.jogamp.opengl.GL2;
import hr.fer.zemris.graphics.common.Camera;
import hr.fer.zemris.graphics.common.Vertex3D;

public abstract class Particle {
    public Vertex3D position;
    public int lifetime;

    public Particle(Vertex3D position, int lifetime) {
        this.position = position;
        this.lifetime = lifetime;
    }

    public void age() {
        this.lifetime--;
    }

    public void update() {
        age();
    }

    public boolean isAlive() {
        return lifetime > 0;
    }

    public abstract void render(GL2 gl, Camera camera);

    public int getLifetime() {
        return lifetime;
    }

    public Vertex3D getPosition() {
        return position;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public void setPosition(Vertex3D position) {
        this.position = position;
    }
}
