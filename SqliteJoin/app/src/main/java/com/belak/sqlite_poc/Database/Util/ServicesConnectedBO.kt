package com.belak.sqlite_poc.Database.Util

import android.os.Parcel
import android.os.Parcelable

class ServicesConnectedBO : Parcelable {

    var carID: String? = null //VIN

    var serviceID: String? = null
    var title: String? = null
    var description: String? = null
    var subscriptionURL: String? = null
    var url: String? = null
    var price: Double? = null
    var currency:String? = null
    var urlCvs:String? = null
    var category: String? = null
    var offers: List<OfferConnectedServiceBO> = listOf()

    constructor() {}

    private constructor(`in`: Parcel) {
        carID = `in`.readString()
        serviceID = `in`.readString()
        title = `in`.readString()
        description = `in`.readString()
        subscriptionURL = `in`.readString()
        url = `in`.readString()
        price = `in`.readDouble()
        currency = `in`.readString()
        urlCvs = `in`.readString()
        category = `in`.readString()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(carID)
        dest.writeString(serviceID)
        dest.writeString(title)
        dest.writeString(description)
        dest.writeString(subscriptionURL)
        dest.writeString(url)
        price?.let { dest.writeDouble(it) }
        dest.writeString(currency)
        dest.writeString(urlCvs)
        dest.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ServicesConnectedBO> {
        override fun createFromParcel(parcel: Parcel): ServicesConnectedBO {
            return ServicesConnectedBO(parcel)
        }

        override fun newArray(size: Int): Array<ServicesConnectedBO?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "$carID , $serviceID , ${offers.map { 
            it.pricingModel + " "+
           it.prices.map { 
               it.periodType + it.price.toString()
           }
        }},$title  "
    }
}
