package com.hypertrack.devicelogger.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by Aman on 20/09/17.
 */
class DeviceLogDatabaseHelper extends SQLiteOpenHelper implements DeviceLogDataSource {

    private static final String TAG = DeviceLogDatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "com.hypertrack.common.device_logs.db";
    private static final int DATABASE_VERSION = 2;

    private static DeviceLogDatabaseHelper deviceLogDatabaseHelper;
    private SQLiteDatabase database;

    private DeviceLogDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.initializeDatabase();
    }

    private void initializeDatabase() {
        if (database == null)
            database = this.getWritableDatabase();
    }

    static DeviceLogDatabaseHelper getInstance(Context context) {
        if (deviceLogDatabaseHelper == null) {
            synchronized (DeviceLogDatabaseHelper.class) {
                if (deviceLogDatabaseHelper == null)
                    deviceLogDatabaseHelper = new DeviceLogDatabaseHelper(context);
            }
        }
        return deviceLogDatabaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DeviceLogTable.onCreate(db);
        SmartLog.i(TAG, "DeviceLogDatabaseHelper onCreate called.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DeviceLogTable.onUpgrade(db, oldVersion, newVersion);
        SmartLog.i(TAG, "DeviceLogDatabaseHelper onUpgrade called.");
    }

    @Override
    public long getDeviceLogCount() {
        // Initialize SQLiteDatabase if null
        initializeDatabase();

        return DeviceLogTable.getCount(database);
    }

    @Override
    public void addDeviceLog(String deviceLog) {
        // Initialize SQLiteDatabase if null
        initializeDatabase();

        DeviceLogTable.addDeviceLog(database, deviceLog);
    }

    @Override
    public void deleteDeviceLog(List<DeviceLog> deviceLogList) {
        // Initialize SQLiteDatabase if null
        initializeDatabase();

        DeviceLogTable.deleteDeviceLog(database, deviceLogList);
    }

    @Override
    public void deleteAllDeviceLogs() {
        // Initialize SQLiteDatabase if null
        initializeDatabase();

        DeviceLogTable.deleteAllDeviceLogs(database);
    }

    @Override
    public List<DeviceLog> getDeviceLogs(int batch) {
        // Initialize SQLiteDatabase if null
        initializeDatabase();
        List<DeviceLog> deviceLogList = null;

        try {
            deviceLogList = DeviceLogTable.getDeviceLogs(database,batch);
        } catch (OutOfMemoryError | Exception e) {
            e.printStackTrace();
        }

        return deviceLogList;
    }

    @Override
    public int getDeviceLogBatchCount() {
        initializeDatabase();

        return DeviceLogTable.getDeviceLogBatchCount(database);
    }

    @Override
    public void clearOldLogs(int expiryTime) {
        initializeDatabase();

        DeviceLogTable.clearOldLogs(database,expiryTime);
    }
}
