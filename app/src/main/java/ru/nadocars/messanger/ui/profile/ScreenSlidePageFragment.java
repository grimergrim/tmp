package ru.nadocars.messanger.ui.profile;

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
        View view= inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ImageView carPhotoImageView = (ImageView) view.findViewById(R.id.carPhotoImageView);
        if (null != photoUrl && photoUrl.length() > 0 && photoUrl.toLowerCase().startsWith("http")) {
            Picasso.with(getContext()).load(photoUrl).resize(0, 1500).into(carPhotoImageView);
        } else if (null != photoUrl && photoUrl.length() > 0) {
            carPhotoImageView.setImageBitmap(BitmapFactory.decodeFile(photoUrl));
        }
        return view;
    }

}
