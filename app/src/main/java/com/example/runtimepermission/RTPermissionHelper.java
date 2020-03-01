package com.example.runtimepermission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
//import android.support.v4.app.ActivityCompat;


/**
 * Created by admin on 22/03/2018.
 */

public class RTPermissionHelper {

    private SharedPreferences permissionStatus;

    public static String[] permissionsRequired = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static String[] permissionsRequiredCall = new String[]{Manifest.permission.CALL_PHONE};
    public static String[] permissionsSMSRequired = new String[]{Manifest.permission.READ_SMS};
    public static String[] LOCATION_PERMS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    public static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    public static final int PERMISSION_CALLBACK_CONSTANT_LOC = 105;


    /**
     * method to check the camera and library permission status
     * @param activity
     * @return return true if Permission is Granted otherwise false.
     */
    public boolean getPermissionAllowedForCameraAndLibrary(final Activity activity) {
        permissionStatus = activity.getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);
        if(ActivityCompat.checkSelfPermission(activity, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permissionsRequired[0])|| ActivityCompat.shouldShowRequestPermissionRationale(activity,permissionsRequired[1]) || ActivityCompat.shouldShowRequestPermissionRationale(activity,permissionsRequired[2])){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(activity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        //Toast.makeText(activity, "Go to Permissions to Grant  Camera and Storage", Toast.LENGTH_LONG).show();
                        showAlertDialog(activity, "Go to Permissions to Grant  Camera and Storage");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(activity,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            return false;
        }
        return true;
    }


    /**
     * method to check the call permission
     * @param activity
     * @return return true if Permission is Granted otherwise false.
     */
    public boolean getPermissionAllowedForCall(final Activity activity) {
        permissionStatus = activity.getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);
        if(ActivityCompat.checkSelfPermission(activity, permissionsRequiredCall[0]) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permissionsRequiredCall[0])){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(activity, permissionsRequiredCall, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequiredCall[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                       // Toast.makeText(activity, "Go to Permissions to Grant  Camera and Storage", Toast.LENGTH_LONG).show();
                        showAlertDialog(activity, "Go to Permissions to Grant  Camera and Storage");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(activity,permissionsRequiredCall,PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequiredCall[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            return false;
        }
        return true;
    }


    /**
     * We will call this method to check the location permission status
     * @param activity
     * @return
     */
    public boolean getPermissionAllowedForLocation(final Activity activity) {
        permissionStatus = activity.getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);
        if(ActivityCompat.checkSelfPermission(activity, permissionsSMSRequired[0]) != PackageManager.PERMISSION_GRANTED ){

            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,LOCATION_PERMS[0])){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Need Location Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(activity, LOCATION_PERMS, PERMISSION_CALLBACK_CONSTANT_LOC);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(LOCATION_PERMS[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Need Location Permissions");
                builder.setMessage("This app needs Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                       // Toast.makeText(activity, "Go to Permissions to Grant  Location access", Toast.LENGTH_LONG).show();
                        showAlertDialog(activity, "Go to Permissions to Grant  Location access");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(activity,LOCATION_PERMS,PERMISSION_CALLBACK_CONSTANT_LOC);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(LOCATION_PERMS[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            return false;
        }
        return true;
    }



    //We will call this method to check the run time permission for Read and Write.
    public boolean getPermissionAllowedReadOTP(final Activity activity) {
        permissionStatus = activity.getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);
        if(ActivityCompat.checkSelfPermission(activity, permissionsSMSRequired[0]) != PackageManager.PERMISSION_GRANTED ){

            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permissionsSMSRequired[0])){
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Auto Detect Otp");
                builder.setMessage("Need read message permissions for auto read otp!");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(activity, permissionsSMSRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsSMSRequired[0],false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Auto Detect Otp");
                builder.setMessage("Need read message permissions for auto read otp!");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                       // Toast.makeText(activity, "Go to Permissions to Grant Read & Write", Toast.LENGTH_LONG).show();
                        showAlertDialog(activity, "Go to Permissions to Grant Read & Write");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(activity,permissionsSMSRequired,PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsSMSRequired[0],true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            return false;
        }
        return true;
    }


    public static void showAlertDialog(final Activity context, String msg) {
        try {
            final AlertDialog.Builder b = new AlertDialog.Builder(context);
            b.setCancelable(false);
            b.setTitle("Alert");
            b.setMessage(msg);
            b.setIcon(android.R.drawable.ic_dialog_alert);
            b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            b.show();
        } catch (Exception e) {
            Log.d("", "Show Dialog: " + e.getMessage());
        }
    }


}