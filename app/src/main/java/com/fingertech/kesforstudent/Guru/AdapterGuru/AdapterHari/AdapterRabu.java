package com.fingertech.kesforstudent.Guru.AdapterGuru.AdapterHari;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fingertech.kesforstudent.Guru.ModelGuru.ModelHari.ModelRabu;
import com.fingertech.kesforstudent.R;

import java.util.List;

public class AdapterRabu extends RecyclerView.Adapter<AdapterRabu.MyHolder> {

    private List<ModelRabu> viewItemList;

    private OnItemClickListener onItemClickListener;
    public int row_index = 0;
    public AdapterRabu(List<ModelRabu> viewItemList) {
        this.viewItemList = viewItemList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_jadwal, parent, false);

        MyHolder myHolder = new MyHolder(itemView,onItemClickListener);
        return myHolder;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        // Get car item dto in list.
        ModelRabu viewItem = viewItemList.get(position);
        holder.mapel.setText(viewItem.getMapel());
        holder.lambel.setText(viewItem.getLama_ngajar() + " Menit");
        holder.jambel.setText(viewItem.getJam_mulai() +" - "+ viewItem.getJam_selesai());
        holder.guru.setText("Kelas "+viewItem.getKelas());
    }

    @Override
    public int getItemCount() {
        return viewItemList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mapel, lambel,jambel,guru;
        OnItemClickListener onItemClickListener;

        public MyHolder(View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            mapel    = itemView.findViewById(R.id.mapel);
            lambel   = itemView.findViewById(R.id.lamber);
            jambel   = itemView.findViewById(R.id.jam);
            guru     = itemView.findViewById(R.id.guru);
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