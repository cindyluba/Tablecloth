package edu.ucsb.cs.cs184.tablecloth;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RecipeFragment extends Fragment {
    private JSONArray recipes;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        rootView = inflater.inflate(R.layout.fragment_recipes, parent, false);
        Button b = (Button) rootView.findViewById(R.id.generateButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               submitQuery();
                TextView textHello = (TextView) rootView.findViewById(R.id.text_slideshow);

            }

        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);


    }

    public void submitQuery(){

        String url = "https://api.spoonacular.com/recipes/findByIngredients?apiKey=" + Constants.API_TOKEN + "&ingredients=apples,+flour,+sugar&number=2";
        Log.d("URL: ", url);
        Request request = new Request.Builder()
                .url(url)
                .build();


        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e){
                Log.d("HELLO","ERROR");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("in on respnose", "in on ");
                Log.d("Response bool:", "" + response.isSuccessful());


                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                recipes = new JSONArray(myResponse);
                             Log.d("HELLO", "HU" + myResponse);
                            }catch(org.json.JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }

        });

        for (int i = 0; i < recipes.length(); i++) {
            try {
                JSONObject jsonobject = recipes.getJSONObject(i);
                Log.d("Titles:", "" + jsonobject.getString("title"));
            }catch(org.json.JSONException e){
                e.printStackTrace();
            }
        }



    }


}
