package com.kimkazandi.security

import javax.net.ssl.SSLContext

class Security {

    companion object{
        fun getSSLContext() : SSLContext {
            val trustManager = CustomTrustManager()
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf(trustManager), null)
            SSLContext.setDefault(sslContext)
            return sslContext
        }
    }

}