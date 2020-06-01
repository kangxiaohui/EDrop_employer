package net.edrop.edrop_employer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.entity.Order;
import net.edrop.edrop_employer.utils.SharedPreferencesUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by Android Studio.
 * User: zhanghaoyu
 * Date: 2019/12/16
 * Time: 15:05
 */
public class DoneOrderAdapter extends BaseAdapter {
    // 原始数据
    private List<Order> dataSource = null;
    // 上下文环境
    private Context context;
    // item对应的布局文件
    private int item_layout_id;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private int userId;
    private int orderId;

    public DoneOrderAdapter(List<Order> dataSource, Context context, int item_layout_id) {
        this.dataSource = dataSource;
        this.context = context;
        this.item_layout_id = item_layout_id;

    }
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
            viewHolder.tvName = convertView.findViewById(R.id.tv_doneorder_name);
            viewHolder.tvPhone = convertView.findViewById(R.id.tv_doneorder_phone);
            viewHolder.tvAddress = convertView.findViewById(R.id.tv_doneorder_address);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_doneorder_time);
            viewHolder.tvNumber = convertView.findViewById(R.id.tv_doneorder_number);
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
//        userId = loginInfo.getInt("userId");
//        orderId = order.getId();

        return convertView;
    }

    private class ViewHolder {
        public TextView tvName;
        public TextView tvPhone;
        public TextView tvAddress;
        public TextView tvTime;
        public TextView tvNumber;
    }

}
