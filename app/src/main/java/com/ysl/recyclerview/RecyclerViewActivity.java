package com.ysl.recyclerview;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ysl.helloworld.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.State;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    @BindView(R.id.add)
    Button add;
    @BindView(R.id.remove)
    Button remove;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        ButterKnife.bind(this);

        //横向或者竖向的列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
//        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //九宫格样式
        /*GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull State state) {
                super.getItemOffsets(outRect, view, parent, state);
            }
        });*/
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));

//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(staggeredGridLayoutManager);


        List<String> nameList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            nameList.add("aaaaa");
            nameList.add("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
            nameList.add("cccdfsdgfdstcc");
            nameList.add("dd");
            nameList.add("eeeee");
            nameList.add("aaaaa");
        }
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(this, nameList);
        recyclerView.setAdapter(recyclerViewAdapter);

        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                nameList.add(2, "ysl");
                recyclerViewAdapter.notifyItemInserted(2);//在index为2的位置添加条目，然后让后面的条目向下移动附带动画
//                recyclerViewAdapter.notifyItemRangeChanged(2, recyclerViewAdapter.getItemCount());//改变index从2以后的所有的条目ui
//                recyclerViewAdapter.notifyDataSetChanged();
//                recyclerViewAdapter.notifyItemChanged(2);//只改变index为2的条目ui;不会刷新下面的条目
//                recyclerViewAdapter.notifyItemChanged(2, null);//只改变index为2的条目ui;不会刷新下面的条目
//                recyclerViewAdapter.notifyItemRangeChanged(2,2);//改变index从2以后的两个的条目ui
//                recyclerViewAdapter.notifyItemRangeChanged(2, recyclerViewAdapter.getItemCount(), null);
//                recyclerViewAdapter.notifyItemRangeInserted(2, 2);//在index为2的位置一次批量添加2个条目，添加的元素是index为2的位置开始向后的2个
            }
        });
        remove.setOnClickListener(v -> {
//            @Override
//            public void onClick(View v) {
                nameList.remove(2);
                recyclerViewAdapter.notifyItemRemoved(2);//移除第二个位置的条目，并附带动画
//                recyclerViewAdapter.notifyItemRangeRemoved(2,6);//从第二个位置开始移除元素，移除6个
//                recyclerViewAdapter.notifyItemMoved(2,4);//移动从位置2到位置4的元素；这里是垂直方向的，所以是列表在无限循环位置2向下移动，位置4移动到位置2
//            }
        });
    }
}
