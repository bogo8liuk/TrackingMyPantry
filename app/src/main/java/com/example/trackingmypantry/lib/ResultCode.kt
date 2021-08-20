package com.example.trackingmypantry.lib

/* Using a class to avoid names conflicts: the class has the only purpose of namespace.
* Set of possible returned values from activities started with `startActivityForResult`.
* 1 is RESULT_OK, 0 is RESULT_CANCELED */
class ResultCode {
    companion object {
        //        RESULT_CANCELED
        //        RESULT_OK
        const val NETWORK_ERR = 2
        const val EXISTENT_USER = 3
        const val EXPIRED_TOKEN = 4
        const val INVALID_SESSION_TOKEN = 5
        const val DENIED_PERMISSIONS = 6
    }
}