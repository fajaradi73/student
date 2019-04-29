package com.fingertech.kesforstudent.Guru.AdapterGuru;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fingertech.kesforstudent.Guru.ModelGuru.ModelAbsenGuru;
import com.fingertech.kesforstudent.Guru.ModelGuru.ModelPenilaian;
import com.fingertech.kesforstudent.R;

import java.util.List;

public class AdapterAbsen extends RecyclerView.Adapter<AdapterAbsen.MyHolder> {

    private List<ModelAbsenGuru> viewItemList;

    Context context;
    private OnItemClickListener onItemClickListener;
    public int row_index = 0;
    public AdapterAbsen(Context context,List<ModelAbsenGuru> viewItemList) {
        this.context = context;
        this.viewItemList = viewItemList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_murid, parent, false);

        MyHolder myHolder = new MyHolder(itemView,onItemClickListener);
        return myHolder;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        ModelAbsenGuru viewItem = viewItemList.get(position);
        holder.nama.setText(viewItem.getNama());

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
            nama       = itemView.findViewById(R.id.tv_nama_murid);

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