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

  private String role;

  public void setRole(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }

  public boolean isDJ() {
    return role.equals(DJ);
  }

  public boolean isCollabifier() {
    return role.equals(COLLABIFIER);
  }

  public boolean isPromoted() {
    return role.equals(PROMOTED);
  }

  public boolean isBlacklisted() {
    return role.equals(BLACKLISTED);
  }

}
