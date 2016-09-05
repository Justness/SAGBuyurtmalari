package uz.sag.sagbuyurtmalari.sagbuyurtmalari;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.dbadapters.DatabaseOpenHelper;

public class MyDialog extends DialogFragment {
    public EditText edit_comment;
    public View view_comment;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setMessage("Добавить коментарии");
        view_comment = inflater.inflate(R.layout.activity_my_dialog, null);
        CheckComment();
        builder.setView(view_comment)
                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (!(edit_comment.getText()).equals("")) {
                            DatabaseOpenHelper.getInstance(getContext()).updateOrderComment(OrderListFragment.orderId, edit_comment.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Закрить", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MyDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void CheckComment() {
        edit_comment = (EditText) view_comment.findViewById(R.id.edit_comment);

        String[] arr_comment = new String[1];
        String comment = DatabaseOpenHelper.getInstance(getContext()).getByIdOrderComment(OrderListFragment.orderId);
        arr_comment = comment.split("#");

        if (!arr_comment[1].equals("null")) {
            edit_comment.setText(arr_comment[1]);
        }
    }

}
