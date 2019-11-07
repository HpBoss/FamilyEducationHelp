package com.example.familyeducationhelp.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.familyeducationhelp.Activity.MyAccountActivity;
import com.example.familyeducationhelp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mine extends ListFragment implements View.OnClickListener{

    private ListView lv;
    private SimpleAdapter adapter;
    private ImageView imageView_head;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //图片
        int[] images = {R.drawable.news, R.drawable.record, R.drawable.setting};
        //内容
        String[] names = {"消息中心","家教记录","设置"};
        //参数一：上下文对象  参数二：数据源List<Map<String,Object>> 参数三：item对应的布局文件
        //参数四：表示由map中定义的key组成的字符串类型的数字  参数五：需要显示的控件id组成的的数组
        //保证参数四和参数五一一对应，否则控件属性会对换（张冠李戴）
        adapter = new SimpleAdapter(getActivity(), getData(images, names),
                R.layout.item_setting_adapter, new String[] { "img", "name","next" },
                new int[] { R.id.image, R.id.name, R.id.next });
        //继承了ListFragment后的方法
        setListAdapter(adapter);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);
        lv = view.findViewById(android.R.id.list);
        imageView_head = view.findViewById(R.id.headImage);
        Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/Android/data/com.example.familyeducationhelp/cache/output_image.jpg");
        if(bitmap != null){
            imageView_head.setImageBitmap(bitmap);
            //将图形设置为圆形
            imageView_head.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        imageView_head.setOnClickListener(this);
        return view;
    }

    //监听点击事件
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(position == 0){
            Toast.makeText(getActivity(),"你点击了 消息中心",Toast.LENGTH_LONG).show();
        }else if(position == 1){
            Toast.makeText(getActivity(),"你点击了  家教记录",Toast.LENGTH_LONG).show();
        }else if(position == 2){
            Toast.makeText(getActivity(),"你点击了  设置",Toast.LENGTH_LONG).show();
        }
        super.onListItemClick(l, v, position, id);
    }


    //对数据进行加载
    private List<? extends Map<String, ?>> getData(int[] images, String[] names) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("img", images[i]);
            map.put("name", names[i]);
            map.put("next", R.drawable.ic_navigate_next_black_48dp);
            list.add(map);
        }

        return list;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.headImage:
                Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.translate_right_in, R.anim.translate_left_out);
                break;
            default:
                break;
        }
    }

}
