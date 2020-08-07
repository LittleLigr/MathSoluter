package team.air.mathsoluter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toolbar;

public class ListViewOfMethods extends AppCompatActivity {

    Toolbar toolbar;
    ListView listViewOfMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_of_methods);
        /*
        toolbar =(Toolbar) findViewById(R.id.toolbar);

        listViewOfMethods=(ListView) findViewById(R.id.listViewOfMethods);
        ArrayAdapter<String> mAdapter=new ArrayAdapter<String>(ListViewOfMethods.this,android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.Methods));

        listViewOfMethods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListViewOfMethods.this, Methods.class);
                intent.putExtra("Method",listViewOfMethods.getItemAtPosition(i).toString());
                startActivity(intent);
            }
        });
        listViewOfMethods.setAdapter(mAdapter);*/
    }
}