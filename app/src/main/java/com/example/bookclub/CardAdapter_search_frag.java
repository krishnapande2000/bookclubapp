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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CardAdapter_search_frag extends RecyclerView.Adapter<CardAdapter_search_frag.BooksHolder> {

    private Context context;
    private ArrayList<Books> books;
    private CardAdapter_search_frag bookscardadapter;

    private RecyclerView recyclerView;
    private UserCardAdapter userCardAdapter;
    private ArrayList<User> userArrayList;


    public CardAdapter_search_frag(Context context, ArrayList<Books> books,CardAdapter_search_frag bookscardadapter,RecyclerView recyclerView,UserCardAdapter userCardAdapter,ArrayList<User> userArrayList) {
        this.context = context;
        this.books = books;
        this.recyclerView=recyclerView;
        this.userArrayList=userArrayList;
        this.userCardAdapter=userCardAdapter;
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildLayoutPosition(v);
            Books item = books.get(itemPosition);
            String id=item.getId();

            createListData(id);

            //startActivity(new Intent(this,Search_and_add_books.class));

        }
    };

    private void getBookVal(String userID,Boolean f)
    {
        final DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("Users");
        bookRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("fromfunction", Objects.requireNonNull(dataSnapshot.getValue(User.class)).getName());
                final User b = new User();
                b.setName(Objects.requireNonNull(dataSnapshot.getValue(User.class)).getName());
                b.setPhone(Objects.requireNonNull(dataSnapshot.getValue(User.class)).getPhone());
                b.setAddress(Objects.requireNonNull(dataSnapshot.getValue(User.class)).getAddress());
                userArrayList.add(b);
                userCardAdapter.notifyDataSetChanged();
                //Log.d("fromfunction",b.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void createListData(String book_query) {
        Log.d("seeeeeeeeeeeeeffffffffffffff",book_query);
        DatabaseReference l1ref = FirebaseDatabase.getInstance().getReference("List_of_lenders").child(book_query);
        l1ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        String uID= dataSnapshot1.getKey();
                        Log.d("seeeeeeeeeeeee",uID);
                        getBookVal(uID,dataSnapshot1.child("available").getValue(Boolean.class));

                    }
                    //Log.d("just","hrgaegg");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
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