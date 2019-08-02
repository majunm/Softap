package com.example.tst;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tst.widget.BallLayer;
import com.example.tst.widget.INoticeBoardView;
import com.example.tst.widget.RaffleLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RaffleLayout mRaffleLayout;
//        setContentView(mRaffleLayout = new RaffleLayout(this));
        setContentView(R.layout.raffle);
        mRaffleLayout = findViewById(R.id.RaffleLayout);
        RaffleLayout mRaffleLayout2 = findViewById(R.id.RaffleLayout2);
        ImageView icon = findViewById(R.id.icon);
        mRaffleLayout2.setShowAssist(true);
        BallLayer.dispose(icon, true);
//        setContentView(getContentViewRsId());
//        initView();
    }

    protected int getContentViewRsId() {
        return R.layout.activity_main;
    }

    protected void initView() {
        INoticeBoardView mINoticeView2 = (INoticeBoardView) findViewById(R.id.INoticeView2);
        INoticeBoardView mINoticeView22 = (INoticeBoardView) findViewById(R.id.INoticeView22);
        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            list1.add("吃饭时，和朋友提到高血压，我就顺势科普起来");
            list1.add("2222222222222222222222222222222222222222222222222222222222222222222222222222222");
            list1.add("往日的欢乐已是往日的快乐，再怎么留恋也无法放在今天；过去的痛苦再一次回顾");
            list1.add("别人家孩子不好吗");
            list1.add("就知道玩");
            list1.add("别人家孩子周末都在家学习");
        }
        mINoticeView22.setAdapter(new INoticeBoardView.INoticeAdapt<String>(list1) {

            @Override
            public void onBindViewHolder(View itemView, String itemData) {
                TextView tvs = itemView.findViewById(R.id.tv_line);
                tvs.setText(mCurrentIndex + "|" + itemData);
                // tvs.setBackgroundColor(mCurrentIndex % 2 == 0 ? Color.RED : Color.GREEN);
                tvs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("INoticeView2", TAG + ":------------点击事件---------------------------------");
                    }
                });
            }

            @Override
            protected View onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
//                return inflater.inflate(R.layout.lines, parent,false);
                return inflater.inflate(R.layout.lines, null);
            }
        });
        mINoticeView2.setAdapter(new INoticeBoardView.INoticeAdapt<String>(list1) {

            @Override
            public void onBindViewHolder(View itemView, String itemData) {
                ((TextView) itemView).setText(mCurrentIndex + "|" + itemData);
                ((TextView) itemView).setGravity(Gravity.CENTER_VERTICAL);
//                TextView tvs = itemView.findViewById(R.id.tv_line);
//                tvs.setText(mCurrentIndex + "|" + itemData);
//                // tvs.setBackgroundColor(mCurrentIndex % 2 == 0 ? Color.RED : Color.GREEN);
//                tvs.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Log.e("INoticeView2", TAG + ":------------点击事件---------------------------------");
//                    }
//                });
            }

            @Override
            protected View onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
//                return inflater.inflate(R.layout.lines, parent,false);
//                return inflater.inflate(R.layout.lines, null);
                TextView tv = new TextView(parent.getContext());

                return tv;
            }
        });

    }

    public final String TAG = getClass().getSimpleName();
}
