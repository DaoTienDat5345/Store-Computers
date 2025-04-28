package model;
public class Manager {
	private int managerID;
	private String userManager;
	private String userPasswordManager;
	public Manager(int managerID, String userManager, String userPasswordManager) {
	    this.managerID = managerID;
	    this.userManager = userManager;
	    this.userPasswordManager = userPasswordManager;
	}
	
	public int getManagerID() {
	    return managerID;
	}
	
	public void setManagerID(int managerID) {
	    this.managerID = managerID;
	}
	
	public String getUserManager() {
	    return userManager;
	}
	
	public void setUserManager(String userManager) {
	    this.userManager = userManager;
	}
	
	public String getUserPasswordManager() {
	    return userPasswordManager;
	}
	
	public void setUserPasswordManager(String userPasswordManager) {
	    this.userPasswordManager = userPasswordManager;
	}
	
	@Override
	public String toString() {
	    return "Nguoi_Quan_ly [managerID=" + managerID + ", userManager=" + userManager + "]";
	}
}