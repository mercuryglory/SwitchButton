package com.wzh.switchbutton;

import com.wzh.switchbutton.view.SwitchButton;
import com.wzh.switchbutton.view.SwitchButton.OnCheckChangeListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private SwitchButton switchbutton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		float density = getResources().getDisplayMetrics().density;
		System.out.println("density:"+density);
		switchbutton = (SwitchButton) findViewById(R.id.switchbutton);
		switchbutton.setOnCheckChangeListener(new OnCheckChangeListener() {

			@Override
			public void onCheckChanged(boolean isOpen) {
				ToastUtil.showToast(MainActivity.this,
						"当前滑动开关的状态为: " + isOpen);

			}
		});
	}

}
