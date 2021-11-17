package com.belak.sqlite_poc.Database.Util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.psa.bouser.mym.dao.OfferConnectedServiceDAO

/**
 * @author Abdelhak GHOUAS
 * @email : abdelhak.ghouas@capgemini.com
 * Created 16/11/2021
 */
class PriceOfferConnectedServiceDAO (context: Context):AbstractDAO(context) {

    private fun cursorToBO(cursor: Cursor): PriceConnectedServiceBO {
        val priceBO = PriceConnectedServiceBO()
        priceBO.periodType =cursor.getString(cursor.getColumnIndex(COLUMN_PERIOD_TYPE))
        priceBO.price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
        priceBO.fk_offer=cursor.getLong(cursor.getColumnIndex(COLUMN_OFFER_FK))
        return priceBO
    }

    private fun boToContentValues(bo: PriceConnectedServiceBO): ContentValues {
        val values = ContentValues()
        values.put(COLUMN_PERIOD_TYPE, bo.periodType)
        values.put(COLUMN_PRICE,bo.price)
        values.put(COLUMN_OFFER_FK,bo.fk_offer)
        return values
    }

    /**
     *
     */
    fun insertOrUpdate(bo: PriceConnectedServiceBO):Long {
        openDatabase()

        var rowId: Long = -1
        try {
            rowId = database.insertWithOnConflict(TABLE_NAME, null, boToContentValues(bo), SQLiteDatabase.CONFLICT_REPLACE)
        } catch (e: Exception) {
             } finally {
            closeDatabase()
        }
        return rowId
    }




    /**
     * return all offers for this services
     */
    fun getAllPricesServiceByID(offerID:Long): List<PriceConnectedServiceBO?> {
        openDatabase()
        val result: ArrayList<PriceConnectedServiceBO?> = ArrayList()
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_OFFER_FK= '$offerID'"
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

    /**
     * return all offers for this services
     */
    fun getAllPricesServiceByID(offerID:Int): List<PriceConnectedServiceBO?> {
        openDatabase()
        val result: ArrayList<PriceConnectedServiceBO?> = ArrayList()
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_OFFER_FK = '$offerID' "
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

        fun cursorToBO(cursor: Cursor): PriceConnectedServiceBO {
            val priceBO = PriceConnectedServiceBO()
            priceBO.periodType =cursor.getString(cursor.getColumnIndex(COLUMN_PERIOD_TYPE))
            priceBO.price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
            priceBO.fk_offer=cursor.getLong(cursor.getColumnIndex(COLUMN_OFFER_FK))
            return priceBO
        }

        const val TABLE_NAME = "PriceOfferConnectedService"
         const val COLUMN_PRICE = "price"
        const val COLUMN_PERIOD_TYPE = "periodType"
        const val COLUMN_PRICE_PK = "pricePK"
       const val COLUMN_OFFER_FK="offerFK"

        private const val SQL_CREATE_TABLE =("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_PRICE + " REAL ,"
                + COLUMN_PRICE_PK + " INTEGER , "
                + COLUMN_PERIOD_TYPE + " TEXT ,"
                + COLUMN_OFFER_FK + " INTEGER ,"
                + "PRIMARY KEY (" + COLUMN_PRICE_PK +")"
                + " FOREIGN KEY ("+ COLUMN_OFFER_FK+") REFERENCES "+ OfferConnectedServiceDAO.TABLE_NAME+"("+ OfferConnectedServiceDAO.COLUMN_OFFER_PK+")"
                + " ON DELETE CASCADE "

        //  + "FOREIGN KEY ("+ COLUMN_VIN_FK+","+COLUMN_SERVICE_ID_FK+") REFERENCES "+OfferConnectedServiceDAO.TABLE_NAME+"("+ OfferConnectedServiceDAO.COLUMN_VIN + "," + OfferConnectedServiceDAO.COLUMN_SERVICE_ID +")"
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


    }
}