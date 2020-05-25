package com.example.familyeducationhelp.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.familyeducationhelp.R;
import com.example.familyeducationhelp.activity.BaseActivity;
import com.example.familyeducationhelp.adapter.PerInformationAdapter;
import com.example.familyeducationhelp.classList.PersonInformation;
import com.lzj.gallery.library.views.BannerViewPager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {
    private BannerViewPager banner;
    private NestedScrollView mNestedScrollView;
    private int[] location = new int[2];
    private LinearLayout suspend_layout, employ_information;
    private List<PersonInformation> mPersonInformationList = new ArrayList<>();
    private RefreshLayout mRefreshLayout;
    private LocationClient mLocationClient;
    private TextView mTextLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        initBanner();
        initLocationOption();
        return view;
    }

    private void initBanner() {
        List<String> urlList = new ArrayList<>();
        //从网络上获取图片
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1574613928635&di=2d4b09eb9080858e0899224297ca5e99&imgtype=0&src=http%3A%2F%2Fpx.thea.cn%2FPublic%2FUpload%2FUploadfiles%2Fimage%2F20190621%2F20190621224202_70068.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1574614382845&di=e660594abd440828e5eed2fc42bfee04&imgtype=0&src=http%3A%2F%2Fstatic.91huoke.com%2Fxxfl%2Fhk91%2Fcustomer%2F28243%2FQL0UfXeGyXinLEuXsoNjJiRYvb6hvl2rYGJ8Pao4.jpeg");
        urlList.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=4232683767,935589539&fm=11&gp=0.jpg");
        urlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1586769798713&di=843c6401aa715cc3b6e0e278a4c4e37f&imgtype=0&src=http%3A%2F%2Fimg.edu.sdnews.com.cn%2Fupimages%2F62a85%2F18180_163a7.jpg");
        banner.initBanner(urlList, true)
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
//        mRefreshLayout = parentView.findViewById(R.id.refreshLayout);
        banner = parentView.findViewById(R.id.viewPager);
        RecyclerView mRecyclerView = parentView.findViewById(R.id.recycleView);
        mNestedScrollView = parentView.findViewById(R.id.scrollView);
        suspend_layout = parentView.findViewById(R.id.suspend_layout);
        employ_information = parentView.findViewById(R.id.employ_information);
        mTextLocation = parentView.findViewById(R.id.tv_location);

        //这里不使用setCurrentItem，轮播图默认第一张图就是list集合里第一个添加的图片，
        //如果用户一开始就向右划，这时是划不动的，所以我们要初始化一个很大的起始位置，这是一个伪的无限循环
        //false表示在第一个子集元素滑动至第pictures.size()*100+1个子集元素时，没有滑动效果
        //insertPoint();
        ////RecyclerView初始化设置
        init_personal_information();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL));//给每一个item添加分割线
        mRecyclerView.setNestedScrollingEnabled(false);//取消recyclerView的滑动效果，因为此时它与最外层的scrollView之间存在滑动冲突
        PerInformationAdapter perInformationAdapter = new PerInformationAdapter(mPersonInformationList, mRecyclerView);
        mRecyclerView.setAdapter(perInformationAdapter);
    }

    private void init_personal_information() {
        for (int i = 0; i < 3; i++) {
            //String person_name, int image, String grade, String time, String site, String subject, String price
            PersonInformation personInformation1 = new PersonInformation("Faker", R.drawable.luosheng, "三年级", "刚刚", "高新区", "90");
            mPersonInformationList.add(personInformation1);
        }
        for (int i = 0; i < 3; i++) {
            PersonInformation personInformation2 = new PersonInformation("Uzi", R.drawable.ming, "四年级", "4小时前", "龙泉驿", "70");
            mPersonInformationList.add(personInformation2);
        }
        for (int i = 0; i < 8; i++) {
            PersonInformation personInformation3 = new PersonInformation("TheShy", R.drawable.hp, "高二", "30分钟前", "金牛区", "80");
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
        mNestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                employ_information.getLocationOnScreen(location);
                if (location[1] < BaseActivity.getStatusBarHeight(requireActivity()) + 2) {
                    employ_information.setVisibility(View.INVISIBLE);
                    suspend_layout.setVisibility(View.VISIBLE);
                } else {
                    employ_information.setVisibility(View.VISIBLE);
                    suspend_layout.setVisibility(View.GONE);
                }
            }
        });
//        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//
//            }
//        });
//        mRefreshLayout.setRefreshHeader(new ClassicsHeader(requireActivity()));
//        mRefreshLayout.setHeaderHeight(60);
    }

    /**
     * 初始化定位参数配置
     */
    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        mLocationClient = new LocationClient(requireActivity().getApplicationContext());
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        mLocationClient.registerLocationListener(myLocationListener);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("gcj02");
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000);
        //可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
        //可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
        //可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
        //可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(locationOption);
        //开始定位
        mLocationClient.start();
    }

    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            int count = location.getLocationDescribe().length();

            if (count > 3) {
                Log.d("location:", location.getLocationDescribe());
                mTextLocation.setText(location.getLocationDescribe().substring(1, count - 2));
            }
            //避免多次进行多次定位
            mLocationClient.unRegisterLocationListener(this);
        }
    }

}
