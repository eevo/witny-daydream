package com.witny.vr.player;

import android.opengl.GLES20;
import android.util.Log;

import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.shaders.fragments.texture.ATextureFragmentShaderFragment;
import org.rajawali3d.materials.textures.ATexture;

import java.util.List;

/**
 * Created by michelle on 1/22/18.
 */

public class CustomFragmentShader extends ATextureFragmentShaderFragment {
    // note - may have to deal with AShader base uniform defaults - adding the field
    public final static String SHADER_ID = "EEVO_DIFFUSE_TEXTURE_FRAGMENT";
    protected RVec2[] muScale;
    protected int[] muScaleHandles;

    public static IGlobalShaderVar U_SCALE = new IGlobalShaderVar() {
        @Override
        public String getVarString() {
            return "uScale";
        }

        @Override
        public DataType getDataType() {
            return DataType.VEC2;
        }
    };

    public CustomFragmentShader(List<ATexture> textures) {
        super(textures);
        Log.d("EEVO-Diffuse", "Run diffuse constructor w/ numTextures in mTextures: " + mTextures.size());
        // Init happens here automaticlaly due to ATextureFragmentShaderFragment
    }

    public String getShaderId() {
        return SHADER_ID;
    }

    @Override
    public void initialize() {
        super.initialize();
        Log.d("EEVO-Diffuse", "EDTFSF Init");
        int numTextures = mTextures.size();

        muScaleHandles = new int[numTextures];

        muScale = new RVec2[numTextures];

        for(int i=0; i < numTextures; i++)
        {
            ATexture texture = mTextures.get(i);
            if(checkScalingEnabled(texture)) {
                Log.d("EEVO-Diffuse", "Add SCALE uniform.");
                muScale[i] = (RVec2) this.addUniform(U_SCALE, i);
            }
        }
    }

    @Override
    public void main() {
        super.main();
        RVec4 color = (RVec4)getGlobal(DefaultShaderVar.G_COLOR);
        RVec2 textureCoord = (RVec2)getGlobal(DefaultShaderVar.G_TEXTURE_COORD);
        RVec4 texColor = new RVec4("texColor");
        Log.d("EEVO-Diffuse", "EDTFSF MAIN CALL - numtextures = " + mTextures.size());

        for(int i=0; i<mTextures.size(); i++)
        {
            ATexture texture = mTextures.get(i);
            //if(texture.offsetEnabled())
            //textureCoord.assignAdd(getGlobal(DefaultShaderVar.U_OFFSET, i));

            Log.d("EEVO-Diffuse", "ENABLES texture: " + texture.getTextureName() + " offsets: " + texture.offsetEnabled() + " scaling: " + ((CustomStreamingTexture) texture).scalingEnabled());

            if(texture.offsetEnabled() && checkScalingEnabled(texture)) {
                Log.d("EEVO-Diffuse", "STREAMING OFFSET AND SCALING ENABLED");
                textureCoord.assign("vec2((vTextureCoord[0] * "+muScale[i].x().getName()+") + "+muOffset[i].x().getName()+", (vTextureCoord[1] * "+muScale[i].y().getName()+") + "+muOffset[i].y().getName()+")");
            } else if (texture.offsetEnabled()) {
                Log.d("EEVO-Diffuse", "STREAMING OFFSET ONLY ENABLED");
                textureCoord.assign("vec2(vTextureCoord[0] + "+muOffset[i].x().getName()+", vTextureCoord[1] + "+muOffset[i].y().getName()+")");
            } else if (checkScalingEnabled(texture)) {
                Log.d("EEVO-Diffuse", "STREAMING SCALING ONLY ENABLED");
                textureCoord.assign("vec2(vTextureCoord[0] * "+muScale[i].x().getName()+", vTextureCoord[1] * "+muScale[i].y().getName());
            }

            //textureCoord.assignAdd(getGlobal(DefaultShaderVar.U_OFFSET, i));
            //if(texture.getWrapType() == WrapType.REPEAT)
            //	textureCoord.assignMultiply(getGlobal(DefaultShaderVar.U_REPEAT, i));

            if(texture.getTextureType() == ATexture.TextureType.VIDEO_TEXTURE)
                texColor.assign(texture2D(muVideoTextures[i], textureCoord));
            else
                texColor.assign(texture2D(muTextures[i], textureCoord));
            //texColor.assignMultiply(muInfluence[i]);
            color.assignAdd(texColor);
        }
    }

    private boolean checkScalingEnabled (ATexture texture) {
        return (texture instanceof CustomStreamingTexture && ((CustomStreamingTexture) texture).scalingEnabled());
    }

    @Override
    public void setLocations (int programHandle) {
        super.setLocations(programHandle);
        Log.d("EEVO-Diffuse", "Setting locations.");
        if(mTextures == null) return;
        for (int i = 0; i < mTextures.size(); i++) {
            muScaleHandles[i] = getUniformLocation(programHandle, U_SCALE, i);
        }
    }

    @Override
    public void applyParams() {
        super.applyParams();
        for(int i = 0; i < mTextures.size(); i++) {
            ATexture texture = mTextures.get(i);
            if(checkScalingEnabled(texture))
                GLES20.glUniform2fv(muScaleHandles[i], 1, ((CustomStreamingTexture)texture).getScale(), 0);
        }
    }

    public void bindTextures(int nextIndex) {}
    public void unbindTextures() {}

    @Override
    public Material.PluginInsertLocation getInsertLocation() {
        return Material.PluginInsertLocation.IGNORE;
    }
}

