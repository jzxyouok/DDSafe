package yyg.buaa.com.yygsafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.db.dao.BlackNumberDAO;
import yyg.buaa.com.yygsafe.domain.BlackNumberInfo;
import yyg.buaa.com.yygsafe.global.Constant;

/**
 * Created by yyg on 2016/4/19.
 */
public class CallSmsSafeAdapter extends BaseAdapter{
    private Context context;
    private List<BlackNumberInfo> list;
    private BlackNumberDAO dao;

    public CallSmsSafeAdapter(Context context, List<BlackNumberInfo> list, BlackNumberDAO dao) {
        this.context = context;
        this.list = list;
        this.dao = dao;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder(); // 减少子孩子查询的次数
            convertView = LayoutInflater.from(context).inflate(R.layout.item_callsms, null);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_item_phone);
            holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_item_mode);
            holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            // 把孩子id的引用 存放在holder里面，设置给父亲 view
            convertView.setTag(holder);
        } else {
            // 使用历史缓存view对象， 减少view对象被创建的次数
            holder = (ViewHolder) convertView.getTag();
        }
        final BlackNumberInfo info = list.get(position);
        holder.tv_phone.setText(info.getNumber());
        if (Constant.BlackNumber.MODE_ALL.equals(info.getMode())) {
            holder.tv_mode.setText("全部拦截");
        } if (Constant.BlackNumber.MODE_PHONE.equals(info.getMode())) {
            holder.tv_mode.setText("电话拦截");
        } if (Constant.BlackNumber.MODE_SMS.equals(info.getMode())) {
            holder.tv_mode.setText("短信拦截");
        }

        //图片设置监听
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = info.getNumber();
                //从数据库删除黑名单号码
                boolean result = dao.deleteDao(number);
                if (result) {
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    //从界面ui里面删除信息
                    list.remove(info);
                    //通知见面刷新
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
     return convertView;
    }

    /**
     * 家庭组View对象的容器
     */
    class ViewHolder {
        TextView tv_phone;
        TextView tv_mode;
        ImageView iv_delete;
    }
}
