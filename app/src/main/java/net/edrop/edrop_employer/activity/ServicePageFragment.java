package net.edrop.edrop_employer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.adapter.AcceptOrderAdapter;
import net.edrop.edrop_employer.adapter.DoneOrderAdapter;
import net.edrop.edrop_employer.adapter.WaitOrderAdapter;
import net.edrop.edrop_employer.entity.Order;
import net.edrop.edrop_employer.utils.Constant;
import net.edrop.edrop_employer.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Android Studio.
 * User: zhanghaoyu
 * Date: 2019/12/12
 * Time: 16:25
 */
public class ServicePageFragment extends Fragment {
    private static final String SECTION_STRING = "fragment_string";
    private View view;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private ListView listView1;
    private ListView listView2;
    private ListView listView3;
    private List<String> mTitleList = new ArrayList<String>();//页卡标题集合
    private View view1, view2, view3;//页卡视图
    private List<View> mViewList = new ArrayList<>();//页卡视图集合
    private OkHttpClient okHttpClient;
    private List<Order>  orderList1= null;
    private List<Order>  orderList2= null;
    private List<Order>  orderList3= null;
    //接单控件
    private SmartRefreshLayout refreshLayout1;
    private SmartRefreshLayout refreshLayout2;
    private SmartRefreshLayout refreshLayout3;
    private int userId;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                String json = (String) msg.obj;
                orderList1 = new Gson().fromJson(json, new TypeToken<List<Order>>() {
                }.getType());
                refreshLayout1.finishRefresh();
                AcceptOrderAdapter adapter = new AcceptOrderAdapter(orderList1, view.getContext(), R.layout.item_acceptorders_layout);
                listView1.setAdapter(adapter);
//                Log.e("a", orderList.toString());
            }else if (msg.what ==0){
                String json = (String) msg.obj;
                orderList2 = new Gson().fromJson(json, new TypeToken<List<Order>>() {
                }.getType());
                refreshLayout2.finishRefresh();
                WaitOrderAdapter adapter = new WaitOrderAdapter(orderList2, view.getContext(), R.layout.item_waitorders_layout);
                listView2.setAdapter(adapter);
            }else if (msg.what ==1){
                String json = (String) msg.obj;
                orderList3 = new Gson().fromJson(json, new TypeToken<List<Order>>() {
                }.getType());
                Log.e("dddddddddddddddd", orderList3.toString());
                refreshLayout3.finishRefresh();
                DoneOrderAdapter adapter = new DoneOrderAdapter(orderList3, view.getContext(), R.layout.item_doneorders_layout);
                listView3.setAdapter(adapter);


            }

        }


    };

    private void setRefreshListeners() {
        refreshLayout1.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getAcceptOrdersByOkHttp();

            }
        });
        refreshLayout2.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
               getWaitOrdersByOkHttp(userId);
            }
        });
        refreshLayout3.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getDoneOrdersByOkHttp(userId);
            }
        });


    }

    public static ServicePageFragment newInstance(String sectionNumber) {
        ServicePageFragment fragment = new ServicePageFragment();
        Bundle args = new Bundle();
        args.putString(SECTION_STRING, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service_page, container, false);
        SharedPreferencesUtils loginInfo = new SharedPreferencesUtils(view.getContext(), "loginInfo");
        userId = loginInfo.getInt("userId");
        initView();
        initTabLayout();
        setRefreshListeners();
        //注册事件订阅者
        EventBus.getDefault().register(this);
        return view;
    }
    @Subscribe(sticky = true)
    public void onMessageReceive(String event){
        if (event.equals("update")){
            getAcceptOrdersByOkHttp();
        }
    }


    private void setmTabLayoutListener() {
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选择时触发
                switch (tab.getPosition()) {
                    case 0:
                        Log.e("aaa", "切换到了" + tab.getText().toString());

                        getAcceptOrdersByOkHttp();
                        break;
                    case 1:
                        getWaitOrdersByOkHttp(userId);
                        break;
                    case 2:
                        getDoneOrdersByOkHttp(userId);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //未选择时触发
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //选中之后再次点击即复选时触发
            }
        });


    }


    /**\
     * 获取当前工作人员已完成的订单
     * @param userId
     */
    private void getDoneOrdersByOkHttp(int userId) {
        FormBody formBody = new FormBody.Builder()
                .add("state", 1 + "").add("userId",userId+"").build();
        Request request = new Request.Builder()
                .url(Constant.BASE_URL + "getOrderFinish")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Message message = new Message();
                message.what = 1;
                message.obj = string;
                handler.sendMessage(message);
            }
        });
    }
    /**
     * 获取当前工作人员的未完成订单
     * @param userId
     */
    private void getWaitOrdersByOkHttp(int userId) {
        FormBody formBody = new FormBody.Builder()
                .add("state", 0 + "").add("userId",userId+"").build();
        Request request = new Request.Builder()
                .url(Constant.BASE_URL + "getOrderDoing")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();

                Message message = new Message();
                message.what = 0;
                message.obj = string;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 获取全部代接订单
     */
    public void getAcceptOrdersByOkHttp() {
        FormBody formBody = new FormBody.Builder()
                .add("state", -1 + "").build();
        Request request = new Request.Builder()
                .url(Constant.BASE_URL + "getOrderByState")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();

                Message message = new Message();
                message.what = -1;
                message.obj = string;
                handler.sendMessage(message);
            }
        });
    }

    private void initView() {
        mViewPager = (ViewPager) view.findViewById(R.id.order_vp_view);
        mTabLayout = (TabLayout) view.findViewById(R.id.order_tabs);
        mInflater = LayoutInflater.from(view.getContext());
        okHttpClient = new OkHttpClient();
    }

    private void initTabLayout() {


        view1 = mInflater.inflate(R.layout.item_service_accept_order, null);
        refreshLayout1 = view1.findViewById(R.id.accept_smart_layout);
        refreshLayout1.setRefreshHeader(new ClassicsHeader(view.getContext()));
        listView1 = view1.findViewById(R.id.lv_acceptOrders);
        view2 = mInflater.inflate(R.layout.item_service_wait_order, null);
        refreshLayout2 = view2.findViewById(R.id.wait_smart_layout);
        refreshLayout2.setRefreshHeader(new ClassicsHeader(view.getContext()));
        listView2 = view2.findViewById(R.id.lv_waitOrders);
        view3 = mInflater.inflate(R.layout.item_service_done_order, null);
        refreshLayout3 = view3.findViewById(R.id.done_smart_layout);
        refreshLayout3.setRefreshHeader(new ClassicsHeader(view.getContext()));
        listView3 = view3.findViewById(R.id.lv_doneOrders);

        //添加页卡视图
        mViewList.add(view1);
        mViewList.add(view2);
        mViewList.add(view3);
        //添加页卡标题
        mTitleList.add("接单大厅");
        mTitleList.add("待服务");
        mTitleList.add("已完成");
        setmTabLayoutListener();
        //添加tab选项卡，默认第一个选中
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)), true);
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));

        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
        //给ViewPager设置适配器
        mViewPager.setAdapter(mAdapter);
        //将TabLayout和ViewPager关联起来
        mTabLayout.setupWithViewPager(mViewPager);
        //给Tabs设置适配器
        mTabLayout.setTabsFromPagerAdapter(mAdapter);
    }

    /**
     * ViewPager适配器
     **/
    private class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }

    }
}
