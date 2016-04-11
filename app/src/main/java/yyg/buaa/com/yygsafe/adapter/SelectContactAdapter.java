package yyg.buaa.com.yygsafe.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import yyg.buaa.com.yygsafe.R;
import yyg.buaa.com.yygsafe.domain.ContactInfo;

/**
 * Created by yyg on 2016/4/9.
 */
public class SelectContactAdapter extends BaseAdapter{
    private Context context;
    private List<ContactInfo> infos;

    public SelectContactAdapter(Context context, List<ContactInfo> infos) {
        this.context = context;
        this.infos = infos;
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            //没有缓存
            convertView = LayoutInflater.from(context).inflate(R.layout.item_contact, null);
            holder = new ViewHolder();
            holder.tv_contactitem_name = (TextView) convertView.findViewById(R.id.tv_contactitem_name);
            holder.tv_contactitem_phone = (TextView) convertView.findViewById(R.id.tv_contactitem_phone);
            convertView.setTag(holder);
        } else {
            //有缓存，直接调用
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_contactitem_name.setText(infos.get(position).getName());
        holder.tv_contactitem_phone.setText(infos.get(position).getPhone());
        return convertView;
    }

    public static class ViewHolder{
        TextView tv_contactitem_name;
        TextView tv_contactitem_phone;
    }
}
