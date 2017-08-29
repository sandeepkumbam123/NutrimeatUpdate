package app.nutrimeat.meat.org.nutrimeat.drawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import app.nutrimeat.meat.org.nutrimeat.R;



public class BulkOrder extends Fragment implements View.OnClickListener {

    private Button btnSubmit;
    private EditText evSpecification;
    private EditText etQuantity;
    private Spinner spType;
    private RadioGroup rdType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        View view = inflater.inflate(R.layout.fragment_menu_bulkorder, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        rdType = (RadioGroup) view.findViewById(R.id.rdType);
        rdType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radio_chicken) {
                    spType.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,getResources()
                            .getStringArray(R.array.chicken_types)));
                }else{
                    spType.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,getResources()
                            .getStringArray(R.array.mutton_types)));

                }
            }
        });
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        evSpecification = (EditText) view.findViewById(R.id.edit_text_specifications);
        etQuantity = (EditText) view.findViewById(R.id.edit_text_quantity);
        spType = (Spinner) view.findViewById(R.id.spinner_chicken_types);

        btnSubmit.setOnClickListener(this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Bulk Order");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            if (!TextUtils.isEmpty(etQuantity.getText().toString()) && !TextUtils.isEmpty(evSpecification.getText().toString())) {
                Toast.makeText(getActivity(), "We will get in touch with you", Toast.LENGTH_SHORT).show();
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "support@nutrimeat.in", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Buil Order Details");
                emailIntent.putExtra(Intent.EXTRA_TEXT, getBuildOrderDetails());
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"support@nutrimeat.in"});
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            } else {
                Toast.makeText(getActivity(), "Please enter all the details", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getBuildOrderDetails(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Type : " + (rdType.getCheckedRadioButtonId()==R.id.radio_chicken?"Chicken" :"Mutton"));
        stringBuilder.append("\nQuantity : " + etQuantity.getText());
        stringBuilder.append("\nSpecifications : " + evSpecification.getText());
        stringBuilder.append("\nCut Type : " + spType.getSelectedItem().toString());
        return stringBuilder.toString();
    }
}