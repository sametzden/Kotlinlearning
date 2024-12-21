package com.samet.instagramclonejava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samet.instagramclonejava.databinding.RcyclerRowBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private ArrayList<Post> postArrayList;
    public PostAdapter (ArrayList<Post> postArrayList){
        this.postArrayList = postArrayList;
    }
    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RcyclerRowBinding rcyclerRowBinding = RcyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new PostHolder(rcyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.rcyclerRowBinding.rcyclerViewUserEmailText.setText(postArrayList.get(position).email);
        holder.rcyclerRowBinding.recylerViewCommentText.setText(postArrayList.get(position).comment);
        Picasso.get().load(postArrayList.get(position).downloadUrl).into(holder.rcyclerRowBinding.rcyclerViewImageView);
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{

        RcyclerRowBinding rcyclerRowBinding;
        public PostHolder(RcyclerRowBinding rcyclerRowBinding) {
            super(rcyclerRowBinding.getRoot());
            this.rcyclerRowBinding = rcyclerRowBinding;
        }
    }
}
