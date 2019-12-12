package edu.ucsb.cs.cs184.tablecloth.ui.recipe;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import edu.ucsb.cs.cs184.tablecloth.Constants;
import edu.ucsb.cs.cs184.tablecloth.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecipeFragment extends Fragment {

    private RecipeViewModel recipeViewModel;

    private View root;
    private JSONArray recipes;
    private String[] toDisplay;
    private String t;
    private String url1;
    private LinearLayout ll;
    private TextView tv;
    private ImageView iv;
    private ImageView ivBig;
    private TextView tvBig;
    private LinearLayout ll2;
    private FloatingActionButton fab;

    class TextImg{

        TextView tv;
        ImageView iv;
        JSONArray missingIngredients;

        TextImg(TextView tv, ImageView iv, JSONArray missingIngredients){
            this.tv = tv;
            this.iv = iv;
            this.missingIngredients = missingIngredients;
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        recipeViewModel =
                ViewModelProviders.of(this).get(RecipeViewModel.class);
        root = inflater.inflate(R.layout.fragment_recipes, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
        recipeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        ll = (LinearLayout) root.findViewById(R.id.linearLayout1);

        Button b = (Button) root.findViewById(R.id.generateButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitQuery();
                TextView textHello = (TextView) root.findViewById(R.id.text_slideshow);

            }

        });

        ll2 =  root.findViewById(R.id.windowLinearLayout);
        ll2.setVisibility(View.GONE);
        fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll2.setVisibility(View.GONE);
                fab.hide();
            }
        });

        fab.hide();



        return root;
    }

    @Override
    public void onActivityCreated(Bundle state){
        super.onActivityCreated(state);


    }
    public void submitQuery(){

        String url = "https://api.spoonacular.com/recipes/findByIngredients?apiKey=" + Constants.API_TOKEN + "&ingredients=eggs,+flour,+sugar& &ranking=1";
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

               // Log.d("in on respnose", "in on ");
                //Log.d("Response bool:", "" + response.isSuccessful());


                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();

                    try {


                        String correct = myResponse.substring(1,myResponse.length()-1);
                        //recipes = new JSONObject(correct);
                        recipes = new JSONArray(myResponse);
                        Log.d("JSON", recipes.toString());

                        toDisplay = new String[recipes.length()];
                        final ArrayList<TextImg> arrtImg = new ArrayList<TextImg>();

                        for (int i = 0; i < recipes.length(); i++) {
                            try {
                                JSONObject jsonobject = recipes.getJSONObject(i);
                                t = jsonobject.getString("title");
                                url1 = jsonobject.getString("image");
                                final JSONArray missingIngredients = jsonobject.getJSONArray("missedIngredients");

                                InputStream is = (InputStream) new URL(url1).getContent();
                                Drawable d = Drawable.createFromStream(is, "src name");


                                tv = new TextView(getContext());
                                iv = new ImageView(getContext());
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                                iv.setLayoutParams(layoutParams);
                                final TextImg tImg = new TextImg(tv,iv,missingIngredients);


                                tImg.tv.setId(i);
                                tImg.iv.setId(i*10);
                                tImg.tv.setTextColor(Color.parseColor("#000000"));
                                tImg.tv.setText("Recipe Name: " + t);
                                tImg.iv.setImageDrawable(d);

                                arrtImg.add(tImg);
                                tImg.iv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Log.v("image click ", " click");
                                        ivBig = root.findViewById(R.id.imageView2);
                                        tvBig = root.findViewById(R.id.textView2);

                                        for(TextImg t: arrtImg){


                                            if(t.tv.getId() == tImg.tv.getId()){
                                                ll2.setVisibility(View.VISIBLE);
                                                ll2.bringToFront();
                                                fab.show();
                                                ivBig.setImageDrawable(t.iv.getDrawable());

                                                tvBig.setTextColor(Color.parseColor("#000000"));
                                                String ing = "";
                                                for(int i =0; i< t.missingIngredients.length(); i++){
                                                    try {
                                                       ing += missingIngredients.getJSONObject(i).getString("name") + ", ";

                                                    }catch(org.json.JSONException e){
                                                        e.printStackTrace();
                                                    }
                                                }
                                                Log.v("Missing:  ", "" +  ing);
                                                tvBig.setText(t.tv.getText() + "\nMissing Ingredients: " + ing);

                                            }

                                        }


                                    }
                                });



                                tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                                //Log.d("outside getactivity: ", "" + tv.getText());
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run()  {
                                        if(tv.getParent() != null || iv.getParent() != null) {
                                        ((ViewGroup)tv.getParent()).removeView(tv); // <- fix
                                            ((ViewGroup)iv.getParent()).removeView(iv);
                                    }
                                        Log.d("in getactivity: ", "" + tv.getText());
                                        ll.addView(tImg.tv);//FUCK THIS
                                        ll.addView(tImg.iv);
                                    }
                                });

                                        // Stuff that updates the UI
                            }catch(org.json.JSONException e){
                                e.printStackTrace();
                            }
                        }



                    }catch(org.json.JSONException e) {
                        e.printStackTrace();
                        Log.d("IN CATCH", " IN CATCH");
                    }
                }



            }

        });







    }



}