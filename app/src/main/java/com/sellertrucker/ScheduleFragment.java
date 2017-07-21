package com.sellertrucker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmall.ultraviewpager.UltraViewPager;

public class ScheduleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        UltraViewPager schedule_viewpager = (UltraViewPager) view.findViewById(R.id.schedule_viewpager);

        schedule_viewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);

        //initialize HomePagerAdapter，and add child view to UltraViewPager
        MyTruckPagerAdapter adapter = new MyTruckPagerAdapter(false);
        schedule_viewpager.setAdapter(adapter);

//        //initialize built-in indicator
//        myTruckViewPager.initIndicator();
//
//        //set style of indicators
//        myTruckViewPager.getIndicator()
//                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
//                .setFocusColor(R.color.colorPrimary)
//                .setNormalColor(Color.WHITE)
//                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
//
//
////        ultraViewPager.setAutoMeasureHeight(true);
//
//        //set the alignment
//        myTruckViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//        //construct built-in indicator, and add it to  UltraViewPager
//        myTruckViewPager.getIndicator().build();

        //set an infinite loop
        schedule_viewpager.setInfiniteLoop(true);
        //enable auto-scroll mode
        schedule_viewpager.setAutoScroll(5000);


        return view;
    }


}

