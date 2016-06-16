package com.kaidoh.mayuukhvarshney.gearjam;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
/**
 * Created by mayuukhvarshney on 12/05/16.
 */
public class DisplayTrackFragment extends Fragment {
    Map<String, String> Params = new HashMap<>();
    Map<String, Integer> offset = new HashMap<>();
    Map<String,Integer> Duration= new HashMap<>();
    public List<Track> mPlayListItems, AllSongs;
    protected Set<Track> Song_Set;
    protected SCTrackAdapter mAdapter;
    protected ListView listView;
    protected ProgressWheel progress;
    DataPass data;
    NewTrackAdapter theAdapter;

    String Inst;
    String category;
    Boolean Load_Result = false;
    View view;
RecyclerView main ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = DisplayTrackFragment.this.getArguments();
        Inst = bundle.getString("Instrument");
        category = bundle.getString("Genre");
        view = inflater.inflate(R.layout.track_layout, container, false);
        mPlayListItems = new ArrayList<Track>();
        Song_Set = new LinkedHashSet<Track>(mPlayListItems);
        //mAdapter = new SCTrackAdapter(getActivity(), mPlayListItems);
//        listView.setAdapter(mAdapter);
        SCService scService = SoundCloud.getService();
        Params.put("q", Inst);
        Params.put("tags", category);
        offset.put("limit", 125);
        Duration.put("duration[from]",180000);
        progress= (ProgressWheel) view.findViewById(R.id.progress_wheel);
        main = (RecyclerView) view.findViewById(R.id.tracklayout_list);
        main.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
        progress.spin();
        try {
            new BackgroundLoadTracks(scService, Params, Duration, offset) {
                @Override
                protected void onPostExecute(List<Track> result) {
                    super.onPostExecute(result);

                }

            }.execute();
        }
        catch(Exception e){
            e.printStackTrace();
        }
return view;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
       Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.FragmentContainer);
        if(f instanceof DisplayTrackFragment){
            theAdapter= new NewTrackAdapter(getActivity(),mPlayListItems,false);
        }
        else
        {
            theAdapter= new NewTrackAdapter(getActivity(),mPlayListItems,true);
        }
        recyclerView.setAdapter(theAdapter);
        theAdapter.notifyDataSetChanged();


    }
     class BackgroundLoadTracks extends AsyncTask<Void,Void,List<Track> >{
       SCService SoundCloudService;
        Map<String, String> Params = new HashMap<>();
        Map<String, Integer> offset = new HashMap<>();
         Map<String,Integer> duration= new HashMap<>();

        public BackgroundLoadTracks(SCService scService,Map<String,String> par,Map<String,Integer> l,Map<String,Integer> off){
            SoundCloudService=scService;
            this.Params=par;
            this.offset=off;
            this.duration=l;
        }

        @Override
        protected List<Track> doInBackground(Void... params) {




  SoundCloudService.getRecentTracks(Params,duration, offset, new Callback<List<Track>>() {
         @Override
         public void success(List<Track> tracks, Response response) {
             loadTracks(tracks);

         }

         @Override
         public void failure(RetrofitError error) {
             main.setVisibility(View.VISIBLE);
             progress.stopSpinning();
             progress.setVisibility(View.INVISIBLE);
             Toast.makeText(getActivity(), "There was an Error :(", Toast.LENGTH_SHORT).show();
             error.printStackTrace();

         }


     });


            //String URl="https://api.soundcloud.com/tracks?client_id=bc88f8a89b97ddabab5142acac00deac&q=guitar&filter.genre_or_tag=acoustic";


return mPlayListItems;

        }

    }
    private void loadTracks(final List<Track> tracks) {
        mPlayListItems.clear();

        mPlayListItems.addAll(tracks);
        Song_Set=new LinkedHashSet<Track>(mPlayListItems);
        mPlayListItems.clear();
        mPlayListItems.addAll(Song_Set);
        Collections.shuffle(mPlayListItems);
        data.onDataPass(mPlayListItems);
        try {
            ((MainActivity) getActivity()).musicSrv.setList(mPlayListItems);
            ((MainActivity) getActivity()).musicSrv.CurrentFragment(false);
            main.setVisibility(View.VISIBLE);
            progress.stopSpinning();
            progress.setVisibility(View.INVISIBLE);
            View recyclerView = view.findViewById(R.id.tracklayout_list);
            assert recyclerView != null;
            setupRecyclerView((RecyclerView) recyclerView);
//        mAdapter.notifyDataSetChanged();
        }
        catch(Exception e){
            e.printStackTrace();

        }

    }
    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
        try
        {
            data=(DataPass) a;
        }
        catch(ClassCastException e){
            e.printStackTrace();
        }



    }
    public interface DataPass{
        void onDataPass(List<Track> tracks);
    }
}
