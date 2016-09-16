package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static int user_id;
    public EditText email;
    public EditText password;
    public Button login;

    public static int getUserId() {
        return user_id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences settings = getSharedPreferences("login", 0);
        email = (EditText) findViewById(R.id.email);
        email.setText(settings.getString("login", "Login"));
        password = (EditText) findViewById(R.id.password);
        password.requestFocus();
        login = (Button) findViewById(R.id.login_id);
        login.setOnClickListener(this);

        DatabaseOpenHelper.getInstance(getBaseContext()).getUsersFromServer();
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        String strMail = email.getText().toString();
        String strPass = password.getText().toString();

        if (strMail.length() > 0 && strPass.length() > 0) {
            user_id = DatabaseOpenHelper.getInstance(getBaseContext()).getCustomerIdAuth(strMail, strPass);
            if (user_id != 0) {
                SharedPreferences.Editor settings = getSharedPreferences("login", 0).edit();
                settings.putString("login", strMail);
                settings.commit();
                intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.login_error), Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.login_error), Toast.LENGTH_SHORT);
            toast.show();
        }

    }
}
