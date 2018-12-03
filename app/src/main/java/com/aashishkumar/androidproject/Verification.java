


/**   DON"T REMOVE ABOVE COMMENTED CODE, WILL REMOVE BEFORE FINAL VERSION     */


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


//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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
//        b.setOnClickListener(this::attemptVerificationHCode);
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

//    private void attemptVerification(View view) {
//
//        EditText vcode = getActivity().findViewById(R.id.enter_verification_code_fragment);
//
//        boolean hasError = false;
//
//        if(vcode.getText().length() == 0) {
//            hasError = true;
//            vcode.setError("Error. Code can't be left empty.");
//        } else if(vcode.getText().length() != 4) {
//            hasError = true;
//            vcode.setError("Error. Check your code.");
//        }
//
////        if(!hasError) {
////           // Uri verifyURI = this.duc.getVerifyEndPointURI();
////
////            Uri uri = Uri.parse("https://group-project-450.herokuapp.com/verfication");
////
////
////            JSONObject msg = new JSONObject();
////            try {
////                msg.put("email", myCredentials.getEmail());
////                msg.put("verficationCode",
////                        Integer.parseInt(mCode.getText().toString()));
////                System.out.println(msg.toString());
////            } catch (JSONException e) {
////                Log.wtf("CREDENTIALS", "Error creating JSON: " +
////                        e.getMessage());
////            }
////            new SendPostAsyncTask.Builder(uri.toString(), msg)
////                    .onPreExecute(this::handleVerifyOnPre)
////                    .onPostExecute(this::handleVerifyOnPost)
////                    .onCancelled(this::handleErrorsInTask)
////                    .build().execute();
////        }
//
//
//
//
////        if (!hasError) {
////            Credentials credentials = new Credentials.Builder(
////                    vcode.getText().toString(),
////                    .addUsername(vcode.getText().toString())
//////                    .addFirstName(fname.getText().toString())
//////                    .addLastName(lname.getText().toString())
////                    .build();
//
//            //build the web service URL
////            Uri uri = new Uri.Builder()
////                    .scheme("https")
////                    .appendPath(getString(R.string.ep_base_url))
////                    .appendPath(getString(R.string.ep_register))
////                    .build();
//
//            //build the JSONObject
////            JSONObject msg = credentials.asJSONObject();
////
////            myCredentials = credentials;
////
////            //instantiate and execute the AsyncTask.
////            //Feel free to add a handler for onPreExecution so that a progress bar
////            //is displayed or maybe disable buttons.
////            new SendPostAsyncTask.Builder(uri.toString(), msg)
////                    .onPreExecute(this::handleVerifyOnPre)
////                    .onPostExecute(this::handleVerifyOnPost)
////                    .onCancelled(this::handleErrorsInTask)
////                    .build().execute();
////        }
//
//
//
//
//        EditText uname = getActivity().findViewById(R.id.unameText_register_fragment);
//        EditText fname = getActivity().findViewById(R.id.fnameText_register_fragment);
//        EditText lname = getActivity().findViewById(R.id.lnameText_register_fragment);
//        EditText regEmail = getActivity().findViewById(R.id.emailText_register_fragment);
//        EditText regPass = getActivity().findViewById(R.id.passText_register_fragment);
//
//
//        if (!hasError) {
//            Credentials credentials = new Credentials.Builder(
//                    regEmail.getText().toString(),
//                    regPass.getText().toString())
//                    vcode.getText().toString())
//                    .addUsername(uname.getText().toString())
//                    .addFirstName(fname.getText().toString())
//                    .addLastName(lname.getText().toString())
//                    .build();
//
//            //build the web service URL
////            Uri uri = new Uri.Builder()
////                    .scheme("https")
////                    .appendPath(getString(R.string.ep_base_url))
////                    .appendPath(getString(R.string.ep_register))
////                    .build();
//
////            Uri uri = Uri.parse("https://group-project-450.herokuapp.com/register");
//            Uri uri = Uri.parse("https://group-project-450.herokuapp.com/verfication");
//
//            //build the JSONObject
//            JSONObject msg = credentials.asJSONObject();
//
//            myCredentials = credentials;
//
//            //instantiate and execute the AsyncTask.
//            //Feel free to add a handler for onPreExecution so that a progress bar
//            //is displayed or maybe disable buttons.
//            System.out.println(uri.toString());
//            new SendPostAsyncTask.Builder(uri.toString(), msg)
//                    .onPreExecute(this::handleVerifyOnPre)
//                    .onPostExecute(this::handleVerifyOnPost)
//                    .onCancelled(this::handleErrorsInTask)
//                    .build().execute();
//        }
//
//    }


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


    private void attemptVerificationHCode(View view) {
        EditText uname = getActivity().findViewById(R.id.unameText_register_fragment);
        EditText fname = getActivity().findViewById(R.id.fnameText_register_fragment);
        EditText lname = getActivity().findViewById(R.id.lnameText_register_fragment);
        EditText regEmail = getActivity().findViewById(R.id.emailText_register_fragment);
        EditText regPass = getActivity().findViewById(R.id.passText_register_fragment);

        boolean hasError = false;

        if (!hasError) {
            Credentials credentials = new Credentials.Builder(
                    regEmail.getText().toString(),
                    regPass.getText().toString(),
                    mCode.getText().toString())
                    .addUsername(uname.getText().toString())
                    .addFirstName(fname.getText().toString())
                    .addLastName(lname.getText().toString())
                    .addCode(mCode.getText().toString())
                    .build();

            //build the web service URL
//            Uri uri = new Uri.Builder()
//                    .scheme("https")
//                    .appendPath(getString(R.string.ep_base_url))
//                    .appendPath(getString(R.string.ep_register))
//                    .build();

            Uri uri = Uri.parse("https://group-project-450.herokuapp.com/verification");

            //build the JSONObject
            JSONObject msg = credentials.asJSONObject();

            myCredentials = credentials;

            //instantiate and execute the AsyncTask.
            //Feel free to add a handler for onPreExecution so that a progress bar
            //is displayed or maybe disable buttons.
            System.out.println(uri.toString());
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleVerifyOnPre)
                    .onPostExecute(this::handleVerifyOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
        }
    }

    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR", result);
    }

    private void handleVerifyOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }


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

//        catch (JSONException e) {
//            Log.e("JSON_PARSE_ERROR", result
//                    + System.lineSeparator()
//                    + e.getMessage());
//            mListener.onWaitFragmentInteractionHide();
//            ((TextView)
//                    getView().findViewById(R.id.enter_verification_code_fragment))
//                    .setError("Registration Unsuccessful");
//        }
//   }


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
                mListener.verifiedUserSendToSuccess(registeredEmail);
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
//        void onFragmentInteraction(Uri uri);
        void verifiedUserSendToSuccess(String email);
    }
}

