package com.example.familyeducationhelp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyeducationhelp.classList.ReleaseInformation;
import com.example.familyeducationhelp.R;

import java.util.ArrayList;
import java.util.List;

public class ReleaseAdapter extends RecyclerView.Adapter<ReleaseAdapter.ViewHolder> {
    private List<ReleaseInformation>  mReleaseInformation= new ArrayList<>();
    private  OnItemClickListener mClickListener;
    private String[] blueFont = {"数学","物理","化学","生物"};
    private String[] yellowFont = {"语文","英语","历史","地理","政治"};
    private boolean flag = false;

    public ReleaseAdapter(List<ReleaseInformation> releaseInformation) {
        mReleaseInformation = releaseInformation;
    }

    public void setOnClickItems(OnItemClickListener listener){
        mClickListener = listener;
    }

    public interface OnItemClickListener{
        void onClickItem(int position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_release_information,parent,false);
        final ViewHolder viewHolder = new ViewHolder(itemView);
        //点击itemView事件
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onClickItem(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReleaseInformation releaseInformation = mReleaseInformation.get(position);
        holder.littleTitle.setText(releaseInformation.getLitleTitle());
        holder.information.setText(releaseInformation.getInformation());
        holder.subject.setVisibility(View.GONE);
        if (position == 0){
            for(String index : blueFont){
                if (index.equals(releaseInformation.getSubject())){
                    holder.subject.setBackgroundResource(R.drawable.ellipse_blue_background_shape);
                    holder.subject.setPadding(40,15,40,15);
                    flag = true;
                }
            }
            if (!flag){
                for (String index : yellowFont){
                    if (index.equals(releaseInformation.getSubject())){
                        holder.subject.setBackgroundResource(R.drawable.ellipse_yellow_background_shape);
                        holder.subject.setPadding(40,15,40,15);
                    }
                }
            }
            holder.subject.setVisibility(View.VISIBLE);
            holder.subject.setText(releaseInformation.getSubject());
        }
    }

    @Override
    public int getItemCount() {
        return mReleaseInformation.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
        TextView littleTitle;
        TextView information;
        TextView subject;
        ImageView next;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            littleTitle = itemView.findViewById(R.id.littleTitle);
            information = itemView.findViewById(R.id.information);
            subject = itemView.findViewById(R.id.subject);
            next = itemView.findViewById(R.id.next);
        }
    }
}
