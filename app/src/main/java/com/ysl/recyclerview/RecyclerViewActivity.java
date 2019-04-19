package com.ysl.recyclerview;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);
        ButterKnife.bind(this);

        //横向或者竖向的列表
        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);*/

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

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(6, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);

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
    }
}
