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
 * Created by mayuukhvarshney on 29/05/16.
 */
public class SaxophoneFragment extends Fragment {
    protected int mPhotoSize, mPhotoSpacing;
    protected ImageAdapter imageAdapter;
    protected GridView photoGrid;
    private static int[] ICONS = {R.mipmap.parker_jazz, R.mipmap.krik_classical, R.mipmap.coleman_blues, R.mipmap.clar_rock, R.mipmap.sax_rock, R.mipmap.proper_house, R.mipmap.sax_electronic, R.mipmap.sax_punk};
    private static String[] CONTENT = {"Jazz", "Classical", "Blues", "Rock", "Pop", "House", "Electronic", "Funk"};
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
            GoToGenre("Saxophone", "Jazz");
        } else if (position == 1) {
            GoToGenre("Saxophone", "Classical");
        } else if (position == 2) {
            GoToGenre("Saxophone", "Jazz & Blues"); // might change the  space to %20 and "&" to %26 and space again to %20
        } else if (position == 3) {
            GoToGenre("Saxophone", "Rock");

        } else if (position == 4) {
            GoToGenre("Saxophone", "Pop");
        } else if (position == 5) {
            GoToGenre("Saxophone", "House");
        } else if (position == 6) {
            GoToGenre("Saxophone", "Electronic");
        } else {
            GoToGenre("Saxophone", "Funk");
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
