package com.example.c.simpleplayer;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.support.v7.widget.SearchView;
import android.util.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.widget.Toolbar;
import android.app.SearchManager;

import android.widget.SearchView.OnQueryTextListener;

import com.squareup.picasso.*;

import java.util.*;

/**
 * Created by c on 1/2/16.
 */
public class SearchActivity extends AppCompatActivity {

    private EditText searchInput;
    private ListView videosFound;
    private android.support.v7.widget.Toolbar Toolbar;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        android.support.v7.widget.Toolbar Toolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(Toolbar);


        //searchInput = (EditText)findViewById(R.id.);
        videosFound = (ListView)findViewById(R.id.videos_found);

        handler = new Handler();

       /* searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE){
                    searchOnYoutube(v.getText().toString());
                    return false;
                }
                return true;
            }


        });
        */
        addClickListener();



    }

    private void setSupportActionBar(Toolbar myToolbar) {
    }

    private List<VideoItem> searchResults;

    private void searchOnYoutube(final String keywords){
        new Thread(){
            public void run(){
                YoutubeConnector yc = new YoutubeConnector(SearchActivity.this);
                searchResults = yc.search(keywords);
                handler.post(new Runnable(){
                    public void run(){
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }


    private void updateVideosFound(){
        ArrayAdapter<VideoItem> adapter = new ArrayAdapter<VideoItem>(getApplicationContext(), R.layout.video_item, searchResults){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.video_item, parent, false);
                }
                ImageView thumbnail = (ImageView)convertView.findViewById(R.id.video_thumbnail);
                TextView title = (TextView)convertView.findViewById(R.id.video_title);
                TextView description = (TextView)convertView.findViewById(R.id.video_description);

                VideoItem searchResult = searchResults.get(position);

                Picasso.with(getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                title.setText(searchResult.getTitle());
                description.setText(searchResult.getDescription());
                return convertView;
            }
        };

        videosFound.setAdapter(adapter);
    }

    private void addClickListener(){
        videosFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("test","clickedggggggggg");


                Intent intent = new Intent(getApplicationContext(), CustomLightboxActivity.class);
                intent.putExtra(CustomLightboxActivity.KEY_VIDEO_ID,searchResults.get(position).getId());
                startActivity(intent);
                Log.d("test","clicked");

            }
        });

        //"VIDEO_ID"



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

       SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
     final SearchView searchView =   (SearchView) menu.findItem(R.id.search).getActionView();
        Log.d("hello", "hello");
        searchView.setQueryHint("Search: ");
      searchView.setSearchableInfo( searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });









        return true;
















        //
        // .setIconifiedByDefault(true);

   //  searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
   //         @Override
    //        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
   //             return false;
    //       }





          //      searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
          //  @Override
         //   public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

           //     if(actionId == EditorInfo.IME_ACTION_DONE){
           //         searchOnYoutube(v.getText().toString());
           //         return false;
           //     }
           //     return true;
          //  }



        //getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;

       // return true;}

 //  @Override
 //  public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
   //    int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     //  if (id == R.id.action_settings) {

    //   }

//       return super.onOptionsItemSelected(item);
 //  }




}}









