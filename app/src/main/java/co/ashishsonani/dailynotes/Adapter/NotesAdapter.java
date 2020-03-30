package co.ashishsonani.dailynotes.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import co.ashishsonani.dailynotes.Model.Listdata;
import co.ashishsonani.dailynotes.R;
import co.ashishsonani.dailynotes.Activity.editNoteActivity;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyHolder> {

    private List<Listdata> noteslist;
    private String noteTitle, noteDescription, noteCurrentDate;
    private Context context;

    public NotesAdapter(List<Listdata> noteslist, Context context) {
        this.context = context;
        this.noteslist = noteslist;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int position) {
        Listdata data = noteslist.get(position);
        myHolder.title.setText(data.getTitle());
        myHolder.description.setText(data.getDesc());
        myHolder.currentDate.setText(data.getCurrentDate());
    }

    @Override
    public int getItemCount() {
        return noteslist.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView currentDate;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            currentDate = itemView.findViewById(R.id.currentDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Listdata listdata = noteslist.get(getAdapterPosition());
                    Intent i = new Intent(context, editNoteActivity.class);
                    i.putExtra("id", listdata.id);
                    i.putExtra("title", listdata.title);
                    i.putExtra("desc", listdata.desc);
                    i.putExtra("currentDate", listdata.currentDate);
                    context.startActivity(i);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Listdata listdata = noteslist.get(getAdapterPosition());
                    noteTitle = listdata.title.trim();
                    noteDescription = listdata.desc.trim();
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, noteTitle);
                    i.putExtra(Intent.EXTRA_TEXT, "Title : "+ noteTitle + "\n" + "Description : "+ noteDescription);
                    context.startActivity(Intent.createChooser(i, "Send To"));
                    return false;
                }
            });
        }


    }
}
