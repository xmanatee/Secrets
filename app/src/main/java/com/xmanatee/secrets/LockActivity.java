package com.xmanatee.secrets;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LockActivity extends AppCompatActivity {

    Map<String, Integer> passToSecret = new HashMap<>();

    public static final String SECRET_TEXT = "com.xmanatee.secret.SECRET_TEXT";


    private class KeyListener implements View.OnClickListener {
        String typed, activeTyped;
        private EditText mPasswordField;

        KeyListener(EditText mPasswordField) {
            this.typed = "";
            this.activeTyped = "";
            this.mPasswordField = mPasswordField;
        }

        void clearAll() {
            typed = "";
            activeTyped = "";
            mPasswordField.setText("");
        }

        void checkForPassword() {
            if (LockActivity.this.passToSecret.containsKey(activeTyped)) {
                LockActivity.this.showSecret(LockActivity.this.passToSecret.get(activeTyped));
                clearAll();
            }
            else if (typed.endsWith("C3P0")) {
                LockActivity.this.showSecret(R.string.star_wars_secret);
                clearAll();
            }
            else if (activeTyped.length() == 8) {
                AnimatorSet set = (AnimatorSet) AnimatorInflater
                        .loadAnimator(LockActivity.this, R.animator.edit_text_animator);
                set.setTarget(mPasswordField);
                set.start();
            }
        }

        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v;
            CharSequence newChar = textView.getText();
            typed += newChar;

            if (activeTyped.length() < 8 && "number_button".equals(v.getTag())) {
                activeTyped += textView.getText();
            }
            else {
                switch (v.getId()) {
                    case R.id.t9_key_clear: {
                        activeTyped = "";
                    }
                    break;
                    case R.id.t9_key_backspace: { // handle backspace button
                        activeTyped = activeTyped.substring(0, activeTyped.length() - 1);
                    }
                }
            }
            mPasswordField.setText(activeTyped);
            checkForPassword();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_lock);
        initListener();
        initSecrets();
    }

    private void initSecrets() {
        passToSecret.put("5", R.string.beginer_secret);
        passToSecret.put("24021996", R.string.no_secret);
        passToSecret.put("14881488", R.string.gotcha_secret);
        passToSecret.put("18111998", R.string.real_secret);
    }

    private void initListener() {
        List<Integer> keys = Arrays.asList(
                R.id.t9_key_0,
                R.id.t9_key_1,
                R.id.t9_key_2,
                R.id.t9_key_3,
                R.id.t9_key_4,
                R.id.t9_key_5,
                R.id.t9_key_6,
                R.id.t9_key_7,
                R.id.t9_key_8,
                R.id.t9_key_9,
                R.id.t9_key_clear,
                R.id.t9_key_backspace
        );

        EditText passwordField = $(R.id.passwordEdit);
        View.OnClickListener keyListener = new KeyListener(passwordField);

        for (Integer key : keys) {
            $(key).setOnClickListener(keyListener);
        }
    }

    protected <T extends View> T $(@IdRes int id) { return (T) super.findViewById(id); }


    public void showSecret(Integer secret_id) {
        Intent intent = new Intent(this, SecretActivity.class);

        String secretText = getResources().getString(secret_id);
        intent.putExtra(SECRET_TEXT, secretText);

        startActivity(intent);
    }
}
