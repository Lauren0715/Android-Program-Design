package com.android.water.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.water.R;
import com.android.water.activity.ChooseAreaActivity;
import com.android.water.gson.ImageBean;
import com.android.water.gson.ValueBean;
import com.android.water.gson.WaterQualityBean;
import com.android.water.helper.HttpUtil;
import com.android.water.helper.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * desc   : 今日天气
 */
public final class WaterFragment extends Fragment {
    Button navButton;
    SwipeRefreshLayout swipeRefresh;
    String mAreaName = "湖北宜昌南津关";
    TextView titleCity;
    TextView titleUpdateTime;
    ImageView ivDay;
    RecyclerView rvQuality;
    private TextView tv_area_desc;
    private BaseQuickAdapter<ValueBean, BaseViewHolder> adapter;

    public static WaterFragment newInstance() {

        return new WaterFragment();
    }

    private View inflate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_water, null, false);
        initView();
        return inflate;
    }

    protected void initView() {
        ivDay = inflate.findViewById(R.id.bing_pic_img);
        navButton = inflate.findViewById(R.id.nav_button);
        swipeRefresh = inflate.findViewById(R.id.swipe_refresh);
        titleCity = inflate.findViewById(R.id.title_city);
        titleUpdateTime = inflate.findViewById(R.id.title_update_time);
        rvQuality= inflate.findViewById(R.id.rv_water);
        tv_area_desc=inflate.findViewById(R.id.tv_area_desc);
        loadBingPic();

        rvQuality.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BaseQuickAdapter<ValueBean, BaseViewHolder>(R.layout.item_quality) {
            @Override
            protected void convert(BaseViewHolder helper, ValueBean item) {
                helper.setText(R.id.tv_name, item.getName());
                helper.setText(R.id.tv_value, item.getValue());
            }


        };
        rvQuality.setAdapter(adapter);
        /*获取区域名字*/
        mAreaName = (String) SPUtils.get(getContext(), "weatherId", "湖北宜昌南津关");

        requestWater(mAreaName);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAreaName = (String) SPUtils.get(getContext(), "weatherId", "湖北宜昌南津关");
                requestWater(mAreaName);
            }
        });
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChooseAreaActivity.class);
                startActivityForResult(intent,101);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==101&&resultCode==100){
            if (data.getStringExtra("area")!=null){
                SPUtils.put(getContext(),"weatherId",data.getStringExtra("area"));
                requestWater(data.getStringExtra("area"));
            }
        }
    }



    /**
     * 获取当前区域水质质量
     */
    public void requestWater(final String areaName) {
        String waterUrl = "http://web.juhe.cn:8080/environment/water/state?state=" + areaName + "&key=f650c5f605ce599e35dc66c942bdb3cf";
        HttpUtil.sendOkHttpRequest(waterUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "请求失败");
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "获取水质信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TAG", "查询成功");
                final String responseText = response.body().string();
                Log.d("TAG", " 返回结果为 " + responseText);
                Gson gson = new Gson();
                final WaterQualityBean waterQualityBean = gson.fromJson(responseText, WaterQualityBean.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (waterQualityBean != null && waterQualityBean.getError_code()==0) {

                            showWeatherInfo(waterQualityBean);
                        } else {
                            Toast.makeText(getContext(), "获取水质信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }




    /**
     * 处理并展示Weather实体类中的数据
     */
    private void showWeatherInfo(WaterQualityBean weather) {


        List<ValueBean> valueBeans =new ArrayList<>();
        WaterQualityBean.ResultBean resultBean = weather.getResult().get(0);

        titleUpdateTime.setText(resultBean.getTime());
        titleCity.setText(resultBean.getState());
        tv_area_desc.setText(resultBean.getProfile());
        valueBeans.add(new ValueBean("ph",resultBean.getPh()));
        valueBeans.add(new ValueBean("PH水质类别",resultBean.getPhquality()));
        valueBeans.add(new ValueBean("溶解氧",resultBean.getOxygen()));
        valueBeans.add(new ValueBean("溶解氧水质类别",resultBean.getOxygenquality()));
        valueBeans.add(new ValueBean("氨氮",resultBean.getNitrogen()));
        valueBeans.add(new ValueBean("氨氮水质类别",resultBean.getNitrogenquality()));
        valueBeans.add(new ValueBean("高锰酸钾指数",resultBean.getPermangan()));
        valueBeans.add(new ValueBean("高锰酸钾指数水质类别",resultBean.getPermanganquality()));
        valueBeans.add(new ValueBean("总有机碳",resultBean.getOrgacarbon()));
        valueBeans.add(new ValueBean("总有机碳水质类别",resultBean.getOrgacarbonquality()));
adapter.setNewData(valueBeans);
    }



    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
        String requestBingPic = "https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                ImageBean imageBean = gson.fromJson(response.body().string(), ImageBean.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getContext()).load("https://www.bing.com" + imageBean.getImages().get(0).getUrl()).into(ivDay);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}