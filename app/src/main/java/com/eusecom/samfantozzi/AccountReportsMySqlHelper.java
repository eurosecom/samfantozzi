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


			case UCTPOH:
				Intent is8 = new Intent(context, ShowPdfActivity.class);
				Bundle extras8 = new Bundle();
				extras8.putString("fromact", "61");
				extras8.putString("drhx", "61");
				extras8.putString("ucex", "0");
				extras8.putString("dokx", "0");
				is8.putExtras(extras8);
				context.startActivity(is8);
				is8.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case DPHPRZ:
				Intent is9 = new Intent(context, ShowPdfActivity.class);
				Bundle extras9 = new Bundle();
				extras9.putString("fromact", "54");
				extras9.putString("drhx", "54");
				extras9.putString("ucex", "0");
				extras9.putString("dokx", "0");
				is9.putExtras(extras9);
				context.startActivity(is9);
				is9.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case FOBPRZ:
				Intent is10 = new Intent(context, ShowPdfActivity.class);
				Bundle extras10 = new Bundle();
				extras10.putString("fromact", "55");
				extras10.putString("drhx", "55");
				extras10.putString("ucex", "0");
				extras10.putString("dokx", "0");
				is10.putExtras(extras10);
				context.startActivity(is10);
				is10.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case SALIDC0:
				Intent is11 = new Intent(context, ShowPdfActivity.class);
				Bundle extras11 = new Bundle();
				extras11.putString("fromact", "71");
				extras11.putString("drhx", "71");
				extras11.putString("ucex", "0");
				extras11.putString("dokx", "0");
				is11.putExtras(extras11);
				context.startActivity(is11);
				is11.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case SALIDC1:
				Intent is12 = new Intent(context, ShowPdfActivity.class);
				Bundle extras12 = new Bundle();
				extras12.putString("fromact", "72");
				extras12.putString("drhx", "72");
				extras12.putString("ucex", "0");
				extras12.putString("dokx", "0");
				is12.putExtras(extras12);
				context.startActivity(is12);
				is12.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case SALITM0:
				Intent is14 = new Intent(context, ShowPdfActivity.class);
				Bundle extras14 = new Bundle();
				extras14.putString("fromact", "73");
				extras14.putString("drhx", "73");
				extras14.putString("ucex", "0");
				extras14.putString("dokx", "0");
				is14.putExtras(extras14);
				context.startActivity(is14);
				is14.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				break;

			case SALITM1:
				Intent is15 = new Intent(context, ShowPdfActivity.class);
				Bundle extras15 = new Bundle();
				extras15.putString("fromact", "74");
				extras15.putString("drhx", "74");
				extras15.putString("ucex", "0");
				extras15.putString("dokx", "0");
				is15.putExtras(extras15);
				context.startActivity(is15);
				is15.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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