package com.example.recycler;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder> {


    List<Produit> lpdt;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView m_nomPdt;
        TextView m_marque;
        ImageView m_image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            m_marque = itemView.findViewById(R.id.text);
            m_nomPdt = itemView.findViewById(R.id.description);
            m_image = itemView.findViewById(R.id.imageView);
        }

        public void setData(String nomPdt, String marquePdt, Drawable img) {
         m_marque.setText(marquePdt);
         m_nomPdt.setText(nomPdt);
         m_image.setImageDrawable(img);
        }
    }

    public ProgramAdapter(List<Produit> p)
    {

        this.lpdt = p;

    }
    @NonNull
    @Override
    public ProgramAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramAdapter.ViewHolder holder, int position) {
        try {
            String nomPdt = lpdt.get(position).getNom();
            String marquePdt = lpdt.get(position).getMarque();
            lpdt.get(position).loadImage();
            Drawable imagePdt = lpdt.get(position).getImage();
            holder.setData(nomPdt,marquePdt, imagePdt);
        }
        catch (Exception e){}



    }

    @Override
    public int getItemCount() {
        return lpdt.size();
    }
}
