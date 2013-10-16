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

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class Main extends Activity {

    public int Selectedtab = 1;
    public String User;
    public String Pass;
    public String Url;
    public String Port;
    public ListView Account_List;
    public ListView Online_List;
    public String Account_id[];
    public String Account_name[];
    public String Character_name[];
    private TabHost Tabs;
    public TextView Message;
    public MangosTelnet Mangos = new MangosTelnet();
    private ProgressDialog ProgressDialog;
    public Common Variables = new Common();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Variables = ((Common)getApplicationContext());
        User = Variables.getUsername();
        Pass = Variables.getPassword();
        Url = Variables.getUrl();
        Port = Variables.getPort();
        
        if (!Mangos.ready()) {
            Mangos.Login(Url, Port, User, Pass);
        }

        Message = (TextView) findViewById(R.id.serverinfo);
        Account_List = (ListView) findViewById(R.id.account_list);
        Online_List = (ListView) findViewById(R.id.online_list);

        // Setups for tabs
        Tabs = (TabHost) findViewById(R.id.tabhost);
        Tabs.setup();
        TabHost.TabSpec spec = Tabs.newTabSpec("tag1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Server");
        Tabs.addTab(spec);
        spec = Tabs.newTabSpec("tag2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Accounts");
        Tabs.addTab(spec);
        spec = Tabs.newTabSpec("tag3");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Online Players");
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
                    Message.setText(Mangos.getInfo());
                }
                if (arg0 == "tag2") {
                    Selectedtab = 2;
                    loadAccounts();
                }
                if (arg0 == "tag3") {
                    Selectedtab = 3;
                    loadOnline();
                }

            }
        });
        
        // Looks at Restart button
        Button addbtn = (Button) findViewById(R.btn.addBtn);
        addbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addBox();
            }
        });

        // Looks at Restart button
        Button sendbtn = (Button) findViewById(R.btn.sendBtn);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendBox();
            }
        });

        // Looks at Shutdown button
        Button shutdownbtn = (Button) findViewById(R.btn.shutdownBtn);
        shutdownbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shutdownBox();
            }
        });

        // Looks at make an announcement
        Button announcebtn = (Button) findViewById(R.btn.makeaBtn);
        announcebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                announceBox();
            }
        });
        
        // Looks at make an announcement
        Button notifybtn = (Button) findViewById(R.btn.notifyBtn);
        notifybtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                notifyBox();
            }
        });

        // Looks at sets Moto
        Button motobtn = (Button) findViewById(R.btn.setmotoBtn);
        motobtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                motoBox();
            }
        });

        // Looks at sets Players limits
        Button limitbtn = (Button) findViewById(R.btn.playerlimitBtn);
        limitbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                limitBox();
            }
        });

        // Looks at reloads Tables
        Button tablesbtn = (Button) findViewById(R.btn.reloadtableBtn);
        tablesbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Mangos.ready()) {
                    Mangos.reloadTable("all");
                } else {
                    finish();
                }
                ProgressDialog = android.app.ProgressDialog.show(Main.this, "",
                        "Reloading Tables...");
                new Thread() {
                    public void run() {
                        try {
                            sleep(30000);
                        } catch (Exception e) {
                        }
                        ProgressDialog.dismiss();
                    }
                }.start();
            }
        });
    }

    // Loads the Online List
    public void loadOnline() {
        String list[] = Mangos.getOnlineList();
        int total = list.length;
        Character_name = new String[total];
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> fpage = new HashMap<String, String>();
        int x = 0;
        int i = 0;
        while (list[x] != null) {
            fpage = new HashMap<String, String>();
            Character_name[i] = list[x];
            fpage.put("name", list[x]);
            mylist.add(fpage);
            SimpleAdapter mSchedule = new SimpleAdapter(this, mylist,
                        R.layout.online_list, new String[] {
                            "name"
                        },
                        new int[] {
                           R.an.name
                        });
            mSchedule.notifyDataSetChanged();
            Online_List.setAdapter(mSchedule);
            x++;
            i++;

        }

        Online_List.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                openCharacter(Character_name[pos], "70");
            }
        });

    }

    // Loads the accounts list
    public void loadAccounts() {
        String list[] = Mangos.getAccounts("%");
        int total = list.length;
        Account_name = new String[total];
        Account_id = new String[total];
        ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> fpage = new HashMap<String, String>();
        int x = 8;
        int i = 0;
        while (list[x] != null) {
            fpage = new HashMap<String, String>();
            Account_id[i] = list[x];
            Account_name[i] = list[x + 1];
            fpage.put("id", Account_id[i]);
            fpage.put("name", list[x + 1]);
            fpage.put("gm", "GM Level: " +list[x + 4]);
            mylist.add(fpage);
            SimpleAdapter mSchedule = new SimpleAdapter(this, mylist,
                        R.layout.account_list, new String[] {
                                "id", "name", "gm"
                        },
                        new int[] {
                                R.an.id, R.an.name, R.an.gm
                        });
            mSchedule.notifyDataSetChanged();
            Account_List.setAdapter(mSchedule);
            x = x + 7;
            i++;

        }

        Account_List.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
            	opendialog(Account_name[pos], Account_id[pos]);
            }
        });

    }
    
	public void opendialog(final String name, final String id) {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setMessage("What would you like to do?");
		alertbox.setCancelable(true);
		alertbox.setPositiveButton("Edit",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						openAccount(name, id);
					}
				});

		alertbox.setNegativeButton("Delete",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						deleteAccount(name);
					}
				});
		alertbox.show();
	}
	
	// Double checks you want to delete the account
	public void deleteAccount(final String name) {
		AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
		alertbox.setMessage("Are you sure you want to delete");
		alertbox.setCancelable(true);
		alertbox.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface arg0, int arg1) {
						Mangos.deleteAccount(name);
						loadAccounts();
                		toastit("Account " + name + " Deleted");
					}
				});

		alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface arg0, int arg1) {
			}
		});
		alertbox.show();
	}


    // Opens up the account Page
    public boolean openAccount(String name, String id) {
        Intent intent = new Intent(this, Account.class);
        Bundle b = new Bundle();
        b.putString("id", id);
        b.putString("name", name);
        intent.putExtras(b);
        startActivity(intent);
        return (true);
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
    
    // adds account
    public void addBox() {
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        final EditText mess = new EditText(this);
        mess.setHint("Enter username");
        fl.addView(mess);
        final EditText pass1 = new EditText(this);
        pass1.setHint("Enter password");
        fl.addView(pass1);
        final EditText pass2 = new EditText(this);
        pass2.setHint("Enter password again");
        fl.addView(pass2);        
        sv.addView(fl);
        new AlertDialog.Builder(this)
                .setView(sv)
                .setTitle("Create an Account")
                .setPositiveButton("Send",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                            	String password1 = pass1.getText().toString();
                            	String password2 = pass2.getText().toString();
                            	if(password1.equals(password2)){
                            		Mangos.createAccount(mess.getText().toString(), password1);
                            		loadAccounts();
                            		toastit("Account Added");
                            	}else{
                            		toastit("The Passwords don't match");
                            	}
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

    // sets PlayerLimit
    public void limitBox() {
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        TextView mess_text = new TextView(this);
        mess_text.setText("Set the player limit");
        fl.addView(mess_text);
        final EditText mess = new EditText(this);
        fl.addView(mess);
        sv.addView(fl);
        new AlertDialog.Builder(this)
                .setView(sv)
                .setTitle("")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        toastit(Mangos
                                .setPlayerLimit(mess.getText().toString()));
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

    // send Commands
    public void sendBox() {
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        TextView mess_text = new TextView(this);
        mess_text.setText("Type in any RA Command");
        fl.addView(mess_text);
        final EditText mess = new EditText(this);
        fl.addView(mess);
        sv.addView(fl);
        new AlertDialog.Builder(this)
                .setView(sv)
                .setTitle("")
                .setPositiveButton("Send",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface d, int which) {
                                displayBox(Mangos.sendCommand(mess.getText()
                                        .toString()));
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

    // Displays message from Server
    public void displayBox(String reply) {
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        TextView mess_text = new TextView(this);
        mess_text.setText(reply);
        fl.addView(mess_text);
        sv.addView(fl);
        new AlertDialog.Builder(this).setView(sv).setTitle("")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        d.dismiss();
                    }
                }).create().show();
    }

    // sets MOTO
    public void motoBox() {
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        TextView mess_text = new TextView(this);
        mess_text.setText("Set your Moto");
        fl.addView(mess_text);
        String current = Mangos.getMotd();
        String[] separated = current.split(":");
        current = separated[1].trim();
        final EditText mess = new EditText(this);
        mess.setText(current);
        fl.addView(mess);
        sv.addView(fl);
        new AlertDialog.Builder(this)
                .setView(sv)
                .setTitle("")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        toastit(Mangos.setMotd(mess.getText().toString()));
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

    // Makes an AnnounceMent
    public void announceBox() {
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        TextView mess_text = new TextView(this);
        mess_text.setText("What do you want to say");
        fl.addView(mess_text);
        final EditText mess = new EditText(this);
        fl.addView(mess);
        sv.addView(fl);
        new AlertDialog.Builder(this)
                .setView(sv)
                .setTitle("")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        Mangos.announce(mess.getText().toString());
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
    
    // sends out an onscreen display
    public void notifyBox() {
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        TextView mess_text = new TextView(this);
        mess_text.setText("What do you want to say?");
        fl.addView(mess_text);
        final EditText mess = new EditText(this);
        fl.addView(mess);
        sv.addView(fl);
        new AlertDialog.Builder(this)
                .setView(sv)
                .setTitle("")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        Mangos.notify(mess.getText().toString());
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

    // restart Server **This Functions Needs to be added
    public void restartBox() {
        ScrollView sv = new ScrollView(this);
        LinearLayout fl = new LinearLayout(this);
        fl.setOrientation(LinearLayout.VERTICAL);
        TextView time_text = new TextView(this);
        time_text.setText("Enter how many Seconds before the server restarts");
        fl.addView(time_text);
        EditText time_secs = new EditText(this);
        fl.addView(time_secs);
        sv.addView(fl);
        new AlertDialog.Builder(this).setView(sv).setTitle("")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int which) {
                        d.dismiss();
                    }
                }).create().show();
    }

    // Shutdown Dialog Box
    public void shutdownBox() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure you want to Shutdown Mangos?")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface d, int which) {
                        toastit(Mangos.shutdownNow());
                        d.dismiss();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int which) {
                                d.dismiss();
                            }
                        }).create().show();

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
            Message.setText(Mangos.getInfo());
        }
        if (Selectedtab == 2) {
            loadAccounts();
        }
        if (Selectedtab == 3) {
            loadOnline();
        }
    }
}
