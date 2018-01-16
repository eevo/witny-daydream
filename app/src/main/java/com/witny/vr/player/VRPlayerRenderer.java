package com.witny.vr.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
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

import java.io.BufferedInputStream;
import java.io.InputStream;
import android.media.AudioTrack;
import android.media.AudioManager;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by jake on 1/14/18.
 */

public class VRPlayerRenderer extends VRRenderer {

  private static final String TAG = "VRPlayerRenderer";
  private Camera camera;
  private Scene scene;
  private Sphere sphere;
  public AudioTrack at;
  public int minBufferSize;
  byte[] music = null;
  InputStream laser;

  public VRPlayerRenderer(Context context) {
    super(context);

    // Allow Rajawali debug logs
    RajLog.setDebugEnabled(true);
  }

  @Override
  public void onNewFrame(HeadTransform headTransform) {
    int i;
    try{
          if((i = laser.read(music)) != -1) {
            at.write(music, 0, i);
        }

    } catch (IOException e) {
      Log.e("Exception", "Exception  for Sound", e);
    }

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

    minBufferSize = AudioTrack.getMinBufferSize(48000, AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT);
    music = new byte[512];
    laser = mContext.getResources().openRawResource(R.raw.laserspeed);
    at = new AudioTrack(AudioManager.STREAM_MUSIC, 48000,
            AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT,
            minBufferSize, AudioTrack.MODE_STREAM);
    at.play();
    //at.stop();
    //at.release();

    // Set up the image sphere
    sphere = new Sphere(10, 50, 50);
    sphere.setPosition(0, 0, -2);
    sphere.enableLookAt();
    sphere.setLookAt(0, 0, 0);
    // sphere.setDoubleSided(true);
    sphere.setBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    sphere.setBlendingEnabled(true);
    sphere.setVisible(true);
    sphere.setScale(-1, 1, 1);
    // plane.setTransparent(true);

    // Create a texture and material
    Texture texture = new Texture("goku");

    // Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.raw.goku);
    InputStream is = mContext.getResources().openRawResource(R.raw.background);
    Bitmap bitmap = BitmapFactory.decodeStream(new BufferedInputStream(is));
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
    Log.e("Sphere is rendered", "rendered");

  }

  /**
   * Only relevant when rendering a live wallpaper, so we just override and ignore.
   */
  @Override
  public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep,
                               float yOffsetStep, int xPixelOffset, int yPixelOffset) {}

}
