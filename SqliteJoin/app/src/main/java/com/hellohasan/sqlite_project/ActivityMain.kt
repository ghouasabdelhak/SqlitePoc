package com.hellohasan.sqlite_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hellohasan.sqlite_project.Database.DatabaseHelper
import com.hellohasan.sqlite_project.Util.*
import com.psa.bouser.mym.dao.OfferConnectedServiceDAO
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Abdelhak GHOUAS
 * @email : abdelhak.ghouas@capgemini.com
 * Created 17/11/2021
 */
class ActivityMain :AppCompatActivity(){
lateinit var databaseHelper:DatabaseHelper
lateinit var  priceOfferConnectedServiceDAO: PriceOfferConnectedServiceDAO
lateinit var carServiceDAO: CarConnectedServicesDAO
lateinit var connectedServiceDAO:ConnectedServiceDAO
lateinit var offerConnectedServiceDAO:OfferConnectedServiceDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseHelper= DatabaseHelper.getInstance(this)
        initViews()
    }
    private fun initViews() {
        connectedServiceDAO=ConnectedServiceDAO(this)
        carServiceDAO=CarConnectedServicesDAO(this)
        offerConnectedServiceDAO=OfferConnectedServiceDAO(this)
        priceOfferConnectedServiceDAO= PriceOfferConnectedServiceDAO(this)
        load.setOnClickListener {
            connectedServiceDAO.get("serviceID","carID")
           val result=  carServiceDAO.getServiceByIDAndVIN("serviceID","carID")
            decrypted_text.text= result?.price.toString()
        }

        val servicesConnectedBO = ServicesConnectedBO()
        servicesConnectedBO.carID = "carID"
        servicesConnectedBO.serviceID = "serviceID"
        servicesConnectedBO.title = "this.title"
        servicesConnectedBO.description = "this.description"
        servicesConnectedBO.subscriptionURL = "this.subscriptionURL"
        servicesConnectedBO.url = "this.url"
        servicesConnectedBO.price = 99.0
        servicesConnectedBO.currency = "this.currency"
        servicesConnectedBO.urlCvs = "this.urlCvs"
        servicesConnectedBO.category = "this.category"
        servicesConnectedBO.offers= listOf(OfferConnectedServiceBO(pricingModel = "Periodical",
            prices = listOf(PriceConnectedServiceBO(periodType = "Monthly",price = 15.0),
                PriceConnectedServiceBO(periodType = "Anually",price = 100.0)
            )
        ), OfferConnectedServiceBO(pricingModel = "OneOff",prices = listOf(PriceConnectedServiceBO(periodType = "",price = 300.0))))

        val id =carServiceDAO.insertOrUpdate(servicesConnectedBO)
        for ( offer in servicesConnectedBO.offers){
           servicesConnectedBO.carID?.let {
               offer.vin=it
           }
            servicesConnectedBO.serviceID?.let {
                offer.serviceId=it
            }
            val idOffer =offerConnectedServiceDAO.insertOrUpdate(offer)
            for (price in offer.prices){
                price.fk_offer=idOffer
                priceOfferConnectedServiceDAO.insertOrUpdate(price)
            }
        }

    }


}