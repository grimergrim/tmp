package ru.nadocars.messanger.ui.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<String> mPhotoList;

    ScreenSlidePagerAdapter(FragmentManager fragmentManager, List<String> photos) {
        super(fragmentManager);
        mPhotoList = photos;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("url", mPhotoList.get(position));
        ScreenSlidePageFragment screenSlidePageFragment = new ScreenSlidePageFragment();
        screenSlidePageFragment.setArguments(bundle);
        return screenSlidePageFragment;
    }

    @Override
    public int getCount() {
        return mPhotoList.size();
    }

    List<String> getPhotoList() {
        return mPhotoList;
    }

    void setPhotoList(List<String> photoList) {
        mPhotoList = photoList;
        notifyDataSetChanged();
    }
}
