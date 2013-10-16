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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Teleport extends Activity {
    Button SearchBtn;
    Button SendBtn;
    Spinner Items;
    EditText Search;
    TextView Header;
    public MangosTelnet Mangos = new MangosTelnet();
    public String User;
    public String Pass;
    public String Url;
    public String Port;
    public String Character_name;
    public Common Variables = new Common();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teleport);

        Variables = ((Common)getApplicationContext());
        User = Variables.getUsername();
        Pass = Variables.getPassword();
        Url = Variables.getUrl();
        Port = Variables.getPort();
        
        Bundle b = getIntent().getExtras();
        Character_name = b.getString("name");
        
        if (!Mangos.ready()) {
            Mangos.Login(Url, Port, User, Pass);
        }
        Items = (Spinner) findViewById(R.id.items);
        Header = (TextView) findViewById(R.id.header);
        Header.setText("You have selected " + Character_name + " to teleport");

        Items.setVisibility(View.INVISIBLE);
        Search = (EditText) findViewById(R.id.search);
        SearchBtn = (Button) findViewById(R.id.searchBtn);
        SearchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                loadSpinner();
            }
        });

        SendBtn = (Button) findViewById(R.id.sendBtn);
        SendBtn.setVisibility(View.INVISIBLE);
        SendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                teleport();
            }
        });
    }

    public void teleport(){
        String selected = Items.getSelectedItem().toString().trim();
        String split[] = selected.split(" ");
        String item_id = split[1].trim();
        toastit(Mangos.teleport(Character_name, item_id));
        finish();
    }

    // Setup for easy Toast messages
    public boolean toastit(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return true;
    }

    public void loadSpinner() {
        String items[] = Mangos.lookupTele(Search.getText().toString());

        ArrayAdapter aa = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, items);

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Items.setAdapter(aa);
        Items.setVisibility(View.VISIBLE);
        SendBtn.setVisibility(View.VISIBLE);
    }

}
