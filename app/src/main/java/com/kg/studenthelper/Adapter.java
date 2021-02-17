package com.kg.studenthelper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kg.studenthelper.Activity.StudentComplainDetailActivity;
import com.kg.studenthelper.Activity.SupervisorDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ComplainHolder> {


    Context context;
    List<NewComplain> complains=new ArrayList<>();

    public Adapter(Context context)
    {

        this.context=context;

    }

    @NonNull
    @Override
    public ComplainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
        return new ComplainHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplainHolder holder, int position) {

        final NewComplain complain=complains.get(position);
        holder.status.setText(complain.getStatus());
        holder.problem.setText(complain.getText_problem());
        holder.date.setText(complain.getDate());
        holder.room.setText(complain.getRoom_problem());
        holder.category.setText(complain.getCategory());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, StudentComplainDetailActivity.class);
                intent.putExtra("name",complain.getUserName());
                intent.putExtra("room",complain.getRoom_problem());
                intent.putExtra("content",complain.getText_problem());
                intent.putExtra("date",complain.getDate());
                intent.putExtra("image",complain.getImg_problem());
                intent.putExtra("status",complain.getStatus());
                intent.putExtra("category",complain.getCategory());
                intent.putExtra("key",complain.getKey());
                intent.putExtra("userId",complain.getUserId());
                intent.putExtra("building",complain.getBuilding());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return complains.size();
    }


    public class ComplainHolder extends RecyclerView.ViewHolder{

        TextView room,date,problem,status,category;
        CardView cardView;

        public ComplainHolder(@NonNull View itemView) {
            super(itemView);
            room=itemView.findViewById(R.id.edit_room);
            date=itemView.findViewById(R.id.edit_date);
            problem=itemView.findViewById(R.id.edit_problem);
            status=itemView.findViewById(R.id.edit_status);
            category=itemView.findViewById(R.id.edit_category);
            cardView=itemView.findViewById(R.id.cardView);
        }
    }


    public void setComplains(List<NewComplain> complains) {
        this.complains = complains;
        notifyDataSetChanged();
    }

    public NewComplain getComplains(int pos)
    {
        return complains.get(pos);
    }
}
