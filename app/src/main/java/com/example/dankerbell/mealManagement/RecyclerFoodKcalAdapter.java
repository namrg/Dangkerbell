package com.example.dankerbell.mealManagement;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dankerbell.Firebase.BloodSugarCrud;
import com.example.dankerbell.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerFoodKcalAdapter extends RecyclerView.Adapter<RecyclerFoodKcalAdapter.ViewHolder> {
    private static RecyclerFoodKcalAdapter instance; //싱글톤


    private ArrayList<RecyclermyfoodItem> mData = null ;

    Context context;
    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecyclerFoodKcalAdapter(Context context, ArrayList<RecyclermyfoodItem> list) {
        this.context=context;
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecyclerFoodKcalAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.food_item, parent, false) ;
        RecyclerFoodKcalAdapter.ViewHolder vh = new RecyclerFoodKcalAdapter.ViewHolder(view) ;
        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(final RecyclerFoodKcalAdapter.ViewHolder holder, final int position) {
        RecyclermyfoodItem item = mData.get(position);
        holder.foodlist.setText(item.getMyfood());
        holder.kcallist.setText(item.getMykcal());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),position+"번째 삭제 클릭");
                mData.get(position).setisDeleted(true);
                for(int i=0;i<mData.size();i++){
                    if(mData.get(position).isDeleted()){ // true
                        Log.d("삭제 한 행 ",mData.get(i).getMyfood());
                        mData.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mData.size());
                    }
                    }
            }
        });

    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        TextView foodlist ;
        TextView kcallist;
        TextView delete;

        ViewHolder(View itemView) {
            super(itemView) ;
            Context context=itemView.getContext();

            // 뷰 객체에 대한 참조. (hold strong reference)
            foodlist = itemView.findViewById(R.id.foodlist) ;
            kcallist=itemView.findViewById(R.id.kcallist);
            delete=itemView.findViewById(R.id.delete);
        }


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        }
    }


}