package app.nutrimeat.meat.org.nutrimeat;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;

/**
 * Created by skumbam on 9/17/17.
 */

public class TimePickerDialogFragment extends DialogFragment {

    private Spinner hourSpinner , minuteSpinner ;
    private String hours[] ={"8" ,"9","10","11","12","13" ,"14","15","16","17" ,"18","19"};
    private String minutes[] = {"00","30"};
    ArrayAdapter<String> hourAdapter;
    ArrayAdapter<String> minuteAdapter;
    private TextView okayButton;

    private String minutesSelectedValue ="" ;
    private String hoursSelectedValue ="" ;
    private TimeListener timeListener;

    public TimePickerDialogFragment(TimeListener listenr) {
        this.timeListener = listenr;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.time_picker,container,false);
        hourSpinner = (Spinner)v.findViewById(R.id.spinner_hour);
        minuteSpinner = (Spinner) v.findViewById(R.id.spinner_minute);
        okayButton = (TextView) v.findViewById(R.id.okay_button);

        hourAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,hours);
        minuteAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,minutes);

        hourSpinner.setPrompt("Hours");
        minuteSpinner.setPrompt("Minutes");
        hourSpinner.setAdapter(hourAdapter);
        minuteSpinner.setAdapter(minuteAdapter);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(minutesSelectedValue.isEmpty() || hoursSelectedValue.isEmpty()) {
                    Toast.makeText(getActivity(), "Plese select the Appropriate Time to deliver ", Toast.LENGTH_SHORT).show();
                } else {
                    timeListener.onClick(hoursSelectedValue+":"+minutesSelectedValue +":00");
                    dismiss();
                }
            }
        });


       minuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               minutesSelectedValue = minutes[position];
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {
             minutesSelectedValue = minutes[0];
           }
       });


        hourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                hoursSelectedValue = hours[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                   hoursSelectedValue = hours[0];
            }
        });

        return  v;
    }


    public interface  TimeListener{
        void onClick(String time );
    }
}
