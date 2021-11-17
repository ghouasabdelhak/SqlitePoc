package com.hellohasan.sqlite_project.Util

/**
 * @author Abdelhak GHOUAS
 * @email : abdelhak.ghouas@capgemini.com
 * Created 15/11/2021
 */
data class OfferConnectedServiceBO(
    var id :Long=-1 ,
    var pricingModel: String="",
    var vin :String="",
    var serviceId:String ="",
    var prices: List<PriceConnectedServiceBO> = listOf()
)


data class PriceConnectedServiceBO(
    var fk_offer:Long=-1 ,
    var periodType: String="",
    var price: Double=0.0
)