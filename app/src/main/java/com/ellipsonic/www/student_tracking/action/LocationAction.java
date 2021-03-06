/*
 *
 *  Copyright (c) 2016 SameBits UG. All rights reserved.
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

package com.ellipsonic.www.student_tracking.action;


import android.content.Context;
import android.content.Intent;

import com.ellipsonic.www.student_tracking.model.NotificationAction;
import com.ellipsonic.www.student_tracking.util.Constants;

/**
 * Created by vitas on 03/01/16.
 */
public class LocationAction extends NoneAction {

    public LocationAction(String param, NotificationAction notification) {
        super(param, notification);
    }

    @Override
    public String execute(Context context) {
        Intent newIntent = new Intent(Constants.GET_CURRENT_LOCATION);
        context.sendBroadcast(newIntent);
        return super.execute(context);
    }

    @Override
    public boolean isParamRequired() {
        return false;
    }

    @Override
    public String toString() {
        return "LocationAction, param: " + param;
    }
}
