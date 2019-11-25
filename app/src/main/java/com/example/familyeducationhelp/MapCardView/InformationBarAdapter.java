package com.example.familyeducationhelp.MapCardView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.familyeducationhelp.R;
import java.util.ArrayList;

public class InformationBarAdapter extends BaseExpandableListAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<FatherData> data_list;
    private boolean isFirstCome = true;

    public InformationBarAdapter(Context context, ArrayList<FatherData> data_list) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.data_list = data_list;
    }
    //刷新数据
    public void flashData(ArrayList<FatherData> data){
        isFirstCome = false;
        this.data_list = data;
        this.notifyDataSetChanged();//更新listView
        isFirstCome = true;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return data_list.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return data_list.get(i).getList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        HolderViewFather holderViewFather;
        if (view == null){
            holderViewFather = new HolderViewFather();
            view = mInflater.inflate(R.layout.item_father,viewGroup,false);
            holderViewFather.avatar = view.findViewById(R.id.personal_avatar);
            holderViewFather.grade = view.findViewById(R.id.grade);
            holderViewFather.username = view.findViewById(R.id.username);
            holderViewFather.sex = view.findViewById(R.id.sex);
            holderViewFather.price = view.findViewById(R.id.price);
            holderViewFather.subject = view.findViewById(R.id.subject);
            holderViewFather.display_status = view.findViewById(R.id.display);
            view.setTag(holderViewFather);
        }else {
            holderViewFather =(HolderViewFather) view.getTag();
        }
        holderViewFather.avatar.setImageResource(data_list.get(i).getAvater());
        holderViewFather.grade.setText(data_list.get(i).getGrade());
        holderViewFather.username.setText(data_list.get(i).getUsername());
        holderViewFather.sex.setImageResource(data_list.get(i).getSex());
        holderViewFather.subject.setText(data_list.get(i).getSubject());
        holderViewFather.price.setText(data_list.get(i).getPrice());
        if (b){
            Animation rotateUpToDown = AnimationUtils.loadAnimation(mContext,R.anim.rotate_up_to_down);
            holderViewFather.display_status.setAnimation(rotateUpToDown);
            holderViewFather.display_status.setImageResource(R.drawable.ic_expand_more_black_32dp);
        }else {
            Animation rotateDownToUp = AnimationUtils.loadAnimation(mContext,R.anim.rotate_down_to_up);
            if (isFirstCome){
                holderViewFather.display_status.setAnimation(rotateDownToUp);
                holderViewFather.display_status.setImageResource(R.drawable.ic_expand_less_black_32dp);
            }

        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        HolderViewChild holderViewChild;
        if (view == null){
            holderViewChild = new HolderViewChild();
            view = mInflater.inflate(R.layout.item_child,viewGroup,false);
            holderViewChild.startTime = view.findViewById(R.id.startTime);
            holderViewChild.frequency = view.findViewById(R.id.frequency);
            holderViewChild.otherRequest = view.findViewById(R.id.otherRequest);
            holderViewChild.location = view.findViewById(R.id.location);
            holderViewChild.distance = view.findViewById(R.id.distance);
            view.setTag(holderViewChild);
        }else {
            holderViewChild =(HolderViewChild) view.getTag();
        }
        holderViewChild.distance.setText(String.format("距离你%s公里", data_list.get(i).getList().get(i1).getDistance()));
        holderViewChild.location.setText(data_list.get(i).getList().get(i1).getLocation());
        holderViewChild.otherRequest.setText(data_list.get(i).getList().get(i1).getOtherRequest());
        holderViewChild.frequency.setText(data_list.get(i).getList().get(i1).getFrequency());
        holderViewChild.startTime.setText(data_list.get(i).getList().get(i1).getStartTime());
        if (b){
            Animation translate_down_up = AnimationUtils.loadAnimation(mContext,R.anim.translate_down_to_up);
            view.setAnimation(translate_down_up);
        }else {
//            Animation translate_up_down = AnimationUtils.loadAnimation(mContext,R.anim.translate_up_to_down);
//            view.setAnimation(translate_up_down);
        }

        return view;
    }

    //定义二级列表类
    public class HolderViewChild{
        TextView startTime;
        TextView frequency;
        TextView otherRequest;
        TextView location;
        TextView distance;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
    //定义一级列表类
    public class HolderViewFather{
        ImageView avatar;//头像
        TextView username;//用户名
        ImageView sex;//性别
        TextView grade;//年级
        TextView subject;//科目
        TextView price;//上课一小时的价格
        ImageView display_status;
    }
}
