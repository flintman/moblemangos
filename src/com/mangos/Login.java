/* 
 * Copyright (C) 2007-2012 Flintman Computers <http://www.flintmancomputers.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package com.mangos;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	public EditText Username;
	public EditText Password;
	public EditText Url_text;
	public EditText Port_text;
	public CheckBox Remember;
	public String User;
	public String Pass;
	public String Url;
	public String Port;
	public String Passmd5;
	public String Loggedin = null;
	public String Saveduser;
	public String Savedurl;
	public String Savedport;
	public String Savedpass;
	public boolean Savedcheck;
	public Common Variables = new Common();
	public MangosTelnet Mangos = new MangosTelnet();

	public static final String PREFS_NAME = "mobleMangos";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.login);
		Variables = ((Common) getApplicationContext());

		Username = (EditText) findViewById(R.login.username);
		Password = (EditText) findViewById(R.login.password);
		Url_text = (EditText) findViewById(R.login.url_text);
		Port_text = (EditText) findViewById(R.login.port_text);
		Remember = (CheckBox) findViewById(R.login.remember);

		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		Saveduser = settings.getString("username", User);
		Savedpass = settings.getString("password", Pass);
		Savedport = settings.getString("port_text", Port);
		Savedurl = settings.getString("url_text", Url);
		Savedcheck = settings.getBoolean("checked", false);

		// Looks at Frt page button
		Button btn = (Button) findViewById(R.btn.loginBtn);
		btn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				setProgressBarIndeterminateVisibility(true);
				User = Username.getText().toString();
				Pass = Password.getText().toString();
				Port = Port_text.getText().toString();
				Url = Url_text.getText().toString();

				boolean checked = Remember.isChecked();
				if (checked) {
					addremember();
				} else {
					removeremember();
				}
				String response = "Error possible wrong URL";
				try {
					response = Mangos.Login(Url, Port, User, Pass);
				} catch (Exception e) {
				}
				;

				if (response.contains("Logged in.")) {
					Mangos.closeConnection();
					setProgressBarIndeterminateVisibility(false);
					logged();
				} else {
					Toast.makeText(getBaseContext(), response,
							Toast.LENGTH_LONG).show();
					setProgressBarIndeterminateVisibility(false);
				}
			}
		});
	}

	protected void removeremember() {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", "");
		editor.putString("password", "");
		editor.putString("url_text", "");
		editor.putString("port_text", "");
		editor.putBoolean("checked", false);

		// Commit the edits!
		editor.commit();

	}

	protected void addremember() {

		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("username", User);
		editor.putString("password", Pass);
		editor.putString("url_text", Url);
		editor.putString("port_text", Port);
		editor.putBoolean("checked", true);

		// Commit the edits!
		editor.commit();

	}

	// Setups after login
	public boolean logged() {
		Variables.setUsername(User);
		Variables.setPassword(Pass);
		Variables.setUrl(Url);
		Variables.setPort(Port);
		Intent intent = new Intent(this, Main.class);
		startActivity(intent);
		return (true);
	}

	// Loads data on resuming
	@Override
	public void onResume() {
		super.onResume();
		if (Savedcheck) {
			Username.setText(Saveduser);
			Password.setText(Savedpass);
			Url_text.setText(Savedurl);
			Port_text.setText(Savedport);
			Remember.setChecked(true);
		}

	}

	// Performed when program shuts down
	public void onDestroy() {
		super.onDestroy();

		if (Mangos.ready()) {
			try {
				Mangos.disconnect();
			} catch (IOException e) {
			}
		}
	}

}
