package com.example.familyeducationhelp.Fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familyeducationhelp.Adapter.LooperPagerAdapter;
import com.example.familyeducationhelp.Adapter.PerInformationAdapter;
import com.example.familyeducationhelp.ClassList.FixedSpeedScroller;
import com.example.familyeducationhelp.ClassList.PersonInformation;
import com.example.familyeducationhelp.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class home extends Fragment implements ViewPager.OnPageChangeListener {
    private ViewPager vpager;
    private LooperPagerAdapter looperPagerAdapter;
    private static List<Integer> pictures = new ArrayList<>();
    private Handler mHandler = new Handler();
    private RecyclerView mrecyclerView;
    private LinearLayout indicator;
    private ScrollView mScrollView;
    private int[] location = new int[2];
    private boolean isSlide=false;
    private boolean isFirst=true;
    private LinearLayout suspend_layout,employ_information,today_recommend;
    static {//想集合中添加数据
        pictures.add(R.drawable.page1);
        pictures.add(R.drawable.page2);
        pictures.add(R.drawable.page3);
        pictures.add(R.drawable.page4);
    }

    private String Tag="竖线的y坐标：";
    private List<PersonInformation> mPersonInformationList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHandler.post(mLooperTask); //当我这个界面绑定到窗口的时候
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler.removeCallbacks(mLooperTask);//当这个界面解除与窗口的绑定时
    }
    //窗口绑定就是当前手机的窗口是否显示这个界面
    private  Runnable mLooperTask = new Runnable() {
        @Override
        public void run() {
            if (!isSlide&&!isFirst){
                int currentItem = vpager.getCurrentItem();
                playDelay(mLooperTask);
//                Log.d(Tag,"初始化后的viewPager滚动");
                vpager.setCurrentItem(++currentItem,true);//切换viewPager的图片到下一个
            }else if (isFirst){
                vpager.setCurrentItem(pictures.size()*100,false);
                playDelay(mLooperTask);
//                Log.d(Tag,"初始化设定第一个viewPager");
                isFirst=false;
            }
            if (isSlide){
                playDelay(mLooperTask);
                isSlide=false;
            }


        }
    };

    private void playDelay(Runnable mLooperTask) {
        mHandler.postDelayed(mLooperTask,4000);
    }

    private void initView(View parentView) {
        indicator = parentView.findViewById(R.id.indicator);
        vpager = parentView.findViewById(R.id.viewPager);
        mrecyclerView =parentView.findViewById(R.id.recycleView);
        mScrollView = parentView.findViewById(R.id.sv);
        suspend_layout = parentView.findViewById(R.id.suspend_layout);
        employ_information = parentView.findViewById(R.id.employ_information);
        today_recommend = parentView.findViewById(R.id.today_recommend);
        vpager.addOnPageChangeListener(this);
        looperPagerAdapter = new LooperPagerAdapter();
        looperPagerAdapter.setData(pictures);
        vpager.setAdapter(looperPagerAdapter);
        //这里不使用setCurrentItem，轮播图默认第一张图就是list集合里第一个添加的图片，
        //如果用户一开始就向右划，这时是划不动的，所以我们要初始化一个很大的起始位置，这是一个伪的无限循环
        //false表示在第一个子集元素滑动至第pictures.size()*100+1个子集元素时，没有滑动效果
        //控制轮播图自动滑动的速度
        controlViewPagerSpeed(getActivity(),vpager,500);//毫秒 速度
        //根据图片的个数，去添加点的个数
        insertPoint();
        ////ReceyclerView初始化设置
        init_personal_information();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mrecyclerView.setLayoutManager(linearLayoutManager);
        mrecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));//给每一个item添加分割线
        mrecyclerView.setNestedScrollingEnabled(false);//取消recyclerView的滑动效果，因为此时它与最外层的scrollView之间存在滑动冲突
        PerInformationAdapter perInformationAdapter = new PerInformationAdapter(mPersonInformationList,mrecyclerView);
        mrecyclerView.setAdapter(perInformationAdapter);
    }

    private void init_personal_information() {
        for (int i = 0; i <3 ; i++) {
            //String person_name, int image, String grade, String time, String site, String subject, String price
            PersonInformation personInformation1 = new PersonInformation("罗胜",R.drawable.luosheng,"三年级","刚刚","高新区","英语","90");
            mPersonInformationList.add(personInformation1);
        }
        for (int i = 0; i <3 ; i++) {
            PersonInformation personInformation2 = new PersonInformation("欧阳鸣",R.drawable.ming,"四年级","4小时前","龙泉驿","数学","70");
            mPersonInformationList.add(personInformation2);
        }
        for (int i = 0; i <8 ; i++) {
            PersonInformation personInformation3 = new PersonInformation("何飘",R.drawable.hp,"高二","30分钟前","金牛区","化学","80");
            mPersonInformationList.add(personInformation3);
        }
    }

    private void insertPoint() {
        for (int i = 0; i < pictures.size(); i++) {
            View point = new View(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20,20);
            layoutParams.setMargins(20,0,20,0);
            point.setBackground(getResources().getDrawable(R.drawable.shape_point_selected));
            point.setLayoutParams(layoutParams);
            indicator.addView(point);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
         * viewPager监听触摸事件，因为我们要保证用户在手动滑动viewPager后，系统重新计时，viewPage按一定时间间隔循环展示，
         * 当手指按上屏幕或者是手指出现滑动动作，子线程都必须撤销（计时也就是消失）
         * 当手机松开之后系统重新计时，子线程与UI线程绑定，进行计时操作
         */
        vpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        mHandler.removeCallbacks(mLooperTask);
                        break;
                    case MotionEvent.ACTION_UP:
                        isSlide=true;
                        mHandler.post(mLooperTask);
                        break;
                }
                return false;
            }
        });
        /*
         * 监听scrollView滑动，当招聘信息标题栏离屏幕顶部垂直距离小于statusBar的宽度时，隐藏scrollView里的“招聘信息”标题栏，
         * 显示事先隐藏在statusBar下面的“招聘信息标”题栏，此时也要确保当手下滑动，标题栏距屏幕顶部垂直距离大于statusBar宽度时，
         * 进行与之前相反的操作，有一点要注意：scrollView中“招聘信息”标题栏的隐藏是使用“INVISIBLE”，不然滑动时会出现头像跳动。
         */
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                employ_information.getLocationOnScreen(location);
                Log.d(Tag,String.valueOf(location[1]));
                if (location[1]<getStatusBarHeight()+2){
//                    Log.d(Tag,"到达了顶端111111111");
                    employ_information.setVisibility(View.INVISIBLE);
                    suspend_layout.setVisibility(View.VISIBLE);
                }else {
                    employ_information.setVisibility(View.VISIBLE);
                    suspend_layout.setVisibility(View.GONE);
                }
            }
        });

    }

    /**
     * 利用反射获取状态栏高度
     * @return
     */
    protected int getStatusBarHeight() {
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return  resources.getDimensionPixelSize(resourceId);
    }
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int position) {
        //当viewPager滑动结束后才做的事情
        int realPosition;
        if (looperPagerAdapter.getDataRealSize()!=0){
            realPosition = position %looperPagerAdapter.getDataRealSize();
        }else {
            realPosition=0;
        }
        setSelectPosition(realPosition);
    }
    @Override
    public void onPageScrollStateChanged(int i) {
    }
    private void setSelectPosition(int realPosition) {
        for (int i = 0; i < indicator.getChildCount(); i++) {
            View point = indicator.getChildAt(i);
            if (i!=realPosition){
                point.setBackground(getResources().getDrawable(R.drawable.shape_point_normal));
            }else{
                point.setBackground(getResources().getDrawable(R.drawable.shape_point_selected));
            }
        }
    }

    //自定义viewpager滑动速度
    private void controlViewPagerSpeed(Context context, ViewPager viewpager, int DurationSwitch) {
        try {
            Field mField;
            mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            FixedSpeedScroller mScroller =
                    new FixedSpeedScroller(context, new AccelerateInterpolator());
            mScroller.setmDuration(DurationSwitch);
            mField.set(viewpager, mScroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
