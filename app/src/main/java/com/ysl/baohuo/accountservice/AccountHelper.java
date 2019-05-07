package com.ysl.baohuo.accountservice;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class AccountHelper {
    private static final String ACCOUNT_TYPE = "com.ysl.baohuo.accountservice.account";
    private static final String CONTENT_AUTHORITY = "com.ysl.baohuo.accountservice.provider";

    public static void addAccount(Context context){
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length > 0){
            Log.d("","账号已存在");
            return;
        }
        Account account = new Account("xx", ACCOUNT_TYPE);
        accountManager.addAccountExplicitly(account, "0", new Bundle());
    }

    public static void autoSync(){
        Account account = new Account("xx", ACCOUNT_TYPE);
        ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY,1);
        ContentResolver.setSyncAutomatically(account, CONTENT_AUTHORITY, true);
        ContentResolver.addPeriodicSync(account, CONTENT_AUTHORITY, new Bundle(), 1);
    }
}
