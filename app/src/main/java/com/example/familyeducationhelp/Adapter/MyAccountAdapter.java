package com.example.familyeducationhelp.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyeducationhelp.Activity.MyAccountActivity;
import com.example.familyeducationhelp.ClassList.MyAccountInformation;
import com.example.familyeducationhelp.R;

import java.util.ArrayList;
import java.util.List;

public class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.ViewHolder>{
    private View itemView;
    private List<MyAccountInformation> myAccountInformationList = new ArrayList<>();
    private MyAccountActivity mMyAccountActivity;
    private OnItemClickListener mClickListener;

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.myaccount_list_item,viewGroup,false);
        final ViewHolder holder = new ViewHolder(itemView);
        mMyAccountActivity = new MyAccountActivity();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view,holder.getAdapterPosition());
            }
        });
        return holder;
    }


    public MyAccountAdapter(List<MyAccountInformation> myAccountInformationList) {
        this.myAccountInformationList = myAccountInformationList;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        MyAccountInformation myAccountInformation = myAccountInformationList.get(i);
        viewHolder.tv_name.setText(myAccountInformation.getName());
        viewHolder.tv_content.setText(myAccountInformation.getContent());
        viewHolder.icon_next.setImageResource(myAccountInformation.getNext());
        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Android/data/com.example.familyeducationhelp/cache/output_image.jpg");
        if (bitmap != null) {
            viewHolder.icon_image.setImageBitmap(myAccountInformation.getBitmap());
            viewHolder.icon_image.setScaleType(ImageView.ScaleType.FIT_XY);
        }else {
            viewHolder.icon_image.setImageResource(myAccountInformation.getImage());
            viewHolder.icon_image.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    @Override
    public int getItemCount() {
        return myAccountInformationList.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name,tv_content;
        ImageView icon_image,icon_next;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.myAccount_name);
            tv_content = itemView.findViewById(R.id.myAccount_content);
            icon_image = itemView.findViewById(R.id.myAccount_image);
            icon_next = itemView.findViewById(R.id.myAccount_next);
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }





}
