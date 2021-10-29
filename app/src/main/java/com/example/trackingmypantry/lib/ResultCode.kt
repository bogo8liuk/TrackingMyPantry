package com.example.trackingmypantry.lib

import android.app.Activity

/**
 * Set of possible returned values from activities started with `startActivityForResult`.
*/
class ResultCode {
    companion object {
        /* The add of Activity.RESULT_FIRST_USER guarantees that no clashes with os values
        * will occur. */
        //        RESULT_CANCELED
        //        RESULT_OK
        const val NETWORK_ERR = Activity.RESULT_FIRST_USER + 1
        const val EXISTENT_USER = Activity.RESULT_FIRST_USER + 2
        const val EXPIRED_TOKEN = Activity.RESULT_FIRST_USER + 3
        const val INVALID_SESSION_TOKEN = Activity.RESULT_FIRST_USER + 4
        const val DENIED_PERMISSIONS = Activity.RESULT_FIRST_USER + 5
        const val DEVICE_ERR = Activity.RESULT_FIRST_USER + 6
    }
}