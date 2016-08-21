package com.kaidoh.mayuukhvarshney.gearjam;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
/**
 * Created by mayuukhvarshney on 05/08/16.
 */

    public class  NoInternetTrackAdapter extends RecyclerView.Adapter<NoInternetTrackAdapter.MyViewHolder> {

        private List<String> TheSongs;
    private LinkedHashMap<Integer,String>NoInternetSongPath;
    public Boolean isFileDeleted=false;
        //private LinkedHashMap<Integer,String> PlayListPaths;

        Context mContext;
        Boolean isPlayList=false;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView TrackTitle;
            public ImageView TrackImage,DeleteButton;
            public  View mView;

            public MyViewHolder(View view) {
                super(view);
                TrackTitle=(TextView)view.findViewById(R.id.track_title);
                DeleteButton = (ImageView) view.findViewById(R.id.delete_btn);
                TrackImage=(ImageView)view.findViewById(R.id.track_image);
                mView=view;

            }
        }


        public NoInternetTrackAdapter (Context context,List<String> songs,boolean result) {
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
            holder.TrackTitle.setText(TheSongs.get(position).substring(0,findbracket(TheSongs.get(position)))); //needs the id filtered here !!! but shall keep it for now to fix the delte option, should be deleted later on.
            if(isPlayList){
                // box = (CheckBox) view.findViewById(R.id.cbBox);

                holder.DeleteButton.setVisibility(View.VISIBLE);
               Picasso.with(mContext).load(R.mipmap.the_default_image).into(holder.TrackImage);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                  //  Track track = TheSongs.get(position);
                    ((MainActivity) mContext).musicSrv.setSong(position);
                    //  ((MainActivity)getActivity()).Current_position=postion;
                    String SetTitleinMediaPLayer =TheSongs.get(position).substring(0,findbracket(TheSongs.get(position)));
                    ((MainActivity) mContext).mSelectedTrackTitle.setText(SetTitleinMediaPLayer);
                   // ((MainActivity) mContext).mArtistTitile.setText(track.getUser().getUsername());
                    Picasso.with(mContext).load(R.mipmap.the_default_image).into(((MainActivity) mContext).mSelectedTrackImage);
                    ((MainActivity) mContext).musicSrv.playSong();


                }
            });
            if(isPlayList)
            {
                holder.DeleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      int id = Integer.parseInt(filter(TheSongs.get(position)));
                        File file = new File(NoInternetSongPath.get(id));
                        boolean isdeleted = file.delete();
                        isFileDeleted=isdeleted;
                        Log.d("NoInternetTrackAdapter","has the song been deleted ? "+isdeleted);
                        TheSongs.remove(position);
                        notifyItemRemoved(position);



                    }
                });
            }


        }

        @Override
        public int getItemCount() {
            return TheSongs.size();
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
            if(txt.charAt(i)=='<'){
                flag=i;
                break;
            }
        }
        return flag;
    }
    public void setSongPath(LinkedHashMap<Integer,String >path){
        this.NoInternetSongPath= path;

    }
    public boolean getIsDeleted()
    {
        return this.isFileDeleted;
    }
}
