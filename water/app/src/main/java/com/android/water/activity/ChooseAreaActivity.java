package com.android.water.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.water.R;
import com.android.water.gson.AddressBean;
import com.android.water.helper.HttpUtil;
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
 * 选择区域
 */
public class ChooseAreaActivity extends AppCompatActivity {

    private RelativeLayout mRlSearch;
    private EditText mEdtInput;
    private RecyclerView mRvArea;
    private BaseQuickAdapter adapter;
    private List<String> areas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void initView() {
        String type = getIntent().getStringExtra("type");
        mRlSearch = (RelativeLayout) findViewById(R.id.rlSearch);
        mEdtInput = (EditText) findViewById(R.id.edtInput);
        mRvArea = (RecyclerView) findViewById(R.id.rv_area);
        mRvArea.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_area) {
            @Override
            protected void convert(@NonNull BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_name, item);

            }


        };
        mRvArea.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String area = (String) adapter.getData().get(position);
                Intent data = new Intent();
                data.putExtra("area",area);
                setResult(100, data);
                finish();
            }
        });


        mEdtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    adapter.setNewData(areas);
                } else {
                    //循环数据，判断数据中的名字是否匹配输入框的 进行删选显示
                    List<String> listBeans = new ArrayList<>();

                    for (int i = 0; i < areas.size(); i++) {
                        String o = (String) areas.get(i);
                        if (o.contains(s)) {
                            listBeans.add(areas.get(i));
                        }
                    }
                    adapter.setNewData(listBeans);
                }
            }
        });

    }

    /**
     * 获取区域数据
     *
     * @return
     */
    public void getData() {

        HttpUtil.sendOkHttpRequest("http://web.juhe.cn:8080/environment/water/stateList?key=f650c5f605ce599e35dc66c942bdb3cf", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG", "请求失败");
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TAG", "查询成功");
                final String responseText = response.body().string();
                Log.d("TAG", " 返回结果为 " + responseText);
                Gson gson = new Gson();
                AddressBean addressBean = gson.fromJson(responseText, AddressBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (addressBean != null &&addressBean.getError_code()==0) {
                            areas=addressBean.getResult();
                          adapter.setNewData(addressBean.getResult());
                        } else {
                            Toast.makeText(ChooseAreaActivity.this, "获取区域信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

}