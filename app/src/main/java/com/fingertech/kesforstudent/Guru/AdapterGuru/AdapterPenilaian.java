package com.fingertech.kesforstudent.Guru.AdapterGuru;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fingertech.kesforstudent.Guru.ModelGuru.ModelPenilaian;
import com.fingertech.kesforstudent.R;

import java.util.List;

public class AdapterPenilaian extends RecyclerView.Adapter<AdapterPenilaian.MyHolder> {

    private List<ModelPenilaian> viewItemList;

    Context context;
    private OnItemClickListener onItemClickListener;
    public int row_index = 0;
    public AdapterPenilaian(Context context,List<ModelPenilaian> viewItemList) {
        this.context = context;
        this.viewItemList = viewItemList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_penilaian, parent, false);

        MyHolder myHolder = new MyHolder(itemView,onItemClickListener);
        return myHolder;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        ModelPenilaian viewItem = viewItemList.get(position);
        holder.nama.setText(viewItem.getNama());
        holder.nis.setText(viewItem.getNis());
        holder.nilai.setText(viewItem.getNilai());
        if ((position % 2) == 0){
            holder.linearLayout.setBackgroundColor(Color.parseColor("#f0f0f0"));
        }else {
            holder.linearLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public int getItemCount() {
        return viewItemList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nama,nis,nilai;
        OnItemClickListener onItemClickListener;
        LinearLayout linearLayout;
        public MyHolder(View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            nama       = itemView.findViewById(R.id.nama);
            nis       = itemView.findViewById(R.id.nis);
            nilai       = itemView.findViewById(R.id.nilai);
            linearLayout    = itemView.findViewById(R.id.ll_penilaian);
//            itemView.setOnClickListener(this);
//            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
//            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }
}