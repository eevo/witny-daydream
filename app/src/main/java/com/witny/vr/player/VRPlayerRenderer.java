package com.witny.vr.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
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
import org.rajawali3d.util.OnFPSUpdateListener;
import org.rajawali3d.util.RajLog;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.RandomAccess;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by jake on 1/14/18.
 */

public class VRPlayerRenderer extends VRRenderer implements OnFPSUpdateListener {

  private static final String TAG = "VRPlayerRenderer";
  private Camera camera;
  private Scene scene;
  private Plane plane;

  // audio
  Thread audioThread;
  int bufferSize;
  int sampleRate;
  AudioTrack audioTrack;
  byte[] audioBuffer;
  float[] audioFloatBuffer;
  InputStream audioStream;
  private volatile boolean audioExit = false;

  public VRPlayerRenderer(Context context) {
    super(context);

    // Allow Rajawali debug logs
    RajLog.setDebugEnabled(true);

    setFPSUpdateListener(this);
  }

  @Override
  public void onNewFrame(HeadTransform headTransform) {
    // Log.d(TAG, "onNewFrame");
//    int i = 0;
//    try {
//      if ((i = audioStream.read(audioBuffer)) != -1)
//        audioTrack.write(audioBuffer, 0, i);
//    } catch (IOException e) {
//      Log.d(TAG, "IOException while playing audio", e);
//    }
  }

  @Override
  public void onFPSUpdate(double fps) {
    Log.d(TAG, "FPS is: " + fps);
  }

  public void stop() {
    audioExit = true;
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

    Sphere sphere = new Sphere(50, 32, 32);
    sphere.setPosition(0, 0, 0);
    sphere.setBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    sphere.setBlendingEnabled(true);
    sphere.setVisible(true);
    sphere.setScaleX(-1);
    sphere.disableLookAt();

    // Set up the image plane
//    plane = new Plane();
//    plane.setPosition(0, 0, -2);
//    plane.enableLookAt();
//    plane.setLookAt(0, 0, 0);
//    plane.setDoubleSided(true);
//    plane.setBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
//    plane.setBlendingEnabled(true);
//    plane.setVisible(true);
    // plane.setTransparent(true);

    // Create a texture and material
    Log.d(TAG, "Adding texture");
    Texture texture = new Texture("goku");
    // Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.raw.background);
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

    // audio
    prepAudio();
    startAudio();
  }

  private void prepAudio() {

    sampleRate = 48000;
    bufferSize = AudioTrack.getMinBufferSize(sampleRate,
            AudioFormat.CHANNEL_CONFIGURATION_STEREO,
            AudioFormat.ENCODING_PCM_16BIT);

    audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
            AudioFormat.CHANNEL_CONFIGURATION_STEREO,
            AudioFormat.ENCODING_PCM_16BIT, bufferSize,
            AudioTrack.MODE_STREAM);

    // can't use this version because our phones are too old :D
//    audioTrack = new AudioTrack.Builder()
//            .setAudioAttributes(new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_MEDIA)
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build())
//            .setAudioFormat(new AudioFormat.Builder()
//                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
//                    .setSampleRate(44100)
//                    .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
//                    .build())
//            .setBufferSizeInBytes(bufferSize)
//            .build();

    Log.d(TAG, "Buffer size is: " + bufferSize);
  }

  // convert two bytes to a float (for 16-bit PCM playback)
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

  private void scaleSamples(float scale) {
    for (int i = 0; i < audioBuffer.length; i+=2) {
      float sample = convertBytesToFloat(audioBuffer[i+1], audioBuffer[i]);
      sample *= scale;
      byte[] b = convertFloatToBytes(sample);
      audioBuffer[i] = b[1];
      audioBuffer[i+1] = b[0];
//      short s = (short)(((audioBuffer[i*2+1] & 0xFF)<<8) | (audioBuffer[i*2] & 0xFF));
//      s = (short)((float)s * 0.5f);
//      audioBuffer[i*2+1] = (byte)(s >> 8);
//      audioBuffer[i*2] = (byte)s;
//
////      short b1 = (short)audioBuffer[i*2];
////      short b2 = (short)audioBuffer[i*2+1];
////      short sample = (short)(b1 << 8 | b2);
//      // Log.d(TAG, "sample: " + bb.getShort(0));
//      // audioFloatBuffer[i] = (float)s / 32767.0f;
    }
  }

  private void startAudio() {
    audioBuffer = new byte[1024];
    audioFloatBuffer = new float[audioBuffer.length/2];
    audioStream = mContext.getResources().openRawResource(R.raw.yelling);
    try {
      audioStream.skip(44L);
    } catch (IOException e) {

    }

    audioThread = new Thread() {
      @Override
      public void run() {

        audioTrack.play();
        while (audioExit != true) {
          int i;
          try {
            if ((i = audioStream.read(audioBuffer)) != -1) {
              if (i == audioBuffer.length) {
                scaleSamples(0.7f);
                audioTrack.write(audioBuffer, 0, i);
                // audioTrack.write(audioBuffer, 0, i);
              }
            } else {
              audioStream.reset();
              // this is a quirk because wav files have a 44 byte header, so we're skipping the header
              // to avoid artifacts in the audio
               audioStream.skip(44);
               Log.d(TAG, "Resetting audio stream");
            }

          } catch (IOException e) {
            Log.d(TAG, "IOException while playing audio", e);
          }
        }
      }
    };

    audioThread.start();
  }

  /**
   * Only relevant when rendering a live wallpaper, so we just override and ignore.
   */
  @Override
  public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep,
                               float yOffsetStep, int xPixelOffset, int yPixelOffset) {}

}