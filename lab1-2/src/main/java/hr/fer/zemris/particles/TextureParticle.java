package hr.fer.zemris.particles;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import hr.fer.zemris.graphics.common.Camera;
import hr.fer.zemris.graphics.common.Vertex3D;
import hr.fer.zemris.linalg.IVector;
import hr.fer.zemris.linalg.Vector;

public abstract class TextureParticle extends Particle {
    private static final IVector NORMAL = new Vector(new double[]{0f, 1f, 0f});

    public float width;
    public float height;

    private Texture texture;

    public TextureParticle(Vertex3D position, int lifetime, Texture texture, float width, float height) {
        super(position, lifetime);
        this.texture = texture;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(GL2 gl, Camera camera) {
        gl.glPushMatrix();
            gl.glTranslated(position.x, position.y, position.z);

            IVector toEye = camera.eye.vector().sub(position.vector());
            IVector axis = NORMAL.nVectorProduct(toEye);
            double cosFi = NORMAL.cosine(toEye);

            gl.glRotated(Math.toDegrees(Math.acos(cosFi)), axis.get(0), axis.get(1), axis.get(2));

            texture.enable(gl);
            texture.bind(gl);

            gl.glColor3f(1, 1, 1);
            gl.glBegin(GL2.GL_QUADS);
                gl.glNormal3f(0, 1, 0);
                gl.glTexCoord2d(0.0, 0.0);
                gl.glVertex3d(-width/2, 0, -height/2);
                gl.glTexCoord2d(1.0, 0.0);
                gl.glVertex3d(width/2, 0, -height/2);
                gl.glTexCoord2d(1.0, 1.0);
                gl.glVertex3d(width/2, 0, height/2);
                gl.glTexCoord2d(0.0, 1.0);
                gl.glVertex3d(-width/2, 0, height/2);
            gl.glEnd();

        gl.glPopMatrix();
    }
}
