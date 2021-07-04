package com.example.trackingmypantry.lib.net

/* Using a class to avoid names conflicts: the class has the only purpose of namespace.
* Set of possible returned values from activities started with `startActivityForResult`.
* 1 is RESULT_OK */
class ResultCode {
    companion object {
        //        RESULT_OK
        const val NETWORK_ERR = 2
        const val EXISTENT_USER = 3
        const val EXPIRED_TOKEN = 4
    }
}