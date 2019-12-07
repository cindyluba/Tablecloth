package edu.ucsb.cs.cs184.tablecloth.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashMap;

import edu.ucsb.cs.cs184.tablecloth.R;

import static android.content.ContentValues.TAG;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    public HashMap<String, Integer> fridge = new HashMap<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_fridge, container, false);
        return root;
    }
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        // set the icon to a speaker (needs to be in resources-->drawable):
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText outView = getActivity().findViewById(R.id.text_for_fridge);
                String input = outView.getText().toString().toLowerCase();
                if(fridge.containsKey(input)){
                    int count = fridge.get(input);
                    count++;
                    fridge.put(input, count);
                    String text_input = "Input ingredient";
                    outView.setText(text_input);
                    String mapper = fridge.toString();
                    Log.d(TAG, mapper);
                }
                else{
                    fridge.put(input,1);
                    String text_input = "Input ingredient";
                    outView.setText(text_input);
                    String mapper = fridge.toString();
                    Log.d(TAG, mapper);
                }

            }
        });
    }
}