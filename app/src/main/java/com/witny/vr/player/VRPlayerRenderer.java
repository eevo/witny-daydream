package com.witny.vr.player;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.opengl.GLES20;
import android.os.Build;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MotionEvent;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;
import com.google.vr.sdk.base.Viewport;
import com.google.vr.sdk.proto.nano.Analytics;

import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.Camera;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.shaders.FragmentShader;
import org.rajawali3d.materials.shaders.VertexShader;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.materials.textures.TextureManager;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.ScreenQuad;
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
import java.util.concurrent.ThreadLocalRandom;
<<<<<<< HEAD

=======
<<<<<<< HEAD
>>>>>>> 2c1505243b038c980ee81af6199dc497b491efce
=======
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
>>>>>>> e96fc226506b60fce66cc318d6d64dc022d0a29b
/**
 * Created by jake on 1/14/18.
 */
public class VRPlayerRenderer extends VRRenderer {
<<<<<<< HEAD

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
    ScreenQuad reticle;
    private Material reticleDefaultMaterial;
    private Material reticleIsLookingAtMaterial;
    private Plane doorFrame;
    private int randomNum;
    private Plane doorFrame1;
    private Plane doorFrame2;
    private Plane doorFrame3;
    private float vScale;
    private float vOffset;
    private boolean videoChanged = false;
    private boolean notDoors = false;



    public VRPlayerRenderer(Context context) {
        super(context);


        // Allow Rajawali debug logs
        RajLog.setDebugEnabled(true);
    }
    private void startAudio(){
        head = new HeadTransform();
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
=======
  private static final String TAG = "VRPlayerRenderer";
  private Camera camera;
  private Scene scene;
  private Sphere sphere;
  public AudioTrack at;
  public int minBufferSize;
  byte[] music = null;
  InputStream alienAudio;
  public double angle;
  private MediaPlayer mMediaPlayer;
  public CustomStreamingTexture videoTexture;
  public double angleOfSound;
  private Thread audioThread;
  private Texture mMutableTexture;
  public  HeadTransform head;
  private static final double maxScale = 0.95;
  private volatile boolean isPlaying = false;
  ScreenQuad reticle;
  private Material reticleDefaultMaterial;
  private Material reticleIsLookingAtMaterial;
  private Plane doorFrame;
<<<<<<< HEAD
  private int randomNum;
=======
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
  private Plane doorFrame1;
  private Plane doorFrame2;
  private Plane doorFrame3;
  private boolean videoChanged = false;
<<<<<<< HEAD
  private float vOffset;
  private float vScale;
  private boolean notDoors = false;



=======
  private int randomNum;
  private float vOffset;
  private float vScale;
  private boolean notDoors = false;
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
  public VRPlayerRenderer(Context context) {
    super(context);
    // Allow Rajawali debug logs
    RajLog.setDebugEnabled(true);
  }
  private void startAudio(){
    head = new HeadTransform();
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
        alienAudio = mContext.getResources().openRawResource(R.raw.kkdirge);
        Log.v(TAG, "startAudio()Inside Run");
        at.play();
        // play the audio track and loop indefinitely, repeatedly writing to AudioTrack
        int i;
        int x = 0;
        try{
          while(x == 0 && isPlaying == true){
            i = alienAudio.read(music);
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
>>>>>>> 2c1505243b038c980ee81af6199dc497b491efce
                }
<<<<<<< HEAD
                // note that we don't want duplicate audio code in 'onNewFrame' and here--we'll now only
                // handle audio playback in this thread
=======
                at.write(music, 0, i);
              }
            }else{
              alienAudio.reset();
              alienAudio.skip(44);
>>>>>>> e96fc226506b60fce66cc318d6d64dc022d0a29b
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
//     double rightAngle = deltaAngle + Math.PI/2;

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

    private void checkLook(){
        Log.d("videoChange", Boolean.toString(videoChanged));
        if (isLookingAtObject(doorFrame1) && randomNum != 0) {
            if (videoChanged == false) {
                notDoors = true;
                openAndCloseDoor(1);
            }
        }
        if (isLookingAtObject(doorFrame2) && randomNum != 1) {
            if (videoChanged == false) {
                notDoors = true;
                openAndCloseDoor(2);
            }
        }
<<<<<<< HEAD
        if (isLookingAtObject(doorFrame3) && randomNum != 2) {
            if (videoChanged == false) {
                notDoors = true;
                openAndCloseDoor(3);
            }
        }
        if (isLookingAtObject(doorFrame1) && randomNum == 0) {
            reticle.setMaterial(reticleIsLookingAtMaterial);
            if (videoChanged == false) {
                openAndChangeVideo();
                reticle.destroy();
                notDoors = true;
                videoChanged = true;
            }
        }
        if (isLookingAtObject(doorFrame2) && randomNum == 1) {
            reticle.setMaterial(reticleIsLookingAtMaterial);
            if (videoChanged == false) {
                notDoors = true;
                openAndChangeVideo();
                reticle.destroy();
                videoChanged = true;
            }
        }
        if (isLookingAtObject(doorFrame3) && randomNum == 2) {
            reticle.setMaterial(reticleIsLookingAtMaterial);
            if (videoChanged == false) {
                notDoors = true;
                openAndChangeVideo();
                reticle.destroy();
                videoChanged = true;
            }
        }
        else {
            reticle.setMaterial(reticleDefaultMaterial);
        }
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onNewFrame(HeadTransform headTransform){
        videoTexture.update();
        float[] fwd = new float[3];
        headTransform.getForwardVector(fwd, 0);
        head = headTransform;
        if(notDoors == false) {
            checkLook();
        }
        super.onNewFrame(headTransform);
    }
    private void openAndChangeVideo() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        if (randomNum == 0) {
            mMediaPlayer = MediaPlayer.create(getContext(),
                    R.raw.door1_open);
        }
        if (randomNum == 1) {
            mMediaPlayer = MediaPlayer.create(getContext(),
                    R.raw.door2_open);
        }
        if (randomNum == 2) {
            mMediaPlayer = MediaPlayer.create(getContext(),
                    R.raw.door3_open);
        }
        videoTexture.updateMediaPlayer(mMediaPlayer);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = MediaPlayer.create(getContext(),
                        R.raw.spaceperson);
                videoTexture.updateMediaPlayer(mMediaPlayer);
                notDoors = true;
                vScale = 1.0f;
                vOffset = 0f;
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.start();
    }
    private void openAndCloseDoor(final int doorNum){
        mMediaPlayer.stop();
        mMediaPlayer.release();
        if (doorNum == 1) {
            mMediaPlayer = MediaPlayer.create(getContext(),
                    R.raw.door1_open);
        }
        if (doorNum == 2) {
            mMediaPlayer = MediaPlayer.create(getContext(),
                    R.raw.door2_open);
        }
        if (doorNum == 3) {
            mMediaPlayer = MediaPlayer.create(getContext(),
                    R.raw.door3_open);
        }
        videoTexture.updateMediaPlayer(mMediaPlayer);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                if (doorNum == 1) {
                    mMediaPlayer = MediaPlayer.create(getContext(),
                            R.raw.door1_close);
                }
                if (doorNum == 2) {
                    mMediaPlayer = MediaPlayer.create(getContext(),
                            R.raw.door2_close);
                }
                if (doorNum == 3) {
                    mMediaPlayer = MediaPlayer.create(getContext(),
                            R.raw.door3_close);
                }
                videoTexture.updateMediaPlayer(mMediaPlayer);
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                        mMediaPlayer = MediaPlayer.create(getContext(),
                                R.raw.doors);
                        videoTexture.updateMediaPlayer(mMediaPlayer);
                        notDoors = false;
                        mMediaPlayer.start();
                    }
                });
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.start();
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
=======
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
<<<<<<< HEAD
//     double rightAngle = deltaAngle + Math.PI/2;

=======
    // double rightAngle = deltaAngle + Math.PI/2;
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
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
<<<<<<< HEAD

  private void checkLook(){
    Log.d("videoChange", Boolean.toString(videoChanged));
    if (isLookingAtObject(doorFrame1) && randomNum != 0) {
      if (videoChanged == false) {
        notDoors = true;
        openAndCloseDoor(1);
      }
    }
    if (isLookingAtObject(doorFrame2) && randomNum != 1) {
      if (videoChanged == false) {
        notDoors = true;
        openAndCloseDoor(2);
      }
    }
    if (isLookingAtObject(doorFrame3) && randomNum != 2) {
      if (videoChanged == false) {
        notDoors = true;
        openAndCloseDoor(3);
      }
    }
    if (isLookingAtObject(doorFrame1) && randomNum == 0) {
      reticle.setMaterial(reticleIsLookingAtMaterial);
      if (videoChanged == false) {
        openAndChangeVideo();
        reticle.destroy();
        notDoors = true;
        videoChanged = true;
      }
    }
    if (isLookingAtObject(doorFrame2) && randomNum == 1) {
      reticle.setMaterial(reticleIsLookingAtMaterial);
      if (videoChanged == false) {
        notDoors = true;
        openAndChangeVideo();
        reticle.destroy();
        videoChanged = true;
      }
    }
    if (isLookingAtObject(doorFrame3) && randomNum == 2) {
      reticle.setMaterial(reticleIsLookingAtMaterial);
      if (videoChanged == false) {
        notDoors = true;
        openAndChangeVideo();
        reticle.destroy();
        videoChanged = true;
      }
    }
=======
  private void checkLook(){
      if (isLookingAtObject(doorFrame1) && randomNum != 0) {
        if (videoChanged == false) {
          notDoors = true;
          openAndCloseDoor(1);
        }
      }
      if (isLookingAtObject(doorFrame2) && randomNum != 1) {
        if (videoChanged == false) {
          notDoors = true;
          openAndCloseDoor(2);
        }
      }
      if (isLookingAtObject(doorFrame3) && randomNum != 2) {
        if (videoChanged == false) {
          notDoors = true;
          openAndCloseDoor(3);
        }
      }
      if (isLookingAtObject(doorFrame1) && randomNum == 0) {
        reticle.setMaterial(reticleIsLookingAtMaterial);
        if (videoChanged == false) {
          openAndChangeVideo();
          reticle.destroy();
          notDoors = true;
          videoChanged = true;
        }
      }
      if (isLookingAtObject(doorFrame2) && randomNum == 1) {
        reticle.setMaterial(reticleIsLookingAtMaterial);
        if (videoChanged == false) {
          notDoors = true;
          openAndChangeVideo();
          reticle.destroy();
          videoChanged = true;
        }
      }
      if (isLookingAtObject(doorFrame3) && randomNum == 2) {
        reticle.setMaterial(reticleIsLookingAtMaterial);
        if (videoChanged == false) {
          notDoors = true;
         openAndChangeVideo();
          reticle.destroy();
          videoChanged = true;
        }
      }
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
    else {
      reticle.setMaterial(reticleDefaultMaterial);
    }
  }
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void onNewFrame(HeadTransform headTransform){
    videoTexture.update();
    float[] fwd = new float[3];
    headTransform.getForwardVector(fwd, 0);
    head = headTransform;
    if(notDoors == false) {
      checkLook();
    }
    super.onNewFrame(headTransform);
  }
  private void openAndChangeVideo() {
    mMediaPlayer.stop();
    mMediaPlayer.release();
    if (randomNum == 0) {
      mMediaPlayer = MediaPlayer.create(getContext(),
              R.raw.door1_open);
    }
    if (randomNum == 1) {
      mMediaPlayer = MediaPlayer.create(getContext(),
              R.raw.door2_open);
    }
    if (randomNum == 2) {
      mMediaPlayer = MediaPlayer.create(getContext(),
              R.raw.door3_open);
    }
    videoTexture.updateMediaPlayer(mMediaPlayer);
    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      public void onCompletion(MediaPlayer mp) {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = MediaPlayer.create(getContext(),
                R.raw.spaceperson);
        videoTexture.updateMediaPlayer(mMediaPlayer);
        notDoors = true;
        vScale = 1.0f;
        vOffset = 0f;
        mMediaPlayer.start();
      }
    });
    mMediaPlayer.start();
  }
  private void openAndCloseDoor(final int doorNum){
<<<<<<< HEAD
    mMediaPlayer.stop();
    mMediaPlayer.release();
    if (doorNum == 1) {
      mMediaPlayer = MediaPlayer.create(getContext(),
              R.raw.door1_open);
    }
    if (doorNum == 2) {
      mMediaPlayer = MediaPlayer.create(getContext(),
              R.raw.door2_open);
    }
    if (doorNum == 3) {
      mMediaPlayer = MediaPlayer.create(getContext(),
              R.raw.door3_open);
    }
    videoTexture.updateMediaPlayer(mMediaPlayer);
    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mp) {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        if (doorNum == 1) {
          mMediaPlayer = MediaPlayer.create(getContext(),
                  R.raw.door1_close);
        }
        if (doorNum == 2) {
          mMediaPlayer = MediaPlayer.create(getContext(),
                  R.raw.door2_close);
        }
        if (doorNum == 3) {
          mMediaPlayer = MediaPlayer.create(getContext(),
                  R.raw.door3_close);
        }
        videoTexture.updateMediaPlayer(mMediaPlayer);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
          @Override
          public void onCompletion(MediaPlayer mp) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = MediaPlayer.create(getContext(),
                    R.raw.doors);
            videoTexture.updateMediaPlayer(mMediaPlayer);
            notDoors = false;
            mMediaPlayer.start();
          }
        });
        mMediaPlayer.start();
      }
    });
    mMediaPlayer.start();
  }
=======
      mMediaPlayer.stop();
      mMediaPlayer.release();
      if (doorNum == 1) {
        mMediaPlayer = MediaPlayer.create(getContext(),
                R.raw.door1_open);
      }
      if (doorNum == 2) {
        mMediaPlayer = MediaPlayer.create(getContext(),
                R.raw.door2_open);
      }
      if (doorNum == 3) {
        mMediaPlayer = MediaPlayer.create(getContext(),
                R.raw.door3_open);
      }
      videoTexture.updateMediaPlayer(mMediaPlayer);
      mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
          mMediaPlayer.stop();
          mMediaPlayer.release();
          if (doorNum == 1) {
            mMediaPlayer = MediaPlayer.create(getContext(),
                    R.raw.door1_close);
          }
          if (doorNum == 2) {
            mMediaPlayer = MediaPlayer.create(getContext(),
                    R.raw.door2_close);
          }
          if (doorNum == 3) {
            mMediaPlayer = MediaPlayer.create(getContext(),
                    R.raw.door3_close);
          }
          videoTexture.updateMediaPlayer(mMediaPlayer);
          mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
              mMediaPlayer.stop();
              mMediaPlayer.release();
              mMediaPlayer = MediaPlayer.create(getContext(),
                      R.raw.doors);
              videoTexture.updateMediaPlayer(mMediaPlayer);
              notDoors = false;
              mMediaPlayer.start();
            }
          });
          mMediaPlayer.start();
        }
      });
      mMediaPlayer.start();
    }

>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
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
      //videoTexture.setScale(1f,0.5f);
      videoTexture.setScale(1f,vScale);
      videoTexture.setOffset(0f, 0f);
<<<<<<< HEAD
>>>>>>> 2c1505243b038c980ee81af6199dc497b491efce
    }
    public void play(){
        isPlaying = true;
        startAudio();
    }

    @Override
    public void onDrawEye(Eye eye) {
        if(eye.getType() == Eye.Type.LEFT){
            videoTexture.setOffset(0f, 0f);
            videoTexture.setScale(1f,vScale );
        }
        else {
            videoTexture.setOffset(0f,vOffset);
            videoTexture.setScale(1f,vScale);
        }
        super.onDrawEye(eye);


    }
<<<<<<< HEAD
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
    public void onTouchEvent(MotionEvent motionEvent) {

    }

    /**
     * Create all objects, materials, textures, etc. The engine is guaranteed to be set up at this
     * point, whereas it may not be ready in onCreate or elsewhere.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void initScene() {
        mMediaPlayer = MediaPlayer.create(getContext(),
                R.raw.doors);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.setVolume(0,0);

        videoTexture = new CustomStreamingTexture("doors", mMediaPlayer);
        vScale= 0.5f;
        vOffset= 0.5F;
        videoTexture.enableScaling(true);
        videoTexture.enableOffset(true);
//        videoTexture.setScale(1f,0.5f);
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
        camera.setNearPlane(0.1);
        camera.setFarPlane(100);
        scene.addChild(sphere);

        createReticle(new Vector3(0,0,-0.05f), new Vector3(0.15f, 0.15f, 0.15f));
        scene.addChild(reticle);

        randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
        Log.v("RandomNum",Integer.toString(randomNum));
        doorFrame1 = createDoorFrame(-1, 0, -1);

        doorFrame2 = createDoorFrame(-400, 1, 80);

        doorFrame3 = createDoorFrame(-8, 0, 12);

        scene.addChild(doorFrame1);
        scene.addChild(doorFrame2);
        scene.addChild(doorFrame3);
    }


    private void createReticle (Vector3 position, Vector3 scale) {
        // NOTE: don't use special characters for the "name" field--it will cause an error.

        // Load the default texture
        reticleDefaultMaterial = loadGraphic(mContext, "reticleDefault", R.raw.reticle);

        // Load the "isLookingAt" texture
        reticleIsLookingAtMaterial = loadGraphic(mContext, "reticleLookingAt", R.raw.thumbs_up);

        reticle = new ScreenQuad();
        reticle.setMaterial(reticleDefaultMaterial);
        reticle.setPosition(position);
        reticle.setTransparent(true);
        reticle.setScale(scale);
        reticle.setBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        reticle.setBlendingEnabled(true);
        reticle.setVisible(true);

        reticle.setName("reticle");
        reticle.rotate(Vector3.Axis.Z, 180);
=======
      videoTexture.setScale(1f,vScale);
    }
    else {
<<<<<<< HEAD
      videoTexture.setOffset(0f, vOffset);
      videoTexture.setScale(1f,vScale);
=======
      //videoTexture.setScale(1f,0.5f);
      //videoTexture.setOffset(0f, 0.5f);
      videoTexture.setScale(1f,vScale);
      videoTexture.setOffset(0f, vOffset);
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
>>>>>>> e96fc226506b60fce66cc318d6d64dc022d0a29b
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Plane createDoorFrame(float x, float y, float z) {
        doorFrame = new Plane();

        // Load the door frame texture
        Material material = loadGraphic(mContext, "doorFrame", R.raw.door_frame);

        doorFrame.setMaterial(material);
        doorFrame.setPosition(x, y, z);
        doorFrame.enableLookAt();
        doorFrame.setLookAt(0,0,0);
        doorFrame.setTransparent(true);
        doorFrame.setScale(0.5f, 1.0f, 1.0f);
        doorFrame.setBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        doorFrame.setBlendingEnabled(true);
        doorFrame.setVisible(true);
        return doorFrame;
    }

    /**
     * Load a material.
     * @param context
     * @param texId
     * @return
     */
    public static Material loadGraphic(Context context, String name, int texId) {
        Log.d(TAG, "Loading graphic");
        Texture texture = new Texture(name);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), texId);
        texture.setBitmap(bitmap);
        Material material = new Material(true);
        try {
            material.addTexture(texture);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        material.setColorInfluence(1);
        material.setColor(1);
        material.enableLighting(false);

        return material;
    }

    /**
     * Only relevant when rendering a live wallpaper, so we just override and ignore.
     */
    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep,
                                 float yOffsetStep, int xPixelOffset, int yPixelOffset) {}

    public void onScreenTap() {
        if (reticle != null) {
            reticle.setVisible(!reticle.isVisible());
        }
    }

}
=======
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
  public void onTouchEvent(MotionEvent motionEvent) {

  }

  /**
   * Create all objects, materials, textures, etc. The engine is guaranteed to be set up at this
   * point, whereas it may not be ready in onCreate or elsewhere.
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void initScene() {

    mMediaPlayer = MediaPlayer.create(getContext(),
            R.raw.doors);
    mMediaPlayer.setLooping(true);
    mMediaPlayer.setVolume(0,0);
    videoTexture = new CustomStreamingTexture("doors", mMediaPlayer);
    vScale = 0.5f;
    vOffset = 0.5f;
    videoTexture.enableScaling(true);
    videoTexture.enableOffset(true);
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
    camera.setNearPlane(0.1);
    camera.setFarPlane(100);
    scene.addChild(sphere);
    createReticle(new Vector3(0,0,-0.05f), new Vector3(0.15f, 0.15f, 0.15f));
    scene.addChild(reticle);
<<<<<<< HEAD

    randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
    Log.v("RandomNum",Integer.toString(randomNum));
      doorFrame1 = createDoorFrame(-1, 0, -1);

      doorFrame2 = createDoorFrame(-400, 1, 80);

      doorFrame3 = createDoorFrame(-8, 0, 12);

      scene.addChild(doorFrame1);
      scene.addChild(doorFrame2);
      scene.addChild(doorFrame3);
    }


=======
    randomNum = ThreadLocalRandom.current().nextInt(0, 2 + 1);
    Log.v("RandomNum",Integer.toString(randomNum));
    doorFrame1 = createDoorFrame(-1, 0, -1);
    doorFrame2 = createDoorFrame(-8, 0, 2);
    doorFrame3 = createDoorFrame(-8, 0, 12);
    scene.addChild(doorFrame1);
    scene.addChild(doorFrame2);
    scene.addChild(doorFrame3);
  }
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
  private void createReticle (Vector3 position, Vector3 scale) {
    // NOTE: don't use special characters for the "name" field--it will cause an error.
    // Load the default texture
    reticleDefaultMaterial = loadGraphic(mContext, "reticleDefault", R.raw.reticle);
    // Load the "isLookingAt" texture
    reticleIsLookingAtMaterial = loadGraphic(mContext, "reticleLookingAt", R.raw.thumbs_up);
    reticle = new ScreenQuad();
    reticle.setMaterial(reticleDefaultMaterial);
    reticle.setPosition(position);
    reticle.setTransparent(true);
    reticle.setScale(scale);
    reticle.setBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    reticle.setBlendingEnabled(true);
    reticle.setVisible(true);
    reticle.setName("reticle");
    reticle.rotate(Vector3.Axis.Z, 180);
  }
<<<<<<< HEAD

=======
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private Plane createDoorFrame(float x, float y, float z) {
    doorFrame = new Plane();
    // Load the door frame texture
    Material material = loadGraphic(mContext, "doorFrame", R.raw.door_frame);
    doorFrame.setMaterial(material);
<<<<<<< HEAD
    doorFrame.setPosition(x, y, z);
=======
    doorFrame.setPosition(x,y,z);
    //doorFrame.setPosition(-8,0,2f);
    //doorFrame.setPosition(-8,0,12);
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
    doorFrame.enableLookAt();
    doorFrame.setLookAt(0,0,0);
    doorFrame.setTransparent(true);
    doorFrame.setScale(0.5f, 1.0f, 1.0f);
    doorFrame.setBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    doorFrame.setBlendingEnabled(true);
    doorFrame.setVisible(true);
    return doorFrame;
  }
  /**
   * Load a material.
   * @param context
   * @param texId
   * @return
   */
  public static Material loadGraphic(Context context, String name, int texId) {
    Log.d(TAG, "Loading graphic");
    Texture texture = new Texture(name);
    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), texId);
    texture.setBitmap(bitmap);
    Material material = new Material(true);
    try {
      material.addTexture(texture);
    } catch (ATexture.TextureException e) {
      e.printStackTrace();
    }
    material.setColorInfluence(1);
    material.setColor(1);
    material.enableLighting(false);
    return material;
  }
  /**
   * Only relevant when rendering a live wallpaper, so we just override and ignore.
   */
  @Override
  public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep,
                               float yOffsetStep, int xPixelOffset, int yPixelOffset) {}
  public void onScreenTap() {
    if (reticle != null) {
      reticle.setVisible(!reticle.isVisible());
    }
  }
}
>>>>>>> 2c1505243b038c980ee81af6199dc497b491efce
