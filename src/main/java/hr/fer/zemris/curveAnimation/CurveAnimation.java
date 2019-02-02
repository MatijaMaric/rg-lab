package hr.fer.zemris.curveAnimation;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import hr.fer.zemris.curve.CubicBSpline;
import hr.fer.zemris.graphics.common.*;
import hr.fer.zemris.linalg.IVector;
import hr.fer.zemris.linalg.Vector;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class CurveAnimation implements GLEventListener, Runnable {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new CurveAnimation());
    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter the path to curve file: ");
        List<String> curveLines = null;
        try {
            String path = br.readLine();
            curveLines = Files.readAllLines(Paths.get(path));
        } catch (IOException ex) {
            System.out.println("Problem occurred while reading curve file.");
            System.exit(1);
        }
        curve = GraphicsUtil.parseCubicBSpline(curveLines);
        List<CubicBSpline.PointAndTangent> pointAndTangents = curve.getPointsAndTangents();
        this.pointAndTangents = pointAndTangents;
        points = pointAndTangents.stream().map(pointAndTangent -> pointAndTangent.point).collect(Collectors.toList());
        tangents = pointAndTangents.stream().map(pointAndTangent -> pointAndTangent.tangent).collect(Collectors.toList());

        System.out.println("Enter the path to OBJ file: ");
        List<String> OBJLines = null;
        try {
            String path = br.readLine();
            OBJLines = Files.readAllLines(Paths.get(path));
        } catch (IOException ex) {
            System.out.println("Problem occurred while reading obj file.");
            System.exit(1);
        }
        object = GraphicsUtil.parseOBJ(OBJLines);
        object.normalize();
        object.computeVertexNormals();

        GLProfile profile = GLProfile.getDefault();
        GLCapabilities capabilities = new GLCapabilities(profile);
        final GLCanvas canvas = new GLCanvas(capabilities);

        canvas.addGLEventListener(this);

        final JFrame frame = new JFrame("Curve animation");
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

        FPSAnimator animator = new FPSAnimator(canvas, 60, true);
        animator.start();
    }

    CubicBSpline curve;
    List<Vertex3D> points;
    List<IVector> tangents;
    List<CubicBSpline.PointAndTangent> pointAndTangents;

    ObjectModel object;

    private IVector defaultOrientation = new Vector(new double[]{0, 0, 1});
    private int index = 0;

    private void update() {
        index++;
        if (index >= tangents.size()) {
            index = tangents.size() - 1;
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = GLU.createGLU(gl);

        gl.glClearColor(1, 1, 1, 0);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glLoadIdentity();
        Vertex3D eye = new Vertex3D(0, 25, -50);
        Vertex3D center = new Vertex3D(0, 25, 0);
        Vertex3D viewUp = new Vertex3D(0, 1, 0);
        glu.gluLookAt(
                eye.x, eye. y, eye.z,
                center.x, center.y, center.z,
                viewUp.x, viewUp.y, viewUp.z
        );

        gl.glEnable(GL2.GL_LIGHTING);
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, new float[] {0.0f, 0.0f, 0.0f, 1f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float[] {-50f, 5f, 25f, 1f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, new float[] {0.5f, 0.5f, 0.5f, 1f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] {0.8f, 0.8f, 0f, 1f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float[] {0f, 0f, 0f, 1f}, 0);
        gl.glEnable(GL2.GL_LIGHT0);

        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glEnable(GL2.GL_DEPTH_TEST);

        renderScene(gl);

        update();
    }

    private void renderScene(GL2 gl) {
        drawCurve(gl);
        drawObject(gl);
    }

    private void drawCurve(GL2 gl) {
        gl.glColor3f(0, 0.7f, 0.3f);
        gl.glBegin(GL2.GL_LINE_STRIP);
        for (Vertex3D v : points) {
            gl.glVertex3d(v.x, v.y, v.z);
        }
        gl.glEnd();

        for (CubicBSpline.PointAndTangent pt : pointAndTangents) {
            gl.glBegin(GL.GL_LINE_STRIP);
            gl.glVertex3d(pt.point.x, pt.point.y, pt.point.z);
            gl.glVertex3d(pt.point.x + pt.tangent.get(0), pt.point.y + pt.tangent.get(1), pt.point.z + pt.tangent.get(2));
            gl.glEnd();
        }
    }



    private void drawObject(GL2 gl) {
        Vertex3D point = points.get(index);

        //draw tangent
        IVector tangent = tangents.get(index);
        gl.glColor3f(255, 0, 0);
        gl.glBegin(GL.GL_LINES);
        gl.glVertex3d(point.x, point.y, point.z);
        gl.glVertex3d(point.x + tangent.get(0), point.y + tangent.get(1), point.z + tangent.get(2));
        gl.glEnd();

        gl.glTranslated(point.x, point.y, point.z);
        gl.glPushMatrix();

            IVector axis = defaultOrientation.nVectorProduct(tangent);
            double cosFi = defaultOrientation.cosine(tangent);

            gl.glRotated(Math.toDegrees(Math.acos(cosFi)), axis.get(0), axis.get(1), axis.get(2));

            gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
            gl.glEnable(GL2.GL_CULL_FACE);
            gl.glCullFace(GL2.GL_BACK);

            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float[] {1f, 1f, 1f, 1f}, 0);
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float[] {1f, 1f, 1f, 1f}, 0);
            gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, new float[] {0.01f, 0.01f, 0.01f, 1f}, 0);
            gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 96f);

            for (Face3D face : object.getFaces()) {
                gl.glBegin(GL2.GL_POLYGON);
                IVector normal;
                for(Vertex3D v : face.getVertices()) {
                    normal = v.getNormal();
                    gl.glNormal3d(normal.get(0), normal.get(1), normal.get(2));
                    gl.glVertex3d(v.x, v.y, v.z);
                }
                gl.glEnd();
            }

        gl.glPopMatrix();

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

    @Override
    public void init(GLAutoDrawable drawable) {
    }
}
