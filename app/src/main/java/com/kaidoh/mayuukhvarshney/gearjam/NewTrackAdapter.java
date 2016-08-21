package com.kaidoh.mayuukhvarshney.gearjam;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
/**
 * Created by mayuukhvarshney on 28/05/16.
 */
public class NewTrackAdapter extends RecyclerView.Adapter<NewTrackAdapter.MyViewHolder> {

    private List<Track> TheSongs;
    private LinkedHashMap<Integer,String>SongPath;
    //private LinkedHashMap<Integer,String> PlayListPaths;

    Context mContext;
    Boolean isPlayList=false;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TrackTitle;
        public ImageView TrackImage,DeleteButton;
        private View mView;

        public MyViewHolder(View view) {
            super(view);
            TrackTitle=(TextView)view.findViewById(R.id.track_title);
            if(isPlayList){
            DeleteButton = (ImageView) view.findViewById(R.id.delete_btn);}
            TrackImage=(ImageView)view.findViewById(R.id.track_image);
            mView=view;

        }
    }


    public NewTrackAdapter(Context context,List<Track> songs,boolean result) {
        this.TheSongs = songs;
        this.mContext=context;
        this.isPlayList=result;



    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        if (isPlayList) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.playlist_track_row, parent, false);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.track_list_row, parent, false);
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       Track track=new Track();
        track= TheSongs.get(position);
        holder.TrackTitle.setText(track.getTitle());
        if(isPlayList){
            // box = (CheckBox) view.findViewById(R.id.cbBox);

            holder.DeleteButton.setVisibility(View.VISIBLE);
        }



         if(track.getArtworkURL()==null)
         {
             Picasso.with(mContext).load(track.getUser().getAvatar()).into(holder.TrackImage);
         }
        else
         {
             Picasso.with(mContext).load(track.getArtworkURL()).into(holder.TrackImage);
         }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Track track = TheSongs.get(position);
                ((MainActivity) mContext).musicSrv.setSong(position);
                //  ((MainActivity)getActivity()).Current_position=postion;
                ((MainActivity) mContext).mSelectedTrackTitle.setText(track.getTitle());
                ((MainActivity) mContext).mArtistTitile.setText(track.getUser().getUsername());
                if (track.getArtworkURL() == null) {
                    Picasso.with(mContext).load(track.getUser().getAvatar()).into(((MainActivity) mContext).mSelectedTrackImage);
                } else {
                    Picasso.with(mContext).load(track.getArtworkURL()).into(((MainActivity) mContext).mSelectedTrackImage);
                }
                ((MainActivity) mContext).musicSrv.playSong();


            }
        });
   if(isPlayList)
   {
       holder.DeleteButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               // if successfull need to display an alert dialouge.
               Track track = TheSongs.get(position);
                int id = track.getID();
               File file = new File(SongPath.get(track.getID()));
               boolean isdelete = file.delete();
               Log.d("NewTrackAdapter"," has the file been delete on delete button click? "+isdelete);
               TheSongs.remove(position);
               notifyItemRemoved(position);

           }
       });
   }
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(!isPlayList){


                    Track track = TheSongs.get(position);
                    String path = "https://api.soundcloud.com/tracks/" + track.getID() + "/stream?client_id=" + Config.CLIENT_ID;
                    Toast.makeText(mContext,"Adding to PlayList",Toast.LENGTH_SHORT).show();
                    new DownloadFileFromURL(mContext,Integer.toString(track.getID()), track.getTitle(),((MainActivity)mContext).folder).execute(path);


                }


             return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return TheSongs.size();
    }

    public void returnmessage(String path){
        Log.d("NewTrackAdapter","the delete button was cliked"+path);


    }
  public void setSongPath(LinkedHashMap<Integer,String> paths){
      this.SongPath=paths;
  }
}