//ECE 573 Project
//Team: Witty
//Date: 3/13/14
//Authors: Brianna Heersink, Brian Smith, Alex Warren

package edu.arizona.ece473573.witti.cloudview;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import edu.arizona.ece473573.witti.activities.CloudCamera;
import edu.arizona.ece473573.witti.activities.DisplayActivity;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;

public class CloudRenderer implements Renderer {
    private static final String CAT_TAG = "WITTI_CloudRenderer";
    private DisplayActivity mDisplayActivity;
    private PointCloudArtist mPointCloudArtist;

    //Matricies to store the model, view and projection for the "camera"
    private float[] mModelMatrix = new float[16];
    private CloudCamera mCamera;
    private float[] mProjectionMatrix = new float[16];
    //Combined matrix used in shader
    private float[] mMVPMatrix = new float[16];
    public float mTime;

    public CloudRenderer(DisplayActivity display, CloudCamera cc) {
        Log.v(CAT_TAG, "CloudRenderer constructor");
        mDisplayActivity = display;
        mPointCloudArtist = new PointCloudArtist(display);
        mTime = 0.0f;
        mCamera = cc;
        mCamera.setCamera(0.0f, -10.0f, 10.0f,
                          0.0f,  20.0f, 10.0f,
                          0.0f,   0.0f,  1.0f);
        Matrix.setIdentityM(mModelMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mCamera.getViewMatrix(), 0, mModelMatrix, 0);
        
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        //Log.v(CAT_TAG, "MVP matrix: "+floatArrayToString(mMVPMatrix));
        mPointCloudArtist.draw(mMVPMatrix);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.v(CAT_TAG, "CloudRenderer onSurfaceChanged");
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 250.0f;
        
        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.v(CAT_TAG, "CloudRenderer onSurfaceCreated");
        
        //Set the background clear color to black.
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE);
        mPointCloudArtist.initializeShaders();
    }
    
    
    
    public String floatArrayToString(float[] arr){
        StringBuilder result = new StringBuilder();
        for (int ii=0; ii<arr.length; ii++){
            result.append(arr[ii]);
            result.append(' ');
        }
        return result.toString();
    }
    
    public void setCamera(CloudCamera cc){
    	mCamera = cc;
    }
    
}
