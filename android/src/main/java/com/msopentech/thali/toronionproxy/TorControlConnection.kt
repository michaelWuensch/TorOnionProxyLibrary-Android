package com.msopentech.thali.toronionproxy

import net.freehaven.tor.control.TorControlConnection
import java.io.*
import java.net.Socket

class TorControlConnection : TorControlConnection {

    constructor(socket: Socket) :
            super(socket) {}

    constructor(inputStream: InputStream, outputStream: OutputStream) :
            super(inputStream, outputStream) {}

    constructor(reader: Reader, writer: Writer) :
            super(reader, writer) {}

    @Throws(IOException::class)
    fun takeownership() {
        sendAndWaitForResponse("TAKEOWNERSHIP\r\n", null)
    }

    @Throws(IOException::class)
    fun resetOwningControllerProcess() {
        resetConf(setOf("__OwningControllerProcess"))
    }

    @Throws(IOException::class)
    fun reloadConf() {
        signal("HUP")
    }
}