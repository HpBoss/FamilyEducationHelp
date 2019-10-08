package com.example.familyeducationhelp.Adapter;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.familyeducationhelp.ClassList.PersonInformation;
import com.example.familyeducationhelp.R;
import java.util.List;

public class PerInformationAdapter extends RecyclerView.Adapter<PerInformationAdapter.ViewHolder> {
    private List<PersonInformation> mPersonInformationList;
    private View itemView;
    private RecyclerView mRv;
    private boolean isCalculationRvHeight ;

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon_head;
        TextView tv_name,tv_time,tv_grade,tv_location,tv_price,tv_subject;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon_head = itemView.findViewById(R.id.icon_head);
            tv_name = itemView.findViewById(R.id.person_name);
            tv_time = itemView.findViewById(R.id.label_time);
            tv_grade = itemView.findViewById(R.id.label_grade);
            tv_location = itemView.findViewById(R.id.label_site);
            tv_price = itemView.findViewById(R.id.label_price);
            tv_subject = itemView.findViewById(R.id.label_subject);
        }
    }

    public PerInformationAdapter(List<PersonInformation> personInformationList,RecyclerView recyclerView) {
        mPersonInformationList = personInformationList;
        mRv = recyclerView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.personal_information_item,viewGroup,false);
//        ViewHolder holder = new ViewHolder(itemView);2019.9.24改动（注释，holder直接被new ViewHolder(itemView)代替）
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        PersonInformation personInformation = mPersonInformationList.get(i);
        viewHolder.tv_subject.setText(personInformation.getSubject());
        viewHolder.tv_location.setText(personInformation.getSite());
        viewHolder.tv_grade.setText(personInformation.getGrade());
        viewHolder.tv_time.setText(personInformation.getTime());
        viewHolder.icon_head.setImageResource(personInformation.getImage());
        viewHolder.tv_name.setText(personInformation.getPerson_name());
        viewHolder.tv_price.setText(personInformation.getPrice());
        //setRecyclerViewHeight();//设置recyclerView的高度
    }

    @Override
    public int getItemCount() {
        return mPersonInformationList.size();
    }
    //得到recyclerView的高度
    private  void setRecyclerViewHeight(){
        if (isCalculationRvHeight || mRv == null) return;
        isCalculationRvHeight = true;
        RecyclerView.LayoutParams itemViewLp = (RecyclerView.LayoutParams)itemView.getLayoutParams();
        int itemCount = getItemCount();
        int recyclerViewHeight = itemViewLp.height * itemCount;
        ConstraintLayout.LayoutParams rvLp = (ConstraintLayout.LayoutParams)mRv.getLayoutParams();
        rvLp.height = recyclerViewHeight+60;
        mRv.setLayoutParams(rvLp);
    }
}
