package com.kaidoh.mayuukhvarshney.gearjam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;

/**
 * Created by mayuukhvarshney on 30/05/16.
 */
public class AccordionFragment extends Fragment {
    protected int mPhotoSize, mPhotoSpacing;
    protected ImageAdapter imageAdapter;
    protected GridView photoGrid;

    private int[] ICONS={R.mipmap.accordion_classical,R.mipmap.accordion_jazz,R.mipmap.accordion_tango,R.mipmap.accordion_folk,R.mipmap.accordion_instrumental,R.mipmap.accoridon_better_soundtrack,R.mipmap.accordion_world,R.mipmap.accordion_acoustiv};
    private String[] CONTENT={"Classical","Jazz","Tango","Folk","Instrumental","Soundtrack","World","Acoustic"};
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.grid_menu, container, false);
        mPhotoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);
        mPhotoSpacing = getResources().getDimensionPixelSize(R.dimen.photo_spacing);
        imageAdapter = new ImageAdapter(getActivity(), ICONS, CONTENT);


        photoGrid = (GridView) view.findViewById(R.id.grid_view1);
        photoGrid.setAdapter(imageAdapter);
        photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    GoToGenre("Accordion", "Classical");
                } else if (position == 1) {
                    GoToGenre("Accordion", "Jazz");
                } else if (position == 2) {
                    GoToGenre("Accordion", "Tango");
                } else if (position == 3) {
                    GoToGenre("Accordion", "Folk");
                } else if (position == 4) {
                    GoToGenre("Accordion", "Instrumental");
                } else if (position == 5) {
                    GoToGenre("Accordion", "Soundtrack");
                } else if (position == 6) {
                    GoToGenre("Accordion", "World");
                } else {
                    GoToGenre("Accordion", "Acoustic");
                }
            }
        });
        photoGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (imageAdapter.getNumColumns() == 0) {
                    final int numColumns = (int) Math.floor(photoGrid.getWidth() / (mPhotoSize + mPhotoSpacing));
                    if (numColumns > 0) {
                        final int columnWidth = (photoGrid.getWidth() / numColumns);
                        imageAdapter.setNumColumns(numColumns);
                        imageAdapter.setItemHeight(columnWidth);


                    }
                }
            }
        });
    return view;
    }
    protected void GoToGenre(String I,String G){

        DisplayTrackFragment fragobj=new DisplayTrackFragment();
        Bundle bundle=new Bundle();
        bundle.putString("Instrument", I);
        bundle.putString("Genre", G);
        fragobj.setArguments(bundle);
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FragmentContainer, fragobj);
        ft.addToBackStack(null);
        ft.commit();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Fragment", " the stop method called on back press");

        unbindDrawables(view.findViewById(R.id.gridroot));
        System.gc();
        Runtime.getRuntime().gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            if (!(view instanceof AdapterView<?>))
                ((ViewGroup) view).removeAllViews();
        }

    }
}
