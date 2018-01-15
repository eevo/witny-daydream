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
  private Plane plane;

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

    // Set up the image plane
    plane = new Plane();
    plane.setPosition(0, 0, -2);
    plane.enableLookAt();
    plane.setLookAt(0, 0, 0);
    plane.setDoubleSided(true);
    plane.setBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    plane.setBlendingEnabled(true);
    plane.setVisible(true);
    // plane.setTransparent(true);

    // Create a texture and material
    Texture texture = new Texture("goku");
    Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.raw.goku);
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

    plane.setMaterial(material);

    scene = getCurrentScene();
    camera = getCurrentCamera();
    scene.addChild(plane);
  }

  /**
   * Only relevant when rendering a live wallpaper, so we just override and ignore.
   */
  @Override
  public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep,
                               float yOffsetStep, int xPixelOffset, int yPixelOffset) {}

}
