//William Bugenis
//This is the main activity for the PCRemote app
//It allows users to send mouse movements and clicks to the computer
//It also allows the user to open a keyboard, which will allow them to send keyboard input.
package com.wbs.pcremote;

import java.io.*;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.bluetooth.*;
import android.widget.EditText;
import android.widget.Toast;
import android.view.inputmethod.*;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;

public class MainActivity extends Activity {
	
	private static final int REQUEST_ENABLE_BT = 1;
	private BluetoothAdapter btAdapter = null;
	private BluetoothSocket btSocket = null;	
	private DataOutputStream dataOut = null;
	private EditText textEdit;	
	
	private static final UUID MY_UUID = UUID.fromString("976a7076-d728-11e3-bab4-1a514932ac01");
	//MAC Address string	
	private static String address = "";
	
	//onCreate sets the Bluetooth Adapter, and makes sure it is activated. 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		
		//Enables Bluetooth if it isn't enabled already.
		if(!btAdapter.isEnabled()) {			
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);				
		}
	}
	
	@Override
	public void onStart(){
		super.onStart();		
	}
	
	//Prompts the user for a MAC address to connect to.
	@Override
	public void onResume(){
		super.onResume();
		/*
		if(address==null){
			btSetup(findViewById(android.R.id.content));
		}	
		*/	
		
			startConnection(findViewById(android.R.id.content));
			
	}
	
	//Closes connections (to preserve battery) and empties the TextEdit used to capture keyboard input	
	@Override
	public void onPause(){
		super.onPause();		     
		
		textEdit.setText("");				
		
		if(dataOut!=null){
			try{
				dataOut.flush();
			}catch (IOException e) {
				Toast.makeText(MainActivity.this, "dataStream flush error" + e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}		
	
		try{		
			dataOut.close();
		} catch (IOException e) {
			Toast.makeText(MainActivity.this, "intStream close error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
		try{			
			btSocket.close();
			Toast.makeText(MainActivity.this, "Connection closed.", Toast.LENGTH_SHORT).show();
		} catch (IOException e){
			Toast.makeText(MainActivity.this, "Socket didn't close properly" + e.getMessage(), Toast.LENGTH_SHORT).show();			
		}		
	}	
	
	@Override
	public void onStop(){
		super.onStop();	
	}
	
	@Override
	public void onDestroy(){		
		super.onDestroy();		
	}
	
	//Starts the Bluetooth connections
	public void startConnection(View view){		
		BluetoothDevice device = btAdapter.getRemoteDevice(address);
		
		try{
			btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
		} catch(IOException e){			
			Toast.makeText(MainActivity.this, "Socket creation failed :" + e.getMessage(), Toast.LENGTH_SHORT).show();
		}
		
		btAdapter.cancelDiscovery();		
		
		try {
			btSocket.connect();			
			Toast.makeText(MainActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
		} catch (IOException e){
			try{
				btSocket.close();
				Toast.makeText(MainActivity.this, "\n Connection error: " + e.getMessage(), Toast.LENGTH_SHORT).show();			
			} catch (IOException e2){				
				Toast.makeText(MainActivity.this, "Socket didn't close properly (onResume)" + e2.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}	
	
		try{
			dataOut = new DataOutputStream(btSocket.getOutputStream());	
		} catch (IOException e) {
			Toast.makeText(MainActivity.this,"Int output stream not created" + e.getMessage(), Toast.LENGTH_SHORT).show();
		}			
	}
	
	//Sends left click signal
	public void sendLeftClick(View view){		
		try{
			//1024 is constant field value for left click in InputEvent class			
			dataOut.writeInt(1024);
			dataOut.flush();			
		} catch (IOException e) {			
			Toast.makeText(MainActivity.this, "LClick failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}			
	}
	
	//Sends right click signal
	public void sendRightClick(View view){		
		try{
			//4096 is constant field value for right click in InputEvent class		
			dataOut.writeInt(4096);
			dataOut.flush();			
		} catch (IOException e) {			
			Toast.makeText(MainActivity.this, "RClick failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}			
	}
	
	//Moves mouse up
	public void sendMouseUp(View view){		
		try{
			//1111 for MouseUp			
			dataOut.writeInt(1111);
			dataOut.flush();			
		} catch (IOException e) {			
			Toast.makeText(MainActivity.this, "MouseUp failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}			
	}	
	
	//Moves mouse right
	public void sendMouseRight(View view){		
		try{
			//2222 for MouseRight				
			dataOut.writeInt(2222);
			dataOut.flush();			
		} catch (IOException e) {			
			Toast.makeText(MainActivity.this, "MouseRight failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}			
	}	
	
	//Moves mouse down
	public void sendMouseDown(View view){		
		try{
			//3333 for MouseDown		
			dataOut.writeInt(3333);
			dataOut.flush();			
		} catch (IOException e) {			
			Toast.makeText(MainActivity.this, "MouseDown failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}			
	}	
	
	//Moves mouse left
	public void sendMouseLeft(View view){		
		try{
			//4444 for MouseLeft			
			dataOut.writeInt(4444);
			dataOut.flush();			
		} catch (IOException e) {			
			Toast.makeText(MainActivity.this, "MouseLeft failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}			
	}
	
	//Moves mouse up and left
	public void sendMouseNW(View view){		
		try{
			//1111 for MouseUp			
			dataOut.writeInt(5555);
			dataOut.flush();			
		} catch (IOException e) {			
			Toast.makeText(MainActivity.this, "MouseNW failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}			
	}	
	
	//Moves mouse up and right
	public void sendMouseNE(View view){		
		try{
			//2222 for MouseRight				
			dataOut.writeInt(6666);
			dataOut.flush();			
		} catch (IOException e) {			
			Toast.makeText(MainActivity.this, "MouseNE failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}			
	}	
	
	//Moves mouse down and right
	public void sendMouseSE(View view){		
		try{
			//3333 for MouseDown		
			dataOut.writeInt(7777);
			dataOut.flush();			
		} catch (IOException e) {			
			Toast.makeText(MainActivity.this, "MouseSE failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}			
	}	
	
	//Moves mouse down and left
	public void sendMouseSW(View view){		
		try{
			//4444 for MouseLeft			
			dataOut.writeInt(8888);
			dataOut.flush();			
		} catch (IOException e) {			
			Toast.makeText(MainActivity.this, "MouseSW failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}			
	}
	
	//Opens the keyboard	
	public void openKeyboard(View view){	
		
		InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        
        //Creates EditText and a TextWatcher to catch keyboard input, then sends keys pressed.
		textEdit = (EditText) findViewById(R.id.textIn);
		textEdit.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        textEdit.addTextChangedListener(new TextWatcher() {
        	public void afterTextChanged(Editable s){
        		char c = s.charAt(s.length()-1);
        		try{
        			dataOut.writeChar(c);
        			dataOut.flush();        			
        		} catch (IOException e) {
        			Toast.makeText(MainActivity.this, "Keyboard input failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        		}
        	}
        	public void beforeTextChanged (CharSequence s, int start, int count, int after){};
        	public void onTextChanged (CharSequence s, int start, int before, int count){};;
        });            
	}
	
	//Will ask user for their MAC Address
	//The user's MAC address is shown in the right click menu of the system tray icon on the PC
	public void btSetup(View view) {		
			AlertDialog.Builder alert = new AlertDialog.Builder(this); 
			alert.setTitle("Welcome!");
			alert.setMessage("Enter your MAC address as shown in right click tray icon menu");
		        
			final EditText input = new EditText(this);		
			alert.setView(input);
		        
			alert.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {				
					Editable addr = input.getText();
					address = addr.toString();
					address = address.toUpperCase(Locale.ENGLISH);
					startConnection(findViewById(android.R.id.content));
				}		       
		    });			
			
		 alert.show(); 		
	}
}

