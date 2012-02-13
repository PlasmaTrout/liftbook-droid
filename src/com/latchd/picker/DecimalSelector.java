package com.latchd.picker;

import com.latchd.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class DecimalSelector extends LinearLayout implements OnClickListener
{
	public interface OnChangedListener {
	    void onChanged(DecimalSelector selector, float oldVal, float newVal);
	}
	
	float min;
	float max;
	float increment;
	ImageButton minusButton;
	ImageButton plusButton;
	EditText editText;
	OnClickListener listener;
	com.latchd.picker.DecimalSelector.OnChangedListener changeListener;

	public DecimalSelector(Context context){
		super(context);
		
		
		SetupControl(context);
		
	}
	
	public DecimalSelector(Context context, AttributeSet attrs){
		super(context,attrs);
		SetupControl(context);
	}
	
	private void SetupControl(Context context){
		
		min = 0;
		max = Float.MAX_VALUE;
		increment = 1;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.decimal_selector, this, true);
		
		if(!v.isInEditMode()){
		
		minusButton = (ImageButton) v.findViewById(R.id.dselector_minus);
		plusButton = (ImageButton) v.findViewById(R.id.dselector_plus);
		editText = (EditText) v.findViewById(R.id.dselector_text);
		
		
		
		minusButton.setOnClickListener(this);
		plusButton.setOnClickListener(this);
		
		editText.setText(Integer.toString(0));
		
		}
		
		
	}
	
	public void setRange(float min, float max){
		this.min = min;
		this.max = max;
	}
	
	public void setRange(float min, float max, float increment){
		setRange(min,max);
		this.increment = increment;
	}
	
	public void setCurrent(float value){
		float oldValue = Float.parseFloat(editText.getText().toString());
		editText.setText(Float.toString(value));
		FireChange(oldValue);
	}
	
	public float getCurrent(){
		return Float.parseFloat(editText.getText().toString());
	}

	public void setOnClickListener(OnClickListener listener){
		this.listener = listener;
	}

	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case(R.id.dselector_plus):
			float result = this.getCurrent();
		
			if(result <= max){
				result = result+increment;
			}else{
				result = max;
			}
			
			this.setCurrent(result);
			break;
		case(R.id.dselector_minus):
			float result2 = this.getCurrent();
			result2 = result2-increment;
			if(result2 >= 0){
				this.setCurrent(result2);
			}else{
				result2 = min;
			}
			break;
		default:
			break;
		}
		
		if(listener != null){
			listener.onClick(editText);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		editText.setEnabled(enabled);
		minusButton.setEnabled(enabled);
		plusButton.setEnabled(enabled);
		super.setEnabled(enabled);
	}
	
	public void setOnChangeListener(OnChangedListener listener){
		this.changeListener = listener;
	}
	
	public void FireChange(float oldValue){
		if(this.changeListener != null){
			changeListener.onChanged(this, oldValue, this.getCurrent());
		}
	}
	
}
