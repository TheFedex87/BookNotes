package it.thefedex87.booknotes.arguments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import it.thefedex87.booknotes.R;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by federico.creti on 21/04/2015.
 */
public class ArgumentsRecyclerViewAdapter extends RecyclerView.Adapter<ArgumentsRecyclerViewAdapter.MyViewHolder> {
    LayoutInflater inflater;
    ArrayList<Argument> arguments;
    Context context;

    public ArgumentsRecyclerViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        arguments = new ArrayList<Argument>();
    }

    public void addItem(Argument argument) {
        arguments.add(argument);
        notifyItemInserted(arguments.size() - 1);
    }

    public void removeItem(Argument argument) {
        int position = arguments.indexOf(argument);
        arguments.remove(position);
        notifyItemRemoved(position);
    }

    public void updateItem(Argument argument, int position) {
        arguments.set(position, argument);
        notifyDataSetChanged();
    }

    public Argument getItemAtPosition(int position) {
        return arguments.get(position);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.line_layout, null, false);

        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(arguments.get(position).getArgumentName());
    }

    @Override
    public int getItemCount() {
        return arguments.size();
    }

    class MyViewHolder extends ViewHolder {
        private TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.textViewString);
        }
    }
}
