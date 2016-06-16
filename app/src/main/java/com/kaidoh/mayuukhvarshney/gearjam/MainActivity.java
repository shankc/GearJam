package com.kaidoh.mayuukhvarshney.gearjam;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kaidoh.mayuukhvarshney.gearjam.MusicService.MusicBinder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DisplayTrackFragment.DataPass,PlayListFragment.PlayListPass {
    public MusicService musicSrv=new MusicService();
    private Intent playIntent;
    private boolean musicBound=false;
    Boolean Pause=false;
   // private BroadcastReceiver mMessageReceiver;
    protected boolean Preparation=false;
    protected int Current_position=0;
    protected long mediapos;
    protected long mediamax;
protected final Handler mHandler= new Handler();
    private List<Track> mPlayListItems;
 private SeekBar mSeek_Bar;
    protected ImageView mSelectedTrackImage,mPlayerControl,Sc_icon;
    protected TextView mSelectedTrackTitle,mArtistTitile;
protected File folder;
    public static final String TAG ="MainActivity";
    protected LinkedHashMap<Integer,String> IDS;
    protected List<Track> PlayListSongs;
    protected ArrayList<LinkedHashMap<String,String>> PlayListTracks;
    RelativeLayout MusicPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        folder =this.getDir("GearJam", Context.MODE_PRIVATE); //new File(Environment.getExternalStorageDirectory()+File.separator+"GearJam");

        boolean success=false;
        if(!folder.exists())
        {
            success=folder.mkdir();

        }
        if(success){
            Log.d(TAG,"folder made!!");
        }

       MainMenuFragment fragobj=new MainMenuFragment();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.FragmentContainer, fragobj);
        ft.addToBackStack(null);
        ft.commit();

        mSeek_Bar =  (SeekBar)findViewById(R.id.seek_bar);
        mSelectedTrackTitle = (TextView) findViewById(R.id.selected_track_title);
        mSelectedTrackImage = (ImageView) findViewById(R.id.selected_track_image);
        mPlayerControl = (ImageView) findViewById(R.id.player_control);
        mArtistTitile=(TextView) findViewById(R.id.artist_name);
        Sc_icon=(ImageView)findViewById(R.id.soundcloud_icon);
        MusicPlayer= (RelativeLayout) findViewById(R.id.music_player);
        Sc_icon.setImageResource(R.drawable.logo_sc_white);

        MusicPlayer.setVisibility(View.GONE);


        mPlayerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toggle();
            }
        });



    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int index;
            boolean isplaying;
            MusicPlayer.setVisibility(View.VISIBLE);
            String title="",art="",username="";
            Bundle extras=intent.getExtras();
            index=extras.getInt("SongIndex");
            Preparation=extras.getBoolean("PrepState");
            title=extras.getString("Title");
            art=extras.getString("Art");
            username=extras.getString("UserName");
            Log.d(TAG," the recived information "+index+" "+Preparation+" ");
            if(Current_position!=index){
                if(mPlayListItems==null)
                {
                    Log.d("MainActivity"," the list has become null");
                }
               // Track track = mPlayListItems.get(index);
                mSelectedTrackTitle.setText(title);
                mArtistTitile.setText(username);
                Picasso.with(MainActivity.this).load(art).into(mSelectedTrackImage);
            }
            ToggleSwitch();
            SeekInit();

        }
    };

    private void SeekInit() {
        if(musicSrv!=null) {
            if (musicSrv.isPng()) {
                mediapos = musicSrv.getPosn();
                mediamax = musicSrv.getDur();
                mSeek_Bar.setMax((int) mediamax);
                mSeek_Bar.setProgress((int) mediapos);
                mHandler.removeCallbacks(moveSeekBarThread);
                mHandler.postDelayed(moveSeekBarThread, 100);

            }
        }
        else
        {
            Log.d(TAG,"Came Crashing here in seekint"+musicBound);
        }
    }
    private Runnable moveSeekBarThread=new Runnable() {
        @Override
        public void run() {
            if(musicSrv!=null) {
                if (musicSrv.isPng()) {
                    long newmediapos = musicSrv.getPosn();
                    long newmediamax = musicSrv.getDur();
                    mSeek_Bar.setMax((int) newmediamax);
                    mSeek_Bar.setProgress((int) newmediapos);
                    mHandler.postDelayed(this, 100);
                }
            }
            else
            {
                Log.d(TAG,"Crashing here run "+musicBound);
            }
        }
    };
    public void Toggle() {

        if (musicSrv.isPng()) {
            Pause = true;
            musicSrv.pausePlayer();
            mPlayerControl.setImageResource(R.drawable.ic_play_circle_filled_white_24dp);
        } else {
            Pause = false;
            musicSrv.go();
            mPlayerControl.setImageResource(R.drawable.ic_pause_circle_filled_white_24dp);
        }
        musicSrv.PauseState(Pause);

    }
    public void ToggleSwitch() {
        if(musicSrv!=null) {
            if (musicSrv.isPng()) {
                mPlayerControl.setImageResource(R.drawable.ic_pause_circle_filled_white_24dp);
            } else {
                mPlayerControl.setImageResource(R.drawable.ic_play_circle_filled_white_24dp);
            }
        }
        else
        {
            Log.d("MainActivity","problem due to music srv becoming null");
        }


    }
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //SongList songfragment=(SongList)getSupportFragmentManager().
            MusicBinder binder = (MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(mPlayListItems);
            musicSrv.setPlayList(PlayListSongs);
            musicSrv.setIDs(IDS);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;

        }

    };
    @Override
    protected void onStart(){
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            //  playIntent.putExtra("M",new Messenger(messageHandler));
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                    new IntentFilter("Prep"));
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(playIntent);
        musicSrv=null;
        unbindService(musicConnection);

        //LocalBroadcastManager.getInstance(this).unregisterReceiver((mMessageReceiver));

    }

    @Override

    public void onDataPass(List<Track> tracks){
        mPlayListItems=tracks;
        if(mPlayListItems!=null){
            Log.d("MainActivity", " the songs recieved " + mPlayListItems.get(5).getTitle());
        }

    }
    @Override
    public void onPlayListDataPass(ArrayList<LinkedHashMap<String,String> >pTracks,List<Track> playTracks,LinkedHashMap<Integer,String> theIDs){
        PlayListTracks=pTracks;
        PlayListSongs=playTracks;
        IDS=theIDs;
        if(IDS!=null && PlayListSongs!=null){
            //Track track = PlayListSongs.get(0);
            Log.d("MainActivity"," the song is "+PlayListSongs.get(0).getTitle());
            //Log.d("MainActivity", " the songs recieved " + IDS.get(track.getID()));
        }
        else
        {
            Log.d("MainActivity","Data was not recived");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id== R.id.playlist_item){
            PlayListFragment fragobj=new PlayListFragment();
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.FragmentContainer, fragobj);
            ft.addToBackStack(null);
            ft.commit();

        }




        return super.onOptionsItemSelected(item);
    }

}
