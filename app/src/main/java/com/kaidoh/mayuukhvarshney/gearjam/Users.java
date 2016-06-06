package com.kaidoh.mayuukhvarshney.gearjam;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mayuukhvarshney on 12/05/16.
 */
public class Users {
    @SerializedName("username")
    private String mUsername;

    public String getUsername(){return mUsername;}

    @SerializedName("avatar_url")

    private String Avatar;

    public String getAvatar(){return this.Avatar;}

}