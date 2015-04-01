package space.collabify.collabify.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import space.collabify.collabify.R;
import space.collabify.collabify.base.CollabifyActivity;

/**
 * This file was born on March 11 at 14:01
 */
public class DetailedSearchActivity extends ListActivity {

    ArrayList<String> listItems = new ArrayList<>();

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_search);

        adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);

        Intent intent = getIntent();
        String query = intent.getStringExtra("query");

        listItems.add(query);
        adapter.notifyDataSetChanged();
    }
}
