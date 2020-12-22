package com.iastate.edu.coms309.sb4.getit.client.screens.recyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/* Course List Recylcer View Adapter
 * acts as a base class to implement a recycler view used in the course list screen
 */

public class RecylcerViewAdapter extends RecyclerView.Adapter<RecylcerViewAdapter.ViewHolder> implements Filterable {

    private List<String> dataSet;
    private List<String> allDataSet;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private int layout;
    private int textView;


    public RecylcerViewAdapter(Context context, List<String> data, int layout, int textView) {
        this.mInflater = LayoutInflater.from(context);
        this.dataSet = data;
        this.layout = layout;
        this.textView = textView;

        allDataSet = new ArrayList<>();
        allDataSet.addAll(dataSet);
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    /* on create view holder
     * creates the view holders for the classes in the recycler view views
     *
     * @param parent - viewgroup to inflate
     */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(layout, parent, false);
        return new ViewHolder(view);
    }


    /* on bind view holder
     * sets text for the view holder in the arraylist
     *
     * @param holder - view holder the view will get binded to
     * @param position -  position in the arraylist to set text to
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String course = dataSet.get(position);
        holder.myTextView.setText(course);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {

        return myFilter;
    }

    private Filter myFilter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<String> filteredList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(allDataSet);
            } else {
                for (String course : allDataSet) {
                    if (course.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(course);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            filteredList.add("Add New Course");
            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            dataSet.clear();
            dataSet.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

}
