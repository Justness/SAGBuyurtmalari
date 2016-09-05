package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;


public class Registration extends Fragment implements View.OnClickListener {

    private Button add, read, delete, update;
    private EditText name, email, password, edId;
    private ListView ctList;
    private SimpleCursorAdapter mAdapter;
    private String updateId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onClick(View v) {

        String sname = name.getText().toString();
        String smail = email.getText().toString();
        String spass = password.getText().toString();
        String sid = edId.getText().toString();

        switch (v.getId()) {

            case R.id.reg_add:
                DatabaseOpenHelper.getInstance(getContext()).insertCustomer(sname, smail, spass, sid);

//                name.setText("Name");
//                email.setText("email");
//                password.setText("password");

                Toast toast_add = Toast.makeText(getContext(),
                        "Новый пользователь успешно добавлен", Toast.LENGTH_SHORT);
                toast_add.show();

                mAdapter.changeCursor(DatabaseOpenHelper.getInstance(getContext()).getAllCustomer());
                mAdapter.notifyDataSetChanged();
//                Intent Intent_add = new Intent(this,Registration.class);
//                Intent_add.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Intent_add.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(Intent_add);

                break;

            case R.id.reg_read:
                DatabaseOpenHelper.getInstance(getContext()).readCustomerTable();
                break;
            case R.id.reg_update:
                if (sid.equalsIgnoreCase("")) {
                    break;
                }
                DatabaseOpenHelper.getInstance(getContext()).updateCustomerTable(sname, smail, spass, sid);
//                name.setText("Name");
//                email.setText("email");
//                password.setText("password");

                Toast toast_update = Toast.makeText(getContext(),
                        "Пользователь успешно обновлен", Toast.LENGTH_SHORT);
                toast_update.show();
                mAdapter.changeCursor(DatabaseOpenHelper.getInstance(getContext()).getAllCustomer());
                mAdapter.notifyDataSetChanged();
//                Intent Intent_update = new Intent(this,Registration.class);
//                Intent_update.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Intent_update.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(Intent_update);


                Log.d("mLog", "updates rows count = " + sid);
                break;
            case R.id.reg_delete:
                if (sid.equalsIgnoreCase("")) {
                    break;
                }
                String adminId = edId.getText().toString();
                if (adminId.equals("1")) {
                    Toast toast = Toast.makeText(getContext(),
                            "Нельзя удалить админа!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    DatabaseOpenHelper.getInstance(getContext()).deleteCustomerTable(sid);
//                    name.setText("Name");
//                    email.setText("email");
//                    password.setText("password");

                    Toast toast_delete = Toast.makeText(getContext(),
                            "Пользователь успешно удален", Toast.LENGTH_SHORT);
                    toast_delete.show();
                    mAdapter.changeCursor(DatabaseOpenHelper.getInstance(getContext()).getAllCustomer());
                    mAdapter.notifyDataSetChanged();

//                    Intent Intent_delete = new Intent(this,Registration.class);
//                    Intent_delete.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    Intent_delete.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(Intent_delete);

                }
                // int delCount = database.delete(DBHelper.TABLE_CONTACTS, DBHelper.KEY_ID + "=" + id, null);
                Log.d("mLog", "deleted rows count = " + sid);
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_registration, null);
        //super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //super.onCreateView(inflater, container, savedInstanceState);
        //setContentView(R.layout.activity_registration);

        add = (Button) view.findViewById(R.id.reg_add);
        add.setOnClickListener(this);

        read = (Button) view.findViewById(R.id.reg_read);
        read.setOnClickListener(this);

        update = (Button) view.findViewById(R.id.reg_update);
        update.setOnClickListener(this);

        delete = (Button) view.findViewById(R.id.reg_delete);
        delete.setOnClickListener(this);


        edId = (EditText) view.findViewById(R.id.reg_id);
        name = (EditText) view.findViewById(R.id.reg_name);
        email = (EditText) view.findViewById(R.id.reg_mail);
        password = (EditText) view.findViewById(R.id.reg_password);

        ctList = (ListView) view.findViewById(R.id.customerList);

        mAdapter = new SimpleCursorAdapter(
                getContext(), // Context.
                android.R.layout.simple_expandable_list_item_2,  // Specify the row template to use (here, two columns bound to the two retrieved cursor   rows).
                DatabaseOpenHelper.getInstance(getContext()).getAllCustomer(),                                              // Pass in the cursor to bind to.
                new String[]{"name", "email"},           // Array of cursor columns to bind to.
                new int[]{android.R.id.text1, android.R.id.text2}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        ctList.setAdapter(mAdapter);

        ctList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String[] arr = new String[3];
                String strCustomer = DatabaseOpenHelper.getInstance(getContext()).getByIdCustomer(String.valueOf(id));

                if (strCustomer.equals("")) {

                } else {
                    String[] entries = strCustomer.split("#");
                    updateId = entries[0];
                    edId.setText(entries[4]);
                    name.setText(entries[1]);
                    email.setText(entries[2]);
                    password.setText(entries[3]);
                }

            }
        });
    }
}
