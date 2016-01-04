package com.example.c.simpleplayer;

import android.content.*;
import android.util.*;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.*;
import com.google.api.client.json.jackson2.*;
import com.google.api.services.youtube.*;
import com.google.api.services.youtube.model.*;

import java.io.*;
import java.util.*;

/**
 * Created by c on 1/2/16.
 */
public class YoutubeConnector {

        private YouTube youtube;
        private YouTube.Search.List query;

        // Your developer key goes here
        public static final String KEY
                = "AIzaSyBtR6Rc_m_9AUXHa9UY9UURbPAuUxlVfZc";

        public YoutubeConnector(Context context) {
            youtube = new YouTube.Builder(new NetHttpTransport(),
                    new JacksonFactory(), new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest hr) throws IOException {}
            }).build();

            //setApplicationName(context.getString(R.string.app_name)

            try{
                query = youtube.search().list("id,snippet");
                query.setKey(KEY);
                query.setType("video");
                query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
            }catch(IOException e){
                Log.d("YC", "Could not initialize: "+e);
            }
        }

    public List<VideoItem> search(String keywords){
        query.setQ(keywords);
        try{
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();

            List<VideoItem> items = new ArrayList<VideoItem>();
            for(SearchResult result:results){
                VideoItem item = new VideoItem();
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setId(result.getId().getVideoId());
                items.add(item);
            }
            return items;
        }catch(IOException e){
            Log.d("YC", "Could not search: "+e);
            return null;
        }
    }
}
