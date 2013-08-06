package ca.pnelson.opscal;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class ScheduleAdapter extends BaseAdapter
{
  private ScheduleActivity mContext;
  private ArrayList<Date> mSchedule;
  
  private static final int[] DAYS_BEFORE_MONTH = {0, 31, 59, 90, 120, 151, 181,
    212, 243, 273, 304, 334};
  
  private static final byte[] PATTERN = {
    0, 0, 0, 0, 2, 2, 2,
    2, 0, 0, 0, 2, 2, 2,
    2, 0, 0, 0, 1, 1, 1,
    1, 0, 0, 0, 1, 1, 1,
    1, 2, 2, 2, 0, 0, 0,
    0, 2, 2, 2, 0, 0, 0,
    0, 1, 1, 1, 0, 0, 0,
    0, 1, 1, 1, 0, 0, 0
  };

  private static final int[] ORIGIN = {26, 40, 54, 12};

  public ScheduleAdapter(ScheduleActivity context)
  {
    mContext = context;
    mSchedule = new ArrayList<Date>();
  }

  public int getCount()
  {
    return mSchedule.size();
  }

  public Object getItem(int pos)
  {
    return mSchedule.get(pos);
  }

  public long getItemId(int pos)
  {
    return pos;
  }

  public View getView(int pos, View convertView, ViewGroup parent)
  {
    Button view = (Button)convertView;
    Date cell = (Date)getItem(pos);

    if (view == null)
    {
      view = (Button)LayoutInflater.from(mContext).inflate(
        R.layout.cell, parent, false);

      if (cell.disable)
        view.setVisibility(Button.INVISIBLE);
      else
      {
        view.setText(Integer.toString(cell.day));
        view.setOnClickListener(new Date.OnClickListener(mContext, cell));
        view.setOnTouchListener(mContext);

        if (cell.today)
          view.setBackgroundResource(R.drawable.now);
        else
        {
          switch (cell.data[mContext.getTeam()])
          {
          case 1:
            view.setBackgroundResource(R.drawable.day);
            break;
          
          case 2:
            view.setBackgroundResource(R.drawable.eve);
            break;
          
          default:
            view.setBackgroundResource(R.drawable.off);
            break;
          }
        }
      }
    }
    
    return view;
  }

  public void update()
  {
    int team = mContext.getTeam();
    int year = mContext.getYear();
    int month = mContext.getMonth();

    GregorianCalendar now = new GregorianCalendar();
    GregorianCalendar date = new GregorianCalendar(year, month, 1);

    int index = (ORIGIN[team] + getOrdinal(year, month, 1)) % 56;
    int leading = date.get(GregorianCalendar.DAY_OF_WEEK) - GregorianCalendar.SUNDAY;

    mSchedule.clear();
    
    date.add(GregorianCalendar.DATE, -leading);

    // leading days
    for (int i = 0; i < leading; i++)
    {
      mSchedule.add(new Date(date));
      date.add(GregorianCalendar.DATE, 1);
    }

    while (true)
    {
      int day = date.get(GregorianCalendar.DAY_OF_MONTH);
      int delta = getOrdinal(year, month, day);
      int julian = daysBeforeMonth(year, month) + day;
      boolean today = isEqualDate(now, date);

      byte[] data = new byte[]{
        PATTERN[(ORIGIN[0] + delta) % 56],
        PATTERN[(ORIGIN[1] + delta) % 56],
        PATTERN[(ORIGIN[2] + delta) % 56],
        PATTERN[(ORIGIN[3] + delta) % 56]
      };

      mSchedule.add(new Date(date, julian, data, today));

      if (index < 55)
        index++;
      else
        index = 0;
      
      date.add(GregorianCalendar.DATE, 1);

      if (date.get(GregorianCalendar.MONTH) != month)
        break;
    }

    // trailing days
    for (int i = 0; i < mSchedule.size() % 7; i++)
    {
      mSchedule.add(new Date(date));
      date.add(GregorianCalendar.DATE, 1);
    }
  }

  // number of days since January 1st, 2010
  private int getOrdinal(int year, int month, int day)
  {
    return daysBeforeYear(year) + daysBeforeMonth(year, month) + day - 1;
  }

  private int daysBeforeYear(int year)
  {
    year -= 2010;
    return (int)((year * 365) + (year / 4) - (year / 100) + (year / 400));
  }

  // number of days before the first of `month` in the given `year`
  private int daysBeforeMonth(int year, int month)
  {
    int days = DAYS_BEFORE_MONTH[month];

    if (month > 1 && isLeap(year))
      days++;
    
    return days;
  }

  private boolean isLeap(int year)
  {
    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
  }

  private boolean isEqualDate(GregorianCalendar d1, GregorianCalendar d2)
  {
    return d1.get(GregorianCalendar.DAY_OF_MONTH) == d2.get(GregorianCalendar.DAY_OF_MONTH)
        && d1.get(GregorianCalendar.MONTH) == d2.get(GregorianCalendar.MONTH)
        && d1.get(GregorianCalendar.YEAR) == d2.get(GregorianCalendar.YEAR);
  }
}
