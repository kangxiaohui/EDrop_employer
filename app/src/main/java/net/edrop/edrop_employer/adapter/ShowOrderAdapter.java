package net.edrop.edrop_employer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.edrop.edrop_employer.R;
import net.edrop.edrop_employer.entity.Order;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import static net.edrop.edrop_employer.utils.Constant.ORDER_STATE_FINISH;
import static net.edrop.edrop_employer.utils.Constant.ORDER_STATE_NO_FINISH;
import static net.edrop.edrop_employer.utils.Constant.ORDER_STATE_NO_RECEIVE;

/**
 * Created by Android Studio.
 * User: zhanghaoyu
 * Date: 2019/12/10
 * Time: 15:05
 */
public class ShowOrderAdapter extends BaseAdapter {
    // 原始数据
    private List<Order> dataSource = null;
    // 上下文环境
    private Context context;
    // item对应的布局文件
    private int item_layout_id;

    public ShowOrderAdapter(List<Order> dataSource, Context context, int item_layout_id) {
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
            viewHolder.tvName = convertView.findViewById(R.id.tv_showorder_name);
            viewHolder.tvPhone = convertView.findViewById(R.id.tv_showorder_phone);
            viewHolder.tvAddress = convertView.findViewById(R.id.tv_showorder_address);
            viewHolder.tvTime = convertView.findViewById(R.id.tv_showorder_time);
            viewHolder.tvNumber = convertView.findViewById(R.id.tv_showorder_number);
            viewHolder.tvStatus = convertView.findViewById(R.id.tv_showorder_status);
            viewHolder.ivStatus = convertView.findViewById(R.id.iv_status);
            convertView.setTag(viewHolder);
        }else {
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
        if (order.getState()==ORDER_STATE_NO_RECEIVE){
            //订单待确认
            viewHolder.tvStatus.setText("待确认");
            viewHolder.ivStatus.setImageResource(R.drawable.waitlook);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_blue));
        }else if (order.getState()==ORDER_STATE_NO_FINISH){
            //订单待服务
            viewHolder.tvStatus.setText("待服务");
            viewHolder.ivStatus.setImageResource(R.drawable.waitservice);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_yellow));
        }else if (order.getState()==ORDER_STATE_FINISH){
            //订单已完成
            viewHolder.tvStatus.setText("已完成");
            viewHolder.ivStatus.setImageResource(R.drawable.complete);
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.color_green));
        }
        return convertView;
    }

    private class ViewHolder {
        public TextView tvName;
        public TextView tvPhone;
        public TextView tvAddress;
        public TextView tvTime;
        public TextView tvNumber;
        public TextView tvStatus;
        public ImageView ivStatus;
    }
}
