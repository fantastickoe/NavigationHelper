package courseEE202C.navigationhelper;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xively.android.service.IHttpService;
import com.xively.android.service.Response;

public class ShowMapActivity extends Activity {
	private static final String TAG = "ShowMapActivity";
	IHttpService service;
	HttpServiceConnection connection;
	Button startButton,Button2,Button3,Button4;
	TextView testTextView;
		
	// ------------- SET THE FOLLOWING ACCORDINGLY ----------------------

	// DEFINE YOUR CREDENTIALS BELOW (TODO: make these configurable)
	private final String myApiKey = "hqgIruCjRLbn0LnoNRcGDqXfafXhi4WnR6ylj4p3F2eqbnUf";
	private final int myFeedId = 17449011; 

	// a datastream id which belongs to myFeedId
	private final String myDatastreamId = "navigation";
	// a datapoint to be created on myDatastreamId
	private final String myNewDatapoint = "{ \"datapoints\":[ {\"at\":\"2010-05-20T11:01:43Z\",\"value\":\"294\"} ] }";
	private final String myNewDatapoint1 = "{ \"datapoints\": \"current_value\" : \"400\" }";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initService();
		setContentView(R.layout.activity_show_map);
		startButton=(Button)findViewById(R.id.button1);
		startButton.setOnClickListener(new commlistener());
		
		Button2=(Button)findViewById(R.id.button2);
		Button2.setOnClickListener(new testlistener());

		
		Button3=(Button)findViewById(R.id.button3);
		Button3.setOnClickListener(new demolistener1());
		
		Button4=(Button)findViewById(R.id.button4);
		Button4.setOnClickListener(new demolistener2());

		testTextView=(TextView)findViewById(R.id.textView1);
		
		Paint paint = new Paint();
		
		Bitmap bg = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bg); 

		paint.setColor(Color.parseColor("#CD5C5C"));
		float width=(float) 1.8;
		float height=(float) 3;
		canvas.drawRect(0, 0, 9*width, 226*height, paint); 
		canvas.drawRect(0, 14*height, 264*width, 23*height, paint); 
		canvas.drawRect(228*width, 14*height, 237*width, 226*height, paint); 
		canvas.drawRect(0, 197*height, 237*width, 206*height, paint); 
		canvas.drawRect(228*width, 209*height, 264*width, 218*height, paint); 
		//hallway
		canvas.drawRect(107*width, 183*height, 130*width, 219*height, paint); 
		
		//classroom left
		canvas.drawRect(11*width, 208*height, 33*width, 236*height, paint);
		canvas.drawRect(0*width, 218*height, 33*width, 220*height, paint);
		canvas.drawRect(14*width, 200*height, 16*width, 208*height, paint);
		//classroom right
		canvas.drawRect(179*width, 208*height, 202*width, 231*height, paint);
		canvas.drawRect(181*width, 205*height, 183*width, 209*height, paint);
		canvas.drawRect(198*width, 205*height, 200*width, 209*height, paint);
		
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll1);
		ll.setBackgroundDrawable(new BitmapDrawable(bg));
	}
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		releaseService();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class testlistener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TextView resultField = (TextView) findViewById(R.id.textView1);
			Response response = null;
			try
			{
				
				response = service.getDatastream(myFeedId, myDatastreamId);
				
			} catch (RemoteException e)
			{
				Log.e(ShowMapActivity.TAG, "onClick failed", e);
			}

			if (response != null)
			{
				try {
					JSONObject JO=new JSONObject(response.getContent());
					resultField.setText(JO.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					resultField.setText("parsing error");
					e.printStackTrace();
				} 				
				
			}			
		}
	}
	class demolistener1 implements OnClickListener{

		TextView resultField = (TextView) findViewById(R.id.textView1);
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Response response = null;
			try
			{
				service.setApiKey(myApiKey);
				response = service.createDatapoint(myFeedId, myDatastreamId, myNewDatapoint1);
			} catch (RemoteException e)
			{
				Log.e(ShowMapActivity.TAG, "onClick failed", e);
			}

			if (response != null)
			{
				resultField.setText(response.getContent());
			}

		}
		
	}
	class demolistener2 implements OnClickListener{

		TextView resultField = (TextView) findViewById(R.id.textView1);
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Response response = null;
			try
			{
				service.setApiKey(myApiKey);
				response = service.listFeed();
			} catch (RemoteException e)
			{
				Log.e(ShowMapActivity.TAG, "onClick failed", e);
			}

			if (response != null)
			{
				resultField.setText(response.getContent());
			}

		}
		
	}

	class commlistener implements OnClickListener{

		
		
		@Override
		public void onClick(View v) {
			
			
			
			
			// TODO Auto-generated method stub
			TextView resultField = (TextView) findViewById(R.id.textView1);
			Response response = null;
			try
			{
				//JSONObject JO=new JSONObject();
				
				
				response = service.getDatastream(myFeedId, myDatastreamId);
			} catch (RemoteException e)
			{
				Log.e(ShowMapActivity.TAG, "onClick failed", e);
			}

			if (response != null)
			{
				try {
					JSONObject J=new JSONObject(response.getContent());
					String J1=J.getString("current_value");
					JSONObject result=new JSONObject(J1);
					int n=Integer.parseInt(result.getString("number"));
					JSONArray data=new JSONArray(result.getString("data"));
					if(n<=1){
						Log.e(ShowMapActivity.TAG, "number too small");
					}
					
					Paint green=new Paint();
					green.setColor(Color.rgb(0x99, 0xcc, 0));
					float width=(float) 1.8;
					float height=(float) 3;
					
					Paint paint = new Paint();
					paint.setColor(Color.parseColor("#CD5C5C"));
					Bitmap bg = Bitmap.createBitmap(480, 800, Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(bg); 
					//canvas.drawCircle(200, 200, 200, green);
					//canvas.drawCircle(200, 200, 200, red);
					canvas.drawRect(0, 0, 9*width, 226*height, paint); 
					canvas.drawRect(0, 14*height, 264*width, 23*height, paint); 
					canvas.drawRect(228*width, 14*height, 237*width, 226*height, paint); 
					canvas.drawRect(0, 197*height, 237*width, 206*height, paint); 
					canvas.drawRect(228*width, 209*height, 264*width, 218*height, paint); 
					//hallway
					canvas.drawRect(107*width, 183*height, 130*width, 219*height, paint); 
					
					//classroom left
					canvas.drawRect(11*width, 208*height, 33*width, 236*height, paint);
					canvas.drawRect(0*width, 218*height, 33*width, 220*height, paint);
					canvas.drawRect(14*width, 200*height, 16*width, 208*height, paint);
					//classroom right
					canvas.drawRect(179*width, 208*height, 202*width, 231*height, paint);
					canvas.drawRect(181*width, 205*height, 183*width, 209*height, paint);
					canvas.drawRect(198*width, 205*height, 200*width, 209*height, paint);
					
					for (int i=0;i<n-1;i++){
						JSONObject coord1=(JSONObject) data.get(i);
						JSONObject coord2=(JSONObject) data.get(i+1);
						
						resultField.setText(result.getString("number"));
						Log.v("ShowMapActivity", "begin drawing");
						
						Integer x1=Integer.parseInt(coord1.getString("x"));
						Integer y1=237-Integer.parseInt(coord1.getString("y"));
						Integer x2=Integer.parseInt(coord2.getString("x"));
						Integer y2=237-Integer.parseInt(coord2.getString("y"));
						
						Log.v("ShowMapActivity", x1.toString());
						Log.v("ShowMapActivity", x2.toString());
						Log.v("ShowMapActivity", y1.toString());
						Log.v("ShowMapActivity", y2.toString());
						//canvas.drawCircle(200, 200, 200, green);
						canvas.drawRect(x1*width, y1*height, (x2+2)*width, (y2-2)*height, green); 
						//canvas.drawLine(x1*width, y1*height, x2*width, y2*height, green);
						//canvas.drawRect(0, 14*height, 264*width, 23*height, green);
						
					}
					LinearLayout ll = (LinearLayout) findViewById(R.id.ll1);
					ll.setBackgroundDrawable(new BitmapDrawable(bg));
					
					//resultField.append(n);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
			}
		}
		
	}
	
	private void initService()
	{
		connection = new HttpServiceConnection();
		Intent i = new Intent("com.xively.android.service.HttpService");
		boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
		Log.d(TAG, "initService() bound with " + ret);
	}

	/**
	 * Unbinds this activity from the service.
	 */
	private void releaseService()
	{
		unbindService(connection);
		connection = null;
		Log.d(TAG, "releaseService() unbound.");
	}

	class HttpServiceConnection implements ServiceConnection
	{
		public void onServiceConnected(ComponentName name, IBinder boundService)
		{
			service = IHttpService.Stub.asInterface((IBinder) boundService);
			Log.d(ShowMapActivity.TAG, "onServiceConnected() connected");
			Toast.makeText(ShowMapActivity.this, "Service connected", Toast.LENGTH_LONG).show();
		}

		public void onServiceDisconnected(ComponentName name)
		{
			service = null;
			Log.d(ShowMapActivity.TAG, "onServiceDisconnected() disconnected");
			Toast.makeText(ShowMapActivity.this, "Service connected", Toast.LENGTH_LONG).show();
		}
	}

}
