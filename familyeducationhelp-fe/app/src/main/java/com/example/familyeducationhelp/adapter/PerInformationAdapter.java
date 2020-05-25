package com.example.familyeducationhelp.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyeducationhelp.R;
import com.example.familyeducationhelp.classList.PersonInformation;
import com.example.familyeducationhelp.map.MapActivity;

import java.util.List;

public class PerInformationAdapter extends RecyclerView.Adapter<PerInformationAdapter.ViewHolder> {
    private List<PersonInformation> mPersonInformationList;
    private View itemView;
    private RecyclerView mRv;
    private boolean isCalculationRvHeight;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View perInformationView;
        //        ImageView icon_head;
        TextView tv_name, tv_time, tv_grade, tv_location, tv_price;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            perInformationView = itemView;
//            icon_head = itemView.findViewById(R.id.icon_head);
            tv_name = itemView.findViewById(R.id.person_name);
            tv_time = itemView.findViewById(R.id.label_time);
            tv_grade = itemView.findViewById(R.id.label_grade);
            tv_location = itemView.findViewById(R.id.label_site);
            tv_price = itemView.findViewById(R.id.label_price);
        }
    }

    public PerInformationAdapter(List<PersonInformation> personInformationList, RecyclerView recyclerView) {
        mPersonInformationList = personInformationList;
        mRv = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_personal_information, viewGroup, false);
        final ViewHolder holder = new ViewHolder(itemView);
        holder.perInformationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //学到了！！！怎样在Adapter中得到活动对象并执行活动代码
                Intent intent = new Intent(view.getContext(), MapActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PersonInformation personInformation = mPersonInformationList.get(i);
        viewHolder.tv_location.setText(personInformation.getSite());
        viewHolder.tv_grade.setText(personInformation.getGrade());
        viewHolder.tv_time.setText(personInformation.getTime());
//        viewHolder.icon_head.setImageResource(personInformation.getImage());
        viewHolder.tv_name.setText(personInformation.getPerson_name());
        viewHolder.tv_price.setText(String.format("%s%s", personInformation.getPrice(), "元/小时"));
        //setRecyclerViewHeight();//设置recyclerView的高度
    }

    @Override
    public int getItemCount() {
        return mPersonInformationList.size();
    }

    //得到recyclerView的高度
    private void setRecyclerViewHeight() {
        if (isCalculationRvHeight || mRv == null) return;
        isCalculationRvHeight = true;
        RecyclerView.LayoutParams itemViewLp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        int itemCount = getItemCount();
        int recyclerViewHeight = itemViewLp.height * itemCount;
        ConstraintLayout.LayoutParams rvLp = (ConstraintLayout.LayoutParams) mRv.getLayoutParams();
        rvLp.height = recyclerViewHeight + 60;
        mRv.setLayoutParams(rvLp);
    }
}
