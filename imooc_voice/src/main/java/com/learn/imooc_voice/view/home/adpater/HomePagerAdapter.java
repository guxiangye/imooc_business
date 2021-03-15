package com.learn.imooc_voice.view.home.adpater;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.learn.imooc_voice.model.CHANNEL;
import com.learn.imooc_voice.view.discovery.DiscoveryFragment;
import com.learn.imooc_voice.view.friend.FriendFragment;
import com.learn.imooc_voice.view.mine.MineFragment;
import com.learn.imooc_voice.view.video.VideoFragment;

/**
 * 首页 ViewPager Adapter
 * */
public class HomePagerAdapter extends FragmentPagerAdapter {
    private CHANNEL[] mDataList;

    public HomePagerAdapter(FragmentManager fm, CHANNEL[] dataList) {
        super(fm);

        mDataList = dataList;
    }

    @Override
    public Fragment getItem(int position) {
        int type = mDataList[position].getValue();
        switch (type) {
            case CHANNEL.MINE_ID:
                return MineFragment.newInstance();
            case CHANNEL.DISCOVERY_ID:
                return DiscoveryFragment.newInstance();
            case CHANNEL.FRIEND_ID:
                return FriendFragment.newInstance();
            case CHANNEL.VIDEO_ID:
                return VideoFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.length;
    }
}
