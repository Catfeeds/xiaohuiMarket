package com.xiaohui.replenishment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaohui.replenishment.response.Task;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * @author  zhumg on 8/29.
 */
public class TaskAdapter extends AfinalAdapter<Task> {

    int w_color = 0;
    int g_color = 0;
    int r_color = 0;
    int orange_color = 0;
    int taskType = 0;//0未完成，1完成

    public TaskAdapter(Context context, List<Task> tasks, int taskType) {
        super(context, tasks);
        this.taskType = taskType;
        this.w_color = context.getResources().getColor(R.color.white);
        this.r_color = context.getResources().getColor(R.color.red);
        this.g_color = context.getResources().getColor(R.color.font_6);
        this.orange_color = context.getResources().getColor(R.color.orange);
    }

    public void changeTaskType(int taskType) {
        this.taskType = taskType;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        Task task = getItem(position);
        return task.getUiType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Task task = getItem(position);
        if(task.getUiType() == 1) {
            //标题
            Holder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.adapter_task_title, null);
                holder = new Holder(convertView, true);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.task_title.setText(task.getItemName());
            //holder.task_type_txt.setText(task.getUnitName());
        } else {
            //内容
            Holder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.adapter_task, null);
                holder = new Holder(convertView, false);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.refresh(task);
        }

        return convertView;
    }

    class Holder {

        private TextView task_title;
        private TextView task_type_txt;
        private TextView item_code_txt;
        private TextView task_time;
        private TextView location_code_txt;

        private TextView t_itemname;
        private TextView t_itemcount;

        private TextView task_status;


        public Holder(View view, boolean title) {

            if(title) {
                task_title = (TextView) view.findViewById(R.id.task_title);
                task_type_txt = (TextView) view.findViewById(R.id.task_type);
                return;
            }

            location_code_txt = (TextView) view.findViewById(R.id.location_code);
            item_code_txt = (TextView) view.findViewById(R.id.item_code);
            task_time = (TextView) view.findViewById(R.id.task_time);
            t_itemname = (TextView) view.findViewById(R.id.item_name);
            t_itemcount = (TextView) view.findViewById(R.id.item_count);

            task_status = (TextView) view.findViewById(R.id.task_status);

        }

        public void refresh(Task task) {

            //如果有原数据，则 ...
            item_code_txt.setText(task.getBarcode());
            location_code_txt.setText(task.getLocationCode());
            t_itemname.setText(task.getItemName());
            t_itemcount.setText(task.getHasCompleteQuantity() + "/" + task.getItemQuantity() + task.getUnitName());

            if(task.isUpTask()) {
                task_status.setText("上架任务");
//                if(task.isError()) {
                    task_status.setBackgroundResource(R.drawable.btn_border_grey);
                    task_status.setTextColor(g_color);
//                } else {
//                    task_status.setBackgroundResource(R.drawable.btn_red);
//                    task_status.setTextColor(w_color);
//                }
            } else {
                task_status.setText("下架任务");
//                task_status.setBackgroundResource(R.drawable.btn_red);
//                if(task.isError()) {
                    task_status.setBackgroundResource(R.drawable.btn_border_orange);
                    task_status.setTextColor(orange_color);
//                } else {
//                    task_status.setBackgroundResource(R.drawable.btn_red);
//                    task_status.setTextColor(w_color);
//                }
            }

            if(taskType == 1) {
                if (task.isError()) {
                    //数量全部变色
                    t_itemcount.setTextColor(r_color);
                } else {
                    t_itemcount.setTextColor(g_color);
                }
                task_time.setVisibility(View.VISIBLE);
                task_time.setText(task.getCompleteTime());
            } else {
                task_time.setVisibility(View.GONE);
            }

        }

    }
}
