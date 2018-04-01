package com.eusecom.samfantozzi;

import android.content.Context;

public class CommandExecutorProxy implements CommandExecutor {

	private boolean isAdmin;
	private CommandExecutor executor;
	
	public CommandExecutorProxy(String usuid, String fir, String usadmin){
		if(usadmin.equals("1")) isAdmin=true;
		executor = new CommandExecutorImpl();
	}
	
	@Override
	public void runCommand(String perm, AccountReportsHelperFacade.DBTypes dbType
			, AccountReportsHelperFacade.ReportTypes reportType, AccountReportsHelperFacade.ReportName tableName
			, Context context) throws Exception {
		if(isAdmin){
			executor.runCommand(perm, dbType, reportType, tableName, context );
		}else{
			if(perm.trim().startsWith("rm")){
				throw new Exception("rm command is not allowed for non-admin users.");
			}else{
				executor.runCommand(perm, dbType, reportType, tableName, context );
			}
		}
	}

}