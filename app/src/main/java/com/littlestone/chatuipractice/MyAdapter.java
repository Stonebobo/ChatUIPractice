package com.littlestone.chatuipractice;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Message> data;
    private Context context;
    private String TAG = "MyAdapter";

    public MyAdapter(List<Message> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_layout, null);
        ViewHolder holder=new ViewHolder(view);
        //长按文本弹出提示框
        holder.left_text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //自定义方法
                popWindow(holder.left_text);
                return false;
            }
        });
        holder.right_text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                popWindow(holder.right_text);
                return false;
            }
        });
        return holder;
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

    /**
     * 长按聊天气泡 弹出提示框
     * @param textView 将提示框放在这个View下边
     */
    public void popWindow(TextView textView) {
        View popview = LayoutInflater.from(context).inflate(R.layout.pop_textview_layout, null, false);
        PopupWindow popupWindow = new PopupWindow(popview, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true); //点击空白处 popupwindow 消失
        popupWindow.showAsDropDown(textView);


        TextView popWindow_copy = popview.findViewById(R.id.popWindow_copy);
        popWindow_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sub_start=textView.getSelectionStart();
                int sub_end=textView.getSelectionEnd();
                ClipboardManager copy= (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                String sub=textView.getText().toString().substring(sub_start,sub_end);
                ClipData clipData=ClipData.newPlainText("littlestone",sub);
                copy.setPrimaryClip(clipData);
                Toast.makeText(context,"已复制到粘贴板",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
