//ECE 573 Project
//Team: Witty
//Date: 4/18/14
//Author: Brian Smith

package edu.arizona.ece473573.witti.test;

import java.nio.FloatBuffer;
import java.util.concurrent.TimeUnit;

import junit.framework.Assert;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import edu.arizona.ece473573.witti.activities.DisplayActivity;
import edu.arizona.ece473573.witti.cloudview.PointCloud;
import edu.arizona.ece473573.witti.sequence.CloudSequence;


/**
 * 
 * 	B.3 Server Download Requirement: �The phone application software shall be capable of downloading
 * 	Velodyne LiDAR data in the form of an XYZpoint binary file from a remote server.�
 */
public class ServerPlaybackTest extends ActivityInstrumentationTestCase2<DisplayActivity>{
	
	private CloudSequence mCloudSequence;
	private DisplayActivity mDisplayActivity;
	private Button mRefreshButton;
	private FloatBuffer AUT;
	private PointCloud mPointCloud;
	
	private float[] testArray_1, testArray_2;
	
	public ServerPlaybackTest(){
		super(DisplayActivity.class);
	}

    /**
     * Launches the DisplayActivity and loads the initial test file into memory
     * 
     */
	
	@Override
	protected void setUp() throws Exception{
		
		super.setUp();
		
		Intent intent = new Intent();
		intent.putExtra("inDemoMode", false);
		intent.putExtra("inTestMode", true);
		
		setActivityIntent(intent);
		
		mDisplayActivity = getActivity();
		mRefreshButton = (Button)mDisplayActivity.findViewById(edu.arizona.ece473573.witti.R.id.displayRefreshButton);

		mCloudSequence = mDisplayActivity.mSequence;
		
		testArray_1 = new float[30];
		float testVal = 0.0f;
		for(int i = 0; i < 30; i ++){
			
			if(i != 0 && i % 3 == 0)
				testVal = testVal - 2.0f;
			
			testArray_1[i] = testVal;
			testVal = testVal + 1.0f;
			
		}
		
		testArray_2 = new float[30];
		testVal = 1000.0f;
		for(int i = 0; i < 30; i ++){
			
			if(i != 0 && i % 3 == 0)
				testVal = testVal - 2.0f;
			
			testArray_2[i] = testVal;
			testVal = testVal + 1.0f;
			
		}
		
	}

	public void testServerPlayback() throws Throwable{
		
		//Wait for async task for render of first frame
		try{
			mCloudSequence.signal.await(4, TimeUnit.SECONDS);
		}catch(InterruptedException e){
			fail("ASyncTask failed");
		}

		Log.d("SERVER", "current frame= " + mCloudSequence.getCurrentFrameNum());
		mPointCloud = mCloudSequence.getSpecifiedFrame(0);
		
		Assert.assertNotNull(mPointCloud);
		AUT = mPointCloud.mVertexBuffer;
		
		//Check that the buffer contains expected values
		for(int i = 0; i < AUT.capacity(); i++){
			Assert.assertTrue(AUT.get(i) == testArray_1[i]);
		}
		
		//Click button to refresh data manually
		runTestOnUiThread(new Runnable() {
			@Override
			public void run(){
				mRefreshButton.performClick();
			}
		});
		
		//Wait for async task for render of second frame
		try{
			mCloudSequence.signal.await(4, TimeUnit.SECONDS);
		}catch(InterruptedException e){
			fail("ASyncTask failed");
		}
		
		mPointCloud = mCloudSequence.getCurrentFrame();
		AUT = mPointCloud.mVertexBuffer;
		
		//Check that the buffer contains expected values
		for(int i = 0; i < AUT.capacity(); i++){
			Assert.assertTrue(AUT.get(i) == testArray_2[i]);
		}
		
		//Verify that the second frame is loaded
		Assert.assertTrue(mCloudSequence.getCurrentFrameNum() == 1);
	}

}

