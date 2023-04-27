package com.suke.widget.sample

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.suke.widget.SwitchButton

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val switchButton = findViewById<View>(R.id.switch_button) as SwitchButton

        switchButton.isChecked = true
        switchButton.toggle() //switch state
        switchButton.toggle(false) //switch without animation
        switchButton.setShadowEffect(true) //disable shadow effect
        switchButton.isEnabled = false //disable button
        switchButton.setEnableEffect(false) //disable the switch animation
        switchButton.setOnCheckedChangeListener(object : SwitchButton.OnCheckedChangeListener {
            override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
                //TODO do your job
            }
        })
    }
}