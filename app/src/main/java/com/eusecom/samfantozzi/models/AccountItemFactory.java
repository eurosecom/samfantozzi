package com.eusecom.samfantozzi.models;

public class AccountItemFactory {
	

	public static AccountItem getAccountItem(String type,String cpl, String dok, String dat, String ucm, String ucd,
                                             String rdp, String ico, String fak, String hod, String pop){

		if("Bank".equalsIgnoreCase(type)) return new BankItem(cpl, dok, dat, ucm, ucd, rdp, ico, fak, hod, pop);
		else if("Universal".equalsIgnoreCase(type)) return new UniItem(cpl, dok, dat, ucm, ucd, rdp, ico, fak, hod, pop);
		
		return null;
	}
}
