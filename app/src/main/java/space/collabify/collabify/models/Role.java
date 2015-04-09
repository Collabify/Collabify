package space.collabify.collabify.models;

import android.view.ViewGroup;

import space.collabify.collabify.activities.PrimaryViewActivity;

/**
 * This file was born on March 20, at 19:47
 */
public class Role {
  public static final String DJ = "DJ";
  public static final String COLLABIFIER = "Collabifier";
  public static final String PROMOTED = "Promoted";
  public static final String BLACKLISTED = "Blacklisted";
  public static final String NOROLE = "NoRole";

  private String role;

  public void setRole(String role) {
    this.role = role;
  }

  public String getRole() {
    if (role == null) {
      role = NOROLE;
    }
    return role;
  }

  public boolean isDJ() {
    return role.equalsIgnoreCase(DJ);
  }

  public boolean isCollabifier() {
    return role.equalsIgnoreCase(COLLABIFIER);
  }

  public boolean isPromoted() {
    return role.equalsIgnoreCase(PROMOTED);
  }

  public boolean isBlacklisted() {
    return role.equalsIgnoreCase(BLACKLISTED);
  }

  public boolean isNoRole() {
    return role.equalsIgnoreCase(NOROLE);
  }

}
