package com.example.project_rajut;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class viewAdapterKeuangan extends FragmentStateAdapter {

    public viewAdapterKeuangan(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0 :
                return new harianFragment();
            case 1 :
                return new mingguanFragment();
            case 2 :
                return new bulananFragment();
            case 3 :
                return new tahunanFragment();
            default:
                return new membayarFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
