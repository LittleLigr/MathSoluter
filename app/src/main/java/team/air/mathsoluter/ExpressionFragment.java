package team.air.mathsoluter;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import katex.hourglass.in.mathlib.MathView;
import team.air.mathsoluter.Core.System.SharedViewModel;

public class ExpressionFragment extends Fragment {

    private MathView katex;
    public ExpressionFragment(){}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expression_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        katex = view.findViewById(R.id.math);
    }
    void displayReceivedData(String message) {
        katex.setDisplayText("$"+message+"$");
    }

}