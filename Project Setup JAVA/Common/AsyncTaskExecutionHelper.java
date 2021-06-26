package com.lmwdelivery.common;

import android.os.AsyncTask;


public class AsyncTaskExecutionHelper {
    static class HoneycombExecutionHelper {
        @SafeVarargs
        public static <P> void execute(AsyncTask<P, ?, ?> asyncTask, boolean parallel, P... params) {
//            if (SosActivity.mLogs != null)
//                SosActivity.mLogs.info("HoneycombExecutionHelper started");
//            Executor executor = parallel ? AsyncTask.THREAD_POOL_EXECUTOR : AsyncTask.SERIAL_EXECUTOR;
//            asyncTask.executeOnExecutor(executor, params);
//            if (SosActivity.mLogs != null)
//                SosActivity.mLogs.info("HoneycombExecutionHelper ended");
        }
    }

    @SafeVarargs
    public static <P> void executeParallel(AsyncTask<P, ?, ?> asyncTask, P... params) {
        execute(asyncTask, true, params);
    }

    @SafeVarargs
    public static <P> void executeSerial(AsyncTask<P, ?, ?> asyncTask, P... params) {
        execute(asyncTask, false, params);
    }

    @SafeVarargs
    private static <P> void execute(AsyncTask<P, ?, ?> asyncTask, boolean parallel, P... params) {
//        if (SosActivity.mLogs != null)
//            SosActivity.mLogs.info("AsyncTask executed");
        HoneycombExecutionHelper.execute(asyncTask, parallel, params);
    }
}
