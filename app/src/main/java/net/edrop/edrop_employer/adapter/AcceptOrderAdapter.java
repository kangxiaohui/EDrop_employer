package net.edrop.edrop_employer.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.entity.Order;
import net.edrop.edrop_employer.utils.Constant;
import net.edrop.edrop_employer.utils.SharedPreferencesUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
 * Date: 2019/12/16
 * Time: 15:05
 */
public class AcceptOrderAdapter extends BaseAdapter {
    // 原始数据
    private List<Order> dataSource = null;
    // 上下文环境
    private Context context;
    // item对应的布局文件
    private int item_layout_id;
    private OkHttpClient okHttpClient = new OkHttpClient();

    public AcceptOrderAdapter(List<Order> dataSource, Context context, int item_layout_id) {
        this.dataSource = dataSource;
        this.context = context;
        this.item_layout_id = item_layout_id;

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -1) {
                getAcceptOrdersByOkHttp();
            }else if (msg.what==888){
                String json = (String) msg.obj;
                dataSource = new Gson().fromJson(json, new TypeToken<List<Order>>() {
                }.getType());
                notifyDataSetChanged();
            }
        }


    };


    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(item_layout_id, null);
            viewHolder.tvName = convertView.findViewById(R.id.tv_acceptorder_name);
            viewHolder.tvPhone = convertView.findViewById(R.id.tv_acceptorder_phone);
            viewHolder.tvAddress = convertView.findViewById(R.id.tv_acceptorder_address);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_acceptorder_time);
            viewHolder.tvNumber = convertView.findViewById(R.id.tv_acceptorder_number);
            viewHolder.btnAcceptOrder = convertView.findViewById(R.id.btn_acceptOrders);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Order order = dataSource.get(position);
        viewHolder.tvName.setText(order.getOuname());

        Timestamp timestamp = order.getReserveTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = dateFormat.format(timestamp);
        viewHolder.tvTime.setText(date);
        viewHolder.tvPhone.setText(order.getOutelephone());
        viewHolder.tvAddress.setText(order.getOrderAddress());
        viewHolder.tvNumber.setText(order.getNumber());
        SharedPreferencesUtils loginInfo = new SharedPreferencesUtils(context, "loginInfo");
        final int userId = loginInfo.getInt("userId");
        final int orderId = order.getId();

        viewHolder.btnAcceptOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("test", userId + "=========" + orderId);
                acceptOrder(userId, orderId);

            }
        });

        return convertView;
    }

    private class ViewHolder {
        public TextView tvName;
        public TextView tvPhone;
        public TextView tvAddress;
        public TextView tvTime;
        public TextView tvNumber;
        private Button btnAcceptOrder;
    }

    private void acceptOrder(int userId, int orderId) {
        FormBody formBody = new FormBody.Builder()
                .add("userId", userId + "").add("orderId", orderId + "").build();
        Request request = new Request.Builder()
                .url(Constant.BASE_URL + "receiveOrder")
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
                handler.sendMessage(message);
            }
        });

    }
    /**
     * 获取全部代接订单
     */
    private void getAcceptOrdersByOkHttp() {
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
                message.what = 888;
                message.obj = string;
                handler.sendMessage(message);
            }
        });
    }
}
