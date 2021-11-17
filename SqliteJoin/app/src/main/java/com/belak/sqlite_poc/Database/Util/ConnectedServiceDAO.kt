package com.belak.sqlite_poc.Database.Util

import android.content.Context
import android.database.Cursor
import android.util.Log
import com.psa.bouser.mym.dao.OfferConnectedServiceDAO

/**
 * @author Abdelhak GHOUAS
 * @email : abdelhak.ghouas@capgemini.com
 * Created 17/11/2021
 */
class ConnectedServiceDAO(context: Context):AbstractDAO(context) {


    private fun cursorToBO(cursor: Cursor): ServicesConnectedBO {
        cursor.moveToFirst()
        val bo = CarConnectedServicesDAO.cursorToBO(cursor)

        val listOffer = arrayListOf<OfferConnectedServiceBO>()
        val listPrice = arrayListOf<PriceConnectedServiceBO>()

        var offer =OfferConnectedServiceDAO.cursorToBO(cursor)

        lateinit var  tmpOffer:OfferConnectedServiceBO
        lateinit var tmpPrice:PriceConnectedServiceBO


        while (!cursor.isAfterLast) {

            tmpOffer=OfferConnectedServiceDAO.cursorToBO(cursor)
            tmpPrice= PriceOfferConnectedServiceDAO.cursorToBO(cursor)
            if(tmpOffer.id ==offer.id){ // another price for the same offer
                listPrice.add(tmpPrice)
            }else{
                offer.prices=listPrice.toMutableList()
                listPrice.clear()
                listOffer.add(offer)
                offer=tmpOffer
            }


            cursor.moveToNext()
        }
        bo.offers=listOffer
        Log.e("hello",bo.toString())
        return bo
    }
    fun get(serviceID:String, vin:String){
        val query1 = "SELECT DISTINCT ${CarConnectedServicesDAO.TABLE_NAME}.*  ," +
                " ${OfferConnectedServiceDAO.TABLE_NAME}.* , " +
                " ${PriceOfferConnectedServiceDAO.TABLE_NAME}.* "+
                " FROM ${CarConnectedServicesDAO.TABLE_NAME} " +
                " LEFT JOIN ${OfferConnectedServiceDAO.TABLE_NAME} ON  ${CarConnectedServicesDAO.TABLE_NAME}.${CarConnectedServicesDAO.COLUMN_VIN} = '$vin' AND ${CarConnectedServicesDAO.TABLE_NAME}.${CarConnectedServicesDAO.COLUMN_SERVICE_ID} LIKE '$serviceID%'"+
                " LEFT JOIN  ${PriceOfferConnectedServiceDAO.TABLE_NAME} ON  ${OfferConnectedServiceDAO.COLUMN_OFFER_PK} = ${PriceOfferConnectedServiceDAO.COLUMN_OFFER_FK} "

        openDatabase()
            var result: ServicesConnectedBO? = null
       //     val query = "SELECT * FROM ${CarConnectedServicesDAO.TABLE_NAME} WHERE ${CarConnectedServicesDAO.COLUMN_VIN} = '$vins' AND ${CarConnectedServicesDAO.COLUMN_SERVICE_ID} LIKE '$serviceID%'"
            val cursor = database.rawQuery(query1, null)

            // order by
            if (cursor != null ) {
                result = cursorToBO(cursor)
            }
            cursor?.close()
            closeDatabase()


    }
}