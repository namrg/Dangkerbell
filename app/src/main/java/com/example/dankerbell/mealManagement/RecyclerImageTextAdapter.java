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

import com.example.dankerbell.R;

import java.util.ArrayList;

public class RecyclerImageTextAdapter extends RecyclerView.Adapter<RecyclerImageTextAdapter.ViewHolder> { // 음식체크 Adapter
    private ArrayList<RecyclerItem> mData = null ;
    int count=0;
    String food="";
    ArrayList<String> foodlist=new ArrayList<>();
    String kcal="";
    Context context;
    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecyclerImageTextAdapter(Context context, ArrayList<RecyclerItem> list) {

        this.context=context;
        mData = list ;
    }

    public String getFood() {
        return food;
    }

    public String getKcal() {
        return kcal;
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
    public void onBindViewHolder(final RecyclerImageTextAdapter.ViewHolder holder, final int position) {
        if(count==0){
            for(int i=0;i<mData.size();i++){
                mData.get(i).setSelected(false);
                Log.d("count : ", String.valueOf(count));
            }
        }
        RecyclerItem item = mData.get(position);
        holder.title.setText(item.getFood());
        holder.kcal.setText(item.getKcal());
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RecyclerItem food1 = (RecyclerItem) holder.check.getTag();
//
//                food1.setSelected(holder.check.isChecked());

                mData.get(position).setSelected(holder.check.isChecked());

                for (int j=0; j<mData.size();j++){
                    if (mData.get(j).isSelected() == true){
                        Toast.makeText(context, mData.get(j).getFood()+"를 선택하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        count++;

    }



    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        TextView title ;
        TextView kcal;
        CheckBox check;
        ViewHolder(View itemView) {
            super(itemView) ;
            Context context=itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() { // 아이템 클릭 시 실행

                @Override
                public void onClick(View view) {
                    int pos=getAdapterPosition();
                    if (pos!= RecyclerView.NO_POSITION){
                        Log.d(this.getClass().getName(), String.valueOf(mData.get(pos)));
                        Log.d(this.getClass().getName(), String.valueOf(mData.listIterator(pos)));


                    }
                }

            });

            // 뷰 객체에 대한 참조. (hold strong reference)
            title = itemView.findViewById(R.id.text1) ;
            kcal=itemView.findViewById(R.id.text2);
            check=itemView.findViewById(R.id.check);
        }
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        }
    }
}