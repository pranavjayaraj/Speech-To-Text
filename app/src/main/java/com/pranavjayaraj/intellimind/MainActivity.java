package com.pranavjayaraj.intellimind;
import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pranavjayaraj.intellimind.Database.DatabaseHandler;
import com.pranavjayaraj.intellimind.Adapter.RecentAdapter;
import com.pranavjayaraj.intellimind.Model.SearchObject;
import com.pranavjayaraj.intellimind.Service.CloudSpeechService;
import com.pranavjayaraj.intellimind.Util.SearchSuggestion.CustomAutoCompleteTextChangedListener;
import com.pranavjayaraj.intellimind.Util.SearchSuggestion.CustomAutoCompleteView;
import com.pranavjayaraj.intellimind.Util.VoiceRecorder;
import com.pranavjayaraj.intellimind.Util.VoiceView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Pranav on 23/8/19.
 */
public class MainActivity extends AppCompatActivity implements VoiceView.OnRecordListener {
    private static String TAG = "MainActivity";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 1;

    private VoiceView mStartStopBtn;

    private CloudSpeechService mCloudSpeechService;
    private VoiceRecorder mVoiceRecorder;
    private boolean mIsRecording = false;

    // Resource caches
    private int mColorHearing;
    private int mColorNotHearing;

    private String mSavedText;
    public CustomAutoCompleteView search;
    private Handler mHandler;

    // adapter for auto-complete
    public ArrayAdapter<String> myAdapter;

    // for database operations
    DatabaseHandler databaseH;

    // just to add some initial value
    public String[] item = new String[]{"Please search..."};

    ImageButton searchButton;
    RecyclerView recyclerView;
    RecentAdapter recentAdapter;
    ArrayList<String> recentArrayList = new ArrayList<String>();
    LottieAnimationView animationView;
    ImageButton settings;


    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speechlayout);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        animationView =  findViewById(R.id.menuAnimation2);
        recyclerView = findViewById(R.id.recycler_view);
        search =  findViewById(R.id.search);
        settings =  findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings);
            }
        });

        searchButton =  findViewById(R.id.search_icon);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!search.getText().toString().isEmpty())
                {
                    insertSearchData();
                    SaveToRecent();
                    Intent searchActivity = new Intent(MainActivity.this, SearchActivity.class);
                    searchActivity.putExtra(String.valueOf(R.string.query),search.getText().toString());
                    startActivity(searchActivity);
                }
                else
                {
                    Toast.makeText(MainActivity.this,"Please ask something",Toast.LENGTH_LONG).show();
                }
            }
        });
        initViews();
        try {

            // instantiate database handler
            databaseH = new DatabaseHandler(MainActivity.this);

            // put sample data to database
            insertSampleData();

            // autocompletetextview is in activity_main.xml
            search = (CustomAutoCompleteView) findViewById(R.id.search);

            // add the listener so it will tries to suggest while the user types
            search.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this,search));

            // set our adapter
            myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item);
            search.setAdapter(myAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ReadRecent();

    }

    // this function is used in CustomAutoCompleteTextChangedListener.java
    public String[] getItemsFromDb() {

        // add items on the array dynamically
        List<SearchObject> products = databaseH.read();
        int rowCount = products.size();

        String[] item = new String[rowCount];
        int x = 0;

        for (SearchObject record : products) {

            item[x] = record.objectName;
            x++;
        }

        return item;
    }

    // Save the recent searches to sharedpref
    public void SaveToRecent() {

            Gson gson = new Gson();
            String json = sharedPreferences.getString("Set", "");
            Type type = new TypeToken<ArrayList<String>>()
            {

            }.getType();
            if (!json.isEmpty())
            {
                recentArrayList = gson.fromJson(json, type);
                if (recentArrayList != null)
                {
                    if (recentArrayList.size() >= 5)
                    {
                        recentArrayList.remove(0);
                    }
                }
            }
            recentArrayList.add(search.getText().toString());
            String json2 = gson.toJson(recentArrayList);
            editor.putString("Set", json2);
            editor.commit();
            Display();

    }

    // Read recently searched items from sharedpref and display them
    public void ReadRecent() {
        // Read the reverse queue from sharedpref
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Set", "");
        if (json.isEmpty()) {
        } else {
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            recentArrayList = gson.fromJson(json, type);
            Display();
        }
    }

    // Insert sample data into the database
    public void insertSampleData() {

        databaseH.create(new SearchObject("who is the CEO of Microsoft"));
        databaseH.create(new SearchObject("who is the CEO of Google"));
        databaseH.create(new SearchObject("Who is the Prime minister of India"));
        databaseH.create(new SearchObject("Who is the President of America"));

    }

    // Insert the data which you searched for into the database for future reference
    public void insertSearchData() {
            databaseH.create(new SearchObject(search.getText().toString()));
    }
    //Function to initialize the views
    private void initViews() {

        mStartStopBtn = (VoiceView) findViewById(R.id.recordButton);
        mStartStopBtn.setOnRecordListener(this);
        final Resources resources = getResources();
        final Resources.Theme theme = getTheme();
        mColorHearing = ResourcesCompat.getColor(resources, R.color.status_hearing, theme);
        mColorNotHearing = ResourcesCompat.getColor(resources, R.color.status_not_hearing, theme);
        mHandler = new Handler(Looper.getMainLooper());
    }

    private final CloudSpeechService.Listener mCloudSpeechServiceListener = new CloudSpeechService.Listener() {
        @Override
        public void onSpeechRecognized(final String text, final boolean isFinal) {
            if (isFinal) {
                mVoiceRecorder.dismiss();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Check if the user has stopped talking
                    if (isFinal && (!search.getText().toString().isEmpty())) {
                       startSearch();// Start searching
                    }
                    else
                        {
                            if(text.toLowerCase().contains("search"))
                            {
                                // Check if the user has spoken the word SEARCH
                                 if (sharedPreferences.getBoolean("search",true))
                                     {
                            // Check if the user is searching for an empty query
                                        if ((!search.getText().toString().isEmpty()))
                                            {
                                            startSearch(); // Start searching
                                            }
                                        else
                                        {
                                            Toast.makeText(MainActivity.this,"Please ask something",Toast.LENGTH_LONG).show();

                                        }
                                     }
                            }
                            // Check if the user has spoken the word STOP
                             else if (text.toLowerCase().contains("stop"))
                                     {

                                    if(sharedPreferences.getBoolean("stop",true))
                                        {
                                            stopListening();// Stop searching
                                        }
                                      }
                             else
                                    {
                                    search.setText(text);// Set user spoken words into the search bar
                                    }
                        }
                    }
            });
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            mCloudSpeechService = CloudSpeechService.from(binder);
            mCloudSpeechService.addListener(mCloudSpeechServiceListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mCloudSpeechService = null;
        }

    };

    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {

        @Override
        public void onVoiceStart() {
            if (mCloudSpeechService != null)
            {
                mCloudSpeechService.startRecognizing(mVoiceRecorder.getSampleRate(), sharedPreferences.getString("language-code","Default"));
            }
        }

        @Override
        public void onVoice(final byte[] buffer, int size) {
            if (mCloudSpeechService != null) {
                mCloudSpeechService.recognize(buffer, size);
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    int amplitude = (buffer[0] & 0xff) << 8 | buffer[1];
                    double amplitudeDb3 = 20 * Math.log10((double) Math.abs(amplitude) / 32768);
                    float radius2 = (float) Math.log10(Math.max(1, amplitudeDb3)) * dp2px(MainActivity.this, 20);
                    mStartStopBtn.animateRadius(radius2 * 10);
                }
            });
        }

        @Override
        public void onVoiceEnd() {
            if (mCloudSpeechService != null) {
                mCloudSpeechService.finishRecognizing();
            }
        }

    };

    @Override
    public void onRecordStart() {
        startStopRecording();
    }

    @Override
    public void onRecordFinish() {
        mPlayer.stop();
        mPlayer.release();
        startStopRecording();
    }

    private void startStopRecording() {

        Log.d(TAG, "# startStopRecording # : " + mIsRecording);
        if (mIsRecording) {
            mStartStopBtn.changePlayButtonState(VoiceView.STATE_NORMAL);
            mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.off);//Create MediaPlayer object with MP3 file under res/raw folder
            mPlayer.start();//Start playing the music
            stopVoiceRecorder();
        } else {
            mStartStopBtn.changePlayButtonState(VoiceView.STATE_RECORDING);
            mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.on);//Create MediaPlayer object with MP3 file under res/raw folder
            mPlayer.start();//Start playing the music
            startVoiceRecorder();
        }
    }

    //Function to start recording
    private void startVoiceRecorder() {
        search.requestFocus();
        animationView.setVisibility(View.VISIBLE);
        animationView.setAnimation("animations/siri.json");
        animationView.loop(true);
        animationView.playAnimation();
        Log.d(TAG, "# startVoiceRecorder #");
        mIsRecording = true;
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
        }
        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();
    }

    MediaPlayer mPlayer;

    //Function to stop recording
    private void stopVoiceRecorder() {
        animationView.cancelAnimation();
        animationView.setVisibility(View.INVISIBLE);
        Log.d(TAG, "# stopVoiceRecorder #");
        mIsRecording = false;
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (permissions.length == 1 && grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                showPermissionMessageDialog();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showPermissionMessageDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This app needs to record audio and recognize your speech")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        }).create();

        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    public static int dp2px(Context context, int dp) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context
                .getResources().getDisplayMetrics());
        return px;
    }

    //Function to display the set of 5 recent searches
    void Display() {
        recentAdapter = new RecentAdapter(recentArrayList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recentAdapter);
        recentAdapter.notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    //Function to activate the voice listener
    void startSearch() {
        insertSearchData();//Save data into database
        SaveToRecent();// save data into sharedpreferences for fetching the recent searches
        stopVoiceRecorder();
        mStartStopBtn.changePlayButtonState(VoiceView.STATE_NORMAL);
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.off);//Create MediaPlayer object with MP3 file under res/raw folder
        mPlayer.start();//Start playing the audio
        Intent searchActivity = new Intent(MainActivity.this, SearchActivity.class);
        searchActivity.putExtra(String.valueOf(R.string.query),search.getText().toString());
        search.setText("");
        startActivity(searchActivity);
    }

    //Function to deactivate the voice listener
    void stopListening()
    {
        search.setText("");
        stopVoiceRecorder();
        mStartStopBtn.changePlayButtonState(VoiceView.STATE_NORMAL);
        mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.off);
        mPlayer.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Prepare Cloud Speech API
        bindService(new Intent(this, CloudSpeechService.class), mServiceConnection,
                BIND_AUTO_CREATE);

        //Check for permission to record voice
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            showPermissionMessageDialog();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_RECORD_AUDIO_PERMISSION);
        }
    }

    @Override
    public void onStop() {
        // Stop listening to voice
        // Stop Cloud Speech API
        if (mCloudSpeechService != null) {
            mCloudSpeechService.removeListener(mCloudSpeechServiceListener);
            unbindService(mServiceConnection);
            mCloudSpeechService = null;
        }

        super.onStop();
    }
    //Deactivates the voice listener when some activity comes in the foreground
    @Override
    public void onPause() {
        super.onPause();
        if (mIsRecording) {
           stopListening();
        }
    }
}
