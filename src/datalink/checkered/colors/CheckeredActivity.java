package datalink.checkered.colors;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class CheckeredActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkered);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new FiveSquaresFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.checkered, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) 
        {
            // DialogFragment.show() will take care of adding the fragment
            // in a transaction.  We also want to remove any currently showing
            // dialog, so make our own transaction and take care of that here.
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            DialogFragment newFragment = new AboutDialogFragment();
            newFragment.show(ft, "dialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class FiveSquaresFragment extends Fragment
    {
        public FiveSquaresFragment() 
        {
        }
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            mFragmentRoot = inflater.inflate(R.layout.fragment_checkered, container, false);
            mSeeker = (ColorSeeker) getActivity().findViewById(R.id.colorSeeker);
            mSeeker.attachSquares(mFragmentRoot);
            return mFragmentRoot;
        }
        
        View mFragmentRoot;
        ColorSeeker mSeeker;
    }
    
    public static class AboutDialogFragment extends DialogFragment 
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            final String[] moreInfoUrls = getResources().getStringArray(R.array.more_info_urls);
            
            builder.setTitle(R.string.about_text)
                   .setItems(R.array.more_info, new DialogInterface.OnClickListener()
                   {
                       public void onClick(DialogInterface dialog, int item) {
                           // Do something with the selection
                           String url = moreInfoUrls[item];
                           Intent i = new Intent(Intent.ACTION_VIEW);
                           i.setData(Uri.parse(url)); 
                           startActivity(i); 
                      }
                   })
                   .setNegativeButton(R.string.action_not_now, new DialogInterface.OnClickListener()
                   {
                       public void onClick(DialogInterface dialog, int id) {
                           // FIRE ZE MISSILES!
                       }
                   })
                   ;
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}
