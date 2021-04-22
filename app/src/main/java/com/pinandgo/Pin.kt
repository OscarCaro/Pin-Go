package com.pinandgo

import java.net.URL

class Pin (val link : String){

    val domain : String = URL(link).host

}