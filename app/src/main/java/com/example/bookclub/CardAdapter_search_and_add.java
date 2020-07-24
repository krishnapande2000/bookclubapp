package com.example.bookclub;



import android.content.Context;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.view.View.GONE;

public class CardAdapter_search_and_add extends RecyclerView.Adapter<CardAdapter_search_and_add.BooksHolder> {

    private Context context;
    private ArrayList<Books> books;
    private RecyclerView recyclerView;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    String userID;


    public CardAdapter_search_and_add(Context context, ArrayList<Books> books,RecyclerView recyclerView) {
        this.context = context;
        this.books = books;
        this.recyclerView=recyclerView;
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildLayoutPosition(v);
            Books item = books.get(itemPosition);
            String id=item.getId();
            String bookname=item.getName();
            String author=item.getAuthor();

            //recyclerView.setVisibility(v,GONE);

            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            myRef = FirebaseDatabase.getInstance().getReference().child("Users");
            final FirebaseUser mUser = mFirebaseAuth.getCurrentUser();
            userID = mUser.getUid();
            Log.d("saw", userID);
            final DatabaseReference dbref = mFirebaseDatabase.getReference(mFirebaseAuth.getUid());

            //adding book to users list..
            DatabaseReference d1ref = mFirebaseDatabase.getReference().child("books_for_rent").child(mFirebaseAuth.getUid());
            Map<String, Object> userUpdates = new HashMap<>();
            String idslashava= id + "/available";
            userUpdates.put(idslashava, true);
            d1ref.updateChildren(userUpdates);


            //adding user to books list
            DatabaseReference d2ref = mFirebaseDatabase.getReference().child("List_of_lenders").child(id);
            Map<String, Object> bookUpdates = new HashMap<>();
            idslashava= userID + "/available";
            bookUpdates.put(idslashava, true);
            d2ref.updateChildren(bookUpdates);


            //adding book details to our personal list of books...
            d2ref = mFirebaseDatabase.getReference().child("Books");
            bookUpdates = new HashMap<>();
            idslashava= id + "/name";
            bookUpdates.put(idslashava, bookname);
            idslashava = id + "/author";
            bookUpdates.put(idslashava,author);
            d2ref.updateChildren(bookUpdates);

            MainScreens obj = new MainScreens();
            obj.loadFragment(new DashBoardFragment());

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