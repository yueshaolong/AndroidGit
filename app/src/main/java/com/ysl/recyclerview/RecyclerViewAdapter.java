package com.ysl.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ysl.helloworld.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyHolder> {

    private Context context;
    private List<String> nameList;

    public RecyclerViewAdapter(Context context, List<String> nameList) {
        this.context = context;
        this.nameList = nameList;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_item, parent, false);

        //控件的点击事件建议写在这个方法里；只会设置一次；如果设置在onBindViewHolder()中，每次加载控件都会重新设置一个点击事件，性能损耗
        click(view);

        return new MyHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String name = nameList.get(position);

        holder.textView.setText(name);
//        click(holder.itemView);//建议写在onCreateViewHolder()方法中。
    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class MyHolder extends ViewHolder{

        @BindView(R.id.tv)
        TextView textView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void click(View view) {
        view.findViewById(R.id.tv).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点击name", Toast.LENGTH_SHORT).show();
            }
        });
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "点击条目", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
