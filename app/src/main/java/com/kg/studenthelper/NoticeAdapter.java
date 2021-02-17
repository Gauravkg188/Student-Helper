package com.kg.studenthelper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kg.studenthelper.Activity.NoticeDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeHolder>{

    Context context;
    List<Notice> noticeList=new ArrayList<>();

    public NoticeAdapter(Context context)
    {
        this.context=context;
    }

    @NonNull
    @Override
    public NoticeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.noticeitem,parent,false);

        return new NoticeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeHolder holder, int position) {

        final Notice notice=noticeList.get(position);
        holder.notice_date.setText(notice.getDate());
        holder.notice_desc.setText(notice.getContent());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent=new Intent(context, NoticeDetailActivity.class);
               intent.putExtra("text",notice.getContent());
               intent.putExtra("image",notice.getImage());
               intent.putExtra("date",notice.getDate());
               context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    public void setNoticeList(List<Notice> noticeList)
    {
        this.noticeList=noticeList;
        notifyDataSetChanged();
    }

    public Notice getNotice(int pos)
    {
        return noticeList.get(pos);
    }

    public class NoticeHolder extends RecyclerView.ViewHolder{

        TextView notice_desc;
       TextView notice_date;
        CardView cardView;

        public NoticeHolder(@NonNull View itemView) {
            super(itemView);

            notice_date=itemView.findViewById(R.id.notice_date);
            notice_desc=itemView.findViewById(R.id.notice_desc);
            cardView=itemView.findViewById(R.id.notice_cardView);
        }
    }
}
