package com.example.lgallet.myapplication;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;






import java.util.ArrayList;

import seanzorWebGLdetect.OnReceiveDetectJsResult;
import seanzorWebGLdetect.WebGLDetector;
import seanzorWebGLdetect.WebGLSupportLevel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    TextView mainTextView;
    Button mainButton;
    EditText mainEditText;
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();
    ShareActionProvider mShareActionProvider;
    WebView testBrowser;
    Button checkButton;
    //final private BindableSupportLevel mBindableSupportLevel = new BindableSupportLevel();

    VideoView video;
    MediaController mp;

    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "name";
    SharedPreferences mSharedPreferences;
    private int level;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final Context context = this;

        video = (VideoView) findViewById(R.id.video_view);
        video.setVideoPath("http://techslides.com/demos/sample-videos/small.mp4");
        mp = new MediaController(this);
        mp.setAnchorView(video);
        video.setMediaController(mp);
//        video.la

        video.start();

        // 1. Access the TextView defined in layout XML
        // and then set its text
        mainTextView=(TextView)findViewById(R.id.main_textview);
        mainTextView.setText("Set in Java!");

        // 2. Access the Button defined in layout XML and listen for it here
        mainButton= (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(this);

        testBrowser = (WebView)findViewById(R.id.testBrowser);

        //myBrowser.getSettings().setJavaScriptEnabled(true);
        //final String useragent = myBrowser.getSettings().getUserAgentString();

        checkButton = (Button) findViewById(R.id.check_button);
       checkButton.setOnClickListener(new View.OnClickListener() {
          @Override
           public void onClick(View v) {

            WebGLDetector.detect(testBrowser, new OnReceiveDetectJsResult() {
                @Override
                public void onReceiveDetectJsResult(WebGLSupportLevel supportLevel) {
                    level = supportLevel.getCode();
                    switch (supportLevel) {
                        case UNKNOWN:
                            Toast.makeText(getApplicationContext(), "unknown", Toast.LENGTH_LONG).show();
                            break;
                        case NOT_SUPPORTED:
                            Toast.makeText(getApplicationContext(), "Not supported", Toast.LENGTH_LONG).show();
                            break;
                        case SUPPORTED_DISABLED:
                            Toast.makeText(getApplicationContext(), "supported but disabled", Toast.LENGTH_LONG).show();
                            break;
                        case SUPPORTED:
                            Toast.makeText(getApplicationContext(), "supported", Toast.LENGTH_LONG).show();
                            openWebMap(context);
                            break;

                    }
                }
            });








                /*WebGLDetector.detect(myBrowser, new OnReceiveDetectJsResult() {
                    @Override
                    public void onReceiveDetectJsResult(WebGLSupportLevel supportLevel) {
                        Log.d("state", "state" + supportLevel.getCode());
                        Log.d("state", "Webkit" + useragent);
                        switch (supportLevel) {
                            case UNKNOWN:
                                Toast.makeText(getApplicationContext(), "unknown", Toast.LENGTH_LONG).show();
                                break;
                            case NOT_SUPPORTED:
                                Toast.makeText(getApplicationContext(), "Not supported", Toast.LENGTH_LONG).show();
                                break;
                            case SUPPORTED_DISABLED:
                                Toast.makeText(getApplicationContext(), "supported but disabled", Toast.LENGTH_LONG).show();
                                break;
                            case SUPPORTED:
                                Toast.makeText(getApplicationContext(), "supported", Toast.LENGTH_LONG).show();
                                break;

                        }
                    }
                });*/
            }
        });

        // 3. Access the EditText defined in layout XML
        mainEditText = (EditText) findViewById(R.id.main_edittext);

        // 4. Access the ListView
        mainListView = (ListView) findViewById(R.id.main_listview);

// Create an ArrayAdapter for the ListView
        mArrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,mNameList);

// Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mArrayAdapter);

        // 5. Set this activity to react to list items being pressed
        mainListView.setOnItemClickListener(this);

        // 7. Greet the user, or ask for their name if new
        displayWelcome();

    }

    private void openWebMap(Context context){
        Intent intent = new Intent(context, WebViewActivity.class);
        startActivity(intent);
    }

    public void displayWelcome() {
        //Access the device's key-value storage
        mSharedPreferences = getSharedPreferences(PREFS,MODE_PRIVATE);

        //Read the user's name, or an empty string if found nothing
        String name = mSharedPreferences.getString(PREF_NAME, "");

        if (name.length() > 0) {

            // If the name is valid, display a Toast welcoming them
            Toast.makeText(this, "Welcome back, " + name + "!", Toast.LENGTH_LONG).show();
        }else {

            // otherwise, show a dialog to ask for their name
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Hello!");
            alert.setMessage("What is your name?");

            // Create EditText for entry
            final EditText input = new EditText(this);
            alert.setView(input);

            // Make an "OK" button to save the name
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {

                    // Grab the EditText's input
                    String inputName = input.getText().toString();

                    // Put it into memory (don't forget to commit!)
                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(PREF_NAME, inputName);
                    e.commit();

                    // Welcome the new user
                    Toast.makeText(getApplicationContext(), "Welcome, " + inputName + "!", Toast.LENGTH_LONG).show();
                }
            });

            // Make a "Cancel" button
            // that simply dismisses the alert
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {}
            });

            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Access the Share item defined in Menu XML
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        //Access the object responsible for putting together the sharing submenu
        if(shareItem != null){
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        }
        //Create an Intent to share your content
        setShareIntent();
        return true;
    }

    private void setShareIntent() {

        if(mShareActionProvider != null){

            //Create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Development");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mainTextView.getText());

            //Make sure provider knows it should worlk with that Intent
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public void onClick(View v) {
        // Take what was typed into the EditText
        // and use in TextView
        mainTextView.setText(mainEditText.getText().toString() + " is learning Android development!");

        // Also add that value to the list shown in the ListView
        mNameList.add(mainEditText.getText().toString());
        mArrayAdapter.notifyDataSetChanged();

        // 6. The text you'd like to share has changed,
// and you need to update
        setShareIntent();

        //Test the button

        mainTextView.setText("Button pressed");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        // Log the item's position and contents
        // to the console in Debug
        Log.d("omg android", position + ": " + mNameList.get(position));
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
