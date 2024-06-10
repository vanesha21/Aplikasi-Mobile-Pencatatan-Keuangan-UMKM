package com.example.project_rajut;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewAdapter extends FragmentStateAdapter {

    public viewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0 :
                return new membayarFragment();
            case 1 :
                return new belummembayarFragment();
            default:
                return new membayarFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
