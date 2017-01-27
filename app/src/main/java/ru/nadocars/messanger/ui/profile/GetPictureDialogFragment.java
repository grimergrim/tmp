package ru.nadocars.messanger.ui.profile;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.io.File;

import ru.nadocars.messanger.R;

public class GetPictureDialogFragment extends DialogFragment {

    protected static final int AVATAR_CAMERA_REQUEST = 0;
    protected static final int AVATAR_GALLERY_REQUEST = 1;
    protected static final int CAR_CAMERA_REQUEST = 2;
    protected static final int CAR_GALLERY_REQUEST = 3;
    private int cameraRequestCode;
    private int galleryRequestCode;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        final View view = View.inflate(getActivity().getApplicationContext(),
                R.layout.choose_picture_fragment, null);
        final Button openGalleryButton = (Button) view.findViewById(R.id.open_gallery_button);
        final Button openCameraButton = (Button) view.findViewById(R.id.open_camera_button);
        cameraRequestCode = arguments.getInt("camera request code");
        galleryRequestCode = arguments.getInt("gallery request code");
        openGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, galleryRequestCode);
                dismiss();
            }
        });
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.CAMERA, cameraRequestCode);
                dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        Window alertDialogWindow = alertDialog.getWindow();
        if (alertDialogWindow != null) {
            alertDialogWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return alertDialog;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void checkPermission(String permission, int requestCode) {
        if (permission.equals(Manifest.permission.CAMERA)) {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CAMERA);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                openCamera(requestCode);
            } else {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (cameraRequestCode == AVATAR_CAMERA_REQUEST) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                AVATAR_CAMERA_REQUEST);
                    } else if (cameraRequestCode == CAR_CAMERA_REQUEST) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.CAMERA},
                                CAR_CAMERA_REQUEST);
                    }

                }
            }
        } else if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                openGallery(requestCode);
            } else {
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (galleryRequestCode == AVATAR_GALLERY_REQUEST) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                AVATAR_GALLERY_REQUEST);
                    } else if (galleryRequestCode == CAR_GALLERY_REQUEST) {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                CAR_GALLERY_REQUEST);
                    }

                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case AVATAR_CAMERA_REQUEST: {
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    openCamera(AVATAR_CAMERA_REQUEST);
                }
                break;
            }
            case AVATAR_GALLERY_REQUEST: {
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    openGallery(AVATAR_GALLERY_REQUEST);
                }
                break;
            }
            case CAR_CAMERA_REQUEST: {
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    openCamera(CAR_CAMERA_REQUEST);
                }
                break;
            }
            case CAR_GALLERY_REQUEST: {
                if (grantResults.length > 0 && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    openGallery(CAR_GALLERY_REQUEST);
                }
                break;
            }
        }
    }

    private void openCamera(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        getActivity().startActivityForResult(intent, requestCode);
    }

    private void openGallery(int requestCode) {
        Intent pictureActionIntent;
        pictureActionIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        getActivity().startActivityForResult(pictureActionIntent, requestCode);
    }

}