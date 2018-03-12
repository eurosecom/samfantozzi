package com.eusecom.samfantozzi.models;

public class AccountItemFactory {
	

	public static AccountItem getAccountItem(String type, String drh, String cpl, String dok, String dat, String ucm, String ucd,
                                             String rdp, String ico, String nai, String fak, String hod, String pop){

		if("Bank".equalsIgnoreCase(type)) return new BankItem(drh, cpl, dok, dat, ucm, ucd, rdp, ico, nai, fak, hod, pop);
		else if("Universal".equalsIgnoreCase(type)) return new UniItem(drh, cpl, dok, dat, ucm, ucd, rdp, ico, nai, fak, hod, pop);
		
		return null;
	}
}
