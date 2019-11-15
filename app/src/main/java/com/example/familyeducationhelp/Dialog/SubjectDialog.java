package com.example.familyeducationhelp.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.familyeducationhelp.Adapter.SubjectAdapter;
import com.example.familyeducationhelp.ClassList.SubjectData;
import com.example.familyeducationhelp.R;

public class SubjectDialog extends Dialog implements AdapterView.OnItemClickListener {
    private int position;
    private SubjectAdapter subjectAdapter;
    private Context mContext;
    private TextView confirmSubject;
    private updateSubjectListener mUpdateSubjectListener;

    public SubjectDialog(@NonNull Context context, int themeResId,int position) {
        super(context, themeResId);
        this.position = position;
        mContext = context;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setUpdateSubjectListener(updateSubjectListener updateSubjectListener ) {
        mUpdateSubjectListener = updateSubjectListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_subject);
        Window window = this.getWindow();
        assert window != null;
        window.setGravity(Gravity.BOTTOM);
        //设置弹出动画
        window.setWindowAnimations(R.style.main_menu_animStyle);
        //设置对话框大小
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setSubject(position);

    }

    private void setSubject(int position) {
        GridView gridView = findViewById(R.id.gridView_subject);
        gridView.setOnItemClickListener(this);
        SubjectData subjectData = new SubjectData();
        if (subjectAdapter == null){
            subjectAdapter = new SubjectAdapter(mContext,subjectData.iconSubjectShallow);
        }else {
            subjectData.iconSubjectShallow[position] = subjectData.iconSubjectDeep[position];
            subjectAdapter = new SubjectAdapter(mContext,subjectData.iconSubjectShallow);
        }
        gridView.setAdapter(subjectAdapter);
        confirmSubject = findViewById(R.id.confirm);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final int count = i;
        setSubject(i);
        confirmSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpdateSubjectListener.setUpdateSubject(count);
            }
        });
    }

    public interface updateSubjectListener{
        void setUpdateSubject(int count);
    }
}
