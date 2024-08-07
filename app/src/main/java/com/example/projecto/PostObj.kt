package com.example.projecto

data class PostObj(
   val dogAge:String = "",
   val dogDes:String = "",
   val dogImg:String = "",
   val dogName:String = "",
   val dogOwner:String = "",
   val ownerAddress:String = "",
   val ownerCity:String = "",
   val ownerPhone:String = "",
   val postId:String = "",
   val vaccination:Boolean = true,

)

data class CartObj(
   val postId:String = "",
   val userMob:String = "",
   val beneficiary:MutableList<String>,
   val sponsorDate:String = "",
   var paymentStatus:String = ""
)

