package com.eusecom.samfantozzi.models;

public class UniItem extends AccountItem {

	private String drh;
	private String cpl;
	private String dok;
	private String dat;
	private String ucm;
	private String ucd;
	private String rdp;
	private String ico;
	private String fak;
	private String hod;
	private String pop;

	public UniItem(String drh, String cpl, String dok, String dat, String ucm, String ucd,
					String rdp, String ico, String fak, String hod, String pop){
		this.drh=drh;
		this.cpl=cpl;
		this.dok=dok;
		this.dat=dat;
		this.ucm=ucm;
		this.ucd=ucd;
		this.rdp=rdp;
		this.ico=ico;
		this.fak=fak;
		this.hod=hod;
		this.pop=pop;
	}

	@Override
	public String getDrh() {
		return drh;
	}

	@Override
	public String getCpl() {
		return cpl;
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
	public String getRdp() {
		return rdp;
	}

	@Override
	public String getIco() {
		return ico;
	}

	@Override
	public String getFak() {
		return fak;
	}

	@Override
	public String getHod() {
		return hod;
	}

	@Override
	public String getPop() {
		return pop;
	}
}