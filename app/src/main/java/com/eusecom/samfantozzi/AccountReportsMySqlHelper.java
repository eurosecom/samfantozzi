package com.eusecom.samfantozzi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


public class AccountReportsMySqlHelper {
	
	public static SharedPreferences getMySqlDBConnection(){
		//get MySql DB connection using connection parameters
		return null;
	}
	
	public void generateMySqlPDFReport(AccountReportsHelperFacade.ReportName tableName, SharedPreferences con, Context context){
		//get data from table and generate pdf report
		System.out.println("MySqlHelper " + "PDF");

		switch(tableName) {

			case PENDEN:
				Intent is = new Intent(context, ShowPdfActivity.class);
				Bundle extras = new Bundle();
				extras.putString("fromact", "41");
				extras.putString("drhx", "41");
				extras.putString("ucex", "0");
				extras.putString("dokx", "0");
				is.putExtras(extras);
				context.startActivity(is);
				is.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case PENDEN2:
				Intent is2 = new Intent(context, ShowPdfActivity.class);
				Bundle extras2 = new Bundle();
				extras2.putString("fromact", "42");
				extras2.putString("drhx", "42");
				extras2.putString("ucex", "0");
				extras2.putString("dokx", "0");
				is2.putExtras(extras2);
				context.startActivity(is2);
				is2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case PRIVYD:
				Intent is3 = new Intent(context, ShowPdfActivity.class);
				Bundle extras3 = new Bundle();
				extras3.putString("fromact", "43");
				extras3.putString("drhx", "43");
				extras3.putString("ucex", "0");
				extras3.putString("dokx", "0");
				is3.putExtras(extras3);
				context.startActivity(is3);
				is3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case MAJZAV:
				Intent is4 = new Intent(context, ShowPdfActivity.class);
				Bundle extras4 = new Bundle();
				extras4.putString("fromact", "44");
				extras4.putString("drhx", "44");
				extras4.putString("ucex", "0");
				extras4.putString("dokx", "0");
				is4.putExtras(extras4);
				context.startActivity(is4);
				is4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case FINSTA:
				Intent is5 = new Intent(context, ShowPdfActivity.class);
				Bundle extras5 = new Bundle();
				extras5.putString("fromact", "51");
				extras5.putString("drhx", "51");
				extras5.putString("ucex", "0");
				extras5.putString("dokx", "0");
				is5.putExtras(extras5);
				context.startActivity(is5);
				is5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case KNIODB:
				Intent is6 = new Intent(context, ShowPdfActivity.class);
				Bundle extras6 = new Bundle();
				extras6.putString("fromact", "52");
				extras6.putString("drhx", "52");
				extras6.putString("ucex", "0");
				extras6.putString("dokx", "0");
				is6.putExtras(extras6);
				context.startActivity(is6);
				is6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case KNIDOD:
				Intent is7 = new Intent(context, ShowPdfActivity.class);
				Bundle extras7 = new Bundle();
				extras7.putString("fromact", "53");
				extras7.putString("drhx", "53");
				extras7.putString("ucex", "0");
				extras7.putString("dokx", "0");
				is7.putExtras(extras7);
				context.startActivity(is7);
				is7.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;


		}

	}
	
	public void generateMySqlHTMLReport(AccountReportsHelperFacade.ReportName tableName, SharedPreferences con){
		//get data from table and generate pdf report
		System.out.println("MySqlHelper " + "HTML");
	}

	public void generateMySqlCSVReport(AccountReportsHelperFacade.ReportName tableName, SharedPreferences con){
		//get data from table and generate csv report
		System.out.println("MySqlHelper " + "CSV");
	}

	public void generateMySqlJSONReport(AccountReportsHelperFacade.ReportName tableName, SharedPreferences con){
		//get data from table and generate json report
		System.out.println("MySqlHelper " + "JSON");
	}


}