package com.ellipsonic.www.student_tracking.action;
import android.content.Context;
/**
 * Created by lovin on 8/2/2017.
 */

abstract class Action implements IAction {
    @Override
    abstract public String execute(Context context);

}

