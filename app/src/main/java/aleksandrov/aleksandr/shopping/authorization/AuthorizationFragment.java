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

public class AuthorizationFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private EditText username, password;
    private Button login;

    public AuthorizationFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.authorization_fragment, container, false);

        sharedPreferences = getContext().getSharedPreferences(Res.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        username = (EditText) view.findViewById(R.id.auth_username);
        password = (EditText) view.findViewById(R.id.auth_password);

        login = (Button) view.findViewById(R.id.button_log_in);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!username.getText().equals("")) {
                    Observable.create(new Observable.OnSubscribe<User>() {
                        @Override
                        public void call(final Subscriber<? super User> subscriber) {
                            Communication communication = new Communication();
                            User user = communication.authorizeUser(username.getText().toString(), password.getText().toString());
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
                                        Toast.makeText(getContext(), R.string.incorrect_data, Toast.LENGTH_LONG).show();
                                    } else if (user != null) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(Res.PREF_USERNAME, user.getUsername());
                                        editor.putString(Res.PREF_TOKEN, user.getToken());
                                        editor.commit();
                                        getActivity().getSupportFragmentManager().beginTransaction().remove(AuthorizationFragment.this).commit();
                                        getActivity().finish();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(getContext(), R.string.fill_in_username, Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}
