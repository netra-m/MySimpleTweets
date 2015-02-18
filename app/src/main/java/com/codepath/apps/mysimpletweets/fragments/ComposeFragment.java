package com.codepath.apps.mysimpletweets.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.ComposeCallBack;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

/**
 * Created by netram on 2/9/15.
 */
public class ComposeFragment extends DialogFragment {
    private ComposeCallBack composeCallBack;
    private EditText etCompose;

    public ComposeFragment() {

    }

    public static ComposeFragment newInstance(String titleOfForm) {
        ComposeFragment fragment = new ComposeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        composeCallBack = (ComposeCallBack) a;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Holo_Dialog_NoActionBar);

        final View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_compose, null);

        dialog.getWindow().setContentView(view);

        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.TOP;

        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_compose, container);
        getDialog().setCanceledOnTouchOutside(true);

        User currentUser = (User) this.getArguments().get("user");


        TextView tvComposeUserName = (TextView) view.findViewById(R.id.tvComposeUserName);
        final TextView tvComposeNumChars = (TextView) view.findViewById(R.id.tvComposeNumChars);
        ImageView ivComposeImage = (ImageView) view.findViewById(R.id.ivComposeImage);
        etCompose = (EditText) view.findViewById(R.id.etCompose);
        TextView tvComposeScreenName = (TextView) view.findViewById(R.id.tvComposeScreenName);
        Button btnTweet = (Button) view.findViewById(R.id.btTweet);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTweet(v);
                dismiss();
            }
        });

        tvComposeUserName.setText(currentUser.getName());
        tvComposeScreenName.setText("@" + currentUser.getScreenName());
        ivComposeImage.setImageResource(0);

        Picasso.with(getActivity()).load(currentUser.getProfileImageUrl()).into(ivComposeImage);

        TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tvComposeNumChars.setText("140");
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvComposeNumChars.setText(String.valueOf(140 - s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };
        etCompose.addTextChangedListener(mTextEditorWatcher);

        if( this.getArguments().get("replyTo") != null )
        {
            String replyTo = "@" + (String) this.getArguments().get("replyTo");
            etCompose.setText(replyTo);
            etCompose.setSelection(etCompose.getText().length());
        }

        return view;
    }

    public void onTweet(View view) {

        composeCallBack.onCompose(etCompose.getText().toString());

    }
}
