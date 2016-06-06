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
public class FluteFragment extends Fragment {
    protected int mPhotoSize, mPhotoSpacing;
    protected ImageAdapter imageAdapter;
    protected GridView photoGrid;
    private static int[] ICONS={R.mipmap.flute_classical,R.mipmap.flute_jazz,R.mipmap.flute_instrumental,R.mipmap.flute_contemporary,R.mipmap.flute_electronic,R.mipmap.flute_ambient,R.mipmap.flute_soundtrack,R.mipmap.flute_world};

    private static String[] CONTENT={"Classical","Jazz","Instrumental","Contemporary","Electronic","Ambient","Soundtrack","World"};
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
                switch (position) {
                    case 0:
                        GoToGenre("Flute", "Classical");
                        break;
                    case 1:
                        GoToGenre("Flute", "Jazz");
                        break;
                    case 2:
                        GoToGenre("Flute", "Instrumental");
                        break;
                    case 3:
                        GoToGenre("Flute", "Contemporary classical");
                        break;
                    case 4:
                        GoToGenre("Flute", "Electronic");
                        break;
                    case 5:
                        GoToGenre("Flute", "Ambient");
                        break;
                    case 6:
                        GoToGenre("Flute", "Soundtrack");
                        break;
                    default:
                        GoToGenre("Flute", "World");
                        break;

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
