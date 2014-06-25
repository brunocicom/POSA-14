package edu.vuum.mocca;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.widget.TextView;
import android.util.Log;

/**
 * @class AndroidPlatformStrategy
 * 
 * @brief Provides methods that define a platform-independent API for
 *        output data to Android UI thread and synchronizing on thread
 *        completion in the ping/pong game.  It plays the role of the
 *        "Concrete Strategy" in the Strategy pattern.
 */
public class AndroidPlatformStrategy extends PlatformStrategy
{	
    /** TextViewVariable. */
    private TextView mTextViewOutput;
	
    /** Activity variable finds gui widgets by view. */
    private WeakReference<Activity> mActivity;

    public AndroidPlatformStrategy(Object output,
                                   final Object activityParam)
    {
        /**
         * A textview output which displays calculations and
         * expression trees.
         */
        mTextViewOutput = (TextView) output;

        /** The current activity window (succinct or verbose). */
        mActivity = new WeakReference<Activity>((Activity) activityParam);
    }

    /**
     * Latch to decrement each time a thread exits to control when the
     * play() method returns.
     */
    private static CountDownLatch mLatch = null;

    /** Do any initialization needed to start a new game. */
    public void begin()
    {
        /** (Re)initialize the CountDownLatch. */
        // TODO - You fill in here.
    	mLatch = new CountDownLatch(NUMBER_OF_THREADS);
    }

    /** Print the outputString to the display. */
    public void print(final String outputString)
    {
        /** 
         * Create a Runnable that's posted to the UI looper thread
         * and appends the outputString to a TextView. 
         */
        // TODO - You fill in here.
    	//// Print is different, because we can't have background threads
        //// in android printing to the console. So we're going to have
        //// to create a runnable and use the activity weak reference as
        //// a way to post that runnable to the user interface thread. 
        //// Here it is called the runOnUiThread.
    	Activity mActivityPrint = mActivity.get();
    	mActivityPrint.runOnUiThread(
    			new Thread(new Runnable() { 
    						public void run() {
    							mTextViewOutput.append(outputString + "\n");
    						}
    				}
    			)
    	);
    }

    /** Indicate that a game thread has finished running. */
    public void done()
    {	
        // TODO - You fill in here.
    	//// The done method down here can be done in one of two ways.
        //// You can either call runOnUiThread and pass a runnable
        //// that will decrement the count, which is how I did it.
        //// Because I wanted to make sure everything else was done
        //// before I decremented the count. Or you could just go
        //// ahead and call done, call the countdown latch directly here
        //// if you want.
    	Activity mActivityDone = mActivity.get();
    	mActivityDone.runOnUiThread(
    			new Thread(new Runnable() { 
    						public void run() {
    							mLatch.countDown();
    						}
    				}
    			)
    	);
    }

    /** Barrier that waits for all the game threads to finish. */
    public void awaitDone()
    {
        // TODO - You fill in here.
    	try {
            mLatch.await();
        } catch(java.lang.InterruptedException e) {
        }
    }

    /** 
     * Error log formats the message and displays it for the
     * debugging purposes.
     */
    public void errorLog(String javaFile, String errorMessage) 
    {
       Log.e(javaFile, errorMessage);
    }
}
