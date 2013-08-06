package ca.pnelson.opscal;

import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ListView;

import android.util.Log; // 53, 65, 79

public class DateActivity extends ListActivity
{
  private TextView mDate;
  private ListView mList;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    Bundle bundle = getIntent().getExtras();
    Resources res = getResources();

    String[] months = res.getStringArray(R.array.months);
    String[] weekdays = res.getStringArray(R.array.weekdays);

    int weekday = bundle.getInt("weekday");
    int month = bundle.getInt("month");
    int day = bundle.getInt("day");
    int year = bundle.getInt("year");
    int julian = bundle.getInt("julian");
    byte[] data = bundle.getByteArray("data");

    super.onCreate(savedInstanceState);
    setContentView(R.layout.date);

    mDate = (TextView)findViewById(R.id.date);
    mList = (ListView)findViewById(android.R.id.list);

    mDate.setText(
      weekdays[weekday - 1]
      + ", "
      + months[month]
      + " "
      + day
      + ", "
      + year
      + " ("
      + julian
      + ") "
    );

    setListAdapter(new TeamAdapter(this, data));
  }
}
