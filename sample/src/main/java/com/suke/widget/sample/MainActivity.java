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
		switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(SwitchButton view, boolean isChecked) {
				//TODO do your job
			}
		});

	}
}
