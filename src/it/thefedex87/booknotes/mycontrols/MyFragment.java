package it.thefedex87.booknotes.mycontrols;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by federico.creti on 08/04/2015.
 */
public class MyFragment extends Fragment {
	
    public static MyFragment getInstance(int position) {
    	MyFragment myFragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = null;
        /*textView = (TextView) layout.findViewById(R.id.position);*/
        Bundle bundle = getArguments();
        if (bundle != null) {
            //textView.setText("The page selected is " + bundle.getInt("position"));
        	switch (bundle.getInt("position")) {
        		case 0:
        			//layout = inflater.inflate(R.layout.tab_note_layout, container, false);
        			break;
        			
        		case 1:
        			//layout = inflater.inflate(R.layout.tab_note_gallery, container, false);
        			break;
        	}
        }
        //layout.inflate(this, R.layout.tab_note_layout, null);
        return layout;
    }
}
