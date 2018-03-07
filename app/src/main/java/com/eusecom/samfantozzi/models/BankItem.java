package com.eusecom.samfantozzi.models;

public class BankItem extends AccountItem {

	private String dok;
	private String dat;
	private String ucm;
	private String ucd;
	private String hod;
	
	public BankItem(String dok, String dat, String ucm, String ucd, String hod){
		this.dok=dok;
		this.dat=dat;
		this.ucm=ucm;
		this.ucd=ucd;
		this.hod=hod;
	}

	@Override
	public String getDok() {
		return dok;
	}

	@Override
	public String getDat() {
		return dat;
	}

	@Override
	public String getUcm() {
		return ucm;
	}

	@Override
	public String getUcd() {
		return ucd;
	}

	@Override
	public String getHod() {
		return hod;
	}
}