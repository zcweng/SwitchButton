# SwitchButton
SwitchButton.具有与IOS开关控件相似样式和行为的Android控件\<br>
<img src="21879.gif" style="max-width:212px;height:182px;" />

使用方法
-------
```
    <attr name="sb_shadow_radius" format="reference|dimension"/>       阴影半径
    <attr name="sb_shadow_offset" format="reference|dimension"/>       阴影偏移
    <attr name="sb_shadow_color" format="reference|color"/>            阴影颜色
    <attr name="sb_uncheck_color" format="reference|color"/>           关闭颜色
    <attr name="sb_checked_color" format="reference|color"/>           开启颜色
    <attr name="sb_border_width" format="reference|dimension"/>        边框宽度
    <attr name="sb_checkline_color" format="reference|color"/>         开启指示器颜色
    <attr name="sb_checkline_width" format="reference|dimension"/>     开启指示器线宽
    <attr name="sb_uncheckcircle_color" format="reference|color"/>     关闭指示器颜色
    <attr name="sb_uncheckcircle_width" format="reference|dimension"/> 关闭指示器线宽
    <attr name="sb_uncheckcircle_radius" format="reference|dimension"/>关闭指示器半径
    <attr name="sb_checked" format="reference|boolean"/>               是否选中
    <attr name="sb_shadow_effect" format="reference|boolean"/>         是否启用阴影
    <attr name="sb_effect_duration" format="reference|integer"/>       动画时间，默认300ms
    <attr name="sb_button_color" format="reference|color"/>            按钮颜色
    <attr name="sb_show_indicator" format="reference|boolean"/>        是否显示指示器，默认true：显示
    <attr name="sb_background" format="reference|color"/>              背景色，默认白色
    <attr name="sb_enable_effect" format="reference|boolean"/>         是否启用特效，默认true


	com.suke.widget.SwitchButton switchButton = (com.suke.widget.SwitchButton)
	    findViewById(R.id.switch_button);
	switchButton.setChecked(true);
	switchButton.isChecked();
	switchButton.toggle();     //切换开关
	switchButton.toggle(false);//无动画切换
	//禁用按钮
	switchButton.setEnabled(false);

	switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(SwitchButton view, boolean isChecked) {
			//TODO do your job
		}
	});

``` 


截图
-------
<img src="device-capture.png" style="max-width:480px;height:auto;" />


Gradle
-------
```
compile 'com.github.zcweng:switch-button:0.0.1-SNAPSHOT@aar'
```

License
-------
MIT, See the <a href="https://github.com/zcweng/SwitchButton/LICENSE">[LICENSE]</a> file for details.
