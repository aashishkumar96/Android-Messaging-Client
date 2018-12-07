
package com.aashishkumar.androidproject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aashishkumar.androidproject.model.Credentials;
import com.aashishkumar.androidproject.utils.SendPostAsyncTask;


import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnVerificationFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Verification#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author  Aayush Shah
 */
public class Verification extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "tag";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnVerificationFragmentInteractionListener mListener;
    private Credentials myCredentials;
    private EditText mCode;

    private String registeredEmail;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VerificationCode.
     */
    // TODO: Rename and change types and number of parameters
    public static Verification newInstance(String param1, String param2) {
        Verification fragment = new Verification();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verification, container, false);
        Bundle bundle=getArguments();
        registeredEmail=String.valueOf(bundle.getString("email"));

        mCode = (EditText) view.findViewById(R.id.enter_verification_code_fragment);
        //TextView email = view.findViewById(R.id.emailText_register_fragment);
        //email.setText(myCredentials.getEmail());
        Button b = view.findViewById(R.id.verify_button);
        b.setOnClickListener(this::attemptVerification);
        b = view.findViewById(R.id.resend_email_button);
 //       b.setOnClickListener(this::reSendEmail);
        return view;

        // return inflater.inflate(R.layout.fragment_verification, container, false);
    }

    /**
     * When button is clicked.
     * @param view The view
     */
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.verify_button:
                    attemptVerification(view);
                    break;
                default:
                    Log.wtf("", "Didn't expect to see me...");
            }
        }
    }

    /**
     * When user tries to verify.
     * @param view The view.
     */
    private void attemptVerification(View view) {

        Uri verifyURI = Uri.parse("https://group-project-450.herokuapp.com/verification");
        Integer mCode1 = Integer.parseInt(mCode.getText().toString());

        JSONObject msg = new JSONObject();
        try {
            msg.put("email", registeredEmail);
            Toast.makeText(getContext(), registeredEmail+" "+mCode1, Toast.LENGTH_SHORT).show();
//            msg.put("email", myCredentials.getEmail());
            msg.put("verificationCode", mCode1);
            System.out.println(msg.toString());
        } catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        new SendPostAsyncTask.Builder(verifyURI.toString(), msg)
                .onPreExecute(this::handleVerifyOnPre)
                .onPostExecute(this::handleVerifyOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }


    
    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR", result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleVerifyOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleVerifyOnPost(String result) {

        Log.d("JSON result", result);
        try {

          //  JSONObject resultsJSON = new JSONObject(result);
            String verificationStatus = result;

            if (verificationStatus.compareTo("Verification Number Matched")==0) {
                mListener.onWaitFragmentInteractionHide();
                // mListener.verifiedUserSendToSuccess(registeredEmail);
                mListener.verifiedUserSendToSuccess(registeredEmail);
            }
//            } else if (verificationStatus.equals("Verification Failed")) { // Wrong Credentials
//                mListener.onWaitFragmentInteractionHide();
//                ((TextView)
//                        getView().findViewById(R.id.enter_verification_code_fragment))
//                        .setError(getString(R.string.invalid_code));
//                        .setError("Error");
//

            else {
                Toast.makeText(getContext(),"result" +result, Toast.LENGTH_SHORT).show();
                mListener.onWaitFragmentInteractionHide();
                ((TextView)
                        getView().findViewById(R.id.enter_verification_code_fragment))
//                        .setError(getString(R.string.verification_fail
//                        ));
                        .setError("Error");
            }
        } catch (Exception e) {
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView)
                    getView().findViewById(R.id.enter_verification_code_fragment))
                    .setError("Verification Unsuccessful");
        }
    }



//     private void reSendEmail(View view) {

//         Uri uri = Uri.parse("https://group-project-450.herokuapp.com/verfication");

//         JSONObject msg = new JSONObject();
//         try {
//             msg.put("email", myCredentials.getEmail());
//         }catch (JSONException e) {
//             Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
//         }
//         new SendPostAsyncTask.Builder(uri.toString(), msg)
//                 .onPreExecute(this::handleVerifyOnPre)
//                 .onPostExecute(this::handleResendEmailOnPost)
//                 .onCancelled(this::handleErrorsInTask)
//                 .build().execute();
//     }

//     private void handleResendEmailOnPost(String result) {

//         try {

//             Log.d("JSON result",result);
//             JSONObject resultsJSON = new JSONObject(result);
//             String verificationStatus = result;

//             if (verificationStatus.equals("Verification Number Matched")) { // success
//                 mListener.onWaitFragmentInteractionHide();
//                 mListener.verifiedUserSendToSuccess(registeredEmail);
//             } else if (verificationStatus.equals("Verification Number Not Matched")) { // Wrong Credentials
//                 mListener.onWaitFragmentInteractionHide();
//                 ((TextView)
//                         getView().findViewById(R.id.enter_verification_code_fragment))
// //                        .setError(getString(R.string.invalid_code));
//                         .setError("Well something is wrong..");

//             }

//         } catch(JSONException e){
//             Log.e("JSON_PARSE_ERROR", result
//                     + System.lineSeparator()
//                     + e.getMessage());
//             mListener.onWaitFragmentInteractionHide();
//             ((TextView) getView().findViewById(R.id.enter_verification_code_fragment))
//                     .setError("Unsuccessful");

//         }
//     }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof
                OnVerificationFragmentInteractionListener) {
            mListener = (OnVerificationFragmentInteractionListener)
                    context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVerificationFragmentInteractionListener");
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
    interface OnVerificationFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {
        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
        void verifiedUserSendToSuccess(String email);
    }
}

