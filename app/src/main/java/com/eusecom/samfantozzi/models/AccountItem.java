package com.eusecom.samfantozzi.models;

public abstract class AccountItem {

	public abstract String getCpl();
	public abstract String getDok();
	public abstract String getDat();
	public abstract String getUcm();
	public abstract String getUcd();
	public abstract String getRdp();
	public abstract String getIco();
	public abstract String getFak();
	public abstract String getHod();
	public abstract String getPop();

	@Override
	public String toString(){
		return "CPL= "+this.getCpl()+", DOK= "+this.getDok()+", DAT="+this.getDat()+
				", UCM="+this.getUcm()+", UCD="+this.getUcd()+", RDP="+this.getRdp()+
				", ICO="+this.getIco()+", FAK="+this.getFak()+", HOD="+this.getHod()+
				", POP="+this.getPop();
	}
}