package com.example.bookclub;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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

import static androidx.core.content.ContextCompat.getSystemService;

public class SearchFragment extends Fragment {


    private RecyclerView recyclerViewbooks;
    private RecyclerView recyclerViewusers;
    private FloatingActionButton floatingActionButton;

    private UserCardAdapter useradapter;
    private ArrayList<User> UserArrayList;
    private CardAdapter_search_frag bookAdapter;
    private ArrayList<Books> booksArrayList;
    private EditText search_query;
    String book_query;
    Button search_button;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, null);

        super.onCreate(savedInstanceState);

        recyclerViewbooks = rootView.findViewById(R.id.recyclerViewBooks);
        recyclerViewbooks.setLayoutManager(new LinearLayoutManager(getActivity()));
        booksArrayList = new ArrayList<>();
        bookAdapter = new CardAdapter_search_frag(getActivity(),booksArrayList,bookAdapter,recyclerViewbooks,useradapter,UserArrayList);
        recyclerViewbooks.setAdapter(bookAdapter);



        recyclerViewusers = rootView.findViewById(R.id.recyclerViewUsers);
        recyclerViewusers.setLayoutManager(new LinearLayoutManager(getActivity()));
        UserArrayList = new ArrayList<>();
        useradapter = new UserCardAdapter(getActivity(),UserArrayList);
        recyclerViewusers.setAdapter(useradapter);

        search_query= rootView.findViewById(R.id.SearchedNo);
        search_button = rootView.findViewById(R.id.search_button);
        // recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book_query = search_query.getText().toString();
                booksArrayList.clear();
                new FetchBook( search_query,booksArrayList,bookAdapter,2).execute(book_query);
            }
        });


        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("Recycler View with Card View");

//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), Search_and_add_books.class));
//            }
//        });

        return rootView;
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




