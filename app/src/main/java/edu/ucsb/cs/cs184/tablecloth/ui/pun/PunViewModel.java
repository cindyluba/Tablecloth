package edu.ucsb.cs.cs184.tablecloth.ui.pun;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PunViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PunViewModel() {
        mText = new MutableLiveData<>();

    }

    public LiveData<String> getText() {
        return mText;
    }
}