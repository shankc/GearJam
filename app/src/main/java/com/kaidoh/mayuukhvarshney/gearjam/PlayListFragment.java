package com.kaidoh.mayuukhvarshney.gearjam;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by mayuukhvarshney on 17/05/16.
 */
public class PlayListFragment extends Fragment {
    List<Track> mPlayListItems;
  ArrayList< LinkedHashMap<String,String> >SongList;
    LinkedHashMap<Integer,String> IDS;
    SCTrackAdapter mAdapter;
    ListView listView;
    protected int tempID;
    protected String tempPath;
    protected int Current_position=0;
protected File home;
    PlayListPass data;
    NewTrackAdapter theAdapter;
    View view;
    ProgressWheel progress;
    RecyclerView main;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.track_layout, container, false);

        mPlayListItems = new ArrayList<Track>();
        SongList = new ArrayList<LinkedHashMap<String, String>>();
        IDS = new LinkedHashMap<>();
      home = new File(getActivity().getFilesDir(),"");

        SCTrackService trackService = SoundCloud.getTrackService();
        progress= (ProgressWheel) view.findViewById(R.id.progress_wheel);
        main = (RecyclerView) view.findViewById(R.id.tracklayout_list);
        main.setVisibility(View.INVISIBLE);
        progress.setVisibility(View.VISIBLE);
        progress.spin();
        File[] contents = home.listFiles();
        Log.d("PlayList","the contents "+contents.toString());

         if(home.list().length<=0){
            main.setVisibility(View.VISIBLE);
            progress.stopSpinning();
            progress.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), "No Songs in PlayList :(", Toast.LENGTH_SHORT).show();
        }

        else{
             if(home.list().length>0){
                 Log.d("PlayList","folder has greater length.");
             }

            new RetriveFromFolder(trackService){
                @Override
            protected void onPostExecute(List<Track> array){
                    super.onPostExecute(array);


                }
            }.execute();
        }

            return view;
        }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
       Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.FragmentContainer);
        if(f instanceof PlayListFragment){
            theAdapter= new NewTrackAdapter(getActivity(),mPlayListItems,true);
            Log.d("PlayListFragment "," it is true");
        }
        else
        {
            theAdapter= new NewTrackAdapter(getActivity(),mPlayListItems,false);
            Log.d("PlayListFragment "," it is false");
        }
        recyclerView.setAdapter(theAdapter);
        theAdapter.notifyDataSetChanged();


    }
    class RetriveFromFolder extends AsyncTask<Void,Void,List<Track>>{

        SCTrackService theTrackService;
        public RetriveFromFolder(SCTrackService ScService){
            this.theTrackService=ScService;

        }


        @Override
        protected List<Track> doInBackground(Void... params) {
            {

                if (home.listFiles(new FileExtensionFilter()).length > 0) {
                    for (File file : home.listFiles(new FileExtensionFilter())) {
                        LinkedHashMap<String, String> song = new LinkedHashMap<>();
                        //Log.d("File","the file is "+file.getName());
                        song.put("SongTitle", file.getName().substring(0, findbracket(file.getName())));
                        song.put("SongPath", file.getPath());
                        String txt = file.getName().substring(0, findbracket(file.getName()));

                        SongList.add(song);
                        tempID = convert(filter(file.getName()));

                        tempPath = file.getPath();

                        IDS.put(tempID, tempPath);

                        theTrackService.getTrack(tempID, new Callback<Track>() {

                            @Override
                            public void success(Track track, Response response) {
                                mPlayListItems.add(track);

                               data.onPlayListDataPass(SongList, mPlayListItems, IDS);// only works in the success method!
                                ((MainActivity)getActivity()).musicSrv.setPlayList(mPlayListItems);
                                ((MainActivity)getActivity()).musicSrv.setIDs(IDS);
                                ((MainActivity)getActivity()).musicSrv.CurrentFragment(true);
                                main.setVisibility(View.VISIBLE);
                                progress.stopSpinning();
                                progress.setVisibility(View.INVISIBLE);
                                View recyclerView = view.findViewById(R.id.tracklayout_list);
                                assert recyclerView != null;
                                setupRecyclerView((RecyclerView) recyclerView);

                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.d("PLayListFragment", "retrofit error ", error);
                                Toast.makeText(getActivity(), "Network Error ArtWork couldn't be Loaded :(", Toast.LENGTH_SHORT).show();


                            }

                        });

                    }


                }
                else
                {
                    Log.d("PlayList"," Inside background task.. else ");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            main.setVisibility(View.VISIBLE);
                            progress.stopSpinning();
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "No Songs in PlayList :(", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            return mPlayListItems;

        }

    }
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
    public String filter(String txt){
   int mark=0;

        for(int i=txt.length()-1;i>0;i--)
        {
            if(txt.charAt(i)=='>' ){
                mark = i;
                break;
            }
        }
        int start=mark-1;
        StringBuilder temp=new StringBuilder();
        while(txt.charAt(start)!='<')
        {
            temp.append(txt.charAt(start));
            start--;
        }

        String ID = new StringBuffer(temp).
                reverse().toString();
        return ID;
    }
    public int findbracket(String txt){
        int flag=0;
        for(int i=txt.length()-1;i>0;i--)
        {
            if(txt.charAt(i)=='>'){
                flag=i;
                break;
            }
        }
        return flag;
    }
    public int convert(String s){
        int foo=Integer.parseInt(s);
        return foo;
    }
    @Override
    public void onAttach(Activity a){
        super.onAttach(a);
        try
        {
            data=(PlayListPass) a;
        }
        catch(ClassCastException e){
            e.printStackTrace();
        }



    }
    public interface PlayListPass{
        void onPlayListDataPass(ArrayList<LinkedHashMap<String,String>> theSongList,List<Track> tracks,LinkedHashMap<Integer,String>IDs);
    }
}
