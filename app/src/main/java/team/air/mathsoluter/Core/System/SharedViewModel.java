package team.air.mathsoluter.Core.System;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ClipData.Item;

public class SharedViewModel extends ViewModel
{
    private MutableLiveData<String> selected = new MutableLiveData<>();
    public void setText(String expression) {
        selected.setValue(expression);
    }
    public LiveData<String> getText() {
        return selected;
    }
}
