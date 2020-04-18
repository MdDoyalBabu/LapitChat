package com.example.chatlapit20;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class SectionViewPagerAdapter extends FragmentPagerAdapter {
    public SectionViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                  RequestFragment requestFragment=new RequestFragment();
                  return  requestFragment;
               case 1:
                  FriendsFragment friendsFragment=new FriendsFragment();
                  return  friendsFragment;
               case 2:
                  ChatFragment chatFragment=new ChatFragment();
                  return  chatFragment;

                  default:
                      return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){

        switch (position){
            case 0:
                return "Request";
            case 1:
                return "Friends";
            case 2:
                return "Chats";
                default:
                    return null;
        }

    }


}
