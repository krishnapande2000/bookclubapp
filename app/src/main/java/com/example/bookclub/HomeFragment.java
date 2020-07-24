package com.example.bookclub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpCookie;
import java.util.Objects;

import static android.app.ProgressDialog.show;

public class HomeFragment extends Fragment {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    String userID;
    private Toolbar toolbar;

    String curr_name, curr_address, curr_phone, curr_email;


    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        Button signout;
        View rootView = inflater.inflate(R.layout.fragment_home, null);
        signout = rootView.findViewById(R.id.signout);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
         toolbar = rootView.findViewById(R.id.toolbar);




        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        final FirebaseUser mUser = mFirebaseAuth.getCurrentUser();
        userID = mUser.getUid();
        Log.d("saw", userID);
        final DatabaseReference dbref = mFirebaseDatabase.getReference(mFirebaseAuth.getUid());

        final EditText Disp_Name = rootView.findViewById(R.id.DisplayName);
        final EditText Disp_Phone = rootView.findViewById(R.id.DisplayPhone);
        final EditText Disp_Address = rootView.findViewById(R.id.DisplayAddress);
        final EditText Disp_Email = rootView.findViewById(R.id.DisplayEmail);

        ImageButton editButton = rootView.findViewById(R.id.editButton);
        final Button saveButton = rootView.findViewById(R.id.saveButton);
        final Button cancelButton = rootView.findViewById(R.id.cancelButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uName, uPhone, uAddress, uEmail;
                uName = Disp_Name.getText().toString();
                //Log.d("SEE THISSSSSSSSSSSSSSSSSS",uName);
                uPhone = Disp_Phone.getText().toString();
                uAddress = Disp_Address.getText().toString();
                uEmail = Disp_Email.getText().toString();
                final User new_user = new User();
                new_user.setPhone(uPhone);
                new_user.setAddress(uAddress);
                new_user.setName(uName);
                new_user.setEmail(uEmail);

                if (uName.isEmpty() || uEmail.isEmpty() || uPhone.isEmpty() || uAddress.isEmpty()) {
                    Toast.makeText(getActivity(), "Fields are Empty !", Toast.LENGTH_SHORT).show();
                } else {
                    mUser.updateEmail(uEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                FirebaseDatabase.getInstance().getReference().child("Users")
                                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                        .setValue(new_user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Saved!!!", Toast.LENGTH_SHORT).show();
                                            Disp_Name.setFocusable(false);
                                            Disp_Address.setFocusable(false);
                                            Disp_Phone.setFocusable(false);
                                            saveButton.setVisibility(View.GONE);
                                            cancelButton.setVisibility(View.GONE);
                                        } else {
                                            Toast.makeText(getActivity(), "Error while Updating Database", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getActivity(), "Error while Updating Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disp_Name.setFocusableInTouchMode(true);
                Disp_Address.setFocusableInTouchMode(true);
                Disp_Phone.setFocusableInTouchMode(true);
                saveButton.setVisibility(View.VISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disp_Name.setText(curr_name);
                Disp_Email.setText(curr_email);
                Disp_Address.setText(curr_address);
                Disp_Phone.setText(curr_phone);
                Disp_Name.setFocusable(false);
                Disp_Address.setFocusable(false);
                Disp_Phone.setFocusable(false);
                saveButton.setVisibility(View.GONE);
                cancelButton.setVisibility(View.GONE);
            }
        });

        myRef.child(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                curr_name = Objects.requireNonNull(dataSnapshot.getValue(User.class)).getName();
                Disp_Name.setText(curr_name);
                curr_address = Objects.requireNonNull(dataSnapshot.getValue(User.class)).getAddress();
                Disp_Address.setText(curr_address);
                curr_phone = Objects.requireNonNull(dataSnapshot.getValue(User.class)).getPhone();
                Disp_Phone.setText(curr_phone);
                curr_email = Objects.requireNonNull(dataSnapshot.getValue(User.class)).getEmail();
                Disp_Email.setText(curr_email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return rootView;


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.app_bar_menu, menu) ;


    }

    @Override
    public void onStart() {
        super.onStart();
        // mFirebaseAuth=FirebaseAuth.getInstance();
        // mFirebaseAuth.addAuthStateListener(mAuthStateListener);

    }


    private void signOut() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseAuth.signOut();
        startActivity(new Intent(getActivity(), EmailPasswordActivity.class));
        getActivity().finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        // mFirebaseAuth=FirebaseAuth.getInstance();
        // mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }
}
