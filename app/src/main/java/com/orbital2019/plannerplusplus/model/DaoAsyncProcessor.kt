/**
 * This Class is the solution to the problem of having to make multiple AsyncTasks for each specific function passed
 * to the database.
 * This is achieved by making a async processor class with generics parameter. This class accepts as a generic
 * parameter the type that will be returned by the method where async processor object is invoked.
 */
package com.orbital2019.plannerplusplus.model

import android.os.AsyncTask

/**
 * @param T the type of the result expected
 */
abstract class DaoAsyncProcessor<T>(val daoProcessCallback: DaoProcessCallback<T>?) {

    interface DaoProcessCallback<T> {
        fun onResult(result: T)
    }

    protected abstract fun doAsync(): T

    fun start() {
        DaoProcessAsyncTask().execute()
    }

    private inner class DaoProcessAsyncTask : AsyncTask<Void, Void, T>() {

        override fun doInBackground(vararg params: Void): T {
            return doAsync()
        }

        override fun onPostExecute(t: T) {
            daoProcessCallback?.onResult(t)
        }
    }
}