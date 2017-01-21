package ru.nadocars.messanger.ui.profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.io.File;

import ru.nadocars.messanger.R;

public class GetPictureDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        final View view = View.inflate(getActivity().getApplicationContext(), R.layout.choose_picture_fragment, null);
        final Button openGalleryButton = (Button) view.findViewById(R.id.open_gallery_button);
        final Button openCameraButton = (Button) view.findViewById(R.id.open_camera_button);
        final int cameraRequestCode =arguments.getInt("camera request code");
        final int galleryRequestCode = arguments.getInt("gallery request code");
        openGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pictureActionIntent = null;
                pictureActionIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getActivity().startActivityForResult(pictureActionIntent, galleryRequestCode);
                dismiss();
            }
        });
        openCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment
                        .getExternalStorageDirectory(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(f));
                getActivity().startActivityForResult(intent, cameraRequestCode);
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
}