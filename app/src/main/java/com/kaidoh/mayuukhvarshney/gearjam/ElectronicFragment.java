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
public class ElectronicFragment extends Fragment {
    protected int mPhotoSize, mPhotoSpacing;
    protected ImageAdapter imageAdapter;
    protected GridView photoGrid;
    private int[] ICONS={R.mipmap.armin_trance,R.mipmap.edm,R.mipmap.electronic_skrillex,R.mipmap.guetta_dance,R.mipmap.house,R.mipmap.progressive_house,R.mipmap.progressive_trance,R.mipmap.psytrance};
    private String[] CONTENT={"Trance","EDM","Electronic","Dance","House","Progressive House","Progressive Trance","PsyTrance"};
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
                    DisplayTrackFragment fragobj = new DisplayTrackFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Instrument", "Electronic");
                    bundle.putString("Genre", "Trance");
                    fragobj.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FragmentContainer, fragobj);
                    ft.addToBackStack(null);
                    ft.commit();
                } else if (position == 1) {
                    DisplayTrackFragment fragobj = new DisplayTrackFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Instrument", "Electronic");
                    bundle.putString("Genre", "Edm");
                    fragobj.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FragmentContainer, fragobj);
                    ft.addToBackStack(null);
                    ft.commit();
                } else if (position == 2) {
                    DisplayTrackFragment fragobj = new DisplayTrackFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Instrument", "Electronic");
                    bundle.putString("Genre", "Electronic");
                    fragobj.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FragmentContainer, fragobj);
                    ft.addToBackStack(null);
                    ft.commit();
                } else if (position == 3) {
                    DisplayTrackFragment fragobj = new DisplayTrackFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Instrument", "Electronic");
                    bundle.putString("Genre", "Dance");
                    fragobj.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FragmentContainer, fragobj);
                    ft.addToBackStack(null);
                    ft.commit();
                } else if (position == 4) {
                    DisplayTrackFragment fragobj = new DisplayTrackFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Instrument", "Electronic");
                    bundle.putString("Genre", "House");
                    fragobj.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FragmentContainer, fragobj);
                    ft.addToBackStack(null);
                    ft.commit();
                } else if (position == 5) {
                    DisplayTrackFragment fragobj = new DisplayTrackFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Instrument", "Electronic");
                    bundle.putString("Genre", "Progressive house");
                    fragobj.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FragmentContainer, fragobj);
                    ft.addToBackStack(null);
                    ft.commit();
                } else if (position == 6) {
                    DisplayTrackFragment fragobj = new DisplayTrackFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Instrument", "Electronic");
                    bundle.putString("Genre", "Progressive trance");
                    fragobj.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FragmentContainer, fragobj);
                    ft.addToBackStack(null);
                    ft.commit();
                } else {
                    DisplayTrackFragment fragobj = new DisplayTrackFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Instrument", "Electronic");
                    bundle.putString("Genre", "Psytrance");
                    fragobj.setArguments(bundle);
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FragmentContainer, fragobj);
                    ft.addToBackStack(null);
                    ft.commit();
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
