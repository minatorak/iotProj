package com.example.sirirak.iotproj;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by sirirak on 14-Nov-17.
 */

public class AwesomeDialogFragment extends DialogFragment {
    private static final String KEY_MESSAGE = "key_message";
    private static final String KEY_POSITIVE = "key_positive";
    private static final String KEY_NEGATIVE = "key_negative";

    private TextView tvMessage;
    private Button btnPositive;
    private Button btnNegative;

    private String message;
    private String positive;
    private String negative;

    public static AwesomeDialogFragment newInstance( String message, String positive, String negative) {
        AwesomeDialogFragment fragment = new AwesomeDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MESSAGE, message);
        bundle.putString(KEY_POSITIVE, positive);
        bundle.putString(KEY_NEGATIVE, negative);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            restoreArguments(getArguments());
        } else {
            restoreInstanceState(savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_awesome, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        setupView();
    }

    private void bindView(View view) {
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
        btnPositive = (Button) view.findViewById(R.id.btn_positive);
        btnNegative = (Button) view.findViewById(R.id.btn_negative);
    }

    private void setupView() {
        tvMessage.setText(message);
        btnPositive.setText(positive);
        btnNegative.setText(negative);

        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnDialogListener listener = getOnDialogListener();
                if (listener != null) {
                    listener.onPositiveButtonClick();
                }
                dismiss();
            }
        });

        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnDialogListener listener = getOnDialogListener();
                if (listener != null) {
                    listener.onNegativeButtonClick();
                }
                dismiss();
            }
        });
    }

    private OnDialogListener getOnDialogListener() {
        Fragment fragment = getParentFragment();
        try {
            if (fragment != null) {
                return (OnDialogListener) fragment;
            } else {
                return (OnDialogListener) getActivity();
            }
        } catch (ClassCastException ignored) {
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_MESSAGE, message);
        outState.putString(KEY_POSITIVE, positive);
        outState.putString(KEY_NEGATIVE, negative);
    }

    private void restoreInstanceState(Bundle bundle) {
        message = bundle.getString(KEY_MESSAGE);
        positive = bundle.getString(KEY_POSITIVE);
        negative = bundle.getString(KEY_NEGATIVE);
    }

    private void restoreArguments(Bundle bundle) {
        message = bundle.getString(KEY_MESSAGE);
        positive = bundle.getString(KEY_POSITIVE);
        negative = bundle.getString(KEY_NEGATIVE);
    }

    public interface OnDialogListener {
        void onPositiveButtonClick();

        void onNegativeButtonClick();
    }

    public static class Builder {
        private String message;
        private String positive;
        private String negative;

        public Builder() {
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPosition(String positive) {
            this.positive = positive;
            return this;
        }

        public Builder setNegative(String negative) {
            this.negative = negative;
            return this;
        }

        public AwesomeDialogFragment build() {
            AwesomeDialogFragment fragment = AwesomeDialogFragment.newInstance(message, positive, negative);
            return fragment;
        }
    }
}