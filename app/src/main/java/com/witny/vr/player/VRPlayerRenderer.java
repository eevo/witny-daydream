package com.witny.vr.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;

import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import org.rajawali3d.cameras.Camera;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.scene.Scene;
import org.rajawali3d.util.RajLog;
import org.rajawali3d.materials.textures.StreamingTexture;

import java.io.InputStream;
import android.media.AudioTrack;
import android.media.AudioManager;
import android.media.AudioFormat;
import java.io.IOException;

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
  InputStream yell;
  private int k;
  public double angle;
  private MediaPlayer mMediaPlayer;
  public StreamingTexture videoTexture;
  public double angleOfSound;
  private Thread audioThread;
  public  HeadTransform head;
  public VRPlayerRenderer(Context context) {
    super(context);


    // Allow Rajawali debug logs
    RajLog.setDebugEnabled(true);
  }
  private void startAudio(){

    // perform any needed setup
    // audioThread is a member variable (you can declare it as 'private Thread audioThread;' )
    audioThread = new Thread() {
      // Thread has a function called 'run' that we want to override
      @TargetApi(Build.VERSION_CODES.LOLLIPOP)
      @Override
      public void run() {
        Log.v(TAG, "startAudio()");
        minBufferSize = AudioTrack.getMinBufferSize(48000, AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        music = new byte[5000];
        at = new AudioTrack(AudioManager.STREAM_MUSIC, 48000,
                AudioFormat.CHANNEL_CONFIGURATION_STEREO, AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize, AudioTrack.MODE_STREAM);
        yell = mContext.getResources().openRawResource(R.raw.yelling);
        Log.v(TAG, "startAudio()Inside Run");
        at.play();
        // play the audio track and loop indefinitely, repeatedly writing to AudioTrack
        int i;
        int x = 0;
        double scale;
        try{
          while(x == 0){
            i = yell.read(music);
            if (i != -1){
              if (i == music.length){
                angleOfSound = -90;
                float[] fwd = new float[3];
                head.getForwardVector(fwd, 0);
                angle = Math.atan2(fwd[0], fwd[2]);
                angle = Math.toDegrees(angle);
                double diff = angle-angleOfSound;
                for(k = 0; k <music.length; k+=2){
                  float f = convertBytesToFloat(music[k], music[k+1]);
                  if(angle == angleOfSound){
                    f = 1.0f;
                  }
                  if(diff == 180){
                    f = 0.0f;
                  }
                  if(diff > 0 && diff < 180){
                    scale = diff/180;
                    f = f * (float)scale;
                  }
                  byte[] B = convertFloatToBytes(f);
                  music[0] = B[1];
                  music [1] = B[0];
                }
                at.write(music, 0, i);
              }
            }else{
              yell.reset();
              yell.skip(44);
            }
          }
        }catch (IOException e){
          Log.e("Exception", "Exception  for Sound", e);
        }
        // note that we don't want duplicate audio code in 'onNewFrame' and here--we'll now only
        // handle audio playback in this thread
      }
    };

    // we've created the thread, now we need to start it
    audioThread.start();
  }
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void onNewFrame(HeadTransform headTransform){
    videoTexture.update();
    float[] fwd = new float[3];
    headTransform.getForwardVector(fwd, 0);
    head = headTransform;
  }
  private float convertBytesToFloat(byte b1, byte b2) {
    short s = (short)(((b1 & 0xFF)<<8) | (b2 & 0xFF));
    return (float)s;
  }

  // convert a float to two bytes (for 16-bit PCM playback)

  private byte[] convertFloatToBytes(float f) {
    short s = (short)f;
    byte[] b = new byte[2];
    b[0] = (byte)(s >> 8);
    b[1] = (byte)s;
    return b;
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
    //float[] s = new float[3];
   head = new HeadTransform();
    startAudio();
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

    // Create a texture and materia
    Texture texture = new Texture("video");
    mMediaPlayer = MediaPlayer.create(getContext(),
            R.raw.video);
    mMediaPlayer.setLooping(true);
    mMediaPlayer.setVolume(0,0);

    videoTexture = new StreamingTexture("video", mMediaPlayer);
    Material material = new Material();
    material.setColorInfluence(0);
    try {
      material.addTexture(videoTexture);
    } catch (ATexture.TextureException e) {
      e.printStackTrace();
    }

    Sphere sphere = new Sphere(50, 64, 32);
    sphere.setScaleX(-1);
    sphere.setMaterial(material);

    getCurrentScene().addChild(sphere);


    getCurrentCamera().setFieldOfView(75);

    mMediaPlayer.start();

    material.setColorInfluence(0f);
    try {
      material.addTexture(videoTexture);
    } catch (ATexture.TextureException e){
      throw new RuntimeException(e);
    }
    sphere.setMaterial(material);

    // add sphere to scene
    getCurrentScene().addChild(sphere);

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
