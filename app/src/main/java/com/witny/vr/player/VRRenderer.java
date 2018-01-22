package com.witny.vr.player;

import android.content.Context;
import android.util.Log;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;
import com.google.vr.sdk.base.GvrView;

import org.rajawali3d.Object3D;
import org.rajawali3d.math.Matrix4;
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.Renderer;

import javax.microedition.khronos.egl.EGLConfig;

public abstract class VRRenderer extends Renderer implements GvrView.StereoRenderer {
    private static final float MAX_LOOKAT_ANGLE = 10;

    protected Matrix4 mCurrentEyeMatrix;
    protected Matrix4 mHeadViewMatrix;
    protected Quaternion mCurrentEyeOrientation;
    protected Quaternion mHeadViewQuaternion;
    protected Vector3 mCameraPosition;
    private Vector3 mForwardVec;
    private Vector3 mHeadTranslation;


    private Matrix4 mLookingAtMatrix;
    private float[] mHeadView;

	public VRRenderer(Context context) {
		super(context);
        mCurrentEyeMatrix = new Matrix4();
        mHeadViewMatrix = new Matrix4();
        mLookingAtMatrix = new Matrix4();
        mCurrentEyeOrientation = new Quaternion();
        mHeadViewQuaternion = new Quaternion();
        mHeadView = new float[16];
        mCameraPosition = new Vector3();
        mForwardVec = new Vector3();
        mHeadTranslation = new Vector3();
	}

    @Override
    public void onNewFrame(HeadTransform headTransform) {
        headTransform.getHeadView(mHeadView, 0);
        mHeadViewMatrix.setAll(mHeadView);
        Log.i("VRRenderer", "onNewFrame");
    }

    @Override
    public void onDrawEye(Eye eye) {
        getCurrentCamera().updatePerspective(
                eye.getFov().getLeft(),
                eye.getFov().getRight(),
                eye.getFov().getBottom(),
                eye.getFov().getTop());
        float[] eyeview = eye.getEyeView();
        float[] newEyeview = new float[]{
                 eyeview[ 0],  eyeview[ 4],  eyeview[ 8],  eyeview[12],
                 eyeview[ 1],  eyeview[ 5],  eyeview[ 9],  eyeview[13],
                 eyeview[ 2],  eyeview[ 6],  eyeview[10],  eyeview[14],
                 eyeview[ 3],  eyeview[ 7],  eyeview[11],  eyeview[15] };
        mCurrentEyeMatrix.setAll(newEyeview); // eye.getEyeView());
        mCurrentEyeOrientation.fromMatrix(mCurrentEyeMatrix);
        getCurrentCamera().setOrientation(mCurrentEyeOrientation);
        getCurrentCamera().setPosition(mCameraPosition);
        getCurrentCamera().getPosition().add(mCurrentEyeMatrix.getTranslation().inverse());
        super.onRenderFrame(null);
    }

    @Override
    public void onFinishFrame(Viewport viewport) {
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        super.onRenderSurfaceSizeChanged(null, width, height);
    }

    @Override
    public void onSurfaceCreated(EGLConfig eglConfig) {
        super.onRenderSurfaceCreated(eglConfig, null, -1, -1);
    }

    @Override
    public void onRendererShutdown() {
        super.onRenderSurfaceDestroyed(null);
    }

    public boolean isLookingAtObject(Object3D target) {
        return this.isLookingAtObject(target, MAX_LOOKAT_ANGLE);
    }

    public boolean isLookingAtObject(Object3D target, float maxAngle) {
        mHeadViewQuaternion.fromMatrix(mHeadViewMatrix);
        mHeadViewQuaternion.inverse();
        mForwardVec.setAll(0, 0, 1);
        mForwardVec.rotateBy(mHeadViewQuaternion);

        mHeadTranslation.setAll(mHeadViewMatrix.getTranslation());
        mHeadTranslation.subtract(target.getPosition());
        mHeadTranslation.normalize();

        return mHeadTranslation.angle(mForwardVec) < maxAngle;
    }
}
