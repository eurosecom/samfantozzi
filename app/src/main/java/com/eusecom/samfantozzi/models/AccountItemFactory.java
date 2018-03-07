package com.eusecom.samfantozzi.models;

public class AccountItemFactory {
	

	public static AccountItem getAccountItem(String type, String dok, String dat, String ucm, String ucd, String hod){

		if("Bank".equalsIgnoreCase(type)) return new BankItem(dok, dat, ucm, ucd, hod);
		else if("Universal".equalsIgnoreCase(type)) return new UniItem(dok, dat, ucm, ucd, hod);
		
		return null;
	}
}
