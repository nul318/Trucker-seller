package com.sellertrucker;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CaptureActivity extends Activity {
    private static final String TAG = "ARActivity";
    Preview preview;
    Camera camera;
    Activity act;
    Context ctx;
    Handler handler;



    // Request code for camera
    private final int CAMERA_REQUEST_CODE = 100;

    // Request code for runtime permissions
    private final int REQUEST_CODE_STORAGE_PERMS = 321;

    private boolean hasPermissions() {
        int res = 0;
        // list all permissions which you want to check are granted or not.
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                // it return false because your app dosen't have permissions.
                return false;
            }

        }
        // it return true, your app has permissions.
        return true;
    }

    private void requestNecessaryPermissions() {
        // make array of permissions which you want to ask from user.
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // have arry for permissions to requestPermissions method.
            // and also send unique Request code.
            requestPermissions(permissions, REQUEST_CODE_STORAGE_PERMS);
        }
    }

    /* when user grant or deny permission then your app will check in
      onRequestPermissionsReqult about user's response. */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grandResults) {
        // this boolean will tell us that user granted permission or not.
        boolean allowed = true;
        switch (requestCode) {
            case REQUEST_CODE_STORAGE_PERMS:
                for (int res : grandResults) {
                    // if user granted all required permissions then 'allowed' will return true.
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
//                    Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                // if user denied then 'allowed' return false
//                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
                allowed = false;
                break;
        }
        if (allowed) {
            // if user granted permissions then do your work.
            //startCamera();
            doRestart(this);
        } else {
            // else give any custom waring message.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
//                    Toast.makeText(ARActivity.this, "Camera Permissions denied", Toast.LENGTH_SHORT).show();
                } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    Toast.makeText(ARActivity.this, "Storage Permissions denied", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    public static void doRestart(Context c) {
        //http://stackoverflow.com/a/22345538
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
//                        Log.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
//                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
//                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
//            Log.e(TAG, "Was not able to restart application");
        }
    }

    public void startCamera() {
        if (preview == null) {
            preview = new Preview(this, (SurfaceView) findViewById(R.id.surfaceView));
            preview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            ((FrameLayout) findViewById(R.id.layout)).addView(preview);
            preview.setKeepScreenOn(true);

//            preview.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View arg0) {
//                    camera.takePicture(shutterCallback, rawCallback, jpegCallback);
//                }
//            });
//

        }

        preview.setCamera(null);
        if (camera != null) {
            camera.release();
            camera = null;
        }

        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {

                // Camera.CameraInfo.CAMERA_FACING_FRONT or Camera.CameraInfo.CAMERA_FACING_BACK
                int CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_BACK;
                camera = Camera.open(CAMERA_FACING);
                // camera orientation
                camera.setDisplayOrientation(setCameraDisplayOrientation(this, CAMERA_FACING, camera));
                // get Camera parameters
                Camera.Parameters params = camera.getParameters();
                // picture image orientation
                params.setRotation(setCameraDisplayOrientation(this, CAMERA_FACING, camera));
                camera.startPreview();
//                Toast.makeText(this, "camera start", Toast.LENGTH_LONG).show();

            } catch (RuntimeException ex) {
//                Toast.makeText(ctx, "camera_not_found " + ex.getMessage().toString(), Toast.LENGTH_LONG).show();
//                Log.d(TAG, "camera_not_found " + ex.getMessage().toString());
            }
        }

        preview.setCamera(camera);
    }

    TextView direct_register_info;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        ctx = this;
        act = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_capture);

        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            if (!hasPermissions()) {
                // your app doesn't have permissions, ask for them.
                requestNecessaryPermissions();
            } else {
                // your app already have permissions allowed.
                // do what you want.
                startCamera();
            }


        } else {
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        PosFragment.menus.clear();
                        Toast.makeText(CaptureActivity.this, "결제되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }).start();
    }


    @Override
    protected void onResume() {
        super.onResume();

        startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Surface will be destroyed when we return, so stop the preview.
        if (camera != null) {
            // Call stopPreview() to stop updating the preview surface
            camera.stopPreview();
            preview.setCamera(null);
            camera.release();
            camera = null;
        }

        ((FrameLayout) findViewById(R.id.layout)).removeView(preview);
        preview = null;

    }

    private void resetCam() {
        startCamera();
    }

    /**
     * @param activity
     * @param cameraId Camera.CameraInfo.CAMERA_FACING_FRONT, Camera.CameraInfo.CAMERA_FACING_BACK
     * @param camera   Camera Orientation
     *                 reference by https://developer.android.com/reference/android/hardware/Camera.html
     */
    public static int setCameraDisplayOrientation(Activity activity,
                                                  int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);

        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation){
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

}