package com.eusecom.samfantozzi.models;

public abstract class AccountItem {
	
	public abstract String getDok();
	public abstract String getDat();
	public abstract String getUcm();
	public abstract String getUcd();
	public abstract String getHod();
	
	@Override
	public String toString(){
		return "DOk= "+this.getDok()+", DAT="+this.getDat()+", UCM="+this.getUcm()+", UCD="+this.getUcd()+", HOD="+this.getHod();
	}
}