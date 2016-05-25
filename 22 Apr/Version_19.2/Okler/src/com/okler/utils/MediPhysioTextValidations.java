package com.okler.utils;

import com.okleruser.R;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MediPhysioTextValidations {
	
	EditText edt_firstname,edt_surname, edt_email, edt_phoneno, edt_state, edt_city, edt_address, edt_pincode, edt_weight;
	TextView  edt_From, edt_To;
	
	public MediPhysioTextValidations(View view)
	{
		
		edt_firstname = (EditText) view.findViewById(R.id.edtTet_firstname);
		edt_surname = (EditText) view.findViewById(R.id.edtTxt_surname); 
		edt_email = (EditText) view.findViewById(R.id.edtTxt_email);
		edt_phoneno = (EditText) view.findViewById(R.id.edtTxt_phonenumber);				
		edt_From = (TextView) view.findViewById(R.id.txtFrom);
		edt_To = (TextView) view.findViewById(R.id.txtTo);		
		
		edt_state = (EditText) view.findViewById(R.id.edtTxt_state);
		edt_city = (EditText) view.findViewById(R.id.edtTxt_city);
	}
	
	
	private EditText editTextToValidate;
	private TextView textToValidate;
	
	TextValidations text ;
	
	public boolean validateFirstName()
	{
		this.editTextToValidate = edt_firstname;
		text = new TextValidations(editTextToValidate);
		return text.ValidateMinimumChars("Please Enter First name",3);
	}
	
	public boolean validateSurnmae()
	{
		this.editTextToValidate = edt_surname;
		text = new TextValidations(editTextToValidate);
		return text.ValidateMinimumChars("Please Enter surname",3);
	}
	
	public boolean validateEmail()
	{
		this.editTextToValidate = edt_email;
		text = new TextValidations(editTextToValidate);
		return text.ValidateEmailId();
	}
	
	public boolean validatePhoneNo()
	{
		this.editTextToValidate = edt_phoneno;
		String mno = editTextToValidate.getText().toString();
		long mobile_no;
		if(mno.equals("")||mno.length()==0){
			mobile_no = 0;
			editTextToValidate.setError("Please Enter Correct mobile number");
			return false;
		}else{
		mobile_no = Long.parseLong(editTextToValidate.getText().toString());
		if(mobile_no == 0)
		{
			editTextToValidate.setError("Please Enter Correct mobile number");
			return false;
		}
		else if(editTextToValidate.length()<10 ||editTextToValidate.length()>10)
		{
			editTextToValidate.setError("Please enter 10 digit mobile number");
			return false;
		}else if(!(mno.startsWith("9")||mno.startsWith("8")||mno.startsWith("7"))){
			editTextToValidate.setError("Number should starts with 9,8 or 7");
			return false;
		}
		
			return true;
		}
	}
	
	public boolean validateState()
	{
		this.editTextToValidate = edt_state;
		text = new TextValidations(editTextToValidate);
		return text.validateText("Please Enter State");
	}
	
	public boolean validateCity()
	{
		this.editTextToValidate = edt_city;
		text = new TextValidations(editTextToValidate);
		return text.validateText("Please Enter City");
	}
	
	/*public boolean validateFrom()
	{
		this.textToValidate = edt_From;
		text = new TextValidations(editTextToValidate);
		return text.validateText("Please Enter from date");
	}
	
	public boolean validateTo()
	{
		this.textToValidate = edt_To;
		text = new TextValidations(editTextToValidate);
		return text.validateText("Please Enter to date");
	}*/

}
