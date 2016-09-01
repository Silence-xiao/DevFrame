package hui.devframe.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hui.devframe.R;
import hui.devframe.fragment.Fragment1;
import hui.devframe.fragment.Fragment2;


/**
 * main fragment
 */
public class FragmentMain extends Fragment {

    public FragmentMain() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_layout, container, false);
        v.findViewById(R.id.main_button_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_fragment_container, new Fragment1());
                ft.commit();
            }
        });

        v.findViewById(R.id.main_button_two).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_fragment_container, new Fragment2());
                ft.commit();
            }
        });
        return v;
    }
}
