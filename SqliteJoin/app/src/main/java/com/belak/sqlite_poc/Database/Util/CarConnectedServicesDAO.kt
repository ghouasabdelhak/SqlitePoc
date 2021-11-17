package com.belak.sqlite_poc.Database.Util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import com.psa.bouser.mym.dao.OfferConnectedServiceDAO

class CarConnectedServicesDAO(context: Context) : AbstractDAO(context) {

    /*---------------------------------------------------------------------------
     *  CONVERT METHODS
     * --------------------------------------------------------------------------
     */

    private fun cursorToBO(cursor: Cursor): ServicesConnectedBO {
        val bo = ServicesConnectedBO()
        bo.carID = cursor.getString(cursor.getColumnIndex(COLUMN_VIN))
        bo.serviceID = cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE_ID))
        bo.title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
        bo.description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
        bo.subscriptionURL = cursor.getString(cursor.getColumnIndex(COLUMN_SUBSCRIPTION_URL))
        bo.url = cursor.getString(cursor.getColumnIndex(COLUMN_URL))
        bo.price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
        bo.currency = cursor.getString(cursor.getColumnIndex(COLUMN_CURRENCY))
        bo.urlCvs = cursor.getString(cursor.getColumnIndex(COLUMN_URL_CVS))
        bo.category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))
        return bo
    }

    private fun boToContentValues(bo: ServicesConnectedBO): ContentValues {
        val values = ContentValues()
        values.put(COLUMN_VIN, bo.carID)
        values.put(COLUMN_SERVICE_ID, bo.serviceID)
        values.put(COLUMN_TITLE, bo.title)
        values.put(COLUMN_DESCRIPTION, bo.description)
        values.put(COLUMN_SUBSCRIPTION_URL, bo.subscriptionURL)
        values.put(COLUMN_URL,bo.url)
        values.put(COLUMN_PRICE,bo.price)
        values.put(COLUMN_CURRENCY,bo.currency)
        values.put(COLUMN_URL_CVS,bo.urlCvs)
        values.put(COLUMN_CATEGORY, bo.category)
        return values
    }


    /*---------------------------------------------------------------------------
     *  CRUD METHODS
     * --------------------------------------------------------------------------
     */

    /**
     * Insert the bo info for an user
     *
     * @param servicesConnectedBO the BO
     * @return true if OK.
     */
    fun insertOrUpdate(servicesConnectedBO: ServicesConnectedBO):Long {
        openDatabase()

        var rowId: Long = -1
        try {
            rowId = database.insertWithOnConflict(TABLE_NAME, null, boToContentValues(servicesConnectedBO), SQLiteDatabase.CONFLICT_REPLACE)
        } catch (e: Exception) {
      //      Logger.get().e(javaClass, " ", "insertOrUpdate", "COuld not insertOrUpdate or update contract ", e)
        } finally {
            closeDatabase()
        }
        return rowId
    }

    /**
     * Replaces the bo info for an user
     *
     * @param servicesConnectedBO the BO
     * @return true if OK.
     */
    fun update(servicesConnectedBO: ServicesConnectedBO): Long {
        openDatabase()

        var rowId: Long = -1
        try {
            val values = boToContentValues(servicesConnectedBO)
            rowId = database.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        } catch (e: Exception) {
      //      Logger.get().e(javaClass, " ", "insertOrUpdate", "COuld not insertOrUpdate or update contract ", e)
        } finally {
            closeDatabase()
        }
        return rowId
    }

    /*
     * Retrieve data
     *
     * @param serviceID the service ID
     * @param vin       the car VIN
     * @return service info
     */
    fun getServiceByIDAndVIN(serviceID: String, vin: String): ServicesConnectedBO? {
        openDatabase()
        var result: ServicesConnectedBO? = null
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_VIN = '$vin' AND $COLUMN_SERVICE_ID LIKE '$serviceID%'"
        val cursor = database.rawQuery(query, null)

        // order by
        if (cursor != null && cursor.count == 1) {
            cursor.moveToFirst()
            result = cursorToBO(cursor)
        }
        cursor?.close()
        closeDatabase()
        return result
    }

    /**
     * Delete vehicle data
     */
    fun deleteByVin(vin: String?): Int {
        openDatabase()
        var deleteId: Int = -1
        try {
            deleteId = database.delete(TABLE_NAME, "$COLUMN_VIN = ? ", arrayOf(vin))
        } catch (e: Exception) {
         //   Logger.get().e(javaClass, "", "deleteByVIN", "CarConnectedServicesDAO: Could not delete", e)
        } finally {
            closeDatabase()
        }
     //   Logger.get().i(javaClass, "", "deleteByVIN", "CarConnectedServicesDAO : Delete successful")
        return deleteId
    }

    fun getAll(vin:String, serviceID: String){
        //SELECT b.*, a.name FROM tableB AS b INNER JOIN tableA as A ON (b.id=a.id);
        val query = "SELECT $TABLE_NAME.* ," +
                " ${OfferConnectedServiceDAO.TABLE_NAME}.* FROM $TABLE_NAME ," +
                " ${PriceOfferConnectedServiceDAO.TABLE_NAME}.* " +
                " LEFT JOIN ${OfferConnectedServiceDAO.TABLE_NAME} ON WHERE $COLUMN_VIN = '$vin' AND $COLUMN_SERVICE_ID LIKE '$serviceID%'"
    }
    companion object {

        val TABLE_NAME = "CarConnectedService"
        val COLUMN_VIN = "vin"
        val COLUMN_SERVICE_ID = "serviceID"
        val COLUMN_TITLE = "title"
        val COLUMN_DESCRIPTION = "description"
        val COLUMN_SUBSCRIPTION_URL = "subscriptionURL"
        val COLUMN_URL="url"
        val COLUMN_PRICE="price"
        val COLUMN_CURRENCY = "currency"
        val COLUMN_URL_CVS = "urlCvs"
        val COLUMN_CATEGORY = "category"

        private val SQL_CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_VIN + " TEXT NOT NULL, "
                + COLUMN_SERVICE_ID + " TEXT ,"
                + COLUMN_TITLE + " TEXT ,"
                + COLUMN_DESCRIPTION + " TEXT ,"
                + COLUMN_SUBSCRIPTION_URL + " TEXT ,"
                + COLUMN_URL +" TEXT ,"
                + COLUMN_PRICE + " REAL ,"
                + COLUMN_CURRENCY + " TEXT ,"
                + COLUMN_URL_CVS + " TEXT ,"
                + COLUMN_CATEGORY + " TEXT ,"
                + "PRIMARY KEY (" + COLUMN_VIN + "," + COLUMN_SERVICE_ID + ")"
                + ");")
        private val SQL_ALTER_TABLE_ADD_COLUMN_URL = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_URL + " TEXT"
        private val SQL_ALTER_TABLE_ADD_COLUMN_PRICE = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_PRICE + " REAL"
        private val SQL_ALTER_TABLE_ADD_COLUMN_CURRENCY = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_CURRENCY + " TEXT"
        private val SQL_ALTER_TABLE_ADD_COLUMN_CVS_URL = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_URL_CVS + " TEXT"
        private val SQL_ALTER_TABLE_ADD_COLUMN_CATEGORY = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_CATEGORY + " TEXT"

        private val ALL_COLUMNS = arrayOf(
            COLUMN_VIN, COLUMN_SERVICE_ID, COLUMN_TITLE, COLUMN_DESCRIPTION, COLUMN_SUBSCRIPTION_URL,
            COLUMN_URL,
            COLUMN_PRICE,
            COLUMN_CURRENCY,
            COLUMN_URL_CVS, COLUMN_CATEGORY
        )

        private val SQL_ALTER_TABLE_ADD_COLUMN_OFFER = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_URL + " TEXT"
        /*---------------------------------------------------------------------------
     *  INITIALIZATION
     * --------------------------------------------------------------------------
     */

        /**
         * Create the table
         *
         * @param database the SQLiteDatabase current instance
         */
        fun createTable(database: SQLiteDatabase) {
            database.execSQL(SQL_CREATE_TABLE)
        }
        fun upgradeTableAddColumnUrlPrice(database: SQLiteDatabase) {
            try{
                database.execSQL(SQL_ALTER_TABLE_ADD_COLUMN_URL)
                database.execSQL(SQL_ALTER_TABLE_ADD_COLUMN_PRICE)
                database.execSQL(SQL_ALTER_TABLE_ADD_COLUMN_CURRENCY)
                database.execSQL(SQL_ALTER_TABLE_ADD_COLUMN_CVS_URL)
            }catch(e : SQLiteException){

            }
        }

        fun upgradeTableAddColumnCategorization(database: SQLiteDatabase) {
            database.execSQL(SQL_ALTER_TABLE_ADD_COLUMN_CATEGORY)
        }

        fun cursorToBO(cursor: Cursor): ServicesConnectedBO {
            val bo = ServicesConnectedBO()
            bo.carID = cursor.getString(cursor.getColumnIndex(COLUMN_VIN))
            bo.serviceID = cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE_ID))
            bo.title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
            bo.description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION))
            bo.subscriptionURL = cursor.getString(cursor.getColumnIndex(COLUMN_SUBSCRIPTION_URL))
            bo.url = cursor.getString(cursor.getColumnIndex(COLUMN_URL))
            bo.price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
            bo.currency = cursor.getString(cursor.getColumnIndex(COLUMN_CURRENCY))
            bo.urlCvs = cursor.getString(cursor.getColumnIndex(COLUMN_URL_CVS))
            bo.category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY))
            return bo
        }
    }

}
