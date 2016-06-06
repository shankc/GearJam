package com.kaidoh.mayuukhvarshney.gearjam;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.Util;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by mayuukhvarshney on 12/05/16.
 */
public class MusicService extends Service {
    private final IBinder musicBind = new MusicBinder();
    private List<Track> songs,PlayListsongs;
    private LinkedHashMap<Integer,String> PlayListIDs;

    private int songPosn;
    private boolean PrepStage=false;
    private static final int NOTIFY_ID=1;
    private final Handler handler=new Handler();
    Intent intent;
    private ExoPlayer exoPlayer;
    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 256;
    public boolean pause;
    public boolean inPlayList;

    public void onCreate(){
        super.onCreate();
        intent = new Intent();
        songPosn=0;
        exoPlayer=ExoPlayer.Factory.newInstance(1);


        exoPlayer.addListener(new ExoPlayer.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (exoPlayer.getPlaybackState() == exoPlayer.STATE_ENDED) {
                    exoPlayer.stop();
                    exoPlayer.seekTo(0);
                    playNext();

                } else {
                    if (exoPlayer.getPlaybackState() == exoPlayer.STATE_READY) {
                        PrepStage = true;

                        //sendMessage();
                        String songTitle;
                        //Track theTitle = songs.get(getSongIndex());
                        if(inPlayList){
                            songTitle=PlayListsongs.get(getSongIndex()).getTitle();
                            send(PlayListsongs);
                        }
                        else
                        {
                            songTitle=songs.get(getSongIndex()).getTitle();
                            send(songs);
                        }
                        //String songTitle = theTitle.getTitle();
                        Intent notIntent = new Intent(MusicService.this, MainActivity.class);
                        notIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        //notIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                        PendingIntent pendInt = PendingIntent.getActivity(MusicService.this, PendingIntent.FLAG_CANCEL_CURRENT,
                                notIntent, 0);
                        // PendingIntent pendInt= PendingIntent.getActivity(MusicService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
                        //  PendingIntent pendInt = PendingIntent.getActivity(MusicService.this,0,intent,0);

                        Notification.Builder builder = new Notification.Builder(MusicService.this);

                        builder.setContentIntent(pendInt)
                                .setSmallIcon(android.R.drawable.ic_media_play)
                                .setTicker(songTitle)
                                .setOngoing(true)
                                .setContentTitle("Playing")
                                .setContentText(songTitle);
                        Notification not = builder.build();
                        startForeground(NOTIFY_ID, not);
                    }


                }
            }

            @Override
            public void onPlayWhenReadyCommitted() {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                exoPlayer.stop();
                exoPlayer.seekTo(0);
                PrepStage = false;
                playSong();

            }
        });

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Do what you need in onStartCommand when service has been started
        //Bundle extras= intent.getExtras();
        exoPlayer.stop();
        exoPlayer.seekTo(0);

        return START_NOT_STICKY;
    }
    public void initExoPlayer(){
        String url="";
        if(inPlayList) {
            Track theSong = PlayListsongs.get(songPosn);
          // url = theSong.getStreamURL() + "?client_id=" + Config.CLIENT_ID; // till this statement can be an if statement.
           url=PlayListIDs.get(theSong.getID());

            Log.d("MusicSevice"," get the path of the song "+url+" "+PlayListIDs.get(theSong.getID()));
        }
        else
        {
            Track theSong=songs.get(songPosn);
            url = theSong.getStreamURL() + "?client_id=" + Config.CLIENT_ID;

        }
            Uri radioUri = Uri.parse(url);
// Settings for exoPlayer
        Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
        String userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
        DefaultUriDataSource dataSource = new DefaultUriDataSource(this, null, userAgent);
        ExtractorSampleSource sampleSource = new ExtractorSampleSource(
                radioUri, dataSource, allocator, BUFFER_SEGMENT_SIZE * BUFFER_SEGMENT_COUNT);
        MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource, MediaCodecSelector.DEFAULT);
// Prepare ExoPlayer
        exoPlayer.prepare(audioRenderer);
    }
    public void setList(List<Track> theSongs){
        this.songs=theSongs;
    }
    public void setPlayList(List<Track> theSongs){
        PlayListsongs=theSongs;
    }
    public void setIDs(LinkedHashMap<Integer,String> IDS){
        this.PlayListIDs=IDS;

    }
    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }
    @Override
    public boolean onUnbind(Intent intent){
        exoPlayer.stop();
        exoPlayer.release();
        return false;
    }
    public void playSong(){
        if(exoPlayer!=null && exoPlayer.getPlayWhenReady())
        {
            exoPlayer.stop();
            exoPlayer.seekTo(0);
            PrepStage=false;

        }
        else
        {
            if(PrepStage && pause)

            {   if(exoPlayer!=null)
                exoPlayer.stop();
                exoPlayer.seekTo(0);
                exoPlayer.setPlayWhenReady(false);
                PrepStage=false;
            }
        }
        initExoPlayer();
        exoPlayer.setPlayWhenReady(true);
    }
    public void setSong(int songIndex){
        songPosn=songIndex;

    }
    public void CurrentFragment(boolean flag){   // 0 is DisplayTrackFragment and 1 is PlaylistFragment
        this.inPlayList=flag;

    }
    public long getPosn(){
        return exoPlayer.getCurrentPosition();
    }
    public boolean PrepState(){
        return this.PrepStage;
    }
    public long getDur(){
        return exoPlayer.getDuration();
    }

    public boolean isPng(){
        return exoPlayer.getPlayWhenReady();
    }
    public void go(){
        exoPlayer.setPlayWhenReady(true);
    }
    public void pausePlayer(){
        exoPlayer.setPlayWhenReady(false);
    }
    public void PauseState(boolean Pause_state){
        this.pause=Pause_state;
    }
    public int getSongIndex(){
        return songPosn;
    }
    public void ResetPlayer(){ exoPlayer.stop();exoPlayer.seekTo(0);}


    public void playNext(){
        {
            songPosn++;
            if(songPosn>=songs.size()) songPosn=0;
        }
        PrepStage=false;
        playSong();
    }
    private void send(List<Track> songlist){
        Intent intent= new Intent("Prep");
        Track track = new Track();
        track = songlist.get(songPosn);
        intent.putExtra("PrepState",PrepStage);
        intent.putExtra("SongIndex",songPosn);
        intent.putExtra("Title",track.getTitle()); // title
        intent.putExtra("UserName",track.getUser().getUsername());
        if(track.getArtworkURL()!=null)
        {
            intent.putExtra("Art",track.getArtworkURL());
        }
        else
        {
            intent.putExtra("Art",track.getUser().getAvatar());
        }

        LocalBroadcastManager.getInstance(MusicService.this).sendBroadcast(intent);
    }
    @Override
    public void onDestroy() {
        stopForeground(true);
    }

}


