package ca.pnelson.opscal;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TeamAdapter extends BaseAdapter
{
  private Context mContext;
  private ArrayList<Team> mTeams;

  public TeamAdapter(Context context, byte[] data)
  {
    mContext = context;
    mTeams = new ArrayList<Team>();

    for (int i = 0; i < data.length; i++)
      mTeams.add(new Team(context, i, data[i]));
  }

  public int getCount()
  {
    return mTeams.size();
  }

  public Object getItem(int pos)
  {
    return mTeams.get(pos);
  }

  public long getItemId(int pos)
  {
    return pos;
  }

  public View getView(int pos, View convertView, ViewGroup parent)
  {
    LinearLayout view = (LinearLayout)convertView;

    if (view == null)
      view = (LinearLayout)LayoutInflater.from(mContext).inflate(
        R.layout.team, parent, false);

    ((TextView)view.findViewById(R.id.title)).setText(mTeams.get(pos).getTitle());
    ((TextView)view.findViewById(R.id.status)).setText(mTeams.get(pos).getStatus());
    ((ImageView)view.findViewById(R.id.symbol)).setImageResource(mTeams.get(pos).getSymbol());
    
    return view;
  }
}
