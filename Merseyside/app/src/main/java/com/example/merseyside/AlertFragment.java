package com.example.merseyside;
//Menu.Reminder/Alert
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class AlertFragment extends Fragment implements View.OnClickListener {
    private int nortificationId = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reminder,
                container, false);
        Button button = (Button) view.findViewById(R.id.setBtn);
        Button button2 = (Button) view.findViewById(R.id.cancelBtn);
        return view;
    }
    public void onClick(View view){

    }
}

