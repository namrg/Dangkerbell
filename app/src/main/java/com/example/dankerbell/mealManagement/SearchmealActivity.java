package com.example.dankerbell.mealManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.R;

import java.util.ArrayList;

public class SearchmealActivity extends AppCompatActivity {
    SearchView searchView;

    static String food;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchmeal);

        searchView = (SearchView) findViewById (R.id.searchView);
        TextView fries,creampasta,tomatopasta,creambread,creamsoup;
        fries=findViewById(R.id.fries);
        creampasta=findViewById(R.id.tomatopasta);
        creambread=findViewById(R.id.creambread);
        creamsoup=findViewById(R.id.creamsoup);
        fries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backintent = new Intent(getApplicationContext(), mealActivity.class);
                backintent.putExtra("food","감자튀김");
                startActivity(backintent);
            }
        });


       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
  @Override
 public boolean onQueryTextSubmit(String query) {


                            return false;
                   }
           @Override
            public boolean onQueryTextChange(String newText) {
                          //    adapter.getFilter().filter(newText);
                            return false;
                        }
    });
         }

}


