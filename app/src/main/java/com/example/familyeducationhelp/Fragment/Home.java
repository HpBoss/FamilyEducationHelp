package com.example.familyeducationhelp.Fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.familyeducationhelp.Adapter.PerInformationAdapter;
import com.example.familyeducationhelp.ClassList.PersonInformation;
import com.example.familyeducationhelp.R;
import com.lzj.gallery.library.views.BannerViewPager;

import java.util.ArrayList;
import java.util.List;


public class Home extends Fragment {
    private BannerViewPager banner;
    private RecyclerView mrecyclerView;
    private ScrollView mScrollView;
    private int[] location = new int[2];
    private LinearLayout suspend_layout,employ_information,today_recommend;
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
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1543221773&di=c63f30c7809e518cafbff961bcd9ec2a&imgtype=jpg&er=1&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0116605851154fa8012060c8587ca1.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542627042541&di=3ad9deeefff266e76d1f5d57a58f63d1&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fdesign%2F00%2F69%2F99%2F66%2F9fce5755f081660431464492a9aeb003.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542627042539&di=95bd41d43c335e74863d9bb540361906&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F019a0558be22d6a801219c77d0578a.jpg%402o.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542627042539&di=cdd54bffd2aac448c70ae6b416a004d4&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F01edb3555ea8100000009af0ba36f5.jpg%401280w_1l_2o_100sh.jpg");
        banner.initBanner(urlList,true)
                .addPageMargin(16, 0)//参数1page之间的间距,参数2中间item距离边界的间距
                .addPoint(4)//添加指示器
                .addStartTimer(5)//自动轮播5秒间隔
                .addPointBottom(2)//指示器离Banner底部的距离
                .addRoundCorners(12)//圆角
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
        mrecyclerView =parentView.findViewById(R.id.recycleView);
        mScrollView = parentView.findViewById(R.id.sv);
        suspend_layout = parentView.findViewById(R.id.suspend_layout);
        employ_information = parentView.findViewById(R.id.employ_information);
        today_recommend = parentView.findViewById(R.id.today_recommend);
        //这里不使用setCurrentItem，轮播图默认第一张图就是list集合里第一个添加的图片，
        //如果用户一开始就向右划，这时是划不动的，所以我们要初始化一个很大的起始位置，这是一个伪的无限循环
        //false表示在第一个子集元素滑动至第pictures.size()*100+1个子集元素时，没有滑动效果
//        insertPoint();
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
     * @return
     */
    protected int getStatusBarHeight() {
        Resources resources = this.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return  resources.getDimensionPixelSize(resourceId);
    }

}
