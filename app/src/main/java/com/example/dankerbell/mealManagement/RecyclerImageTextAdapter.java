package com.example.dankerbell.mealManagement;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dankerbell.R;
import com.example.dankerbell.mealManagement.RecyclerItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
public class RecyclerImageTextAdapter extends RecyclerView.Adapter<RecyclerImageTextAdapter.ViewHolder> {
    private ArrayList<RecyclerItem> mData = null ;

    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecyclerImageTextAdapter(ArrayList<RecyclerItem> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecyclerImageTextAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.item, parent, false) ;
        RecyclerImageTextAdapter.ViewHolder vh = new RecyclerImageTextAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RecyclerImageTextAdapter.ViewHolder holder, int position) {

        RecyclerItem item = mData.get(position) ;
        holder.title.setText(item.getFood()) ;
        holder.kcal.setText(item.getKcal()) ;

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title ;
        TextView kcal;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.text1) ;
            kcal=itemView.findViewById(R.id.text2);
        }
    }
}