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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Account extends Activity {
    public String User;
    public String Pass;
    public String Url;
    public String Port;
    public String User_id;
    public String User_name;
    public String Character_name[];
    public String Character_level[];
    public ListView Character_List;
    public TextView Name_txt;
    public TextView info_txt;
    public TextView No_toons;
    public String User_ip;
    public String User_gm;
    public String User_expansion;
    private TabHost Tabs;
    public int Selectedtab = 1;
    public MangosTelnet Mangos = new MangosTelnet();
    public Common Variables = new Common();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        Variables = ((Common)getApplicationContext());
        User = Variables.getUsername();
        Pass = Variables.getPassword();
        Url = Variables.getUrl();
        Port = Variables.getPort();
        
        Bundle b = getIntent().getExtras();
        User_id = b.getString("id");
        User_name = b.getString("name");
        if (!Mangos.ready()) {
            Mangos.Login(Url, Port, User, Pass);
        }
        String list[] = Mangos.getAccounts(User_name);
        User_ip = list[11];
        User_gm = list[12];
        User_expansion = list[13];
        Name_txt = (TextView) findViewById(R.id.name);
        No_toons = (TextView) findViewById(R.id.no_toons);
        info_txt = (TextView) findViewById(R.id.info);
        Character_List = (ListView) findViewById(R.id.character_list);

        // Setups for tabs
        Tabs = (TabHost) findViewById(R.id.tabhost);
        Tabs.setup();
        TabHost.TabSpec spec = Tabs.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Account Commands");
        Tabs.addTab(spec);
        spec = Tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Characters");
        Tabs.addTab(spec);
        for (int i = 0; i < Tabs.getTabWidget().getChildCount(); i++) {
            Tabs.getTabWidget().getChildAt(i)
                    .setBackgroundColor(Color.parseColor("#596687"));
        }
        Tabs.getTabWidget().getChildAt(0)
                .setBackgroundColor(Color.parseColor("#313849"));
        Tabs.setCurrentTab(0);
        Tabs.setPadding(5, 0, 5, 0);
        Tabs.setOnTabChangedListener(new OnTabChangeListener() {
            public void onTabChanged(String arg0) {
                for (int i = 0; i < Tabs.getTabWidget().getChildCount(); i++) {
                    Tabs.getTabWidget().getChildAt(i)
                            .setBackgroundColor(Color.parseColor("#596687"));
                }
                Tabs.getTabWidget().getChildAt(Tabs.getCurrentTab())
                        .setBackgroundColor(Color.parseColor("#313849"));
                if (arg0 == "tag1") {
                    Selectedtab = 1;
                    Name_txt.setText("Account name: " + User_name);
                    info_txt.setText("Last IP: " + User_ip + " GM Level: " + User_gm);
                }
                if (arg0 == "tag2") {
                    Selectedtab = 2;
                    loadCharacters();
                }

            }
        });

        // Looks at GM button
        Button gmbtn = (Button) findViewById(R.btn.gmlevelBtn);
        gmbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gmLevelBox();
            }
        });
        
        // Looks at password button
        Button passwordbtn = (Button) findViewById(R.btn.passwordBtn);
        passwordbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                passwordBox();
            }
        });
        
        // Looks at GM button
        Button expanbtn = (Button) findViewById(R.btn.expansionBtn);
        expanbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                expansionBox();
            }
        });

    }
    
 // sets to selected expanstion
    public void expansionBox() {
        String[] items = {
                "Normal", "TBC", "WOTLK","Cataclysm"};
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        TextView mess_text = new TextView(this);
        mess_text.setText("Select the expansion Below:");
        fl.addView(mess_text);
        final Spinner expand = new Spinner(this);
        @SuppressWarnings("unchecked")
        ArrayAdapter aa = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                items);

        aa.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        expand.setAdapter(aa);
        expand.setSelection(Integer.parseInt(User_expansion));
        fl.addView(expand);
        sv.addView(fl);
        new AlertDialog.Builder(this)
                .setView(sv)
                .setTitle("")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        toastit(Mangos.setExpansion(User_name, expand.getSelectedItemPosition()+""));
                        User_expansion = expand.getSelectedItemPosition()+"";
                        d.dismiss();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                d.dismiss();
                            }
                        }).create().show();
    }
    
 // sets new password
    public void passwordBox() {
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        TextView mess_text = new TextView(this);
        mess_text.setText("Enter New Password:");
        fl.addView(mess_text);
        final EditText password = new EditText(this);
        fl.addView(password);
        sv.addView(fl);
        new AlertDialog.Builder(this)
                .setView(sv)
                .setTitle("")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        toastit(Mangos.setPassword(User_id, password.getText().toString()));
                        d.dismiss();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                d.dismiss();
                            }
                        }).create().show();
    }

    // sets Gm Level and updates all information
    public void gmLevelBox() {
        String[] items = {
                "0", "1", "2", "3", "4"
        };
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        TextView mess_text = new TextView(this);
        mess_text.setText("Select the GM level Below:");
        fl.addView(mess_text);
        final Spinner level = new Spinner(this);
        @SuppressWarnings("unchecked")
        ArrayAdapter aa = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                items);

        aa.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        level.setAdapter(aa);
        level.setSelection(Integer.parseInt(User_gm));
        fl.addView(level);
        sv.addView(fl);
        new AlertDialog.Builder(this)
                .setView(sv)
                .setTitle("")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        toastit(Mangos.setGM(User_name, level.getSelectedItemPosition()+""));
                        User_gm = level.getSelectedItemPosition()+"";
                        Name_txt.setText("Account name: " + User_name);
                        info_txt.setText("Last IP: " + User_ip + " GM Level: " + User_gm);
                        d.dismiss();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                d.dismiss();
                            }
                        }).create().show();
    }

    // Loads the accounts list
    public void loadCharacters() {
        String list[] = Mangos.getCharacters(User_id);
        int total = list.length;
        if (total < 3) {
            No_toons.setVisibility(View.VISIBLE);
        }
        Character_name = new String[total];
        Character_level = new String[total];
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> fpage = new HashMap<String, String>();
        int x = 8;
        int i = 0;
        try {
            while (list[x] != null) {
                fpage = new HashMap<String, String>();
                Character_name[i] = list[x];
                fpage.put("name", list[x]);
                Character_level[i] = list[x + 3];
                fpage.put("level", list[x + 3]);
                fpage.put("race", list[x + 1] + " / " + list[x + 2]);
                mylist.add(fpage);

                SimpleAdapter mSchedule = new SimpleAdapter(this, mylist,
                        R.layout.character_list, new String[] {
                                "name", "level", "race"
                        },
                        new int[] {
                                R.an.name, R.an.level, R.an.race
                        });
                mSchedule.notifyDataSetChanged();
                Character_List.setAdapter(mSchedule);
                x = x + 6;
                i++;

            }
        } catch (Exception e) {

        }

        Character_List.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
            	openCharacter(Character_name[pos], Character_level[pos]);
            }
        });

    }
    
	// opens up Character
	public boolean openCharacter(String character, String level) {
		Intent intent = new Intent(this, Character.class);
		Bundle b = new Bundle();
		b.putString("name", character);
		b.putString("level", level);
		intent.putExtras(b);
		startActivity(intent);
		return (true);
	}

    // Saves info
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("selectedtab", Selectedtab);
        super.onSaveInstanceState(savedInstanceState);
    }

    // Retrieves info
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Selectedtab = savedInstanceState.getInt("selectedtab");
    }
    
    // Setup for easy Toast messages
    public boolean toastit(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return true;
    }

    // Loads data on resuming
    @Override
    public void onResume() {
        super.onResume();

        if (!Mangos.ready()) {
            Mangos.Login(Url, Port, User, Pass);
        }
        String currenttab = "tag" + Selectedtab;
        Tabs.setCurrentTabByTag(currenttab);
        if (Selectedtab == 1) {
            Selectedtab = 1;
            Name_txt.setText("Account name: " + User_name);
            info_txt.setText("Last IP: " + User_ip + " GM Level: " + User_gm);
        }
        if (Selectedtab == 2) {
            Selectedtab = 2;
            loadCharacters();
        }
    }
}
