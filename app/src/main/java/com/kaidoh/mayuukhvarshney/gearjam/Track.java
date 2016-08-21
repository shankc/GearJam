package com.kaidoh.mayuukhvarshney.gearjam;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mayuukhvarshney on 12/05/16.
 */
public class Track {
    @SerializedName("title")
    private String mTitle;


    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")

    private String mArtworkURL;



    @SerializedName("user")
    private Users mUser;

    @SerializedName("id")
    private int mID;
    @SerializedName("waveform_url")
    private String mWaveformURL;

    private String TrackTitle;

    public void setTrackTitile(String title){
        this.TrackTitle=title;
    }
    public String getTrackTitle(){
        return this.TrackTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getmWaveformURL(){
        return mWaveformURL;
    }


    public String getStreamURL() {
        return mStreamURL;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }
    public int getID() {
        return mID;
    }


    public Users getUser(){return mUser;}

}
