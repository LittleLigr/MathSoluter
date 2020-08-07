package team.air.mathsoluter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class Methods extends AppCompatActivity {

    Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_methods);

        mToolbar = (Toolbar) findViewById(R.id.toolbar1);

        Bundle bundle =getIntent().getExtras();
        if (bundle != null)
        {
            mToolbar.setTitle(bundle.getString("Method"));
        }
    }
}