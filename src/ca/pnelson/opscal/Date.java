package ca.pnelson.opscal;

import java.util.GregorianCalendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Date
{
  public final int day;
  public final int month;
  public final int year;
  public final int weekday;
  public final int julian;
  public final byte[] data;
  public final boolean today;
  public final boolean disable;

  public Date(GregorianCalendar date)
  {
    this.day = date.get(GregorianCalendar.DAY_OF_MONTH);
    this.month = date.get(GregorianCalendar.MONTH);
    this.year = date.get(GregorianCalendar.YEAR);
    this.weekday = date.get(GregorianCalendar.DAY_OF_WEEK);
    this.julian = -1;
    this.data = null;
    this.today = false;
    this.disable = true;
  }

  public Date(GregorianCalendar date, int julian, byte[] data, boolean today)
  {
    this.day = date.get(GregorianCalendar.DAY_OF_MONTH);
    this.month = date.get(GregorianCalendar.MONTH);
    this.year = date.get(GregorianCalendar.YEAR);
    this.weekday = date.get(GregorianCalendar.DAY_OF_WEEK);
    this.julian = julian;
    this.data = data;
    this.today = today;
    this.disable = false;
  }

  public static class OnClickListener implements View.OnClickListener
  {
    private Context mContext;
    private Date mCell;

    public OnClickListener(Context context, Date cell)
    {
      mContext = context;
      mCell = cell;
    }

    public void onClick(View v)
    {
      Intent intent = new Intent(mContext, DateActivity.class);
      Bundle bundle = new Bundle();

      bundle.putInt("day", mCell.day);
      bundle.putInt("month", mCell.month);
      bundle.putInt("year", mCell.year);
      bundle.putInt("weekday", mCell.weekday);
      bundle.putInt("julian", mCell.julian);
      bundle.putByteArray("data", mCell.data);
      intent.putExtras(bundle);

      mContext.startActivity(intent);
    }
  }
}
