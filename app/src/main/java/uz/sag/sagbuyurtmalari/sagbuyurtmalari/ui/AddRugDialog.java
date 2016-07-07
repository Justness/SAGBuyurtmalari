package uz.sag.sagbuyurtmalari.sagbuyurtmalari.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

import uz.sag.sagbuyurtmalari.sagbuyurtmalari.R;

/**
 * Created by User on 24.06.2016.
 */
public class AddRugDialog extends DialogFragment {

    private EditText mWidthValue;

    public static final int[] mainWidths = {
            50,
            60,
            70,
            75,
            80,
            100,
            120,
            150,
            200,
            250,
            300,
            350,
            400,
            500};
    public static final int[] mainHeights = {
            80,
            100,
            110,
            125,
            150,
            200,
            230,
            300,
            350,
            400,
            450,
            500,
            550,
            600,
            700,
            800,
            900,
            1000,
            2500};
    public final int[][] mainSizes = {{3028, 50, 80, 0},
            {3027, 50, 100, 0},
            {3039, 60, 110, 0},
            {3059, 80, 125, 0},
            {3029, 80, 150, 0},
            {2050, 100, 150, 0},
            {2051, 100, 200, 0},
            {2052, 100, 300, 0},
            {2053, 100, 400, 0},
            {2045, 150, 230, 0},
            {2046, 150, 300, 0},
            {2047, 150, 400, 0},
            {2039, 200, 300, 0},
            {2041, 200, 400, 0},
            {2043, 200, 500, 0},
            {2032, 250, 350, 0},
            {2033, 250, 400, 0},
            {2034, 250, 450, 0},
            {2035, 250, 500, 0},
            {2036, 250, 550, 0},
            {2026, 300, 400, 0},
            {2027, 300, 500, 0},
            {2028, 300, 550, 0},
            {2029, 300, 600, 0},
            {2030, 300, 700, 0},
            {2019, 350, 400, 0},
            {2112, 350, 450, 0},
            {2020, 350, 500, 0},
            {2107, 350, 550, 0},
            {2021, 350, 600, 0},
            {2022, 350, 700, 0},
            {2023, 350, 800, 0},
            {2018, 400, 400, 0},
            {2017, 400, 500, 0},
            {2114, 400, 550, 0},
            {2016, 400, 600, 0},
            {2015, 400, 700, 0},
            {2014, 400, 800, 0},
            {2013, 400, 900, 0},
            {2012, 400, 1000, 0},
            {2054, 50, 2500, 0},
            {2055, 60, 2500, 0},
            {2056, 70, 2500, 0},
            {2057, 75, 2500, 0},
            {2058, 80, 2500, 0},
            {2059, 100, 2500, 0},
            {2061, 120, 2500, 0},
            {2063, 150, 2500, 0},
            {2065, 200, 2500, 0},
            {2096, 250, 2500, 0},
            {3034, 50, 80, 1},
            {3035, 50, 100, 1},
            {3049, 60, 110, 1},
            {3067, 80, 125, 1},
            {3032, 80, 150, 1},
            {2085, 100, 150, 1},
            {2084, 100, 200, 1},
            {2083, 100, 300, 1},
            {2082, 100, 400, 1},
            {2081, 150, 230, 1},
            {2080, 150, 300, 1},
            {2047, 150, 400, 1},
            {2073, 200, 300, 1}};

    public String getWidthValue() {
        return mWidthValue.getText().toString();
    }

    public String getHeightValue() {
        return mHeightValue.getText().toString();
    }

    public String getQuantityValue() {
        return mQuantityValue.getText().toString();
    }

    public boolean getFinishingValue() {
        return mFinishingValue;
    }

    private EditText mHeightValue;
    private EditText mQuantityValue;
    private boolean mFinishingValue; // R = False O = TRUE

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface AddRugDialogListener {
        public void onDialogPositiveClick(AddRugDialog dialog);

        public void onDialogNegativeClick(AddRugDialog dialog);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
        mWidthValue.setText(String.valueOf(mainWidths[0]));
        mHeightValue = (EditText)dialogView.findViewById(R.id.heightvalue);
        mHeightValue.setText(String.valueOf(mainHeights[0]));
        mQuantityValue = (EditText)dialogView.findViewById(R.id.quantity);

        CheckBox chbox = (CheckBox) dialogView.findViewById(R.id.checkBox);
        chbox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mHeightValue.isEnabled()) {
                    mHeightValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                    mHeightValue.setEnabled(true);
                    mWidthValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                    mWidthValue.setEnabled(true);
                } else {
                    mHeightValue.setInputType(InputType.TYPE_NULL);
                    mHeightValue.setEnabled(false);
                    mWidthValue.setInputType(InputType.TYPE_NULL);
                    mWidthValue.setEnabled(false);
                }
            }
        });

        SeekBar widthSeek = (SeekBar) dialogView.findViewById(R.id.rugwidth);
        // widthSeek.setProgress(0);
        //widthSeek.incrementProgressBy(10);
        // mWidthValue.setText(Integer.toString(widthSeek.getProgress()).trim());

        widthSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //@TODO count progress from mainSizes progress = DatabaseOpenHelper.getInstance(null).getNearestWidth(progress);
                //ALSO COMPLETE SAVE/SEND ORDER

                mWidthValue.setText(String.valueOf(mainWidths[progress]));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        SeekBar heightSeek = (SeekBar) dialogView.findViewById(R.id.rugheight);
        //heightSeek.setProgress(0);
        //heightSeek.incrementProgressBy(10);
        //  mHeightValue.setText(Integer.toString(heightSeek.getProgress()).trim());
        heightSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //progress = DatabaseOpenHelper.getInstance(null).getNearestHeight(progress);

                mHeightValue.setText(String.valueOf(mainHeights[progress]));
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
