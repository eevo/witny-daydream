///*
// * Copyright 2017 Google Inc. All Rights Reserved.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.witny.vr.player;
//
//import android.content.Context;
//import android.media.AudioFormat;
//import android.media.AudioManager;
//import android.media.AudioTrack;
//import android.opengl.GLES20;
//import android.opengl.Matrix;
//import android.os.Bundle;
//import android.os.Vibrator;
//import android.util.Log;
//import com.google.vr.sdk.base.AndroidCompat;
//import com.google.vr.sdk.base.GvrActivity;
//import com.google.vr.sdk.base.GvrView;
//
//
//
//import org.rajawali3d.primitives.Plane;
//
//import java.io.InputStream;
//import java.io.IOException;
//
//public class VRActivity extends GvrActivity {
//
//  private static final String TAG = "VRActivity";
//
//  private Vibrator vibrator;
//  private VRPlayerRenderer renderer;
//
//    @Override
//  public void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//
//      renderer = new VRPlayerRenderer(getApplicationContext());
//    initializeGvrView();
//    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//
//  }
//
//  public void initializeGvrView() {
//    setContentView(R.layout.common_ui);
//
//    GvrView gvrView = (GvrView)findViewById(R.id.gvr_view);
//    gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
//
//    gvrView.setRenderer(renderer);
//    gvrView.setTransitionViewEnabled(true);
//
//    // Enable Cardboard-trigger feedback with Daydream headsets. This is a simple way of supporting
//    // Daydream controller input for basic interactions using the existing Cardboard trigger API.
//    gvrView.enableCardboardTriggerEmulation();
//
//    if (gvrView.setAsyncReprojectionEnabled(true)) {
//      // Async reprojection decouples the app framerate from the display framerate,
//      // allowing immersive interaction even at the throttled clockrates set by
//      // sustained performance mode.
//      AndroidCompat.setSustainedPerformanceMode(this, true);
//    }
//
//    setGvrView(gvrView);
//  }
//
//  @Override
//  public void onPause() {
//    super.onPause();
//    renderer.stop();
//  }
//
//  @Override
//  public void onResume() {
//    super.onResume();
//    renderer.play();
//  }
//  @Override
//  public void onDestroy() {
//    super.onDestroy();
//    renderer.stop();
//
//  }
//
//  /**
//   * Called when the Cardboard trigger is pulled (i.e. a simple screen touch event).
//   */
//  @Override
//  public void onCardboardTrigger() {
//    Log.i(TAG, "onCardboardTrigger");
//
//    // Always give user feedback.
//    vibrator.vibrate(50);
//
//    renderer.onScreenTap();
//  }
//}
<<<<<<< HEAD
//package com.google.vr.sdk.samples.controllerclient;

=======
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
package com.witny.vr.player;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import com.google.vr.sdk.base.AndroidCompat;
import com.google.vr.sdk.controller.Controller;
import com.google.vr.sdk.controller.Controller.ConnectionStates;
import com.google.vr.sdk.controller.ControllerManager;
import com.google.vr.sdk.controller.ControllerManager.ApiStatus;
<<<<<<< HEAD

/**
 * Minimal example demonstrating how to receive and process Daydream controller input. It connects
 * to a Daydream Controller and displays a simple graphical and textual representation of the
 * controller's sensors. This example only works with Android N and Daydream-ready phones.
 */
public class ControllerClientActivity extends Activity {

  private static final String TAG = ControllerClientActivity.class.getSimpleName();
=======
<<<<<<< HEAD
=======
// import com.google.vr.sdk.samples.controllerclient.R;
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6

/**
 * Minimal example demonstrating how to receive and process Daydream controller input. It connects
 * to a Daydream Controller and displays a simple graphical and textual representation of the
 * controller's sensors. This example only works with Android N and Daydream-ready phones.
 */
<<<<<<< HEAD
public class ControllerClientActivity extends Activity {

  private static final String TAG = ControllerClientActivity.class.getSimpleName();
=======
public class VRActivity extends Activity {

  private static final String TAG = VRActivity.class.getSimpleName();
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
>>>>>>> e96fc226506b60fce66cc318d6d64dc022d0a29b

  // These two objects are the primary APIs for interacting with the Daydream controller.
  private ControllerManager controllerManager;
  private Controller controller;

  // These TextViews display controller events.
  private TextView apiStatusView;
  private TextView controllerStateView;
  private TextView controllerOrientationText;
  private TextView controllerTouchpadView;
  private TextView controllerButtonView;
  private TextView controllerBatteryView;

  // This is a 3D representation of the controller's pose. See its comments for more information.
  private OrientationView controllerOrientationView;

  // The various events we need to handle happen on arbitrary threads. They need to be reposted to
  // the UI thread in order to manipulate the TextViews. This is only required if your app needs to
  // perform actions on the UI thread in response to controller events.
  private Handler uiHandler = new Handler();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // General view initialization.
    setContentView(R.layout.main_layout);
    apiStatusView = (TextView) findViewById(R.id.api_status_view);
    controllerStateView = (TextView) findViewById(R.id.controller_state_view);
    controllerTouchpadView = (TextView) findViewById(R.id.controller_touchpad_view);
    controllerButtonView = (TextView) findViewById(R.id.controller_button_view);
    controllerOrientationText = (TextView) findViewById(R.id.controller_orientation_text);
    controllerTouchpadView = (TextView) findViewById(R.id.controller_touchpad_view);
    controllerButtonView = (TextView) findViewById(R.id.controller_button_view);
    controllerBatteryView = (TextView) findViewById(R.id.controller_battery_view);

    // Start the ControllerManager and acquire a Controller object which represents a single
    // physical controller. Bind our listener to the ControllerManager and Controller.
    EventListener listener = new EventListener();
    controllerManager = new ControllerManager(this, listener);
    apiStatusView.setText("Binding to VR Service");
    controller = controllerManager.getController();
    controller.setEventListener(listener);
<<<<<<< HEAD

    // Bind the OrientationView to our acquired controller.
    controllerOrientationView = (OrientationView) findViewById(R.id.controller_orientation_view);
    controllerOrientationView.setController(controller);

    // This configuration won't be required for normal GVR apps. However, since this sample doesn't
    // use GvrView, it needs pretend to be a VR app in order to receive controller events. The
    // Activity.setVrModeEnabled is only enabled on in N, so this is an GVR-internal utility method
    // to configure the app via reflection.
    //
    // If this sample is compiled with the N SDK, Activity.setVrModeEnabled can be called directly.
    AndroidCompat.setVrModeEnabled(this, true);
  }

  @Override
  protected void onStart() {
    super.onStart();
    controllerManager.start();
    controllerOrientationView.startTrackingOrientation();
  }

  @Override
  protected void onStop() {
    controllerManager.stop();
    controllerOrientationView.stopTrackingOrientation();
    super.onStop();
  }

  // We receive all events from the Controller through this listener. In this example, our
  // listener handles both ControllerManager.EventListener and Controller.EventListener events.
  // This class is also a Runnable since the events will be reposted to the UI thread.
  private class EventListener extends Controller.EventListener
          implements ControllerManager.EventListener, Runnable {

    // The status of the overall controller API. This is primarily used for error handling since
    // it rarely changes.
    private String apiStatus;

    // The state of a specific Controller connection.
    private int controllerState = ConnectionStates.DISCONNECTED;

    @Override
    public void onApiStatusChanged(int state) {
      apiStatus = ApiStatus.toString(state);
      uiHandler.post(this);
    }

    @Override
    public void onConnectionStateChanged(int state) {
      controllerState = state;
      uiHandler.post(this);
    }

    @Override
    public void onRecentered() {
      // In a real GVR application, this would have implicitly called recenterHeadTracker().
      // Most apps don't care about this, but apps that want to implement custom behavior when a
      // recentering occurs should use this callback.
      controllerOrientationView.resetYaw();
    }

    @Override
    public void onUpdate() {
      uiHandler.post(this);
    }

    // Update the various TextViews in the UI thread.
    @Override
    public void run() {
      apiStatusView.setText(apiStatus);
      controllerStateView.setText(ConnectionStates.toString(controllerState));
      controller.update();

      Log.v(TAG, "Controller Orientation: " + controller.orientation);

=======

    // Bind the OrientationView to our acquired controller.
    controllerOrientationView = (OrientationView) findViewById(R.id.controller_orientation_view);
    controllerOrientationView.setController(controller);
<<<<<<< HEAD

    // This configuration won't be required for normal GVR apps. However, since this sample doesn't
    // use GvrView, it needs pretend to be a VR app in order to receive controller events. The
    // Activity.setVrModeEnabled is only enabled on in N, so this is an GVR-internal utility method
    // to configure the app via reflection.
    //
    // If this sample is compiled with the N SDK, Activity.setVrModeEnabled can be called directly.
    AndroidCompat.setVrModeEnabled(this, true);
  }

  @Override
  protected void onStart() {
    super.onStart();
    controllerManager.start();
    controllerOrientationView.startTrackingOrientation();
  }

  @Override
=======

    // This configuration won't be required for normal GVR apps. However, since this sample doesn't
    // use GvrView, it needs pretend to be a VR app in order to receive controller events. The
    // Activity.setVrModeEnabled is only enabled on in N, so this is an GVR-internal utility method
    // to configure the app via reflection.
    //
    // If this sample is compiled with the N SDK, Activity.setVrModeEnabled can be called directly.
    AndroidCompat.setVrModeEnabled(this, true);
  }

  @Override
  protected void onStart() {
    super.onStart();
    controllerManager.start();
    controllerOrientationView.startTrackingOrientation();
  }

  @Override
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
  protected void onStop() {
    controllerManager.stop();
    controllerOrientationView.stopTrackingOrientation();
    super.onStop();
  }

  // We receive all events from the Controller through this listener. In this example, our
  // listener handles both ControllerManager.EventListener and Controller.EventListener events.
  // This class is also a Runnable since the events will be reposted to the UI thread.
  private class EventListener extends Controller.EventListener
          implements ControllerManager.EventListener, Runnable {

    // The status of the overall controller API. This is primarily used for error handling since
    // it rarely changes.
    private String apiStatus;

    // The state of a specific Controller connection.
    private int controllerState = ConnectionStates.DISCONNECTED;

    @Override
    public void onApiStatusChanged(int state) {
      apiStatus = ApiStatus.toString(state);
      uiHandler.post(this);
    }

    @Override
    public void onConnectionStateChanged(int state) {
      controllerState = state;
      uiHandler.post(this);
    }

    @Override
    public void onRecentered() {
      // In a real GVR application, this would have implicitly called recenterHeadTracker().
      // Most apps don't care about this, but apps that want to implement custom behavior when a
      // recentering occurs should use this callback.
      controllerOrientationView.resetYaw();
    }

    @Override
    public void onUpdate() {
      uiHandler.post(this);
    }

    // Update the various TextViews in the UI thread.
    @Override
    public void run() {
      apiStatusView.setText(apiStatus);
      controllerStateView.setText(ConnectionStates.toString(controllerState));
      controller.update();

      Log.v(TAG, "Controller Orientation: " + controller.orientation);
<<<<<<< HEAD

>>>>>>> e96fc226506b60fce66cc318d6d64dc022d0a29b
      float[] angles = new float[3];
      controller.orientation.toYawPitchRollDegrees(angles);
      controllerOrientationText.setText(String.format(
              "%s\n%s\n[%4.0f\u00b0 y %4.0f\u00b0 p %4.0f\u00b0 r]",
              controller.orientation,
              controller.orientation.toAxisAngleString(),
              angles[0], angles[1], angles[2]));
<<<<<<< HEAD

=======

      if (controller.isTouching) {
        controllerTouchpadView.setText(
                String.format("[%4.2f, %4.2f]", controller.touch.x, controller.touch.y));
      } else {
        controllerTouchpadView.setText("[ NO TOUCH ]");
      }

=======

      float[] angles = new float[3];
      controller.orientation.toYawPitchRollDegrees(angles);
      controllerOrientationText.setText(String.format(
              "%s\n%s\n[%4.0f\u00b0 y %4.0f\u00b0 p %4.0f\u00b0 r]",
              controller.orientation,
              controller.orientation.toAxisAngleString(),
              angles[0], angles[1], angles[2]));

>>>>>>> e96fc226506b60fce66cc318d6d64dc022d0a29b
      if (controller.isTouching) {
        controllerTouchpadView.setText(
                String.format("[%4.2f, %4.2f]", controller.touch.x, controller.touch.y));
      } else {
        controllerTouchpadView.setText("[ NO TOUCH ]");
      }

<<<<<<< HEAD
=======
>>>>>>> e080c75938bb4e770ad3e9672e0ba58ddb4db7e6
>>>>>>> e96fc226506b60fce66cc318d6d64dc022d0a29b
      controllerButtonView.setText(String.format("[%s][%s][%s][%s][%s]",
              controller.appButtonState ? "A" : " ",
              controller.homeButtonState ? "H" : " ",
              controller.clickButtonState ? "T" : " ",
              controller.volumeUpButtonState ? "+" : " ",
              controller.volumeDownButtonState ? "-" : " "));

      controllerBatteryView.setText(String.format("[level: %s][charging: %s]",
              Controller.BatteryLevels.toString(controller.batteryLevelBucket),
              controller.isCharging));
    }
  }
}
//package com.witny.vr.player;
//
//import android.content.Context;
//import android.media.AudioFormat;
//import android.media.AudioManager;
//import android.media.AudioTrack;
//import android.opengl.GLES20;
//import android.opengl.Matrix;
//import android.os.Bundle;
//import android.os.Vibrator;
//import android.util.Log;
//import com.google.vr.sdk.base.AndroidCompat;
//import com.google.vr.sdk.base.GvrActivity;
//import com.google.vr.sdk.base.GvrView;
//
//
//
//import org.rajawali3d.primitives.Plane;
//
//import java.io.InputStream;
//import java.io.IOException;
//
//public class VRActivity extends GvrActivity {
//
//  private static final String TAG = "VRActivity";
//
//  private Vibrator vibrator;
//  private VRPlayerRenderer renderer;
//
//    @Override
//  public void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//
//      renderer = new VRPlayerRenderer(getApplicationContext());
//    initializeGvrView();
//    vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//
//  }
//
//  public void initializeGvrView() {
//    setContentView(R.layout.common_ui);
//
//    GvrView gvrView = (GvrView)findViewById(R.id.gvr_view);
//    gvrView.setEGLConfigChooser(8, 8, 8, 8, 16, 8);
//
//    gvrView.setRenderer(renderer);
//    gvrView.setTransitionViewEnabled(true);
//
//    // Enable Cardboard-trigger feedback with Daydream headsets. This is a simple way of supporting
//    // Daydream controller input for basic interactions using the existing Cardboard trigger API.
//    gvrView.enableCardboardTriggerEmulation();
//
//    if (gvrView.setAsyncReprojectionEnabled(true)) {
//      // Async reprojection decouples the app framerate from the display framerate,
//      // allowing immersive interaction even at the throttled clockrates set by
//      // sustained performance mode.
//      AndroidCompat.setSustainedPerformanceMode(this, true);
//    }
//
//    setGvrView(gvrView);
//  }
//
//  @Override
//  public void onPause() {
//    super.onPause();
//    renderer.stop();
//  }
//
//  @Override
//  public void onResume() {
//    super.onResume();
//    renderer.play();
//  }
//  @Override
//  public void onDestroy() {
//    super.onDestroy();
//    renderer.stop();
//
//  }
//
//  /**
//   * Called when the Cardboard trigger is pulled (i.e. a simple screen touch event).
//   */
//  @Override
//  public void onCardboardTrigger() {
//    Log.i(TAG, "onCardboardTrigger");
//
//    // Always give user feedback.
//    vibrator.vibrate(50);
//
//    renderer.onScreenTap();
//  }
//}