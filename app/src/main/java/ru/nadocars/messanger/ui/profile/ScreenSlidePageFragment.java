package ru.nadocars.messanger.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ru.nadocars.messanger.R;

public class ScreenSlidePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String photoUrl = arguments.getString("url");
        View view = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ImageView carPhotoImageView = (ImageView) view.findViewById(R.id.carPhotoImageView);
        if (null != photoUrl && photoUrl.length() > 0 && photoUrl.toLowerCase().startsWith("http")) {
            Picasso.with(getContext()).load(photoUrl).resize(0, 1500).into(carPhotoImageView);
        } else if (null != photoUrl && photoUrl.length() > 0) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(photoUrl, options);
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            String imageType = options.outMimeType;
            Bitmap sampledBitmap = decodeSampledBitmap(photoUrl, 800, 600);
            carPhotoImageView.setImageBitmap(sampledBitmap);
        }
        return view;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public Bitmap decodeSampledBitmap(String url, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(url, options);
    }

}
