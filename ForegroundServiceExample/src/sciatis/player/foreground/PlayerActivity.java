package sciatis.player.foreground;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
 * 
 * NOTIFICATION
 * כדי שמערכת ההפעלה לא תהרוג לנו את השירות אנו נעניק לה רכיב 
 * UI (NOTIFICATION)
 * כאשר השירות ינגן שיר נודיע בבר העליון 
 * וכך גם במקרה שהאפלקציה סגורה כל זמן שהשירות בבר העליון הוא יחיה
 * לחיצה בשורת הבר הנגלל יפתח לנו מחדש
 * את האפלקציה שלנו במקרה שהיא סגורה
 */
public class PlayerActivity extends Activity implements ServicesConsts
{
	
	// הפעלה של השירות כמו תמיד
	protected void startForegroundPlayerService() 
	{
		Intent intent = new Intent(this, ForegroundServicePlayer.class);
	    startService(intent);
	}
	
	// כאשר המשתמש לוחץ עצור שיר נשלח הודעה למי שמאזין
	// ולא נעצור את השירות מאצלנו כי צריך 
	//גם לשחרר את הבר הגלילה
	protected void stopForegroundPlayerService() 
	{
		Intent intent = new Intent(PLAYER_INTENT);
	    intent.setAction(PLAYER_INTENT);
	    
	    sendBroadcast(intent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
				
		Button startForegroundServiceButton = (Button) findViewById(R.id.serviceStartButton);
		startForegroundServiceButton.setOnClickListener(new OnClickListener()
		{
			//כפתור פלי
			public void onClick(View v)
			{

				startForegroundPlayerService();
			}
		});
		
		Button stotForegroundServiceButton = (Button) findViewById(R.id.serviceStopButton);
		stotForegroundServiceButton.setOnClickListener(new OnClickListener()
		{
			//כפתור עצור
			public void onClick(View v)
			{

				stopForegroundPlayerService();
			}
		});
		
	}

	

}
