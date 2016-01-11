package com.example.c.simpleplayer;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
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

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.*;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.squareup.picasso.*;

import java.util.*;

/**
 * Created by c on 1/2/16.
 */
public class SearchActivity extends AppCompatActivity implements OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private static final int RC_SIGN_IN = 9001;
    private EditText searchInput;
    private ListView videosFound;
    private android.support.v7.widget.Toolbar Toolbar;
    private Button Sign_in;
    private GoogleApiClient mGoogleApiClient;




    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        android.support.v7.widget.Toolbar Toolbar =
                (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(Toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient  = new GoogleApiClient.Builder(this)
                //.enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addOnConnectionFailedListener(this)
            //    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        //searchInput = (EditText)findViewById(R.id.);
        videosFound = (ListView)findViewById(R.id.videos_found);

        handler = new Handler();


    //    Button Sign_in = (Button)findViewById(R.id.sign_in_button);
     //   Sign_in.setEnabled(true);
    //    Sign_in.setOnClickListener(new View.OnClickListener() {
   //         @Override
   //         public void onClick(View v) {

    //            switch (v.getId()) {
   //                 case R.id.sign_in:
  //                      signIn();
  //                      break;







         //   }


       // }



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




 //   });

                addClickListener();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("onstart", "onstart");
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void signIn() {
        Log.d("sign in", "sign in");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient );
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //private void setSupportActionBar(Toolbar myToolbar) {
    //}

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            //handleSignInResult(result);
        }
    }

    //private void handleSignInResult(GoogleSignInResult result) {
    //    Log.d(TAG, "handleSignInResult:" + result.isSuccess());
    //    if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
   //         GoogleSignInAccount acct = result.getSignInAccount();
   //         mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
   //         updateUI(true);
   //     } else {
            // Signed out, show unauthenticated UI.
  //          updateUI(false);
  //      }
  //  }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        Log.d("hello", "hello");
        searchView.setQueryHint("Search: ");
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

     searchView.setOnSearchClickListener(new View.OnClickListener() {
          @Override
         public void onClick(View v) {
            Log.d("click", "click");



           }


      });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("click", "click2");

                searchOnYoutube(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("click", "click1");
                searchOnYoutube(newText);
                return true;
            }
        });








        return true;
    }
















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

  @Override
   public boolean onOptionsItemSelected(MenuItem item) {
       //  Handle action bar item clicks here. The action bar will
       //  automatically handle clicks on the Home/Up button, so long
       //  as you specify a parent activity in AndroidManifest.xml.
       int id = item.getItemId();

        //noinspection SimplifiableIfStatement

      switch (item.getItemId()) {

          case R.id.action_settings :
              return true;

          case R.id.sign_in_button :
              Log.d("sign_in_button","sign in button");
              signIn();
              return true;

          case R.id.sign_in:
              Log.d("sign_in_button1","sign in button1");
              signIn();
              return true;


          // if (id == R.id.action_settings) {

          // }

          default:
          return super.onOptionsItemSelected(item);
      }
  }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}









