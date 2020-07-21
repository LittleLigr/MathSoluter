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

    private ExpressionViewModel mViewModel;

    public static ExpressionFragment newInstance() {
        return new ExpressionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expression_fragment, container, false);
        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        //model.getText().observe(this , );
        return view;
    }


}