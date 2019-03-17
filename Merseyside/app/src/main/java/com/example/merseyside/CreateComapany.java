package com.example.merseyside;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateComapany.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateComapany#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateComapany extends Fragment {

    EditText edit_title,edit_content;
    Button btn_post, btn_update, btn_delete;
    RecyclerView recyclerView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CreateComapany() {
    }


    public static CreateComapany newInstance(String param1, String param2) {
        CreateComapany fragment = new CreateComapany();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<Post> options;
    FirebaseRecyclerAdapter<Post, MyRecycleViewHolder> adapter;

    Post selectedPost;
    String selectedKey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("ancient-flag-234610");

    }

    private void postComment() {
        String title = edit_title.getText().toString();
        String content = edit_content.getText().toString();

        Post post = new Post(title,content);
        databaseReference.push().setValue(post);

        adapter.notifyDataSetChanged();

    }
    private void displayComment(){
        options =
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setQuery(databaseReference,Post.class)
                        .build();
        adapter =
                new FirebaseRecyclerAdapter<Post, MyRecycleViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MyRecycleViewHolder holder, final int position, @NonNull final Post model) {
                        holder.txt_title.setText(model.getTitle());
                        holder.txt_comment.setText(model.getContent());

                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int postion) {
                                selectedPost = model;
                                selectedKey = getSnapshots().getSnapshot(position).getKey();
                                Log.d("KEy Item", "" + selectedKey);

                                edit_content.setText(model.getContent());
                                edit_title.setText(model.getTitle());
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public MyRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View itemView = LayoutInflater.from(getActivity().getBaseContext()).inflate(R.layout.post_item,viewGroup, false);
                        return new MyRecycleViewHolder(itemView);
                    }
                };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_comapany, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        edit_content = view.findViewById(R.id.edt_content);
        edit_title = view.findViewById(R.id.edt_title);
        btn_post = view.findViewById(R.id.btn_post);
        btn_update = view.findViewById(R.id.btn_update);
        btn_delete = view.findViewById(R.id.btn_delete);
        recyclerView = view.findViewById(R.id.recycler);

        //System.out.println("edit_content: " + edit_content);
        //System.out.println("edit_title: " + edit_title);
        //System.out.println("btn_post: " + btn_post);
        //System.out.println("recyclerView: " + recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedKey != null) {
                    databaseReference.child(selectedKey).setValue(new Post(edit_title.getText().toString(),
                            edit_content.getText().toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Updated !", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedKey != null) {
                    databaseReference.child(selectedKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Updated !", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        displayComment();
    }

    public void onStop(){
        if(adapter != null)
            adapter.stopListening();
        super.onStop();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
