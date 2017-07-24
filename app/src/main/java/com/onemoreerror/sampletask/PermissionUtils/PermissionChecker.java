package com.onemoreerror.sampletask.PermissionUtils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.onemoreerror.sampletask.Constants.AppConstants;
import com.onemoreerror.sampletask.db.Contact;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static com.onemoreerror.sampletask.Constants.AppConstants.REQUEST_TAGS.REQUEST_CONTACT_TAG;
import static com.onemoreerror.sampletask.Constants.AppConstants.REQUEST_TAGS.REQUEST_EXTERNAL_STORAGE;
import static com.onemoreerror.sampletask.Constants.AppConstants.REQUEST_TAGS.REQUEST_MANAGE_DOC;

/**
 * Created by Nilesh on 17-07-2017.
 */

public class PermissionChecker {

    public static boolean checkReadContactsPermission(){



        return false ;
    }

    public static boolean checkReadWriteStoragePermission(Activity activity) {
        String[] PERMISSIONS_STORAGE = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,


        };
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return true ;
        }
        else {
            return true;
        }

    }
    public static boolean checkManageDocsPermission(Activity activity){
        Log.e("AK","Checking Permissions");
        String[] PERMISSIONS_STORAGE = new String[]{
                Manifest.permission.MANAGE_DOCUMENTS
        };
        int permission = ActivityCompat.checkSelfPermission(activity,Manifest.permission.MANAGE_DOCUMENTS);

        if(permission != PackageManager.PERMISSION_GRANTED){
            requestPermissions(activity,PERMISSIONS_STORAGE,REQUEST_MANAGE_DOC);
            return true ;
        } else {
            return  true ;
        }
    }
    public static boolean checkStoragePermission(Context context) {
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                AppConstants.REQUEST_TAGS.REQUEST_STORAGE_PERMISSION);
    }

    public static void askForContactPermission(final Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(activity,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(activity,
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , AppConstants.REQUEST_TAGS.REQUEST_CONTACT_TAG);
                        }
                    });
                    builder.show();
                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {

                    // No explanation needed, we can request the permission.

                    requestPermissions(activity,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            REQUEST_CONTACT_TAG);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkCameraPermission(final Context context )
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("sampletask");
                    alertBuilder.setMessage("Please Provide Camera Persmission");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, AppConstants.REQUEST_TAGS.REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, AppConstants.REQUEST_TAGS.REQUEST_CAMERA);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
