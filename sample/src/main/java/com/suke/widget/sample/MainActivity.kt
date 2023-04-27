package com.suke.widget.sample;
import android.app.Activity;
import android.os.Bundle;

import com.suke.widget.SwitchButton;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SwitchButton switchButton = (SwitchButton) findViewById(R.id.switch_button);

		switchButton.setChecked(true);
		switchButton.isChecked();
		switchButton.toggle();     //switch state
		switchButton.toggle(false);//switch without animation
		switchButton.setShadowEffect(true);//disable shadow effect
		switchButton.setEnabled(false);//disable button
		switchButton.setEnableEffect(false);//disable the switch animation
		switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(SwitchButton view, boolean isChecked) {
				//TODO do your job
			}
		});



	}
}
