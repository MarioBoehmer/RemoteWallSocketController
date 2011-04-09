package devorama.marioboehmer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class WallSocketControllerActivity extends Activity {
	
	private static final String TAG = WallSocketControllerActivity.class.getSimpleName();
	private static final String SOCKET_IP_ADDRESS = "192.168.0.100"; 
	private static final int SOCKET_PORT = 1234;
	private Socket s;
	private OutputStream os;
	private DataOutputStream dos;
	private ToggleButton tb = null;
	private boolean isConnected;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		tb = (ToggleButton) findViewById(R.id.onOffButton);
		tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (dos != null) {
					if (isChecked) {
						try {
							dos.writeByte(1);
							dos.flush();
						} catch (IOException e) {
							Log.e(TAG, "IOException: ", e);
						}
					} else {
						try {
							dos.writeByte(0);
							dos.flush();
						} catch (IOException e) {
							Log.e(TAG, "IOException: ", e);
						}
					}
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!isConnected) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						s = new Socket(SOCKET_IP_ADDRESS, SOCKET_PORT);
						os = s.getOutputStream();
						dos = new DataOutputStream(os);
						isConnected = true;
					} catch (UnknownHostException e) {
						Log.e(TAG, "Unknown Host: ", e);
					} catch (IOException e) {
						Log.e(TAG, "IOException: ", e);
					}

				}
			}).start();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(isConnected) {
			if(dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					Log.e(TAG, "IOException: ", e);
				}
			}
			if(os != null) {
				try {
					os.close();
				} catch (IOException e) {
					Log.e(TAG, "IOException: ", e);
				}
			}
			if(s != null) {
				try {
					s.close();
				} catch (IOException e) {
					Log.e(TAG, "IOException: ", e);
				}
			}
		}
	}
}