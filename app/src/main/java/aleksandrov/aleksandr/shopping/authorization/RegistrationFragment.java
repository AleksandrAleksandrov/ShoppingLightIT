package aleksandrov.aleksandr.shopping.authorization;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import aleksandrov.aleksandr.shopping.Communication;
import aleksandrov.aleksandr.shopping.R;
import aleksandrov.aleksandr.shopping.Res;
import aleksandrov.aleksandr.shopping.User;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Aleksandr on 12/26/2016.
 */

public class RegistrationFragment extends Fragment {

    private SharedPreferences sharedPreferences;

    private EditText editTextUsername, editTextPassword, editTextConfirmPassword;
    private Button buttonSignup;

    public RegistrationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);

        sharedPreferences = getContext().getSharedPreferences(Res.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        editTextUsername = (EditText) view.findViewById(R.id.reg_username);
        editTextPassword = (EditText) view.findViewById(R.id.reg_password);
        editTextConfirmPassword = (EditText) view.findViewById(R.id.reg_confirm_password);
        buttonSignup = (Button) view.findViewById(R.id.button_sign_up);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())) {
                    Observable.create(new Observable.OnSubscribe<User>() {
                        @Override
                        public void call(final Subscriber<? super User> subscriber) {
                            Communication communication = new Communication();
                            User user = communication.registerUser(editTextUsername.getText().toString(), editTextPassword.getText().toString());
                            subscriber.onNext(user);
                            subscriber.onCompleted();
                        }
                    })
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<User>() {
                                @Override
                                public void call(final User user) {
                                    if (user == null) {
                                        Toast.makeText(getContext(), R.string.user_already_exist, Toast.LENGTH_LONG).show();
                                    } else if (user != null) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(Res.PREF_USERNAME, user.getUsername());
                                        editor.putString(Res.PREF_TOKEN, user.getToken());
                                        editor.commit();
                                        getActivity().getSupportFragmentManager().beginTransaction().remove(RegistrationFragment.this).commit();
                                        getActivity().finish();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getContext(), R.string.password_do_not_match, Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}
