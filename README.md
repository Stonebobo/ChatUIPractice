---
title: Andorid开发 | RecyclerView 仿QQ聊天界面实现
date: 2021-03-21 16:42:18
tags: [面试,安卓开发]
category: 学习笔记
---

> 前言：看了郭霖先生的著作《第一行代码-第二版》决定自己动手实现一个，该篇博客包含了APP制作的全部流程，RecyclerView 视频教程[RecyclerView 基础使用教程](https://www.bilibili.com/video/BV13y4y1E7pF?p=22)

<!-- more -->

## RecyclerView 仿QQ聊天界面实现

##### 1. 效果图

<img src="https://i.loli.net/2021/03/21/j9ialQO3e1gcYb5.gif" alt="1" style="zoom:67%;" />

##### 2. 步骤

###### 2.1 材料准备

​	在这里分享一个好用到炸裂的图库[阿里巴巴矢量图标库](https://www.iconfont.cn/) ,先注册，才可以下载里面的图片，从上述的演示图中 我们需要四个图片 

1. 男生头像

2. 女生头像

3. 左聊天气泡

4. 右聊天气泡

   都可以在[阿里巴巴矢量图标库](https://www.iconfont.cn/) 找到 ，并下载在当前工程目录下的drawable文件夹下 **PS：一定要是PNG格式**

###### 2.2 9图制作

​	什么是9图？ 其实9图是安卓开发当中常用的一种图片格式(.9.png) ，这种图片有一个特点就是 他可以规定图片在被拉伸时，那一部分区域是可以被拉伸的，那一部分是放内容的，**记住：左上 拉伸，右下内容**，如何将PNG图片 转为.9.png格式的图片，在AndroidStudio中 右击PNG 图片就可以看到，**注意：在创作完.9图片以后 需要把原来的图片删掉，不然AS会资源重复错误**（个人觉得是因为 AS认为.9图和原来的图是一个资源）

![image-20210321163245580](https://i.loli.net/2021/03/21/U5TSjsHrNdBxPGO.png)

<img src="https://i.loli.net/2021/03/21/fwMeIZuyndgDrWP.png" alt="image-20210321163049337" style="zoom:67%;" />

###### 2.3 布局代码

1. `activity_main` 布局

   - 效果图：<img src="https://i.loli.net/2021/03/21/U8DwIxZCdqH4iOf.png" alt="image-20210321163837502" style="zoom:67%;" />

   - 嵌套布局：整体就是一个`LinerLayout`中包含了`RecycleView`与两个`LinearLayout`，

     ![image-20210321163946521](https://i.loli.net/2021/03/21/htifMuoVbC4S12a.png)

   - 源码：

     ```XML
     <?xml version="1.0" encoding="utf-8"?>
     <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
         android:layout_width="match_parent"
         android:layout_height="match_parent"
         android:layout_margin="3dp"
         android:orientation="vertical">
     
         <androidx.recyclerview.widget.RecyclerView
             android:id="@+id/rv"
             android:layout_width="match_parent"
             android:layout_height="0dp"
             android:layout_weight="18"/>
         <LinearLayout
             android:layout_width="match_parent"
             android:layout_height="0dp"
             android:orientation="horizontal"
             android:layout_weight="1">
             <ImageView
                 android:id="@+id/img_photo"
                 android:layout_width="0dp"
                 android:layout_height="match_parent"
                 android:layout_weight="1"
                 android:padding="8dp"
                 android:src="@drawable/photo"/>
             <ImageView
                 android:id="@+id/img_camera"
                 android:layout_width="0dp"
                 android:layout_height="match_parent"
                 android:layout_weight="1"
                 android:padding="8dp"
                 android:src="@drawable/camera"/>
             <ImageView
                 android:id="@+id/img_express"
                 android:layout_width="0dp"
                 android:layout_height="match_parent"
                 android:padding="8dp"
                 android:layout_weight="1"
                 android:src="@drawable/expression"/>
             <ImageView
                 android:id="@+id/img_add"
                 android:layout_width="0dp"
                 android:layout_height="match_parent"
                 android:layout_weight="1"
                 android:padding="8dp"
                 android:src="@drawable/add"/>
     
         </LinearLayout>
         <LinearLayout
             android:layout_width="match_parent"
             android:layout_height="wrap_content"
             android:orientation="horizontal">
     
             <EditText
                 android:id="@+id/edit"
                 android:layout_width="0dp"
                 android:layout_height="match_parent"
                 android:layout_weight="4"
                 android:layout_marginLeft="5dp"
                 android:hint="要发送的话"
                 android:lines="2"/>
             <Button
                 android:id="@+id/btn_send"
                 android:layout_width="0dp"
                 android:layout_height="wrap_content"
                 android:layout_weight="1"
                 android:text="发送" />
         </LinearLayout>
     
     
     
     </LinearLayout>
     ```

     

2. RecycleView中`item_layout`布局

   - 效果图：

   - 布局格式：整体我们采用了`RelativeLayout`，又嵌套了两个`LinearLayout` ，`LinearLayout`中又包含了 头像与 聊天框

     ![image-20210321164504456](https://i.loli.net/2021/03/21/cxltiysqgD92nFz.png)

     

   - 源码：

     ```XML
     <?xml version="1.0" encoding="utf-8"?>
     <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         android:padding="10dp">
     
         <LinearLayout
             android:id="@+id/linear_left"
             android:layout_width="wrap_content"
             android:layout_height="wrap_content"
             android:layout_alignParentLeft="true"
             android:orientation="horizontal">
     
             <ImageView
                 android:id="@+id/head_left"
                 android:layout_width="wrap_content"
                 android:layout_height="wrap_content"
                 android:src="@drawable/boy_head" />
     
             <LinearLayout
                 android:layout_width="wrap_content"
                 android:layout_height="wrap_content"
                 android:layout_marginLeft="3dp"
                 android:background="@drawable/left">
     
     
                 <TextView
                     android:id="@+id/msg_left"
                     android:layout_width="wrap_content"
                     android:layout_height="wrap_content"
                     android:layout_gravity="center" />
             </LinearLayout>
         </LinearLayout>
     
         <LinearLayout
             android:id="@+id/linear_right"
             android:layout_width="wrap_content"
             android:layout_height="wrap_content"
             android:layout_below="@+id/linear_left"
             android:layout_alignParentRight="true">
     
     
     
     
             <LinearLayout
     
                 android:layout_width="wrap_content"
                 android:layout_height="wrap_content"
                 android:layout_marginLeft="3dp"
                 android:layout_toLeftOf="@+id/head_right"
                 android:background="@drawable/right">
     
                 <TextView
                     android:id="@+id/msg_right"
                     android:layout_width="wrap_content"
                     android:layout_height="wrap_content"
                     android:layout_gravity="center" />
             </LinearLayout>
     
             <ImageView
                 android:id="@+id/head_right"
                 android:layout_width="wrap_content"
                 android:layout_height="wrap_content"
                 android:layout_alignParentRight="true"
                 android:src="@drawable/girl_head" />
         </LinearLayout>
     
     
     </RelativeLayout>
     ```

     

###### 2.4 后台代码(看不懂 就去看前言中 的视频)

1. Message类

   ```java
   public class Message {
       public static int TYPE_RECEIVE = 0;
       public static int TYPE_SEND = 1;
       private String content;
       private int type;
   
       public Message(String content, int type) {
           this.content = content;
           this.type = type;
       }
   
       public String getContent() {
           return content;
       }
   
       public void setContent(String content) {
           this.content = content;
       }
   
       public int getType() {
           return type;
       }
   
       public void setType(int type) {
           this.type = type;
       }
   }
   ```

   

2. Adapter类

   ```java
   public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
       private List<Message> data;
       private Context context;
   
       public MyAdapter(List<Message> data, Context context) {
           this.data = data;
           this.context = context;
       }
   
       @NonNull
       @Override
       public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view = View.inflate(context, R.layout.item_layout, null);
           return new ViewHolder(view);
       }
   
       @Override
       public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
           Message message = data.get(position);
           if (message.getType() == Message.TYPE_RECEIVE) {
               holder.linear_left.setVisibility(View.VISIBLE);
               holder.linear_right.setVisibility(View.GONE);
               holder.left_text.setText(message.getContent());
           } else {
               holder.linear_left.setVisibility(View.GONE);
               holder.linear_right.setVisibility(View.VISIBLE);
               holder.right_text.setText(message.getContent());
           }
   
       }
   
       @Override
       public int getItemCount() {
           return data == null ? 0 : data.size();
       }
   
       public class ViewHolder extends RecyclerView.ViewHolder {
           private LinearLayout linear_left;
           private LinearLayout linear_right;
           private TextView left_text;
           private TextView right_text;
   
           public ViewHolder(@NonNull View itemView) {
               super(itemView);
               linear_left = itemView.findViewById(R.id.linear_left);
               linear_right = itemView.findViewById(R.id.linear_right);
               left_text = itemView.findViewById(R.id.msg_left);
               right_text = itemView.findViewById(R.id.msg_right);
           }
       }
   }
   ```

   

3. activity_main 类

   ```java
   public class MainActivity extends AppCompatActivity {
       private EditText editText;
       private Button btn_send;
       private RecyclerView recyclerView;
       private ImageView img_add;
       private ImageView img_camera;
       private ImageView img_photo;
       private ImageView img_express;
       private MyAdapter adapter;
       private List<Message> data = new ArrayList<>();
   
       @Override
       protected void onCreate(Bundle savedInstanceState) {
           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);
           ActionBar actionBar = getSupportActionBar();
           if (actionBar != null) {
               actionBar.hide();
           }
           bindView();
           init_data();
           LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
           recyclerView.setLayoutManager(linearLayoutManager);
   
           adapter = new MyAdapter(data, this);
           recyclerView.setAdapter(adapter);
   
           btn_send.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String s = editText.getText().toString();
                   if (s.equals("") == false) {
                       Message message = new Message(s, Message.TYPE_SEND);
                       data.add(message);
                       //当有新消息时，刷新RecyclerView
                       adapter.notifyItemInserted(data.size() - 1);
                       //将屏幕滚动到最新消息
                       recyclerView.scrollToPosition(data.size() - 1);
                       editText.setText("");
   
                   } else {
                       Toast.makeText(MainActivity.this, "发送消息不能为空！", Toast.LENGTH_SHORT).show();
                   }
               }
           });
   
       }
   
       public void bindView() {
           editText = findViewById(R.id.edit);
           btn_send = findViewById(R.id.btn_send);
           recyclerView = findViewById(R.id.rv);
           img_add = findViewById(R.id.img_add);
           img_camera = findViewById(R.id.img_camera);
           img_express = findViewById(R.id.img_express);
           img_photo = findViewById(R.id.img_photo);
       }
   
       public void init_data() {
           Message meg1 = new Message("Hello, 我叫白文磊", Message.TYPE_RECEIVE);
           data.add(meg1);
           Message meg2 = new Message("Hello, 我叫郭涵博", Message.TYPE_SEND);
           data.add(meg2);
       }
   }
   ```

   

##### 3. 源码分享