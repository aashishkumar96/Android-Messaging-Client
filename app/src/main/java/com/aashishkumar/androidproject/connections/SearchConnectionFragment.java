package com.aashishkumar.androidproject.connections;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aashishkumar.androidproject.R;
import com.aashishkumar.androidproject.WaitFragment;
import com.aashishkumar.androidproject.utils.SendPostAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSearchConnetionFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchConnectionFragment extends Fragment implements View.OnClickListener {

    private OnSearchConnetionFragmentInteractionListener mListener;

    public SearchConnectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search_connection, container, false);

        Button search = (Button) v.findViewById(R.id.button_search_searchFragment);
        search.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.button_search_searchFragment:
                    attemptSearch(view);
                    break;
                default:
                    Log.wtf("", "Didn't expect to see me...");
            }
        }
    }

    /**
     * Helper method to do the search
     * @param theButton
     */
    private void attemptSearch(final View theButton) {
        EditText usernameInput = getActivity().findViewById(R.id.text_search_searchFragment);

        boolean hasError = false;
        if (usernameInput.getText().length() == 0) {
            hasError = true;
            usernameInput.setError("Field must not be empty.");
        }

        if (!hasError) {
            String username = usernameInput.getText().toString();

            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_connections))
                    .appendPath(getString(R.string.ep_search))
                    .build();

            JSONObject msg = new JSONObject();
            try {
                msg.put("search", username);

            } catch (JSONException e) {
                Log.wtf("ERROR! ", e.getMessage());
            }

            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleSearchOnPre)
                    .onPostExecute(this::handleSearchOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
        }
    }

    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR",  result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleSearchOnPre() {
        onWaitFragmentInteractionShow();
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleSearchOnPost(String result) {
        try {
            JSONObject root = new JSONObject(result);
            JSONArray data = root.getJSONArray("result");

            List<Connection> connections = new ArrayList<>();

            if (data.length() == 0) {
                Log.e("ERROR", "no connections found");
                Toast.makeText(getActivity(), "Cannot find this connection!", Toast.LENGTH_SHORT).show();
                ((TextView) getView().findViewById(R.id.text_search_searchFragment))
                        .setError("Connection does not exist");
                onWaitFragmentInteractionHide();
            } else {

                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonConnection = data.getJSONObject(i);
                    connections.add(new Connection.Builder(jsonConnection.getString("username"))
                            .addFirstName(jsonConnection.getString("firstname"))
                            .addLastName(jsonConnection.getString("lastname"))
                            .addID(jsonConnection.getInt("memberid"))
                            .build());
                }

                Connection[] connectionsArray = new Connection[connections.size()];
                connectionsArray = connections.toArray(connectionsArray);

                Bundle args = new Bundle();
                args.putSerializable(SearchResultFragment.ARG_SEARCH_LIST, connectionsArray);
                Fragment frag = new SearchResultFragment();
                frag.setArguments(args);
                onWaitFragmentInteractionHide();
                loadFragment(frag);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }

    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_home, frag)
                .addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchConnetionFragmentInteractionListener) {
            mListener = (OnSearchConnetionFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchConnetionFragmentInteractionListener");
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
    public interface OnSearchConnetionFragmentInteractionListener {
        void onFragmentInteraction();
    }

    public void onWaitFragmentInteractionShow() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.activity_home, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    public void onWaitFragmentInteractionHide() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(getActivity().getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }
}
