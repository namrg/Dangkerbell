package com.example.dankerbell.pillManagement;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dankerbell.AlarmManagement.AlarmActivity;
import com.example.dankerbell.R;

import java.util.ArrayList;

import static com.example.dankerbell.Firebase.CrudInterface.db;

public class RecyclerpillAdapter extends RecyclerView.Adapter<RecyclerpillAdapter.ViewHolder> {
    private static RecyclerpillAdapter instance; //싱글톤
    pillCrud mPill = pillCrud.getInstance();
    private ArrayList<RecyclerpillItem> mData = null ;

    Context context;
    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecyclerpillAdapter(Context context, ArrayList<RecyclerpillItem> list) {
        this.context=context;
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecyclerpillAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.pill_item, parent, false) ;
        RecyclerpillAdapter.ViewHolder vh = new RecyclerpillAdapter.ViewHolder(view) ;
        return vh ;
    }


    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(final RecyclerpillAdapter.ViewHolder holder, final int position) {
        RecyclerpillItem item = mData.get(position);
        holder.medname.setText(item.getMedname());
        holder.amount.setText(String.valueOf(item.getPill_amount()));
        Log.d("mData사이즈", String.valueOf(mData.size()));


        for(int i=0;i<mData.size();i++){
            if(mData.get(i).isNotify()){ //true
                Log.d("알람 켜져 있음 ",mData.get(position).getMedname());

                holder.alarm.setVisibility(View.VISIBLE);
                holder.alarm_off.setVisibility(View.GONE);
            }
            else{ //else
                Log.d("알람 꺼져 있음 ",mData.get(position).getMedname());
                holder.alarm_off.setVisibility(View.VISIBLE);
                holder.alarm.setVisibility(View.GONE);
            }
        }


        mPill.alarmpill = new Handler(){

            @Override public void handleMessage(Message msg){
                if (msg.what==1001){
                   Log.d("메세지받음","실행OK");

                }
            }
        };
        holder.alarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String pillname = mData.get(position).getMedname();
                mData.get(position).setNotify(false);
                boolean value = mData.get(position).isNotify();
                mPill.update(pillname, value);
                notifyItemChanged(position);
                Log.d("알람 유무 ",String.valueOf(value));
                Log.d("알람 끄기 클릭 ",mData.get(position).getMedname());
                holder.alarm_off.setVisibility(View.VISIBLE);
                holder.alarm.setVisibility(View.GONE);
            }
        });
        holder.alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pillname = mData.get(position).getMedname();
                mData.get(position).setNotify(true);
                boolean value = mData.get(position).isNotify();
                mPill.update(pillname, value);
                notifyItemChanged(position);
                Log.d("알람 유무 ",String.valueOf(value));
                Log.d("알람 켜기 클릭 ",mData.get(position).getMedname());
                holder.alarm_off.setVisibility(View.GONE);
                holder.alarm.setVisibility(View.VISIBLE);
                Intent alarmintent = new Intent(context.getApplicationContext(), AlarmActivity.class);
                context.startActivity(alarmintent);
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),position+"번째 삭제 클릭");
                mData.get(position).setDel(true);
                for(int i=0;i<mData.size();i++){
                    if(mData.get(position).isDel()){ // true
                        Log.d("삭제 한 행 ",mData.get(i).getMedname());
                        mPill.delete(mData.get(position).getMedname());

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

        TextView medname ;
        TextView amount;
        TextView alarm;
        TextView delete;
        TextView alarm_off;
        ViewHolder(View itemView) {
            super(itemView) ;
            Context context=itemView.getContext();
            // 뷰 객체에 대한 참조. (hold strong reference)
            medname= itemView.findViewById(R.id.re_medname) ; //의약품 이름//
            amount=itemView.findViewById(R.id.re_amount);
            alarm=itemView.findViewById(R.id.re_alarm);
            alarm_off=itemView.findViewById(R.id.re_alarm_off);
            delete=itemView.findViewById(R.id.re_del_btn);
        }


        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        }
    }


}