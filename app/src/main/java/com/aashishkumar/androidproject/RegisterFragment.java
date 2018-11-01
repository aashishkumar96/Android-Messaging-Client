package com.aashishkumar.androidproject;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aashishkumar.androidproject.model.Credentials;
import com.aashishkumar.androidproject.utils.SendPostAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{

    private OnRegisterFragmentInteractionListener mListener;
    private Credentials mCredentials;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        Button register = (Button) v.findViewById(R.id.registerBtn_register_fragment);
        register.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.registerBtn_register_fragment:
                    attemptRegister(view);
                    break;
                default:
                    Log.wtf("", "Didn't expect to see me...");
            }
        }
    }

    private void attemptRegister(final View theButton) {

        EditText uname = getActivity().findViewById(R.id.unameText_register_fragment);
        EditText fname = getActivity().findViewById(R.id.fnameText_register_fragment);
        EditText lname = getActivity().findViewById(R.id.lnameText_register_fragment);
        EditText regEmail = getActivity().findViewById(R.id.emailText_register_fragment);
        EditText regPass = getActivity().findViewById(R.id.passText_register_fragment);
        EditText retryPass =  getActivity().findViewById(R.id.retypePass_register_fragment);

        boolean hasError = false;
        if (uname.getText().length() == 0) {
            hasError = true;
            uname.setError("Field must not be empty.");
        }

        if (fname.getText().length() == 0) {
            hasError = true;
            fname.setError("Field must not be empty.");
        }

        if (lname.getText().length() == 0) {
            hasError = true;
            lname.setError("Field must not be empty.");
        }

        if (regEmail.getText().length() == 0) {
            hasError = true;
            regEmail.setError("Field must not be empty.");
        }  else if (regEmail.getText().toString().chars().filter(ch ->ch == '@').count() != 1) {
            hasError = true;
            regEmail.setError("Field must contain a valid email address.");
        }

        if (regPass.getText().length() == 0) {
            hasError = true;
            regPass.setError("Field must not be empty.");
        } else if (regPass.getText().length() < 6) {
            hasError = true;
            regPass.setError("Pass must be longer than 6.");
        }

        if (retryPass.getText().length() == 0) {
            hasError = true;
            retryPass.setError("Field must not be empty.");
        } else if (retryPass.getText().length() < 6) {
            hasError = true;
            retryPass.setError("Pass must be longer than 6.");
        } else if (!retryPass.getText().toString().equals(regPass.getText().toString())) {
            hasError = true;
            retryPass.setError("Pass must be matched.");
        }

        if (!hasError) {
            Credentials credentials = new Credentials.Builder(
                    regEmail.getText().toString(),
                    regPass.getText().toString())
                    .addUsername(uname.getText().toString())
                    .addFirstName(fname.getText().toString())
                    .addLastName(lname.getText().toString())
                    .build();

            //build the web service URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_register))
                    .build();

            //build the JSONObject
            JSONObject msg = credentials.asJSONObject();

            mCredentials = credentials;

            //instantiate and execute the AsyncTask.
            //Feel free to add a handler for onPreExecution so that a progress bar
            //is displayed or maybe disable buttons.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleRegisterOnPre)
                    .onPostExecute(this::handleRegisterOnPost)
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
    private void handleRegisterOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleRegisterOnPost(String result) {
        try {

            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");

            mListener.onWaitFragmentInteractionHide();
            if (success) {
                //Register was successful. Inform the Activity so it can do its thing.
                mListener.onRegisterSuccess(mCredentials);
            } else {
                //Login was unsuccessful. Don’t switch fragments and inform the user
                ((TextView) getView().findViewById(R.id.emailText_register_fragment))
                        .setError("Register Unsuccessful");
            }

        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            //or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR",  result
                    + System.lineSeparator()
                    + e.getMessage());

            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.emailText_register_fragment))
                    .setError("Register Unsuccessful");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentInteractionListener) {
            mListener = (OnRegisterFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnRegisterFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnRegisterFragmentInteractionListener
            extends WaitFragment.OnFragmentInteractionListener {
        void onRegisterSuccess(Credentials mCredentials);
    }

}