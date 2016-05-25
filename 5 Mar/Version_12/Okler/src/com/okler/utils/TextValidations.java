package com.okler.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

public class TextValidations {
EditText editTextToValidate;
AutoCompleteTextView autoTextToValidate;
TextView textToValidate;	
	public TextValidations(EditText editTextToValidate)
	{
		this.editTextToValidate = editTextToValidate;
		editTextToValidate.addTextChangedListener(watch);	
		
	}
	public TextValidations(AutoCompleteTextView autoTextToValidate)
	{
		this.autoTextToValidate = autoTextToValidate;
		autoTextToValidate.addTextChangedListener(watch);	
		
	}
	public TextValidations(TextView textToValidate)
	{
		this.textToValidate = textToValidate;
		textToValidate.addTextChangedListener(watch);	
		
	}
	 public boolean validateText(String errorMessage)
	    {
		 	String inputText = editTextToValidate.getText().toString(); 
	    	if(inputText == null)
	    	{   	editTextToValidate.setError(errorMessage);
	    			editTextToValidate.requestFocus();
	    			return false;
	    	}
	    	else
	    	{
	    		if(inputText.equals(""))
	    		{
	    			editTextToValidate.setError(errorMessage);
	    			editTextToValidate.requestFocus();
	    			return false;
	    		}
	    		else
	    			return true;
	    	}
	    	  		    
	    }
	 public boolean ValidateMinimumChars(String errorMessage,int length){
		 String text = editTextToValidate.getText().toString();
		 if(text==null||text.equals("")){
			 editTextToValidate.setError(errorMessage);
			 editTextToValidate.requestFocus();
			 return false;
		 }else if(text.length()<length){
			 editTextToValidate.setError(errorMessage);
			 editTextToValidate.requestFocus();
			 return false;
		 }else
		 return true;
	 }
	 public boolean validateTextView(String errorMessage)
	    {
		 	String inputText = textToValidate.getText().toString(); 
	    	if(inputText == null)
	    	{   	textToValidate.setError(errorMessage);
	    			textToValidate.requestFocus();
	    			return false;
	    	}
	    	else
	    	{
	    		if(inputText.equals(""))
	    		{
	    			textToValidate.setError(errorMessage);
	    			textToValidate.requestFocus();
	    			return false;
	    		}
	    		else
	    			return true;
	    	}
	    	  		    
	    }
	 
	 public boolean validateAutoText(String errorMessage)
	    {
		 	String inputText = autoTextToValidate.getText().toString(); 
	    	if(inputText == null)
	    	{   	autoTextToValidate.setError(errorMessage);
	    			autoTextToValidate.requestFocus();
	    			return false;
	    	}
	    	else
	    	{
	    		if(inputText.equals(""))
	    		{
	    			autoTextToValidate.setError(errorMessage);
	    			autoTextToValidate.requestFocus();
	    			return false;
	    		}
	    		else
	    			return true;
	    	}
	    	  		    
	    }
	 
	 public boolean validatePinCode(String errorMessage){
		 String text = editTextToValidate.getText().toString();
		 if(text==null||text.equals("")){
			 editTextToValidate.setError(errorMessage);
			 editTextToValidate.requestFocus();
			 return false;
		 }else if(text.length()<6||text.length()>6){
			 editTextToValidate.setError(errorMessage);
			 editTextToValidate.requestFocus();
			 return false;
		 }else
		 return true;
	 }
	 public boolean validateMobile(String errorMessage){
		 String text = editTextToValidate.getText().toString();
		 if(text==null||text.equals("")){
			 editTextToValidate.setError(errorMessage);
			 editTextToValidate.requestFocus();
			 return false;
		 }else if(text.length()<10||text.length()>10){
			 editTextToValidate.setError(errorMessage);
			 editTextToValidate.requestFocus();
			 return false;
		 }else
		 return true;
	 }
	 public boolean ValidateEmailId()
	 {
		 String inputText = editTextToValidate.getText().toString().trim(); 
		 
		 if(inputText == null)
		 { 
	    		editTextToValidate.setError("Please enter Emailid");
	    		editTextToValidate.requestFocus();
	    		return false;
	     }
		 else
		 {
				if(inputText.equals(""))
				{
	    			editTextToValidate.setError("Please enter Emailid");	
	    			editTextToValidate.requestFocus();
	    			return false;
				}
				else
					 if(!inputText.contains("@") || !(inputText.contains(".")))
					 {
						editTextToValidate.setError("Please enter valid Emailid");
						editTextToValidate.requestFocus();
						return false;
					 }	    	
					 else
						 return true;
		 }	
   }



public	TextWatcher watch = new TextWatcher(){
		 
		    @Override
		    public void afterTextChanged(Editable arg0) {
		        // TODO Auto-generated method stub
		 
		    }
		 
		    @Override
		    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		            int arg3) {
		        // TODO Auto-generated method stub
		 
		    }
		 
		    @Override
		    public void onTextChanged(CharSequence s, int a, int b, int c) {
		    	if(editTextToValidate!=null){
		    	editTextToValidate.setError(null);
		    	}else if(autoTextToValidate!=null){
		    	autoTextToValidate.setError(null);	
		    	}else{
		    	textToValidate.setError(null);	
		    	}
		        }
		    };
	}

