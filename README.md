# INoticeBoardView

[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)

**INoticeBoardView** 公告信息自定义,最多仅切两个view来回切换,不复用view!!! 动画方式(水平|竖直)两种

## 效果预览

![INoticeBoardView.gif](https://upload-images.jianshu.io/upload_images/6260557-c8355f296316f1ea.gif?imageMogr2/auto-orient/strip)

## 特性
- 直播界面或者悬浮窗中使用
- 使用适配器模式，继承 `INoticeAdapt` 来自定义业务逻辑
- 不复用 `View` ,只操作2个view，最多2个view
- 无限轮询

## 使用

### 在XML或者代码中添加INoticeBoardView
```xml
<com.example.tst.widget.INoticeBoardView
    app:INoticeBoardView_AnimTime="6000"
    app:INoticeBoardView_Direction="vertical"
    app:INoticeBoardView_Vertical_Duration_Step="10"
    app:INoticeBoardView_Vertical_OffsetX="100"
    android:layout_width="match_parent"
    android:layout_height="match_parent"/>
```

### 使用方式

```
java
        List<String> list1 = new ArrayList<>();
        list1.add("吃饭时，和朋友提到高血压，我就顺势科普起来： 高血压的预防很重要11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        list1.add("左宗棠很喜欢下围棋，而且，还是个高手，其属僚皆非其对手。");
        list1.add("往日的欢乐已是往日的快乐，再怎么留恋也无法放在今天；过去的痛苦再一次回顾");
        list1.add("别人家孩子不好吗");
        list1.add("就知道玩");
        list1.add("别人家孩子周末都在家学习");
        mINoticeView22.setAdapter(new INoticeBoardView.INoticeAdapt<String>(list1) {
            @Override
            public void onBindViewHolder(View itemView, String itemData) {
                TextView tvs = itemView.findViewById(R.id.tv_line);
                tvs.setText(mCurrentIndex + "|" + itemData);
                tvs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("INoticeView2", ":------------点击事件---------------------------------");
                    }
                });
            }

            @Override
            protected View onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
//                return inflater.inflate(R.layout.lines, parent,false);
                return inflater.inflate(R.layout.lines, null);
            }
        });
```

## 建议直接复制源码吧

attrs.xml
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!--INoticeBoardView-->
    <declare-styleable name="INoticeBoardView">
        <!--动画时间-->
        <attr name="INoticeBoardView_AnimTime" format="integer|reference" />
        <!--竖直方向动画时间步长 动画时间 除以 时间步长-->
        <attr name="INoticeBoardView_Vertical_Duration_Step" format="integer|reference" />
        <!--竖直方向偏移-->
        <attr name="INoticeBoardView_Vertical_OffsetX" format="integer|reference" />
        <!--公告栏执行方向-->
        <attr name="INoticeBoardView_Direction">
            <enum name="horizontal" value="0" />
            <enum name="vertical" value="1" />
        </attr>
    </declare-styleable>
</resources>
```

## 许可证

    Copyright 2019 majunm

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

  [1]: https://github.com/majunm/Softap/blob/master/app/src/main/java/com/example/tst/widget