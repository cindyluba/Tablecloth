package edu.ucsb.cs.cs184.tablecloth.ui.fridge;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;

import edu.ucsb.cs.cs184.tablecloth.FirebaseHelper;
import edu.ucsb.cs.cs184.tablecloth.R;


import static android.content.ContentValues.TAG;

public class FridgeFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    public HashMap<String, Integer> fridge = new HashMap<>();
    public HashMap<String, Integer> fridge2 = new HashMap<>();
    public ArrayAdapter adapter;
    public Spinner spin;
    public DatabaseReference mDatabase;

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

        FirebaseHelper.Initialize(getActivity());

        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, new ArrayList<String>());
        spin = getActivity().findViewById(R.id.fridge_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        mDatabase = FirebaseHelper.db.getReference();
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Log.d("TAG", "KEY: "+ dataSnapshot.getKey() + ", COUNT: " + dataSnapshot.getValue() );
                String count = dataSnapshot.getValue().toString();
                fridge.put(dataSnapshot.getKey(), Integer.parseInt(count));
                adapter.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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

                    mDatabase.child(input).setValue(count);
                    String text_input = "Input Ingredient";
                    outView.setHint(text_input);
                    outView.setText("");
                    String mapper = fridge.toString();
                    Log.d(TAG, mapper);
                    //adapter.add(input);
                }
                else{
                    fridge.put(input,1);
                    String text_input = "Input ingredient";
                    outView.setHint(text_input);
                    outView.setText("");
                    mDatabase.child(input).setValue(1);
                    String mapper = fridge.toString();
                    Log.d(TAG, mapper);
                    adapter.add(input);
                }

            }
        });


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedText = String.valueOf(spin.getSelectedItem());
                String toast_text;
                int count = fridge.get(selectedText);
                if(count == 1){
                    toast_text = "You have " + count + " " + selectedText + " in the fridge.";


                }
                else{
                    toast_text = "You have " + count + " " + selectedText + "s" + " in the fridge.";
                }

                Toast.makeText(getActivity(), toast_text, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}