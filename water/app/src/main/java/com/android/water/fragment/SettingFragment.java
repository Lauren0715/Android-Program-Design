package com.android.water.fragment;

import android.content.Intent;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;


import com.android.water.R;
import com.android.water.activity.LoginActivity;
import com.android.water.activity.PasswordResetActivity;
import com.android.water.helper.SPUtils;
import com.android.water.weatherview.SettingBar;
import com.android.water.weatherview.SwitchButton;

import static android.content.Context.BATTERY_SERVICE;


/**
 * desc   : 設置
 */
public final class SettingFragment extends Fragment implements SwitchButton.OnCheckedChangeListener, View.OnClickListener {
    SwitchButton mAutoSwitchView;
    TextView tvName;

    public static SettingFragment newInstance() {
        return new SettingFragment();
    }

    private View inflate;
    private SettingBar sbPower;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_setting, null, false);
        initView();
        return inflate;
    }

    protected void initView() {
        mAutoSwitchView = inflate.findViewById(R.id.sb_setting_switch);
        tvName = inflate.findViewById(R.id.tv_name);

        // 设置切换按钮的监听
        mAutoSwitchView.setChecked((Boolean) SPUtils.get(getContext(), "isAutoLogin", false));
        mAutoSwitchView.setOnCheckedChangeListener(this);
        tvName.setText((String) SPUtils.get(getContext(), "name", "用户名"));
        String mWeatherId = (String) SPUtils.get(getContext(), "weatherId", "");
        inflate.findViewById(R.id.sb_setting_exit).setOnClickListener(this);
        inflate.findViewById(R.id.sb_setting_pwd).setOnClickListener(this);
        inflate.findViewById(R.id.sb_setting_auto).setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sb_setting_exit:
                // 退出登录
                SPUtils.put(getContext(), "isLogin", false);
                startActivity(new Intent(getContext(), LoginActivity.class));
                // 进行内存优化，销毁掉所有的界面
                break;
            case R.id.sb_setting_pwd:
                startActivity(new Intent(getContext(), PasswordResetActivity.class));

                break;


        }
    }

    @Override
    public void onCheckedChanged(SwitchButton button, boolean isChecked) {
        switch (button.getId()) {
            case R.id.sb_setting_switch:
                SPUtils.put(getContext(), "isAutoLogin", isChecked);
                break;

        }
    }
}