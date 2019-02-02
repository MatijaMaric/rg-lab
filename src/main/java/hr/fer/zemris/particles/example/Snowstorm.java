package hr.fer.zemris.particles.example;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import hr.fer.zemris.graphics.common.Camera;
import hr.fer.zemris.graphics.common.Vertex3D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class Snowstorm implements GLEventListener, Runnable {

    private static final int FRAMERATE = 60;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Snowstorm());
    }

    @Override
    public void run() {
        GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas canvas = new GLCanvas(capabilities);

        canvas.addGLEventListener(this);

        final JFrame frame = new JFrame("Snowstorm");
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
        frame.getContentPane().add(canvas, BorderLayout.CENTER);
        frame.setSize(800, 800);
        frame.setVisible(true);
        canvas.requestFocusInWindow();

        canvas.addKeyListener(new KeyAdapter() {
          @Override
          public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            e.consume();
            if (keyCode == KeyEvent.VK_LEFT) {
              snow.addZForce(-0.1f);
            } else if (keyCode == KeyEvent.VK_RIGHT) {
              snow.addZForce(0.1f);
            } else if (keyCode == KeyEvent.VK_UP) {
              snow.addXForce(0.1f);
            } else if (keyCode == KeyEvent.VK_DOWN) {
              snow.addXForce(-0.1f);
            }
          }
        });

        Vertex3D eye = new Vertex3D(10, 10, 0);
        Vertex3D center = new Vertex3D(0, 10, 0);
        Vertex3D viewUp = new Vertex3D(0, 1, 0);
        camera = new Camera(eye, center, viewUp);

        Vertex3D systemPosition = new Vertex3D(-10, 30, 0);
        snow = new SnowParticleSystem(5, 1, 0.13f, FRAMERATE * 2, 0.8f, 0.5f, systemPosition, 20);

        FPSAnimator animator = new FPSAnimator(canvas, FRAMERATE, true);
        animator.start();
    }

    //--------------------------------------------------------------------

    private SnowParticleSystem snow;
    private Camera camera;

    @Override
    public void init(GLAutoDrawable drawable) {
        try {
            Texture snowTexture = TextureIO.newTexture(new File("textures/snow.png"), true);
            snow.setSnowTexture(snowTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = GLU.createGLU(gl);

        gl.glClearColor(0, 0, 0, 0);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();

        glu.gluLookAt(
                camera.eye.x, camera.eye.y, camera.eye.z,
                camera.center.x, camera.center.y, camera.center.z,
                camera.viewUp.x, camera.viewUp.y, camera.viewUp.z
        );
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        snow.render(gl, camera);

        update();
    }

    private void update() {
        snow.update();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();

        gl2.glFrustum(-0.5, 0.5, -0.5, 0.5, 1, 100);

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glViewport(0, 0, width, height);
    }


    @Override
    public void dispose(GLAutoDrawable drawable) {
    }
}

