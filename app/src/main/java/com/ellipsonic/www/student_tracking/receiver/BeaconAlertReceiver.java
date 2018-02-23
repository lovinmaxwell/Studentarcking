/*
 *
 *  Copyright (c) 2015 SameBits UG. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ellipsonic.www.student_tracking.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ellipsonic.www.student_tracking.R;
import com.ellipsonic.www.student_tracking.model.NotificationAction;
import com.ellipsonic.www.student_tracking.ui.activity.NewMainNavigationActivity;
import com.ellipsonic.www.student_tracking.util.Constants;
import com.ellipsonic.www.student_tracking.util.NotificationBuilder;


public class BeaconAlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equalsIgnoreCase(Constants.ALARM_NOTIFICATION_SHOW)) {
            NotificationAction notificationAction = intent.getParcelableExtra("NOTIFICATION");
            createNotification(context, context.getString(R.string.action_alarm_text_title),
                    notificationAction.getMessage(), notificationAction.getMessage(),
                    notificationAction.getRingtone(), notificationAction.isVibrate());

        }
    }

    private void createNotification(Context context, String title, String msgText, String msgAlert, String ringtone, boolean isVibrate) {

        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0, new Intent(context, NewMainNavigationActivity.class), 0);

        NotificationBuilder notificationBuilder = new NotificationBuilder(context);
        notificationBuilder.createNotification(R.mipmap.ic_launcher, title, notificationIntent);

        notificationBuilder.setMessage(msgText);
        notificationBuilder.setTicker(msgAlert);

        if (isVibrate) {
            notificationBuilder.setVibration();
        }

        if (ringtone != null && ringtone.length() > 0) {
            notificationBuilder.setRingtone(ringtone);
        }

        notificationBuilder.show(1);

    }
}





