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
		switchButton.toggle();     //切换开关
		switchButton.toggle(false);//无动画切换
		switchButton.setShadowEffect(true);//设置是否启用阴影效果
		//禁用按钮
		switchButton.setEnabled(false);

		switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(SwitchButton view, boolean isChecked) {
				//TODO do your job
			}
		});



	}
}
