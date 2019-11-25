package com.example.familyeducationhelp.Fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyeducationhelp.Adapter.PerInformationAdapter;
import com.example.familyeducationhelp.ClassList.PersonInformation;
import com.example.familyeducationhelp.R;
import com.lzj.gallery.library.views.BannerViewPager;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Home extends Fragment {
    private BannerViewPager banner;
    private ScrollView mScrollView;
    private int[] location = new int[2];
    private LinearLayout suspend_layout,employ_information;
    private List<PersonInformation> mPersonInformationList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initView(view);
        initBanner();
        return view;
    }

    private void initBanner() {
        List<String> urlList;
        urlList = new ArrayList<>();
        //从网络上获取图片

        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1574613928635&di=2d4b09eb9080858e0899224297ca5e99&imgtype=0&src=http%3A%2F%2Fpx.thea.cn%2FPublic%2FUpload%2FUploadfiles%2Fimage%2F20190621%2F20190621224202_70068.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1574614382845&di=e660594abd440828e5eed2fc42bfee04&imgtype=0&src=http%3A%2F%2Fstatic.91huoke.com%2Fxxfl%2Fhk91%2Fcustomer%2F28243%2FQL0UfXeGyXinLEuXsoNjJiRYvb6hvl2rYGJ8Pao4.jpeg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1574613804234&di=28167e9b0601aab701f1706ee5014af2&imgtype=0&src=http%3A%2F%2Fsup.user.img37.51sole.com%2Fimages3%2F20140707%2F1449690_20147716788.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1574613747439&di=8dba9b02874790ddd2e493c9a74b7623&imgtype=0&src=http%3A%2F%2Fpan.xici.com%2Fgroup4%2FM03%2F39%2F2B%2FrBABpltgMgKEHioFAAAAAC5kNqw282.jpg%2F1010");
        banner.initBanner(urlList,true)
                .addPageMargin(16, 0)//参数1page之间的间距,参数2中间item距离边界的间距
                .addPoint(4)//添加指示器
                .addStartTimer(5)//自动轮播5秒间隔
                .addPointBottom(2)//指示器离Banner底部的距离
                .addRoundCorners(10)//圆角
                .finishConfig()//加载布局
                .addBannerListener(new BannerViewPager.OnClickBannerListener() {
                    @Override
                    public void onBannerClick(int position) {
                        //点击item
                    }
                });
    }
    private void initView(View parentView) {
        banner = parentView.findViewById(R.id.viewPager);
        RecyclerView mRecyclerView = parentView.findViewById(R.id.recycleView);
        mScrollView = parentView.findViewById(R.id.sv);
        suspend_layout = parentView.findViewById(R.id.suspend_layout);
        employ_information = parentView.findViewById(R.id.employ_information);
        TextView textLocation = parentView.findViewById(R.id.tv_location);
        //显示位置信息，从文件location中获取
        SharedPreferences pref = Objects.requireNonNull(getActivity()).getSharedPreferences("address", Context.MODE_PRIVATE);
        textLocation.setText(pref.getString("locationInformation","定位..."));
        //这里不使用setCurrentItem，轮播图默认第一张图就是list集合里第一个添加的图片，
        //如果用户一开始就向右划，这时是划不动的，所以我们要初始化一个很大的起始位置，这是一个伪的无限循环
        //false表示在第一个子集元素滑动至第pictures.size()*100+1个子集元素时，没有滑动效果
        //insertPoint();
        ////RecyclerView初始化设置
        init_personal_information();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));//给每一个item添加分割线
        mRecyclerView.setNestedScrollingEnabled(false);//取消recyclerView的滑动效果，因为此时它与最外层的scrollView之间存在滑动冲突
        PerInformationAdapter perInformationAdapter = new PerInformationAdapter(mPersonInformationList, mRecyclerView);
        mRecyclerView.setAdapter(perInformationAdapter);
    }


    private void init_personal_information() {
        for (int i = 0; i <3 ; i++) {
            //String person_name, int image, String grade, String time, String site, String subject, String price
            PersonInformation personInformation1 = new PersonInformation("Faker",R.drawable.luosheng,"三年级","刚刚","高新区","90");
            mPersonInformationList.add(personInformation1);
        }
        for (int i = 0; i <3 ; i++) {
            PersonInformation personInformation2 = new PersonInformation("Uzi",R.drawable.ming,"四年级","4小时前","龙泉驿","70");
            mPersonInformationList.add(personInformation2);
        }
        for (int i = 0; i <8 ; i++) {
            PersonInformation personInformation3 = new PersonInformation("TheShy",R.drawable.hp,"高二","30分钟前","金牛区","80");
            mPersonInformationList.add(personInformation3);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
         * 监听scrollView滑动，当招聘信息标题栏离屏幕顶部垂直距离小于statusBar的宽度时，隐藏scrollView里的“招聘信息”标题栏，
         * 显示事先隐藏在statusBar下面的“招聘信息标”题栏，此时也要确保当手下滑动，标题栏距屏幕顶部垂直距离大于statusBar宽度时，
         * 进行与之前相反的操作，有一点要注意：scrollView中“招聘信息”标题栏的隐藏是使用“INVISIBLE”，不然滑动时会出现头像跳动。
         */
        mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                employ_information.getLocationOnScreen(location);
                if (location[1]<getStatusBarHeight()+2){
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
     * @return statusBar的高度
     */
    private int getStatusBarHeight() {
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return  resources.getDimensionPixelSize(resourceId);
    }
}
