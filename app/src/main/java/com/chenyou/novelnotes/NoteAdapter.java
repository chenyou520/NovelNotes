package com.chenyou.novelnotes;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends BaseAdapter implements Filterable {
    private Context mContext;

    private List<Note> backList;//用来备份原始数据
    private List<Note> noteList;//这个数据是会改变的，所以要有个变量来备份一下原始数据
    private MyFilter mFilter;

    public NoteAdapter(Context mContext, List<Note> noteList) {
        this.mContext = mContext;
        this.noteList = noteList;
        backList = noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mContext.setTheme(R.style.DayTheme);
        View v = View.inflate(mContext, R.layout.note_layout, null);
        TextView tv_content = (TextView) v.findViewById(R.id.tv_content);
        TextView tv_time = (TextView) v.findViewById(R.id.tv_time);
        //设置文本视图的文本
        String allText = noteList.get(position).getContent();
        /*if (sharedPreferences.getBoolean("noteTitle" ,true))
            tv_content.setText(allText.split("\n")[0]);*/
        tv_content.setText(allText);
        tv_time.setText(noteList.get(position).getTime());
        //将注释id保存到标记
        v.setTag(noteList.get(position).getId());
        return v;
    }


    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new MyFilter();
        }
        return mFilter;
    }

    class MyFilter extends Filter {
        //我们在performFiltering(CharSequence charSequence)这个方法中定义过滤规则
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Note> list;
            if (TextUtils.isEmpty(constraint)) {//当过滤的关键字为空的时候，我们则显示所有的数据
                list = backList;
            } else {//否则把符合条件的数据对象添加到集合中
                list = new ArrayList<>();
                for (Note note : backList) {
                    if (note.getContent().contains(constraint)) {
                        list.add(note);
                    }
                }
            }

            results.values = list;//将得到的集合保存到FilterResults的value变量中
            results.count = list.size();//将集合的大小保存到FilterResults的count变量中

            return results;
        }

        //在publishResults方法中告诉适配器更新界面
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            noteList = (List<Note>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();//通知数据发生了改变
            } else {
                notifyDataSetInvalidated();//通知数据失效
            }
        }
    }
}
