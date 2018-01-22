package com.witny.vr.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;

import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;

import org.rajawali3d.cameras.Camera;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.shaders.FragmentShader;
import org.rajawali3d.materials.shaders.VertexShader;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.scene.Scene;
import org.rajawali3d.util.RajLog;
import org.rajawali3d.materials.textures.StreamingTexture;

import java.io.InputStream;
import android.media.AudioTrack;
import android.media.AudioManager;
import android.media.AudioFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
  InputStream nookAudio;
  public double angle;
  private MediaPlayer mMediaPlayer;
  public CustomStreamingTexture videoTexture;
  public double angleOfSound;
  private Thread audioThread;
  public  HeadTransform head;
  private static final double maxScale = 0.95;
  private volatile boolean isPlaying = false;
  public VRPlayerRenderer(Context context) {
    super(context);


    // Allow Rajawali debug logs
    RajLog.setDebugEnabled(true);
  }
  private void startAudio(){
    head = new HeadTransform();
    // perform any needed setup
    // audioThread is a member varåiable (you can declare it as 'private Thread audioThread;' )
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
        nookAudio = mContext.getResources().openRawResource(R.raw.nook);
        Log.v(TAG, "startAudio()Inside Run");
        at.play();
        // play the audio track and loop indefinitely, repeatedly writing to AudioTrack
        int i;
        int x = 0;
        try{
          while(x == 0 && isPlaying == true){
            i = nookAudio.read(music);
            if (i != -1){
              if (i == music.length){
                angleOfSound = Math.toRadians(90);
                float[] fwd = new float[3];
                head.getForwardVector(fwd, 0);
                angle = Math.atan2(fwd[0], fwd[2]) + Math.PI;
                //angle = Math.toDegrees(angle);
                double diff = Math.abs(angle-angleOfSound);
                float g = 1.0f;
                float[] scale = getScale(angle,angleOfSound);
                float f = 0;

                for(int k = 0; k <music.length; k+=2){
                    int sampleNumber = (k/2)%2;
                    f = convertBytesToFloat(music[k+1], music[k]);
                    if(sampleNumber == 1){
                        g = f*scale[0];
                    }
                    if(sampleNumber == 0) {
                        g = f * scale[1];
                    }
                  byte[] B = convertFloatToBytes(g);
                    music[k] = B[1];
                    music[k+1] = B[0];
                }
                at.write(music, 0, i);
              }
            }else{
              nookAudio.reset();
              nookAudio.skip(44);
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
    public static float[] getScale(double soundAngle, double userAngle) {
        double minScale = Math.sqrt(1.0-maxScale);
        // are we scaling for the left ear or the right
        // boolean isRight = (index/2 & 0x1) == 1;
        double deltaAngle = soundAngle - userAngle;
        float[] scale = new float[2];

        // The right ear is going to be ~90 degrees clockwise from forward
        // double rightAngle = deltaAngle + Math.PI/2;

        // The left ear is going to be ~90 degrees counterclockwise from forward
        double leftAngle = deltaAngle - Math.PI/2;

        double lScale = Math.cos(leftAngle);
        // get lScale in the range [0, 1]
        lScale = (lScale + 1) * 0.5;
        // get lScale in the range [minScale, maxScale]
        lScale = lScale * (maxScale - minScale) + minScale;
        // scale[0] will be the left scale, scale[1] the right
        scale[0] = (float)lScale;
        scale[1] = (float)Math.sqrt(1.0 - lScale*lScale);

        return scale;
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

  private byte[] convertFloatToBytes(float f){
    short s = (short)f;
    byte[] b = new byte[2];
    b[0] = (byte)(s >> 8);
    b[1] = (byte)s;
    return b;
  }
public void stop(){
isPlaying = false;
  }
  public void play(){
    isPlaying = true;
    startAudio();
    }

  @Override
  public void onDrawEye(Eye eye) {
    if(eye.getType() == Eye.Type.LEFT){
      videoTexture.setOffset(0f, 0f);
    }
    else {
      videoTexture.setOffset(0f, 0.5f);
    }
    super.onDrawEye(eye);
  }
    private Material createMaterial() {
        VertexShader vertexShader = new VertexShader();
        vertexShader.initialize();
        vertexShader.buildShader();

        FragmentShader fragmentShader = new FragmentShader();
        fragmentShader.initialize();
        List<ATexture> diffuseList = new ArrayList<>();
        diffuseList.add(videoTexture);
        CustomFragmentShader customShader = new CustomFragmentShader(diffuseList);
        fragmentShader.addShaderFragment(customShader);
        fragmentShader.addPreprocessorDirective("#extension GL_OES_EGL_image_external : require");
        fragmentShader.buildShader();
        fragmentShader.setNeedsBuild(false);

        Material material = new Material(vertexShader, fragmentShader);
        return material;
    }


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
    mMediaPlayer = MediaPlayer.create(getContext(),
            R.raw.doors);
    mMediaPlayer.setLooping(true);
    mMediaPlayer.setVolume(0,0);

    videoTexture = new CustomStreamingTexture("doors", mMediaPlayer);
    videoTexture.enableScaling(true);
    videoTexture.enableOffset(true);
    videoTexture.setScale(1f,0.5f);
    Material material = createMaterial();
    try {
      material.addTexture(videoTexture);
    } catch (ATexture.TextureException e) {
      e.printStackTrace();
    }

    sphere = new Sphere(50, 64, 32);
    sphere.setScale(-1,1,1);

    //getCurrentScene().addChild(sphere);

    getCurrentCamera().setFieldOfView(75);
    getCurrentCamera().setPosition(Vector3.ZERO);
      mMediaPlayer.start();

    // add sphere to scene
   // getCurrentScene().addChild(sphere);

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
