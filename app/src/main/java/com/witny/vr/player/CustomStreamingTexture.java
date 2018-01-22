package com.witny.vr.player;

import android.media.MediaPlayer;
import org.rajawali3d.materials.textures.StreamingTexture;

/**
 * Created by eram on 1/22/18.
 */

public class CustomStreamingTexture extends StreamingTexture {

    protected boolean mEnableScaling;
    protected float[] mScale = new float[] {1.0f,1.0f};

    public CustomStreamingTexture (String textureName, MediaPlayer mediaPlayer) {
        super(textureName, mediaPlayer);
    }

    public void enableScaling (boolean value) {
        mEnableScaling = value;
    }

    public boolean scalingEnabled () {
        return mEnableScaling;
    }

    public void setScale (float x, float y) {
        mScale[0] = x;
        mScale[1] = y;
    }

    public float[] getScale () {
        return mScale;
    }
}
