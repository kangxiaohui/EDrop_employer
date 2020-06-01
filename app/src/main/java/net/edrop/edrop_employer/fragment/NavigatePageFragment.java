package net.edrop.edrop_employer.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.utils.MapUtil;

/**
 * Created by mysterious
 * User: mysterious
 * Date: 2019/11/25
 * Time: 16:42
 */
public class NavigatePageFragment extends Fragment {
    private static final String SECTION_STRING = "fragment_string";
    private Button btnStartNavigate;
    private View myView;
    //popupWindow
    private PopupWindow popupWindow = null;
    private View popupView = null;

    //这里的经纬度是直接获取的，在实际开发中从应用的地图中获取经纬度;
    private double latx = 39.9037448095;
    private double laty = 116.3980007172;
    private String mAddress = "北京天安门";

    public static NavigatePageFragment newInstance(String sectionNumber) {
        NavigatePageFragment fragment = new NavigatePageFragment();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_navigate_page, container, false);
        init();
        setLinstener();
        return myView;
    }

    private void setLinstener() {
        btnStartNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 显示PopupWindow
                if (popupWindow == null || !popupWindow.isShowing())
                    showPopupWindow();
            }
        });
    }

    private void showPopupWindow() {
        // 创建popupWindow对象
        setBackgroundAlpha(0.5f, myView.getContext());
        popupWindow = new PopupWindow();
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 通过布局填充器创建View
        popupView = getLayoutInflater().inflate(R.layout.item_popupwindow_navigate, null);
        // 设置PopupWindow显示的内容视图
        popupWindow.setContentView(popupView);
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow.setOutsideTouchable(true);
        // 设置PopupWindow是否相应点击事件
        popupWindow.setTouchable(true);
        popupView.setFocusable(true);
        View view_list = View.inflate(myView.getContext(), R.layout.item_popupwindow_navigate, null);
        popupWindow.setOnDismissListener(new popupDismissListener());
        popupWindow.showAtLocation(view_list.findViewById(R.id.popup_navigate), Gravity.CENTER, 0, 0);
        // 获取按钮并添加监听器
        Button btnBaidu = popupView.findViewById(R.id.btn_baidu_map);
        Button btnGaode = popupView.findViewById(R.id.btn_gaode_map);
        Button btnTencent = popupView.findViewById(R.id.btn_tencent_map);
        btnBaidu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MapUtil.isBaiduMapInstalled()) {
                    MapUtil.openBaiDuNavi(myView.getContext(), 0, 0, null, latx, laty, mAddress);
                } else {
                    Toast.makeText(myView.getContext(), "尚未安装百度地图", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");//进入应用市场下载
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
        btnGaode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MapUtil.isGdMapInstalled()) {
                    MapUtil.openGaoDeNavi(myView.getContext(), 0, 0, null, latx, laty, mAddress);
                } else {
                    //这里必须要写逻辑，不然如果手机没安装该应用，程序会闪退，这里可以实现下载安装该地图应用
                    Toast.makeText(myView.getContext(), "尚未安装高德地图", Toast.LENGTH_SHORT).show();
                    Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });
        btnTencent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MapUtil.isTencentMapInstalled()) {
                    MapUtil.openTencentMap(myView.getContext(), 0, 0, null, latx, laty, mAddress);
                } else {
                    Toast.makeText(myView.getContext(), "尚未安装腾讯地图", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        btnStartNavigate=myView.findViewById(R.id.btn_start_navigate);
    }

    private class popupDismissListener implements PopupWindow.OnDismissListener {
        @Override
        public void onDismiss() {
            setBackgroundAlpha(1f, myView.getContext());
        }
    }

    public static void setBackgroundAlpha(float bgAlpha, Context mContext) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

}
