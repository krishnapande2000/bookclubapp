package com.example.bookclub;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DashBoardFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    private CardAdapter adapter;
    private ArrayList<Books> booksArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_dashboard,null);

        super.onCreate(savedInstanceState);

        recyclerView =  rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        booksArrayList = new ArrayList<>();
        adapter = new CardAdapter(getActivity(), booksArrayList,recyclerView);
        recyclerView.setAdapter(adapter);
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        createListData();

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Recycler View with Card View");
        floatingActionButton = rootView.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booksArrayList =new ArrayList<>();
                startActivity(new Intent(getActivity(),Search_and_add_books.class));
            }
        });

        return rootView;
    }

    private void getBookVal(String bID,Boolean f)
    {
        final DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("Books");
        bookRef.child(bID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Log.d("fromfunction",dataSnapshot.getValue(Books.class).getName());
                final Books b = new Books();
                b.setName(dataSnapshot.getValue(Books.class).getName());
                b.setAuthor(dataSnapshot.getValue(Books.class).getAuthor());
                booksArrayList.add(b);
                adapter.notifyDataSetChanged();
                //Log.d("fromfunction",b.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    private void createListData() {
        FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference bookRef = FirebaseDatabase.getInstance().getReference().child("Books");


        DatabaseReference l1ref = FirebaseDatabase.getInstance().getReference("books_for_rent").child(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid());

        l1ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Books b = new Books();
                        String bID= dataSnapshot1.getKey();
                        //Log.d("seeeeeeeeeeeee",bID);
                        getBookVal(bID,dataSnapshot1.child("available").getValue(Boolean.class));

                    }
                    //Log.d("just","hrgaegg");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                getActivity().finish();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}


