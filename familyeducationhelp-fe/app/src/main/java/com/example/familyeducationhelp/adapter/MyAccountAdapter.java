package com.example.familyeducationhelp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyeducationhelp.R;
import com.example.familyeducationhelp.activity.MyAccountActivity;
import com.example.familyeducationhelp.classList.MyAccountInformation;

import java.util.ArrayList;
import java.util.List;

public class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.ViewHolder> {
    private View itemView;
    private List<MyAccountInformation> myAccountInformationList = new ArrayList<>();
    private MyAccountActivity mMyAccountActivity;
    private OnItemClickListener mClickListener;

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_myaccount_list, viewGroup, false);
        final ViewHolder holder = new ViewHolder(itemView);
        mMyAccountActivity = new MyAccountActivity();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, holder.getAdapterPosition());
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
        viewHolder.tv_name.setText(myAccountInformation.getAccountName());
        viewHolder.tv_content.setText(myAccountInformation.getAccountItemContent());
        viewHolder.icon_next.setImageResource(myAccountInformation.getAccountNextImage());
        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Android/data/com.example.familyeducationhelp/cache/output_image.jpg");
        if (bitmap != null) {
            viewHolder.icon_image.setImageBitmap(myAccountInformation.getAccountBitmap());
            viewHolder.icon_image.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            viewHolder.icon_image.setImageResource(myAccountInformation.getAccountImage());
            viewHolder.icon_image.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    @Override
    public int getItemCount() {
        return myAccountInformationList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_content;
        ImageView icon_image, icon_next;

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
