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
 * Created by mayuukhvarshney on 12/05/16.
 */
public class MainMenuFragment extends Fragment {
    protected int mPhotoSize, mPhotoSpacing;
    protected ImageAdapter imageAdapter;
    protected  GridView photoGrid;
    public static final int[] ICONS={R.mipmap.jimmy_page, R.mipmap.stirling,

            R.mipmap.hardwell, R.mipmap.elton_john, R.mipmap.ravi_shankar, R.mipmap.charlie_parker, R.mipmap.accordion_menu, R.mipmap.flute_player};

    View view;
    public static final String[] CONTENT={"Guitar","Violin","Electronic","Piano","Sitar","Saxophone","Accordion","Flute"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

  view = inflater.inflate(R.layout.grid_menu, container, false);
        mPhotoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);
        mPhotoSpacing = getResources().getDimensionPixelSize(R.dimen.photo_spacing);
        imageAdapter = new ImageAdapter(getActivity(),ICONS,CONTENT);


        photoGrid = (GridView)view.findViewById(R.id.grid_view1);
        photoGrid.setAdapter(imageAdapter);

        photoGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    GuitarGenreFragment fragobj=new GuitarGenreFragment();
                    FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.FragmentContainer, fragobj,"Guitar");
                    ft.addToBackStack(null);
                    ft.commit();
                }
                else if(position==1){
                    ViolinFragment fragment= new ViolinFragment();
                    GoToFragment(fragment,"Violin");

                }
                else if(position==2){
                    ElectronicFragment fragment = new ElectronicFragment();
                    GoToFragment(fragment,"Electronic");
                }
                else if(position==3){
                    PianoFragment fragment = new PianoFragment();
                    GoToFragment(fragment,"Piano");
                }
                else if(position==4){
                    SitarFragment fragment = new SitarFragment();
                    GoToFragment(fragment,"Sitar");
                }
                else if(position==5){
                    SaxophoneFragment fragment = new SaxophoneFragment();
                    GoToFragment(fragment,"Saxophone");
                }
                else if(position==6){
                    AccordionFragment fragment = new AccordionFragment();
                    GoToFragment(fragment,"Accordion");
                }
                else
                {
                    FluteFragment fragment = new FluteFragment();
                    GoToFragment(fragment,"Flute");

                }

            }
        });
        photoGrid.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (imageAdapter.getNumColumns() == 0) {
                    final int numColumns = (int) Math.floor(photoGrid.getWidth()/(mPhotoSize+mPhotoSpacing));

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
    protected void GoToFragment(Fragment fragment,String txt){
        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FragmentContainer, fragment,txt);
        ft.addToBackStack(null);
        ft.commit();

    }
    @Override
    public void onStop() {
        super.onStop();
        Log.d("GuitarFragment", " the stop method called on back press");

        unbindDrawables(view.findViewById(R.id.gridroot));
        System.gc();

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
