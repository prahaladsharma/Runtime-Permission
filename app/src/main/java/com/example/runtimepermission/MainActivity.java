package com.example.runtimepermission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileWithBitmapCallback;

import java.io.File;

import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

import static com.example.runtimepermission.RTPermissionHelper.permissionsRequired;
import static com.example.runtimepermission.RTPermissionHelper.showAlertDialog;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences permissionStatus;
    private Activity context;
    private Button btnClick;
    private ImageView image;
    private byte[] byteArrayOfImage;
    private String pathOfImage;
    private String fileName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnClick = (Button) findViewById(R.id.btnClick);
        image = (ImageView) findViewById(R.id.image);

        context = this;

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFile();
            }
        });

    }

    private void getFile() {
        try {
            permissionStatus = context.getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);
            boolean isAllowed = new RTPermissionHelper().getPermissionAllowedForCameraAndLibrary(context);
            if (!isAllowed) {
                showPictureDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPictureDialog() {
        EasyImage.openChooserWithGallery(context, "Select files", 0);
    }


    /**
     * Call for Run-Time permission
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RTPermissionHelper.PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            if (allgranted) {
                showPictureDialog();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(context, permissionsRequired[0])) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Need Permissions for Camera and Gallery!");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(context, permissionsRequired, RTPermissionHelper.PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //  Snackbar.make(contactUsLayout, "Unable to get permission!", Snackbar.LENGTH_LONG).show();
                showAlertDialog(context, "Unable to get permission!");
                showPictureDialog();
            }
        }
    }

    /**
     * convert path into Base 64 and make array.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, context, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
            }

            @Override
            public void onImagePicked(File file, EasyImage.ImageSource source, int type) {
                //Handle the images
                Log.e("Images", file.getAbsolutePath());

                //Getting image path
                pathOfImage = file.getAbsolutePath();

                //convert into bitmap
                Bitmap bitmap = Utility.autocompressImage(Uri.fromFile(file).toString(), context);

                //Image compressor without loosing image quality.
                Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                Tiny.getInstance().source(bitmap).asFile().withOptions(options).compress(new FileWithBitmapCallback() {
                    @Override
                    public void callback(boolean isSuccess, Bitmap newbitmap, String outfile, Throwable t) {

                        byteArrayOfImage = Utility.toBytes(newbitmap);

                    }
                });


            }

        });
    }
}
