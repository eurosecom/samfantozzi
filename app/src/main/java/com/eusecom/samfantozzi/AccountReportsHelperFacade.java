package com.eusecom.samfantozzi;


import com.eusecom.samfantozzi.retrofit.AbsServerService;

import io.realm.Realm;

public class AccountReportsHelperFacade {

	public static void generateReport(DBTypes dbType, ReportTypes reportType, String tableName){
		switch (dbType){
		case MYSQL:
			AbsServerService con = AccountReportsMySqlHelper.getMySqlDBConnection();
			AccountReportsMySqlHelper mySqlHelper = new AccountReportsMySqlHelper();
			switch(reportType){
			case HTML:
				mySqlHelper.generateMySqlHTMLReport(tableName, con);
				break;
			case PDF:
				mySqlHelper.generateMySqlPDFReport(tableName, con);
				break;
			case CSV:
				mySqlHelper.generateMySqlCSVReport(tableName, con);
				break;
			case JSON:
				mySqlHelper.generateMySqlJSONReport(tableName, con);
				break;
			}


		case REALM:
			Realm conr = AccountReportsRealmHelper.getRealmDBConnection();
			AccountReportsRealmHelper realmHelper = new AccountReportsRealmHelper();
			switch(reportType){
			case HTML:
				realmHelper.generateRealmHTMLReport(tableName, conr);
				break;
			case PDF:
				realmHelper.generateRealmPDFReport(tableName, conr);
				break;
			case CSV:
				realmHelper.generateRealmCSVReport(tableName, conr);
				break;
			case JSON:
				realmHelper.generateRealmJSONReport(tableName, conr);
				break;

			}
			break;
		}
		
	}
	
	public static enum DBTypes{
		MYSQL,REALM;
	}
	
	public static enum ReportTypes{
		HTML,PDF,CSV,JSON;
	}
}