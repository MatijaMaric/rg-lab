package hr.fer.zemris.graphics.common;

public class Camera {
    public final Vertex3D eye;
    public final Vertex3D center;
    public final Vertex3D viewUp;

    public Camera(Vertex3D eye, Vertex3D center, Vertex3D viewUp) {
        this.eye = eye;
        this.center = center;
        this.viewUp = viewUp;
    }
}
