package app.nutrimeat.meat.org.nutrimeat;

import android.annotation.SuppressLint;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.nutrimeat.meat.org.nutrimeat.product.ModelCart;

import static app.nutrimeat.meat.org.nutrimeat.PrefManager.PREF_PREORDER_CART;

/**
 * Created by skumbam on 9/17/17.
 */

public class TimePickerDialogFragment extends DialogFragment {

    private Spinner hourSpinner /*, minuteSpinner */;
//    private String hours[] ={"8" ,"9","10","11","12","13" ,"14","15","16","17" ,"18","19"};
    private String timeOfOrder[] = {"08:00" ,"08:30" ,"09:00" ,"09:30" ,"10:00", "10:30" ,"11:00" ,"11:30", "12:00",
            "12:30","13:00","13:30","15:00","15:30","16:00","16:30","17:00","17:30","18:00",
            "19:00","19:30"};
    private ArrayList<String> timeorderPageOpened =new ArrayList<>();
//    private String minutes[] = {"00","30"};
    ArrayAdapter<String> hourAdapter;
//    ArrayAdapter<String> minuteAdapter;
    private TextView okayButton;

    private String minutesSelectedValue ="" ;
    private String hoursSelectedValue ="" ;
    private TimeListener timeListener;
    Calendar mCalendar;

   public TimePickerDialogFragment(){}

    @SuppressLint("ValidFragment")
    public TimePickerDialogFragment(TimeListener listenr) {
        this.timeListener = listenr;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.time_picker,container,false);
        hourSpinner = (Spinner)v.findViewById(R.id.spinner_hour);
//        minuteSpinner = (Spinner) v.findViewById(R.id.spinner_minute);
        okayButton = (TextView) v.findViewById(R.id.okay_button);
        mCalendar = Calendar.getInstance();

        if(isPreorder()){
            for (String time : timeOfOrder)
            timeorderPageOpened .add(time);
        } else {
            addTimetobeOrderedPageOpened();
        }
        hourAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,timeorderPageOpened);
//        minuteAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,minutes);

        hourSpinner.setPrompt("Hours");
//        minuteSpinner.setPrompt("Minutes");
        hourSpinner.setAdapter(hourAdapter);
//        minuteSpinner.setAdapter(minuteAdapter);

        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(/*minutesSelectedValue.isEmpty() ||*/ hoursSelectedValue.isEmpty()) {
                    Toast.makeText(getActivity(), "Plese select the Appropriate Time to deliver ", Toast.LENGTH_SHORT).show();
                } else {
                    timeListener.onClick(hoursSelectedValue/*+":"+minutesSelectedValue */+":00");
                    dismiss();
                }
            }
        });


     /*  minuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               minutesSelectedValue = minutes[position];
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {
             minutesSelectedValue = minutes[0];
           }
       });
*/

        hourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                hoursSelectedValue = timeorderPageOpened.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                   hoursSelectedValue = timeorderPageOpened.get(0);
            }
        });

        return  v;
    }

    private void addTimetobeOrderedPageOpened() {
        for (String time : timeOfOrder) {
            Date date = convertStringtoTime(time);
            if(date.getTime() > mCalendar.getTime().getTime() + 46*60*1000){
                timeorderPageOpened.add(time);
            }
        }

    }


    private Date convertStringtoTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String hournTime[] = time.split(":");

        Date date = null;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hournTime[0]));
        calendar.set(Calendar.MINUTE,Integer.parseInt(hournTime[1]));


        return calendar.getTime();
    }

    private boolean isPreorder() {
        List<ModelCart> sharedPreferenceProductList = CommonFunctions.getSharedPreferenceProductList(getActivity(), PREF_PREORDER_CART);
        if (sharedPreferenceProductList.size() > 0) {
            return true;
        }

        return false;
    }

    public interface  TimeListener{
        void onClick(String time );
    }
}
