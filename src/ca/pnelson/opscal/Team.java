package ca.pnelson.opscal;

import android.content.Context;
import android.content.res.Resources;

public class Team
{
  private final Resources mResources;

  private final int mTeam;
  private final byte mStatus;

  public Team(Context context, int team, byte status)
  {
    mResources = context.getResources();
    mTeam = team;
    mStatus = status;
  }

  public String getTitle()
  {
    // TODO: internationalize!
    return "Team " + mResources.getStringArray(R.array.teams)[mTeam];
  }

  public String getStatus()
  {
    return mResources.getStringArray(R.array.statuses)[mStatus];
  }

  public int getSymbol()
  {
    switch (mStatus)
    {
    case 0:
      return R.drawable.off_symbol;
    
    case 1:
      return R.drawable.day_symbol;
    
    case 2:
      return R.drawable.eve_symbol;
    
    default:
      return -1;
    }
  }
}
