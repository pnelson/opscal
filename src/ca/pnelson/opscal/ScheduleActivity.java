package ca.pnelson.opscal;

import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.SharedPreferences;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScheduleActivity extends Activity implements OnTouchListener
{
  private int mTeam;
  private int mYear;
  private int mMonth;

  private String[] mMonthArray;
  private String[] mTeamArray;
  private String[] mWeekdayArray;

  private Resources mResources;
  private SharedPreferences mConfig;
  private GestureDetector mGesture;
  private ScheduleAdapter mAdapter;

  private TextView mDate;
  private GridView mGrid;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    GregorianCalendar now = new GregorianCalendar();

    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    mResources = getResources();
    mConfig = PreferenceManager.getDefaultSharedPreferences(this);

    // convert team preference from a value from A-D to a value from 0-3
    mTeam = Character.getNumericValue(
      mConfig.getString("team", "A").charAt(0)) - 10;
    mYear = now.get(GregorianCalendar.YEAR);
    mMonth = now.get(GregorianCalendar.MONTH);

    mMonthArray = mResources.getStringArray(R.array.months);
    mTeamArray = mResources.getStringArray(R.array.teams);
    mWeekdayArray = mResources.getStringArray(R.array.weekdays);

    mGesture = new GestureDetector(new SwipeGesture(this));
    mAdapter = new ScheduleAdapter(this);

    mDate = (TextView)findViewById(R.id.current);
    mGrid = (GridView)findViewById(R.id.grid);
    mGrid.setOnTouchListener(this);

    updateViews();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
    case R.id.settings:
      startActivity(new Intent(this, SettingsActivity.class));
      break;
    
    case R.id.about:
      startActivity(new Intent(this, AboutActivity.class));
      break;
    
    case R.id.exit:
      finish();
      break;
    
    default:
      return false;
    }

    return true;
  }

  @Override
  public boolean onTouch(View view, MotionEvent event)
  {
    if (mGesture.onTouchEvent(event))
      return true;
    
    return false;
  }

  public void prevMonth()
  {
    if (--mMonth < 0)
    {
      mMonth = 11;
      mYear--;
    }

    updateViews();
  }

  public void nextMonth()
  {
    if (++mMonth > 11)
    {
      mMonth = 0;
      mYear++;
    }

    updateViews();
  }

  public int getTeam()
  {
    return mTeam;
  }

  public int getYear()
  {
    return mYear;
  }
  
  public int getMonth()
  {
    return mMonth;
  }

  private void updateViews()
  {
    mAdapter.update();

    mGrid.setAdapter(mAdapter);
    mDate.setText(
      mMonthArray[mMonth]
      + ", "
      + mYear
      + " ("
      + mTeamArray[mTeam]
      + ")"
    );
  }

  private class SwipeGesture extends SimpleOnGestureListener
  {
    private ScheduleActivity mContext;

    public SwipeGesture(ScheduleActivity context)
    {
      super();
      mContext = context;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
      float deltaX = e1.getX() - e2.getX();
      float deltaY = Math.abs(e1.getY() - e2.getY());

      // not long or straight enough
      if (Math.abs(deltaX) < 150 || deltaY > 100)
        return false;
      
      // view prev or next month
      if (deltaX < 0)
        mContext.prevMonth();
      else
        mContext.nextMonth();

      return true;
    }
  }
}
