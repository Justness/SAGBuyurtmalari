package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.content.Intent;
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

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login_id);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        Intent intent;

        String strMail = email.getText().toString();
        String strPass = password.getText().toString();

        if (strMail.length() > 0 && strPass.length() > 0) {
            user_id = DatabaseOpenHelper.getInstance(getBaseContext()).getCustomerIdAuth(strMail, strPass);
            if (user_id != 0) {

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
