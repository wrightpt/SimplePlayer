package com.example.c.simpleplayer;

import android.os.*;
import android.util.*;
import android.widget.*;

import com.google.android.youtube.player.*;

/**
 * Created by c on 1/2/16.
 */
public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


    private YouTubePlayerView playerView;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.activity_player);
Log.d("clickable","clickedagain");
        playerView = (YouTubePlayerView)findViewById(R.id.player_view);
        playerView.initialize(YoutubeConnector.KEY, this);
    }





    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        Log.d("youtubeplayer3","youtubeplayer2");
        if(!b) {
            Log.d("youtubeplayer","youtubeplayer1");
            youTubePlayer.cueVideo(getIntent().getStringExtra("VIDEO_ID"));
        }
    }

        @Override
        public void onInitializationFailure (YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult){

            Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show();
        }



}
