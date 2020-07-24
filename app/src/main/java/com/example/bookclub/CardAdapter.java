package com.example.bookclub;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.Locale;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.BooksHolder> {

    private Context context;
    private ArrayList<Books> books;
    private RecyclerView recyclerView;

    public CardAdapter(Context context, ArrayList<Books> books,RecyclerView recyclerView) {
        this.context = context;
        this.books = books;
        this.recyclerView=recyclerView;
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildLayoutPosition(v);
            Books item = books.get(itemPosition);
            Toast.makeText(context, item.getAuthor(), Toast.LENGTH_LONG).show();
        }
    };

    @NonNull
    @Override
    public BooksHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_display_card, parent, false);
        view.setOnClickListener(mOnClickListener);

        return new BooksHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksHolder holder, int position) {
        Books book = books.get(position);
        holder.setDetails(book);
    }





    @Override
    public int getItemCount() {
        return books.size();
    }

    class BooksHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtAuthor;

        BooksHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtAuthor = itemView.findViewById(R.id.txtAuthor);

        }

        void setDetails(Books book) {
            txtName.setText(book.getName());
            txtAuthor.setText(book.getAuthor());

        }
    }
}