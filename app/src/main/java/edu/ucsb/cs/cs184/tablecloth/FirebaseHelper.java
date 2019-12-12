package edu.ucsb.cs.cs184.tablecloth;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

/**
 * Created by Donghao Ren on 03/11/2017.
 * Modified by Ehsan Sayyad on 11/9/2018
 * Modified by Jake Guida on 11/6/2019
 */

/**
 * This is a Firebase helper starter class we have created for you
 * In your Activity, FirebaseHelper.Initialize() is called to setup the Firebase
 * Put your application logic in OnDatabaseInitialized where you'll have the database object initialized
 */
public class FirebaseHelper {

    public static FirebaseApp app;

    /** This is a message data structure that mirrors our Firebase data structure for your convenience */
    public static class Post implements Serializable {

        public Double longitude;
        public Double latitude;
        public Double title;
        public Double timestamp;
    }

    /** Keep track of initialized state, so we don't initialize multiple times */
    private static boolean initialized = false;

    /** The Firebase database object */
    public static FirebaseDatabase db;

    /** Initialize the firebase instance */
    public static void Initialize(final Context context) {
        if (!initialized) {
            initialized = true;
            app = FirebaseApp.initializeApp(context, new FirebaseOptions.Builder()
                            .setDatabaseUrl("https://tablecloth-e493c.firebaseio.com/")
                            .setApiKey("AIzaSyDD2e8K1LUsR5ZoLUQO6zUAeEtA-CngeDA")
                            .setApplicationId("tablecloth-e493c")
                            .build(),
                    "fridge"
            );

            // Call the OnDatabaseInitialized to setup application logic
            OnDatabaseInitialized(app);
        }
    }

    /** This is called once we initialize the firebase database object */
    private static void OnDatabaseInitialized(FirebaseApp app) {
        db = FirebaseDatabase.getInstance(FirebaseApp.getInstance("fridge"));
        DatabaseReference dbr = db.getReference("fridge");

        // TODO: Setup your callbacks to listen for /posts.
        // Your code should handle post added, post updated, and post deleted events.
        // When a post is added: add a marker to the map that when clicked displays the image and the time it was uploaded in a fragment
        // An image can be identified by its title, a long integer representing a UNIX timestamp which corresponds to the image file name (w/o .png)
        // When a post is deleted: remove the marker from the map
        // When a post is updated: update its position on the map and draw the path from its previous point

    }

    // TODO: You *may* (strongly encouraged) create a listener mechanism so that your Activity and Fragments can register callbacks to the database helper
}
