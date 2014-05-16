package sciatis.player.foreground;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;
/**
 * This  example was created by Sciatis Technologies and belongs to.
 * 
 * Using this samples for teaching/training or distribution requires written approval from Sciatis Technologies.
 * 
 * Sciatis Technologies will not allow the use of this examples besides than development. 
 * 
 * For any questions please contact Gabriel@Proto-Mech.com
 * 
 * @author Gabriel@Proto-Mech.com
 * 
 * ForegroundServicePlayer
 * שירות זה מפעיל את בר העליון הנגלל בנוסף להפעלת השיר
 * כמו כן הוא מאזין למי שקרא לו לכפתור עצור שיר
 * בזמן עצירה שיר יופסק השירות 
 * ובזמן הריגת השירות אנו נשחרר את הבר הנגלל
 * 
 */
public class ForegroundServicePlayer extends Service 
	implements OnPreparedListener, ServicesConsts 
{
	final static int NOTIFICATION_ID = 4521;
	private MediaPlayer player = null;
	private boolean isReady = false;

	//אינר קלאס
	//כאשר המשתמש לוחץ עצור שיר
	//אנו בעצם מקבלים טריגר
	private class StopReceiver extends BroadcastReceiver 
	{
	   //מה לעשות בקבלת הטריגר
		@Override
	    public void onReceive(Context context, Intent intent) 
	    {
	    	//Remove this service from foreground state, allowing it to be killed if more memory is needed.
			stopForeground(true);

			//Stop the service, if it was previously started. This is the same as calling stopService(Intent) for this particular service.
	    	stopSelf();
	    }
	}
	
	//יצרת האוביקט עצריה שמאזין ומחכה לטריגר להפעלה על ידי לחיצץ עצור שיר
	StopReceiver stopReciever = new StopReceiver();

	//בנאי ריק
	public ForegroundServicePlayer() {
		super();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		try {

			player = new MediaPlayer();
			player.setWakeMode(getApplicationContext(),
					PowerManager.PARTIAL_WAKE_LOCK);

			// player = MediaPlayer.create(this, R.raw.rihana_disturbia);
			player.setLooping(false);
			player.setOnPreparedListener(this);
			AssetFileDescriptor fd = getResources().openRawResourceFd(
					R.raw.rihana_disturbia);
			player.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(),
					fd.getLength());
			fd.close();

			isReady = true;
		} catch (Throwable e) {
			Log.e(getClass().getName(), "Fail:", e);
			e.printStackTrace();
		}
		
		//יצרת פילטר לקבלת האזנה ללחיצה כפתור עצור
		IntentFilter intentFilter = 
			new IntentFilter(PLAYER_INTENT);

		registerReceiver(stopReciever, intentFilter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		try {
			if (isReady) {
				player.prepareAsync();
			} else {
				
				
				
				Toast.makeText(this, "Failed to init the Player",
						Toast.LENGTH_LONG).show();
			}
		} catch (Throwable e) {
			Log.e(getClass().getName(), "Fail:", e);
			e.printStackTrace();
		}

		return START_STICKY;//super.onStartCommand(intent, flags, startId);
	}

	// בהריגת השירות
	@Override
	public void onDestroy() {
		// שיחרור הנגן
		if (player != null) {
			if (player.isPlaying()) {
				player.stop();
			}
			player.release();
			player = null;

		}
		
		if(stopReciever != null)
		{
			// שיחרור האזנה 
			//של כפתור העצור
			unregisterReceiver(stopReciever);
		}
		super.onDestroy();
	}

	
	//יצרת מה שבבר העליון הנגלל
	// והפעלת הנגן לנגן
	@SuppressWarnings("deprecation")
	@Override
	public void onPrepared(MediaPlayer mp) 
	{
		String name = "Rihana - Disturbia!";
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), PlayerActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification();
		notification.tickerText = name;
		notification.icon = R.drawable.icon_play;
		notification.flags |= Notification.FLAG_ONGOING_EVENT|Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT;
		notification.setLatestEventInfo(getApplicationContext(), "Now playing",
		                "Playing: " + name, pi);
		this.startForeground(NOTIFICATION_ID, notification);

		player.start();

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}






















