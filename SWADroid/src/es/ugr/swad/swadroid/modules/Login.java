/*
 *  This file is part of SWADroid.
 *
 *  Copyright (C) 2010 Juan Miguel Boyero Corral <juanmi1982@gmail.com>
 *
 *  SWADroid is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  SWADroid is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with SWADroid.  If not, see <http://www.gnu.org/licenses/>.
 */

package es.ugr.swad.swadroid.modules;

import android.os.Bundle;
import android.util.Log;
import es.ugr.swad.swadroid.Base64;
import es.ugr.swad.swadroid.Global;
import es.ugr.swad.swadroid.R;
import es.ugr.swad.swadroid.model.User;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Login module for connect to SWAD.
 * @author Juan Miguel Boyero Corral <juanmi1982@gmail.com>
 */
public class Login extends Module {
    /**
     * Digest for user password.
     */
    private MessageDigest md;
    /**
     * User password.
     */
    private String userPassword;
    /**
     * Login tag name for Logcat
     */
    public static final String TAG = Global.APP_TAG + " Login";

    /* (non-Javadoc)
	 * @see android.app.Activity#onCreate()
	 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMETHOD_NAME("loginByUserPasswordKey");
    }

    /* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();      
        connect();
	}

	/**
     * Launches action in a separate thread while shows a progress dialog
     * in UI thread.
     */
    protected void connect() {
    	String progressDescription = getString(R.string.loginProgressDescription);
    	int progressTitle = R.string.loginProgressTitle;
    	Connect con = new Connect(false, progressDescription, progressTitle, true);
    	
    	/*if(!Global.isLogged())
    		Toast.makeText(this, progressDescription, Toast.LENGTH_LONG).show();*/
    	
        con.execute();
    }

    /**
     * Connects to SWAD and gets user data.
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws XmlPullParserException
     * @throws SoapFault
     * @throws InstantiationException 
     * @throws IllegalAccessException 
     */
    protected void requestService()
            throws NoSuchAlgorithmException, IOException, XmlPullParserException, SoapFault, IllegalAccessException, InstantiationException {

    	//If last login time > Global.RELOGIN_TIME, force login
    	if(System.currentTimeMillis()-Global.getLastLoginTime() > Global.RELOGIN_TIME) {
    		Global.setLogged(false);
    	}
    	
    	//If the application isn't logged, force login
    	if(!Global.isLogged())
    	{
	        //Encrypts user password with SHA-512 and encodes it to Base64UrlSafe   	
	        md = MessageDigest.getInstance("SHA-512");
	        md.update(prefs.getUserPassword().getBytes());
	        userPassword = new String(Base64.encodeBytes(md.digest()));
	        userPassword = userPassword.replace('+','-').replace('/','_').replace('=', ' ').replaceAll("\\s+", "").trim();
	        Log.i("Login", "pre send login");
	        //Creates webservice request, adds required params and sends request to webservice
	        createRequest();
	        addParam("userID", prefs.getUserID());
	        addParam("userPassword", userPassword);
	        addParam("appKey", Global.getAppKey());
	        sendRequest(User.class, true);
	        Log.i("Login", "sended login");
	        if (result != null) {
	        	SoapObject soap = (SoapObject) result;
	        	
		        //Stores user data returned by webservice response
		        User.setUserCode(soap.getProperty("userCode").toString());
		        User.setWsKey(soap.getProperty("wsKey").toString());
		        User.setUserID(soap.getProperty("userID").toString());
		        User.setUserSurname1(soap.getProperty("userSurname1").toString());
		        User.setUserSurname2(soap.getProperty("userSurname2").toString());
		        User.setUserFirstName(soap.getProperty("userFirstname").toString());
		        int userRole =Integer.parseInt(soap.getProperty("userRole").toString());
		        User.setUserRole(userRole);
		        
		        //Update application last login time
		        Global.setLastLoginTime(System.currentTimeMillis());
	        }
    	}
    	
    	/*if(isDebuggable) {
    		Log.d(TAG, "userCode=" + User.getUserCode());
    		//Log.d(TAG, "userTypeCode=" + User.getUserTypeCode());
    		Log.d(TAG, "wsKey=" + User.getWsKey());
    		Log.d(TAG, "userID=" + User.getUserID());
    		Log.d(TAG, "userSurname1=" + User.getUserSurname1());
    		Log.d(TAG, "userSurname2=" + User.getUserSurname2());
    		Log.d(TAG, "userFirstName=" + User.getUserFirstName());
    		//Log.d(TAG, "userTypeName=" + User.getUserTypeName());
    		Log.d(TAG, "userRole=" + String.valueOf(User.getUserRole()));
    		Log.d(TAG, "lastLoginTime=" + Global.getLastLoginTime());
    	}*/
    	
        //Request finalized without errors
        setResult(RESULT_OK);
	}

	@Override
	protected void postConnect() {
        finish();
	}
}
