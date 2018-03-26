package com.eusecom.samfantozzi;

import com.eusecom.samfantozzi.retrofit.AbsServerService;

public class AccountReportsMySqlHelper {
	
	public static AbsServerService getMySqlDBConnection(){
		//get MySql DB connection using connection parameters
		return null;
	}
	
	public void generateMySqlPDFReport(String tableName, AbsServerService con){
		//get data from table and generate pdf report
		System.out.println("MySqlHelper " + "PDF");
	}
	
	public void generateMySqlHTMLReport(String tableName, AbsServerService con){
		//get data from table and generate pdf report
		System.out.println("MySqlHelper " + "HTML");
	}

	public void generateMySqlCSVReport(String tableName, AbsServerService con){
		//get data from table and generate csv report
		System.out.println("MySqlHelper " + "CSV");
	}

	public void generateMySqlJSONReport(String tableName, AbsServerService con){
		//get data from table and generate json report
		System.out.println("MySqlHelper " + "JSON");
	}
}