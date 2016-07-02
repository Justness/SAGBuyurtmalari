package uz.sag.sagbuyurtmalari.sagbuyurtmalari.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.R;

/**
 * Created by User on 24.06.2016.
 */
public class AddRugDialog extends DialogFragment {

    private EditText mWidthValue;

    public String getWidthValue() {
        return mWidthValue.getText().toString();
    }

    public String getHeightValue() {
        return mHeightValue.getText().toString();
    }

    public String getQuantityValue() {
        return mQuantityValue.getText().toString();
    }

    public Boolean getFinishingValue() {
        return mFinishingValue;
    }

    private EditText mHeightValue;
    private EditText mQuantityValue;
    private Boolean mFinishingValue; // R = False O = TRUE

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface AddRugDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    AddRugDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddRugDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement AddRugDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView =inflater.inflate(R.layout.add_rug_dialog, null);

        builder.setView(dialogView);

        builder.setMessage(R.string.addrug)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(AddRugDialog.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(AddRugDialog.this);
                    }
                });


        mWidthValue = (EditText)dialogView.findViewById(R.id.widthvalue);
        mHeightValue = (EditText)dialogView.findViewById(R.id.heightvalue);
        mQuantityValue = (EditText)dialogView.findViewById(R.id.quantity);


        SeekBar widthSeek = (SeekBar) dialogView.findViewById(R.id.rugwidth);
        widthSeek.setProgress(100);
        widthSeek.incrementProgressBy(10);
        mWidthValue.setText(Integer.toString(widthSeek.getProgress()).trim());

        widthSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                mWidthValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar heightSeek = (SeekBar) dialogView.findViewById(R.id.rugheight);
        heightSeek.setProgress(100);
        heightSeek.incrementProgressBy(10);
        mHeightValue.setText(Integer.toString(heightSeek.getProgress()).trim());
        heightSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = progress / 10;
                progress = progress * 10;
                mHeightValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        final Button minusBtn = (Button) dialogView.findViewById(R.id.quantityminus);
        final Button plusBtn = (Button) dialogView.findViewById(R.id.quantityplus);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int intVal = Integer.parseInt(mQuantityValue.getText().toString());
                mQuantityValue.setText( String.valueOf(intVal + 2) );
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int intVal = Integer.parseInt(mQuantityValue.getText().toString());
                if (intVal>1)
                mQuantityValue.setText( String.valueOf(intVal - 2) );
                else
                    mQuantityValue.setText( "0" );
            }
        });

        return builder.create();
    }



}
