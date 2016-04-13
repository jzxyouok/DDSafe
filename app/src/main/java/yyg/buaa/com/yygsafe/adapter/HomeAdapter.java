package yyg.buaa.com.yygsafe.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import yyg.buaa.com.yygsafe.R;

/**
 * Created by yyg on 2016/4/7.
 */
public class HomeAdapter extends BaseAdapter {

    private Context context;
    private String[] name;
    private int[] resourceId;

    public HomeAdapter(Context context, String[] name, int[] resourceId) {
        this.context = context;
        this.name = name;
        this.resourceId = resourceId;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //判断是否缓存
        if (convertView == null) {
            holder = new ViewHolder();
            //通过LayoutInflater实例化布局
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_grid, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv_homeitem_icon);
            holder.tv = (TextView) convertView.findViewById(R.id.tv_homeitem_name);
            convertView.setTag(holder);
        } else {
            //通过tag找到缓存的布局
            holder = (ViewHolder) convertView.getTag();
        }
        //设置布局中控件要显示的视图
        holder.iv.setBackgroundResource(resourceId[position]);
        holder.tv.setText(name[position]);
        if (position == 0) {
            SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
            String newname = sp.getString("newname", "");
            if (!TextUtils.isEmpty(newname)) {
                holder.tv.setText(newname);
            }
        }
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return name[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public final class ViewHolder {
        public ImageView iv;
        public TextView tv;
    }

}
