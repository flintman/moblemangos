/* 
 * Copyright (C) 2007-2012 Caledonia Computers <http://www.caledoniacomputers.com>
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
 * MODIFIED BY flintmancomputers
 */
package com.mangos;

import java.util.StringTokenizer;
import com.mangos.SocketConnector;

/**
 * @author Alistair Neil, Caledonia Computers <info@caledoniacomputers.com>
 *         MODIFIED by flintmancomputers
 */
public final class MangosTelnet extends SocketConnector {

    final static String prompt = "s>";

    /**
     * Initiates a mangos telnet session
     */
    public String Login(String host, String sport, String user, String pass) {
        String result = null;
        try {
            setWaitPeriod(5000);
            setPrompt(":");
            connect(host, Integer.parseInt(sport));
            result = send(user);
            if (result.contains("-No such user.")) {
                return "User not found.";
            }
            setPrompt(prompt);
            result = send(pass);
            if (result.contains("-Wrong pass.")) {
                return "Incorrect password.";
            }
            return result;
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    /**
     * Sends message to mangos server
     * 
     * @param msg The message to be sent
     * @param waitms Override default wait period
     */
    private String sendMangos(String msg, long waitms) {
        long lngWait = getWaitPeriod();
        setWaitPeriod(waitms);
        String result = sendMangos(msg);
        setWaitPeriod(lngWait);
        return result;
    }

    /**
     * Sends message to mangos server
     * 
     * @param msg The message to be sent
     */
    private String sendMangos(String msg) {
        try {
            String result = send(msg);
            if (result == null) {
                return "Error: Null result.";
            }
            if (result.length() == 0) {
                return result;
            }
            if (result.contains("mangos>")) {
                result = result.substring(0, result.length() - 7);
            }
            return result;
        } catch (Exception ex) {
            return "Error : " + ex.getMessage();
        }
    }

    /**
     * Gives ability to send any RA command from a command line
     * 
     * @return Returns the server reply
     */
    public String sendCommand(String command) {
        return sendMangos(command);
    }

    /**
     * Sets the specified character for customisation at next login
     * 
     * @param charname The character name
     * @return Returns the server reply
     */
    public String setCharCustomise(String charname) {
        return sendMangos("character customize " + charname);
    }

    /**
     * Sets the specified character for rename at next login
     * 
     * @param charname The character name
     * @return Returns the server reply
     */
    public String setCharRename(String charname) {
        return sendMangos("character rename " + charname);
    }

    /**
     * Sets the specified character level
     * 
     * @param charname The character name
     * @param level The characters level
     * @return Returns the server reply
     */
    public String setCharLevel(String charname, String level) {
        return sendMangos("character level " + charname + " " + level);
    }

    /**
     * Sets Message of the day on the mangos server
     * 
     * @param msg The message to be sent
     * @return Returns the server reply
     */
    public String setMotd(String msg) {
        return sendMangos("server set motd " + msg);
    }

    /**
     * Gets Message of the day from the mangos server
     * 
     * @return Returns the server reply
     */
    public String getMotd() {
        return sendMangos("server motd");
    }

    /**
     * Reloads scriptdev
     * 
     * @return Returns the server reply
     */
    public String loadScripts(String scriptname) {
        return sendMangos("loadscripts " + scriptname);
    }

    /**
     * Broadcasts a system message on online chat log
     * 
     * @param msg The message to be broadcast
     * @return Returns the server reply
     */
    public String announce(String msg) {
        if (msg.length() == 0) {
            return null;
        }
        return sendMangos("announce " + msg);
    }

    /**
     * Broadcasts a system message on screen
     * 
     * @param msg The message to be broadcast
     * @return Returns the server reply
     */
    public String notify(String msg) {
        if (msg.length() == 0) {
            return null;
        }
        return sendMangos("notify " + msg);
    }

    /**
     * Gets the mangos version
     * 
     * @return Returns the version of mangos being run
     */
    public String getVersion() {
        return sendMangos("version", 1000);
    }

    /**
     * Gets the server info
     * 
     * @return Returns server info
     */
    public String getInfo() {
        return sendMangos("server info", 1000);
    }

    /**
     * Gets the player limit
     * 
     * @return Returns player limit
     */
    public String getPlayerLimit() {
        return sendMangos("server plimit", 1000);
    }

    /**
     * Sets the player limit
     * 
     * @param limit The player limit
     * @return Returns the server reply
     */
    public String setPlayerLimit(String limit) {
        return sendMangos("server plimit " + limit, 1000);
    }

    /**
     * Tells server to shutdown after a specified delay as long as server is
     * idle
     * 
     * @param delay The delay in seconds
     */
    public String idleShutdown(String delay) {
        setPrompt(null);
        sendMangos("server idleshutdown " + delay);
        setPrompt(prompt);
        return null;
    }

    /**
     * Tells server to shutdown after a specified delay
     * 
     * @param delay The delay in seconds
     */
    public String shutdown(String delay) {
        setPrompt(null);
        sendMangos("server shutdown " + delay);
        setPrompt(prompt);
        return null;
    }

    /**
     * Tells server to shutdown immediately
     */
    public String shutdownNow() {
        setPrompt(null);
        sendMangos("server exit");
        setPrompt(prompt);
        return null;
    }

    /**
     * Create a new user account
     * 
     * @param username The name of the account
     * @param password The password for this account
     * @return Returns the server reply
     */
    public String createAccount(String username, String password) {
        return sendMangos("account create " + username + " " + password);
    }

    /**
     * Delete a user account
     * 
     * @param username The name of the account
     * @return Returns the server reply
     */
    public String deleteAccount(String username) {
        return sendMangos("account delete " + username);
    }

    /**
     * Kick a character off the server
     * 
     * @param charname The name of the character to kick
     */
    public String kickChar(String charname) {
        setPrompt(null);
        sendMangos("kick " + charname);
        setPrompt(prompt);
        return null;
    }

    /**
     * Dump a characters sql data to a file
     * 
     * @param filename The name of the file
     * @param charnameorguid The name or guid of the character to dump
     */
    public String pDumpWrite(String filename, String charnameorguid) {
        return sendMangos("pdump write " + filename + " " + charnameorguid);
    }

    /**
     * Dump a characters sql data to a file
     * 
     * @param filename The name of the file
     * @param acctname The name of the account to load character to
     * @param newcharname The new character name, optional
     */
    public String pDumpLoad(String filename, String acctname, String newcharname) {
        return sendMangos("pdump load " + filename + " " + acctname + " "
                + newcharname);
    }

    /**
     * Ban a user account
     * 
     * @param acct The name of the account
     * @param reason The reason for the ban
     * @param duration The duration of the ban
     * @return Returns the server reply
     */
    public String banAcct(String acct, String reason, String duration) {
        return sendMangos("ban account " + acct + " " + duration + " \""
                + reason + "\"");
    }

    /**
     * Ban a user account based on the ip
     * 
     * @param ipadd The ipaddress of the user
     * @param reason The reason for the ban
     * @param duration The duration of the ban
     * @return Returns the server reply
     */
    public String banIP(String ipadd, String reason, String duration) {
        return sendMangos("ban ip " + ipadd + " " + duration + " \"" + reason
                + "\"");
    }

    /**
     * Unban a user account
     * 
     * @param acct The name of the account
     * @return Returns the server reply
     */
    public String unbanAcct(String acct) {
        return sendMangos("unban account " + acct);
    }

    /**
     * Unban an ip address
     * 
     * @param ipadd The ip address
     * @return Returns the server reply
     */
    public String unbanIP(String ipadd) {
        return sendMangos("unban ip " + ipadd);
    }

    /**
     * Ban user account with this character
     * 
     * @param character The character name
     * @param reason The reason for the ban
     * @param duration The duration of the ban
     * @return Returns the server reply
     */
    public String banChar(String character, String reason, String duration) {
        return sendMangos("ban character " + character + " " + duration + " \""
                + reason + "\"");
    }

    /**
     * Unban account with this character
     * 
     * @param character The character name
     * @return Returns the server reply
     */
    public String unbanChar(String character) {
        return sendMangos("unban character " + character);
    }

    /**
     * Delete a character
     * 
     * @param character The character name
     * @return Returns the server reply
     */
    public String deleteChar(String character) {
        return sendMangos("character erase " + character);
    }

    /**
     * Reset a character option
     * 
     * @param character The character name
     * @param option The option to reset
     * @return Returns the server reply
     */
    public String resetCharOption(String character, String option) {
        return sendMangos("reset " + option + " " + character);
    }

    /**
     * Set the privileges of this account
     * 
     * @param acct The account name
     * @param level The privilege level 0,1,2,3
     * @return Returns the server reply
     */
    public String setGM(String acct, String level) {
        return sendMangos("account set gmlevel " + acct + " " + level, 1000);
    }

    /**
     * Triggers a corpse clear out
     * 
     * @return Returns the server reply
     */
    public String clearCorpses() {
        return sendMangos("server corpses", 1000);
    }

    /**
     * Save all player data
     * 
     * @return Returns the server reply
     */
    public String saveAll() {
        return sendMangos("saveall", 30000);
    }

    /**
     * Set the password of specified account
     * 
     * @param acct The account name
     * @param passwd The password
     * @return Returns the server reply
     */
    public String setPassword(String acct, String passwd) {
        return sendMangos("account set password " + acct + " " + passwd + " "
                + passwd, 1000);
    }

    /**
     * Reload specified table
     * 
     * @param tablename The table you wish reloaded
     * @return Returns the server reply
     */
    public String reloadTable(String tablename) {
        return sendMangos("reload " + tablename);
    }

    /**
     * Request help info
     * 
     * @param property The property you require help on
     * @return Returns the server reply
     */
    public String help(String property) {
        return sendMangos("help " + property, 1000);
    }

    /**
     * Get ticket
     * 
     * @param charname The character name
     * @return Returns the server reply
     */
    public String getTicket(String charname) {
        return sendMangos("ticket " + charname);
    }

    /**
     * Delete ticket
     * 
     * @param charname The character name
     * @return Returns the server reply
     */
    public String deleteTicket(String charname) {
        return sendMangos("delticket " + charname);
    }

    /**
     * Revive a character
     * 
     * @param charname The character name
     * @return Returns the server reply
     */
    public String revive(String charname) {
        String result = sendMangos("revive " + charname);
        if (result.length() == 0) {
            return charname + " successfully revived.";
        }
        return result;
    }

    /**
     * Repair a characters items
     * 
     * @param charname The account name
     * @return Returns the server reply
     */
    public String repairItems(String charname) {
        return sendMangos("repairitems " + charname);
    }

    /**
     * Set the expansion flag for this account
     * 
     * @param acct The account name
     * @param expansion The expansion value 0 = normal, 1 = TBC, 2 = WOTLK
     * @return Returns the server reply
     */
    public String setExpansion(String acct, String expansion) {
        return sendMangos("account set addon " + acct + " " + expansion);
    }

    /**
     * Send a message to a character
     * 
     * @param charname The character
     * @param msg The message
     * @return Returns the server reply
     */
    public String sendMessage(String charname, String msg) {
        if (msg.length() == 0) {
            return null;
        }
        return sendMangos("send message " + charname + " " + msg, 1000);
    }

    /**
     * Send mail to a character
     * 
     * @param charname The character
     * @param subject The subject
     * @param text The mail text
     * @return Returns the server reply
     */
    public String sendMail(String charname, String subject, String text) {
        if (subject.length() == 0 || subject == null) {
            subject = "No Subject";
        }
        if (text.length() == 0 || text == null) {
            text = "No Message";
        }
        return sendMangos("send mail " + charname + " \"" + subject + "\" \""
                + text + "\"");
    }

    /**
     * Send mail to a character with money
     * 
     * @param charname The character
     * @param subject The subject
     * @param text The mail text
     * @param money The amount of money
     * @return Returns the server reply
     */
    public String sendGold(String charname, String subject, String text,
            String money) {
        if (subject.length() == 0 || subject == null) {
            subject = "No Subject";
        }
        if (text.length() == 0 || text == null) {
            text = "No Message";
        }
        return sendMangos("send money " + charname + " \"" + subject + "\" \""
                + text + "\" " + money);
    }

    /**
     * Send mail to a character with item
     * 
     * @param charname The character
     * @param subject The subject
     * @param text The mail text
     * @param itemids multiple string of item ids
     * @return Returns the server reply
     */
    public String sendItems(String charname, String subject, String text,
            String[] itemids) {

        String strItemId = "";
        if (subject.length() == 0 || subject == null) {
            subject = "No Subject";
        }
        if (text.length() == 0 || text == null) {
            text = "No Message";
        }
        for (String s : itemids) {
            strItemId += (s + " ");
        }
        return sendMangos("send items " + charname + " \"" + subject + "\" \""
                + text + "\" " + strItemId);
    }

    /**
     * Send mail to a character containing items
     * 
     * @param charname The character
     * @param subject The subject
     * @param text The mail text
     * @param itemids string of space separated ids
     * @return Returns the server reply
     */
    public String sendItems(String charname, String subject, String text,
            String itemids) {
        if (subject.length() == 0 || subject == null) {
            subject = "No Subject";
        }
        if (text.length() == 0 || text == null) {
            text = "No Message";
        }
        return sendMangos("send items " + charname + " \"" + subject + "\" \""
                + text + "\" " + itemids);
    }

    /**
     * Send mass mail to a race or faction
     * 
     * @param racefaction The race or faction
     * @param subject The subject
     * @param text The mail text
     * @return Returns the server reply
     */
    public String sendMassMail(String racefaction, String subject, String text) {
        if (subject.length() == 0 || subject == null) {
            subject = "No Subject";
        }
        if (text.length() == 0 || text == null) {
            text = "No Message";
        }
        return sendMangos("send mass mail " + racefaction.toLowerCase() + " \""
                + subject + "\" \"" + text + "\"");
    }

    /**
     * Send mass mail to a race or faction containing money
     * 
     * @param racefaction The race or faction
     * @param subject The subject
     * @param text The mail text
     * @param money The amount of money
     * @return Returns the server reply
     */
    public String sendMassGold(String racefaction, String subject, String text,
            String money) {
        if (subject.length() == 0 || subject == null) {
            subject = "No Subject";
        }
        if (text.length() == 0 || text == null) {
            text = "No Message";
        }
        return sendMangos("send mass money " + racefaction.toLowerCase()
                + " \"" + subject + "\" \"" + text + "\" " + money);
    }

    /**
     * Send mass mail to a race or faction containing items
     * 
     * @param racefaction The race or faction
     * @param subject The subject
     * @param text The mail text
     * @param itemids string of space separated ids
     * @return Returns the server reply
     */
    public String sendMassItems(String racefaction, String subject,
            String text, String itemids) {
        if (subject.length() == 0 || subject == null) {
            subject = "No Subject";
        }
        if (text.length() == 0 || text == null) {
            text = "No Message";
        }
        return sendMangos("send mass items " + racefaction.toLowerCase()
                + " \"" + subject + "\" \"" + text + "\" " + itemids);
    }

    /**
     * Teleport a character to a location
     * 
     * @param charname The character
     * @param location The location name
     * @return Returns the server reply
     */
    public String teleport(String charname, String location) {
        return sendMangos("tele name " + charname + " " + location);
    }

    /**
     * Get All Accounts **Added by Flintman
     * 
     * @return Returns the Account array
     */
    public String[] getAccounts(String name) {
        String reply = sendMangos("lookup account name " + name);
        reply.replace("\r", "").trim();
        StringTokenizer tokens = new StringTokenizer(reply, "|");
        int x = 0;
        int i = 0;
        String reply_array[];
        reply_array = new String[tokens.countTokens() + 1];
        while (tokens.hasMoreTokens()) {
            if (x > 5) {
                reply_array[i] = tokens.nextToken().trim();
                i++;
            }
            x++;
        }
        return reply_array;
    }

    /**
     * Get All Characters for an account **Added by Flintman
     * 
     * @param id The id of the account you want to get all characters.
     * @return Returns the Account array
     */
    public String[] getCharacters(String id) {
        String reply = sendMangos("account characters " + id);
        reply.replace("\r", "").trim();
        StringTokenizer tokens = new StringTokenizer(reply, "|");
        int x = 0;
        int i = 0;
        String reply_array[];
        reply_array = new String[tokens.countTokens() + 1];
        while (tokens.hasMoreTokens()) {
            if (x > 5) {
                reply_array[i] = tokens.nextToken().trim();
                i++;
            }
            x++;
        }
        return reply_array;

    }

    /**
     * Searchs an item **Added by Flintman
     * 
     * @param search The item to search
     * @return Returns the item array
     */
    public String[] lookupItem(String search) {
        String reply = sendMangos("lookup item " + search);
        StringTokenizer tokens = new StringTokenizer(reply, "\r");
        int i = 0;
        String reply_array[];
        reply_array = new String[tokens.countTokens()];
        while (tokens.hasMoreTokens()) {
            reply_array[i] = tokens.nextToken().trim();
            i++;
        }
        return reply_array;
    }

    /**
     * Searchs an teleport locatoins **Added by Flintman
     * 
     * @param search The item to search
     * @return Returns the tele array
     */
    public String[] lookupTele(String search) {
        String reply_array[] = null;
        String reply = sendMangos("lookup tele " + search);
        reply = reply.replace("Locations found are:", "");
        StringTokenizer tokens = new StringTokenizer(reply, "\n");
        int i = 0;
        reply_array = new String[tokens.countTokens()];
        while (tokens.hasMoreTokens()) {
            reply_array[i] = tokens.nextToken();
            i++;
        }
        return reply_array;
    }

    /**
     * Gets online list **Added by Flintman
     * 
     * @param search The item to search
     * @return Returns the tele array
     */
    public String[] getOnlineList() {
        String reply = sendMangos("account onlinelist");
        reply.replace("\r", "").trim();
        reply = reply.replace("=", "").trim();
        StringTokenizer tokens = new StringTokenizer(reply, "|");
        int x = 0;
        int i = 0;
        String account_array[];
        String character_array[] = null;
        character_array = new String[tokens.countTokens()];
        account_array = new String[tokens.countTokens()];
        while (tokens.hasMoreTokens()) {
            String extract = tokens.nextToken().trim();
            if (x > 7) {
                character_array[i] = tokens.nextToken().trim();
                account_array[i] = extract;
                tokens.nextToken();
                if (tokens.hasMoreTokens())
                    tokens.nextToken();
                if (tokens.hasMoreTokens())
                    tokens.nextToken();
                if (tokens.hasMoreTokens())
                    tokens.nextToken();
                if (tokens.hasMoreTokens())
                    tokens.nextToken();
                else
                    break;
                i++;
            }
            x++;
        }
        return character_array;
    }

    /**
     * Closes the telnet session
     */
    public void closeConnection() {
        try {
            setPrompt("");
            sendMangos("quit");
            super.disconnect();
        } catch (Exception ex) {
        }
    }
}
