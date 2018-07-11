package com.example.bsk.mail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.konusarakogren.email.emailproject.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    List<String> to_list;
    List<String> from_list;
    List<String> subject_list;
    List<String> content;
    Context context;

    public RecyclerAdapter(List<String> to_list, List<String> from_list, List<String> subject_list, List<String> content, Context context) {
        this.to_list = to_list;
        this.from_list = from_list;
        this.subject_list = subject_list;
        this.content = content;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(com.konusarakogren.email.emailproject.R.layout.mail_recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.desc_textView.setText(content.get(i));
        viewHolder.to_txtView.setText(to_list.get(i));
        viewHolder.from_textView.setText(from_list.get(i));
        viewHolder.sub_textView.setText(subject_list.get(i));


    }

    @Override
    public int getItemCount() {
        if (subject_list.size() < 11) {
        return subject_list.size();
    }else{
        return 0;
    }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView to_txtView;
        TextView from_textView;
        TextView sub_textView;
        TextView desc_textView;

        ViewHolder(View itemView) {
            super(itemView);
            to_txtView = itemView.findViewById(com.konusarakogren.email.emailproject.R.id.to_text);
            from_textView = itemView.findViewById(com.konusarakogren.email.emailproject.R.id.from_text);
            sub_textView = itemView.findViewById(com.konusarakogren.email.emailproject.R.id.sub_text);
            desc_textView = itemView.findViewById(R.id.desc_text);
        }
    }


}


