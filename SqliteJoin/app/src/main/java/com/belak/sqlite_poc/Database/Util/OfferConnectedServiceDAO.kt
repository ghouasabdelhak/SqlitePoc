package com.psa.bouser.mym.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.belak.sqlite_poc.Database.Util.AbstractDAO
import com.belak.sqlite_poc.Database.Util.CarConnectedServicesDAO
import com.belak.sqlite_poc.Database.Util.OfferConnectedServiceBO

/**
 * @author Abdelhak GHOUAS
 * @email : abdelhak.ghouas@capgemini.com
 * Created 15/11/2021
 */
class OfferConnectedServiceDAO(context: Context): AbstractDAO(context) {

    private fun cursorToBO(cursor: Cursor): OfferConnectedServiceBO {
        val offerBo =OfferConnectedServiceBO()
        offerBo.id= cursor.getLong(cursor.getColumnIndex(COLUMN_OFFER_PK))
        offerBo.pricingModel =cursor.getString(cursor.getColumnIndex(COLUMN_PRICING_MODEL))
        offerBo.vin=cursor.getString(cursor.getColumnIndex(COLUMN_VIN_FK))
        offerBo.serviceId=cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE_ID_FK))
        return offerBo
    }

    private fun boToContentValues(bo:OfferConnectedServiceBO): ContentValues {
        val values = ContentValues()
        values.put(COLUMN_PRICING_MODEL, bo.pricingModel)
        values.put(COLUMN_SERVICE_ID_FK, bo.serviceId)
        values.put(COLUMN_VIN_FK,bo.vin)
        return values
    }

    fun insertOrUpdate(offerConnectedBO: OfferConnectedServiceBO):Long {
        openDatabase()

        var rowId: Long = -1
        try {
            rowId = database.insertWithOnConflict(TABLE_NAME, null, boToContentValues(offerConnectedBO), SQLiteDatabase.CONFLICT_REPLACE)
        } catch (e: Exception) {
        } finally {
            closeDatabase()
        }
        return rowId
    }

    /**
     *
     */
    fun getOfferServiceByID(serviceID: String, vin: String):OfferConnectedServiceBO? {
        openDatabase()
        var result:OfferConnectedServiceBO? = null
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_VIN_FK = '$vin' AND $COLUMN_SERVICE_ID_FK LIKE '$serviceID%'"
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
     * return all offers for this services
     */
    fun getAllOffersServiceByID(serviceID: String, vin: String): List<OfferConnectedServiceBO?> {
        openDatabase()
        var result: ArrayList<OfferConnectedServiceBO?> = ArrayList()
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_VIN_FK = '$vin' AND $COLUMN_SERVICE_ID_FK LIKE '$serviceID%'"
        val cursor = database.rawQuery(query, null)


        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            // The Cursor is now set to the right position
            result.add(cursorToBO(cursor))
            cursor.moveToNext()
        }

        cursor?.close()
        closeDatabase()

        return result
    }
    companion object {

        const val TABLE_NAME = "OfferConnectedService"
        const val COLUMN_PRICING_MODEL = "pricingModel"
        const val COLUMN_OFFER_PK = "offerPK"
        const val COLUMN_VIN_FK = "vin"
        private const val COLUMN_SERVICE_ID_FK = "serviceID"

        private val SQL_CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_PRICING_MODEL + " TEXT ,"
                + COLUMN_OFFER_PK +" INTEGER , "
                + COLUMN_SERVICE_ID_FK + " TEXT ,"
                + COLUMN_VIN_FK + " TEXT ,"
                + "PRIMARY KEY (" + COLUMN_OFFER_PK +")"
                + " FOREIGN KEY ("+ COLUMN_VIN_FK+","+COLUMN_SERVICE_ID_FK+") REFERENCES "+ CarConnectedServicesDAO.TABLE_NAME+"("+ CarConnectedServicesDAO.COLUMN_VIN + "," + CarConnectedServicesDAO.COLUMN_SERVICE_ID +")"
                + " ON DELETE CASCADE "
                + ");")



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

        fun cursorToBO(cursor: Cursor): OfferConnectedServiceBO {
            val offerBo =OfferConnectedServiceBO()
            offerBo.id= cursor.getLong(cursor.getColumnIndex(COLUMN_OFFER_PK))
            offerBo.pricingModel =cursor.getString(cursor.getColumnIndex(COLUMN_PRICING_MODEL))
            offerBo.vin=cursor.getString(cursor.getColumnIndex(COLUMN_VIN_FK))
            offerBo.serviceId=cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE_ID_FK))
            return offerBo
        }

    }

}