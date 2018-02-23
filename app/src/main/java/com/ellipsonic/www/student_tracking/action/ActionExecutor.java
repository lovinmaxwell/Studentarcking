
package com.ellipsonic.www.student_tracking.action;

import android.content.Context;
import android.util.Log;

import com.ellipsonic.www.student_tracking.model.ActionBeacon;
import com.ellipsonic.www.student_tracking.model.NotificationAction;
import com.ellipsonic.www.student_tracking.util.Constants;
import java.util.ArrayList;
import java.util.List;

public class ActionExecutor {

    private final Context mContext;
    private List<IAction> mHistory = new ArrayList<>();
    private List<IAction> mFailed = new ArrayList<>();

    public ActionExecutor(Context context) {
        this.mContext = context;
    }

    public static IAction actionBuilder(ActionBeacon.ActionType type, String param, NotificationAction notification) {
        switch (type) {
            case ACTION_NONE:
                return new NoneAction(param, notification);
            case ACTION_WEB:
                return new WebAction(param, notification);
            case ACTION_URL:
                return new UrlAction(param, notification);
            case ACTION_INTENT_ACTION:
                return new IntentAction(param, notification);
            case ACTION_START_APP:
                return new StartAppAction(param, notification);
            case ACTION_GET_LOCATION:
                return new LocationAction(param, notification);
            case ACTION_SET_SILENT_ON:
                return new SilentOnAction(param, notification);
            case ACTION_SET_SILENT_OFF:
                return new SilentOffAction(param, notification);
            case ACTION_TASKER:
                return new TaskerAction(param, notification);
        }
        return null;
    }

    public String storeAndExecute(IAction action) {
        this.mHistory.add(action); // optional
        try {
            return action.execute(mContext);
        } catch (Exception e) {
            mFailed.add(action);
            Log.d(Constants.TAG, "Error executing action: " + action, e);
        }
        return null;
    }
}
