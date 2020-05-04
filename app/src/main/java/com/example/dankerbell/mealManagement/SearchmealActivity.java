package com.example.dankerbell.mealManagement;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dankerbell.R;

import java.util.ArrayList;

public class SearchmealActivity extends AppCompatActivity implements RecyclerViewAdapterCallback{
    SearchView searchView;

    RecyclerView mRecyclerView = null ;
    com.example.dankerbell.mealManagement.RecyclerImageTextAdapter mAdapter = null ;
    ArrayList<RecyclerItem> mList = new ArrayList<RecyclerItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchmeal);
        //customAdapter = new CustomAdapter(SearchmealActivity.this,  cursor, 0);
        RecyclerView mRecyclerView = findViewById(R.id.recycler1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        mAdapter = new com.example.dankerbell.mealManagement.RecyclerImageTextAdapter(mList);
        mRecyclerView.setAdapter(mAdapter) ;

        addItem("파스타","500kcal");
        addItem("크림파스타","500kcal");
        addItem("토마토파스타","500kcal");
        addItem("알리오올리오","500kcal");
        addItem("파스타","500kcal");
    }

    private void addItem(String food, String kcal) {
        RecyclerItem item = new RecyclerItem();
        item.setFood(food);
        item.setKcal(kcal);
        mList.add(item);

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(String s) {
//                    Log.d(TAG, "onQueryTextSubmit ");
//                    cursor=studentRepo.getStudentListByKeyword(s);
//                    if (cursor==null){
//                        Toast.makeText(MainActivity.this,"No records found!",Toast.LENGTH_LONG).show();
//                    }else{
//                        Toast.makeText(MainActivity.this, cursor.getCount() + " records found!",Toast.LENGTH_LONG).show();
//                    }
//                    customAdapter.swapCursor(cursor);

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
//                   cursor=studentRepo.getStudentListByKeyword(s);
//                    if (cursor!=null){
//                         Log.d(TAG, "onQueryTextChange ");
//                        customAdapter.swapCursor(cursor);
//                    }
                    return false;
                }

            });

        }

        return true;
    }

    @Override
    public void showToast(int position) {
        Toast.makeText(this, position + " clicked.", Toast.LENGTH_SHORT).show();

    }
}


