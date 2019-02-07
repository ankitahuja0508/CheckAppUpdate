package aexyn.com.checkappupdate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.checkappupdate.BuildConfig;
import com.checkappupdate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class CheckAppUpdate {

    private static String TAG = "Utils";
    private static String VERSION_CODE_KEY = "versionCode";
    private static String TITLE_KEY = "title";
    private static String MESSAGE_KEY = "message";
    private static String APP_URL_KEY = "androidappurl";
    private static String FORCE_UPDATE_KEY = "forceupdate";

    private Context context;

    private FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    private int currentAppVersion;

    public CheckAppUpdate(Context context, int currentAppVersion) {
        this.context = context;
        this.currentAppVersion = currentAppVersion;
        mFirebaseRemoteConfig.setConfigSettings(
                new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG)
                        .build());
    }

    public void checkVersion(){
        mFirebaseRemoteConfig.fetch().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mFirebaseRemoteConfig.activateFetched();
                    Log.d(TAG, "Fetched value: " + mFirebaseRemoteConfig.getString(VERSION_CODE_KEY));
                    //calling function to check if new version is available or not
                    checkForUpdate();
                }
            }
        });
    }

    private void checkForUpdate() {
        int latestAppVersion = (int) mFirebaseRemoteConfig.getDouble(VERSION_CODE_KEY);

        String title = mFirebaseRemoteConfig.getString(TITLE_KEY);
        String message = mFirebaseRemoteConfig.getString(MESSAGE_KEY);
        final String appurl = mFirebaseRemoteConfig.getString(APP_URL_KEY);
        boolean forceUpdate = mFirebaseRemoteConfig.getBoolean(FORCE_UPDATE_KEY);

        if (latestAppVersion > currentAppVersion) {
            AlertDialog.Builder builder =  new AlertDialog.Builder(context);

            builder.setTitle(title==null?context.getString(R.string.title):title);
            builder.setMessage(message==null?context.getString(R.string.message):message);
            builder.setPositiveButton(
                    context.getString(R.string.update), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(appurl==null?"https://google.com":appurl));
                            context.startActivity(browserIntent);
                        }
                    });

            builder.setCancelable(!forceUpdate);

            if(!forceUpdate){
                builder.setNegativeButton(
                        context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
            }

            builder.show();

        }
    }

}
