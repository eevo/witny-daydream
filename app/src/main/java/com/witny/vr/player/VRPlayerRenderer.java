package com.witny.vr.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;
import android.view.MotionEvent;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.Camera;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.materials.textures.TextureManager;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.scene.Scene;
import org.rajawali3d.util.RajLog;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by jake on 1/14/18.
 */

public class VRPlayerRenderer extends VRRenderer {

  private static final String TAG = "VRPlayerRenderer";
  private Camera camera;
  private Scene scene;
  private Sphere sphere;


  public VRPlayerRenderer(Context context) {
    super(context);

    // Allow Rajawali debug logs
    RajLog.setDebugEnabled(true);
  }

  @Override
  public void onNewFrame(HeadTransform headTransform) {
  }

//  @Override
//  public void onDrawEye(Eye eye) {
//  }

  @Override
  public void onFinishFrame(Viewport viewport) {
    super.onFinishFrame(viewport);
  }

  @Override
  public void onTouchEvent(MotionEvent motionEvent) {}

  /**
   * Create all objects, materials, textures, etc. The engine is guaranteed to be set up at this
   * point, whereas it may not be ready in onCreate or elsewhere.
   */
  @Override
  public void initScene() {
    Log.v(TAG, "initScene()");

    // Set up the image sphere
    sphere = new Sphere(10,50,50);
    sphere.setPosition(0, 0, -2);
    sphere.enableLookAt();
    sphere.setLookAt(0, 0, 0);
    sphere.setDoubleSided(true);
    sphere.setBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    sphere.setBlendingEnabled(true);
    sphere.setVisible(true);
    // plane.setTransparent(true);

    // Create a texture and material
    Texture texture = new Texture("background");
    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.raw.background);
    texture.setBitmap(bitmap);
    Material material = new Material(true);
    try {
      material.addTexture(texture);
    } catch (ATexture.TextureException e) {
      Log.e(TAG, "Exception while adding the texture: ", e);
    }

    material.setColorInfluence(1);
    material.setColor(1);
    material.enableLighting(false);

    sphere.setMaterial(material);

    scene = getCurrentScene();
    camera = getCurrentCamera();
    scene.addChild(sphere);
  }

  /**
   * Only relevant when rendering a live wallpaper, so we just override and ignore.
   */
  @Override
  public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep,
                               float yOffsetStep, int xPixelOffset, int yPixelOffset) {}

}
