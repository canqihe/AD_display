package com.colin.ad_display;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.colin.ad_display.bean.DataBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static util.Constants.LEFT_BANNER;
import static util.Constants.RIGHT_BANNER;
import static util.Constants.TOP_BANNER;
import static util.Constants.VIDEO_BANNER;

public class ResourceActivity extends AppCompatActivity {

    @BindView(R.id.back_icon)
    ImageButton backIcon;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.resource_list)
    RecyclerView mRecyclerView;

    private CommonAdapter<DataBean> mAdapter;
    private List<DataBean> dataBeanList = new ArrayList<>();
    private int RESOURCE_TYPE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getData();
    }


    public void getData() {

        RESOURCE_TYPE = getIntent().getIntExtra("RESOURCE_TYPE", 1);

        if (RESOURCE_TYPE == TOP_BANNER) {
            dataBeanList = DataBean.getImgTopFromSDcard();
            toolbarTitle.setText("顶部海报图");
        } else if (RESOURCE_TYPE == LEFT_BANNER) {
            dataBeanList = DataBean.getLeftBannerFromSdCard();
            toolbarTitle.setText("左侧广告图");
        } else if (RESOURCE_TYPE == RIGHT_BANNER) {
            dataBeanList = DataBean.getRightBannerFromSdCard();
            toolbarTitle.setText("右侧广告图");
        } else if (RESOURCE_TYPE == VIDEO_BANNER) {
            dataBeanList = DataBean.getVideosFromSDcard();
            toolbarTitle.setText("视频广告");
        } else return;

        mAdapter = new CommonAdapter<DataBean>(ResourceActivity.this, R.layout.item_file, dataBeanList) {
            @Override
            protected void convert(ViewHolder holder, DataBean dataEntity, int position) {
                holder.setText(R.id.file_name, dataEntity.getTitle());
                holder.setText(R.id.file_num, (position + 1) + "");
//                holder.setText(R.id.file_path, dataEntity.getImageUrl());
                ImageView headImg = holder.getConvertView().findViewById(R.id.u_img);

                File imgFile = new File(dataEntity.getImageUrl());
                Glide.with(ResourceActivity.this).load(imgFile).placeholder(R.mipmap.ic_launcher).into(headImg);
            }
        };

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, final int position) {

                new AlertDialog.Builder(ResourceActivity.this).setTitle("提示")
                        .setMessage("确定要删除这个文件吗？").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        FaceServer.getInstance().clearFacesByUserNum(ResourceActivity.this, userEntities.get(position).getUserNo());//删除本地特征库
//                        deleteById(userEntities.get(position).getId());//删除数据库操作
//                        userEntities.remove(position);
//                        mAdapter.notifyItemRemoved(position);
//                        mAdapter.notifyItemRangeChanged(position, dataBeanList.size() - position);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();

                return false;
            }
        });
    }


    @OnClick(R.id.back_icon)
    public void onClick() {
        ResourceActivity.this.finish();
    }
}