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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Character extends Activity {
    public String User;
    public String Pass;
    public String Url;
    public String Port;
    public String Character_name;
    public String Character_level;
    public TextView Name_txt;
    public TextView Info_txt;
    public MangosTelnet Mangos = new MangosTelnet();
    public Common Variables = new Common();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.character);

        Variables = ((Common) getApplicationContext());
        User = Variables.getUsername();
        Pass = Variables.getPassword();
        Url = Variables.getUrl();
        Port = Variables.getPort();

        Bundle b = getIntent().getExtras();
        Character_name = b.getString("name");
        Character_level = b.getString("level");
        if (!Mangos.ready()) {
            Mangos.Login(Url, Port, User, Pass);
        }

        Name_txt = (TextView) findViewById(R.id.name);
        Info_txt = (TextView) findViewById(R.id.info);
        Name_txt.setText("You have selected: " + Character_name);
        Info_txt.setText("Character's Level: " + Character_level);

        // Looks at Change level button
        Button levelbtn = (Button) findViewById(R.btn.levelBtn);
        levelbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                levelBox();
            }
        });

        // Looks at send mail button
        Button mailbtn = (Button) findViewById(R.btn.mailBtn);
        mailbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mailBox();
            }
        });

        // Looks at send item button
        Button itembtn = (Button) findViewById(R.btn.itemBtn);
        itembtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                itemBox();
            }
        });

        // Looks at send itemset button
        Button goldbtn = (Button) findViewById(R.btn.goldBtn);
        goldbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goldBox();
            }
        });

        // Looks at teleport button
        Button teleportbtn = (Button) findViewById(R.btn.teleportBtn);
        teleportbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                teleportBox();
            }
        });
    }

    // Changes level
    public void goldBox() {
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        TextView mess_text = new TextView(this);
        mess_text.setText("How much gold to send:");
        fl.addView(mess_text);
        final EditText gold = new EditText(this);
        fl.addView(gold);
        sv.addView(fl);
        new AlertDialog.Builder(this)
                .setView(sv)
                .setTitle("")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        int value = Integer.parseInt(gold.getText().toString());
                        value = value * 10000;
                        String sendgold = value +"";
                        toastit(Mangos.sendGold(Character_name, "Gold",
                                "Here is a gift send by Moble ManGos", sendgold));
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

    public void teleportBox() {
        Intent intent = new Intent(this, Teleport.class);
        Bundle b = new Bundle();
        b.putString("name", Character_name);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void itemBox() {
        Intent intent = new Intent(this, Send_Item.class);
        Bundle b = new Bundle();
        b.putString("name", Character_name);
        intent.putExtras(b);
        startActivity(intent);
    }

    // Send mail to Character
    public void mailBox() {
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        TextView sub_text = new TextView(this);
        sub_text.setText("Enter in Subject:");
        fl.addView(sub_text);
        final EditText sub = new EditText(this);
        fl.addView(sub);
        TextView main_text = new TextView(this);
        main_text.setText("Enter in Message:");
        fl.addView(main_text);
        final EditText main = new EditText(this);
        fl.addView(main);
        sv.addView(fl);
        new AlertDialog.Builder(this)
                .setView(sv)
                .setTitle("Send mail to " + Character_name)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        toastit(Mangos.sendMail(Character_name, sub.getText().toString(), main
                                .getText().toString()));
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

    // Changes level
    public void levelBox() {
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        TextView mess_text = new TextView(this);
        mess_text.setText("Enter in New Level:");
        fl.addView(mess_text);
        final EditText level = new EditText(this);
        level.setText(Character_level);
        fl.addView(level);
        sv.addView(fl);
        new AlertDialog.Builder(this)
                .setView(sv)
                .setTitle("")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        toastit(Mangos.setCharLevel(Character_name, level.getText().toString()));
                        Character_level = level.getText().toString();
                        Info_txt.setText("Character's Level: " + Character_level);
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
    }
}
