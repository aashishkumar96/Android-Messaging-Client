//package com.aashishkumar.androidproject;
//
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link Verification.OnVerificationFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link Verification#newInstance} factory method to
// * create an instance of this fragment.
// */
//
//public class Verification extends Fragment {
//
//
//
//
//
//        // TODO: Rename parameter arguments, choose names that match
//        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//        private static final String ARG_PARAM1 = "param1";
//        private static final String ARG_PARAM2 = "param2";
//
//        // TODO: Rename and change types of parameters
//        private String mParam1;
//        private String mParam2;
//
//        private OnVerificationFragmentInteractionListener mListener;
//
//        public Verification() {
//            // Required empty public constructor
//        }
//
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment VerificationCode.
//         */
//        // TODO: Rename and change types and number of parameters
//        public static Verification newInstance(String param1, String param2) {
//            Verification fragment = new Verification();
//            Bundle args = new Bundle();
//            args.putString(ARG_PARAM1, param1);
//            args.putString(ARG_PARAM2, param2);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            if (getArguments() != null) {
//                mParam1 = getArguments().getString(ARG_PARAM1);
//                mParam2 = getArguments().getString(ARG_PARAM2);
//            }
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            // Inflate the layout for this fragment
//            return inflater.inflate(R.layout.fragment_verification, container, false);
//        }
//
//        // TODO: Rename method, update argument and hook method into UI event
//        public void onButtonPressed(Uri uri) {
//            if (mListener != null) {
//                mListener.onFragmentInteraction(uri);
//            }
//        }
//
//        public void onClick(View view) {
//            if (mListener != null) {
//                switch (view.getId()) {
//                    case R.id.verify_button:
//                        attemptRegister(view);
//                        break;
//                    default:
//                        Log.wtf("", "Didn't expect to see me...");
//                }
//            }
//        }
//
//        private void attemptRegister(View view) {
//        }
///*
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnVerificationFragmentInteractionListener) {
//            mListener = (OnVerificationFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnVerificationFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//*/
//        /**
//         * This interface must be implemented by activities that contain this
//         * fragment to allow an interaction in this fragment to be communicated
//         * to the activity and potentially other fragments contained in that
//         * activity.
//         * <p>
//         * See the Android Training lesson <a href=
//         * "http://developer.android.com/training/basics/fragments/communicating.html"
//         * >Communicating with Other Fragments</a> for more information.
//         */
//        public interface OnVerificationFragmentInteractionListener {
//            // TODO: Update argument type and name
//            void onFragmentInteraction(Uri uri);
//        }
//    }





/**   DON"T REMOVE ABOVE COMMENTED CODE, WILL REMOVE BEFORE FINAL VERSION     */


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
 * Activities that contain this fragment must implement the
 * {@link OnVerificationFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Verification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Verification extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnVerificationFragmentInteractionListener mListener;
    private Credentials myCredentials;
    private EditText mCode;


    public Verification() {
        // Required empty public constructor
    }

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

    /*
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    */

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
        mCode = view.findViewById(R.id.enter_verification_code_fragment);
        TextView email = view.findViewById(R.id.emailText_register_fragment);
        email.setText(myCredentials.getEmail());
        Button b = view.findViewById(R.id.verify_button);
        b.setOnClickListener(this::attemptVerification);
        b = view.findViewById(R.id.resend_email_button);
        b.setOnClickListener(this::reSendEmail);
        return view;

        // return inflater.inflate(R.layout.fragment_verification, container, false);
    }


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

    private void attemptVerification(View view) {

        EditText vcode = getActivity().findViewById(R.id.enter_verification_code_fragment);

        boolean hasError = false;

        if(vcode.getText().length() == 0) {
            hasError = true;
            vcode.setError("Error. Code can't be left empty.");
        } else if(vcode.getText().length() != 4) {
            hasError = true;
            vcode.setError("Error. Check your code.");
        }

        if(!hasError) {
           // Uri verifyURI = this.duc.getVerifyEndPointURI();

            Uri uri = Uri.parse("https://group-project-450.herokuapp.com/verfication");


            JSONObject msg = new JSONObject();
            try {
                msg.put("email", myCredentials.getEmail());
                msg.put("inputToken",
                        Integer.parseInt(mCode.getText().toString()));
                System.out.println(msg.toString());
            } catch (JSONException e) {
                Log.wtf("CREDENTIALS", "Error creating JSON: " +
                        e.getMessage());
            }
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleVerifyOnPre)
                    .onPostExecute(this::handleVerifyOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
        }

//        if (!hasError) {
//            Credentials credentials = new Credentials.Builder(
//                    vcode.getText().toString(),
//                    .addUsername(vcode.getText().toString())
////                    .addFirstName(fname.getText().toString())
////                    .addLastName(lname.getText().toString())
//                    .build();

            //build the web service URL
//            Uri uri = new Uri.Builder()
//                    .scheme("https")
//                    .appendPath(getString(R.string.ep_base_url))
//                    .appendPath(getString(R.string.ep_register))
//                    .build();

            //build the JSONObject
//            JSONObject msg = credentials.asJSONObject();
//
//            myCredentials = credentials;
//
//            //instantiate and execute the AsyncTask.
//            //Feel free to add a handler for onPreExecution so that a progress bar
//            //is displayed or maybe disable buttons.
//            new SendPostAsyncTask.Builder(uri.toString(), msg)
//                    .onPreExecute(this::handleVerifyOnPre)
//                    .onPostExecute(this::handleVerifyOnPost)
//                    .onCancelled(this::handleErrorsInTask)
//                    .build().execute();
//        }

    }

    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR", result);
    }

    private void handleVerifyOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }


    private void handleVerifyOnPost(String result) {

        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            String verificationStatus = result;  //getInt("status");

            if (verificationStatus.equals("Verification Number Matched")) { // success
                mListener.onWaitFragmentInteractionHide();
                mListener.verifiedUserSendToSuccess(myCredentials);
            } else if (verificationStatus.equals("Verification Number Not Matched")) { // Wrong Credentials
                mListener.onWaitFragmentInteractionHide();
                ((TextView)
                        getView().findViewById(R.id.enter_verification_code_fragment))
//                        .setError(getString(R.string.invalid_code));
                        .setError("Error");

            } else { //Endpoint Error
                mListener.onWaitFragmentInteractionHide();
                ((TextView)
                        getView().findViewById(R.id.enter_verification_code_fragment))
//                        .setError(getString(R.string.verification_fail
//                        ));
                        .setError("Error");
            }
        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView)
                    getView().findViewById(R.id.enter_verification_code_fragment))
                    .setError("Registration Unsuccessful");
        }
    }


    private void reSendEmail(View view) {
//        Uri resendEmail = this.duc.getResendEndPointURI();

        Uri uri = Uri.parse("https://group-project-450.herokuapp.com/verfication");

        JSONObject msg = new JSONObject();
        try {
            msg.put("email", myCredentials.getEmail());
//            msg.put("nickname", myCredentials.getNickName());
        }catch (JSONException e) {
            Log.wtf("CREDENTIALS", "Error creating JSON: " + e.getMessage());
        }
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::handleVerifyOnPre)
                .onPostExecute(this::handleResendEmailOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }

    private void handleResendEmailOnPost(String result) {

        try {

            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            String verificationStatus = result;

            if (verificationStatus.equals("Verification Number Matched")) { // success
                mListener.onWaitFragmentInteractionHide();
                mListener.verifiedUserSendToSuccess(myCredentials);
            } else if (verificationStatus.equals("Verification Number Not Matched")) { // Wrong Credentials
                mListener.onWaitFragmentInteractionHide();
                ((TextView)
                        getView().findViewById(R.id.enter_verification_code_fragment))
//                        .setError(getString(R.string.invalid_code));
                        .setError("Well something is wrong..");

            }

        } catch(JSONException e){
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.enter_verification_code_fragment))
                    .setError("Unsuccessful");

        }
    }

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
        void onFragmentInteraction(Uri uri);
        void verifiedUserSendToSuccess(Credentials myCredentials);
    }
}

