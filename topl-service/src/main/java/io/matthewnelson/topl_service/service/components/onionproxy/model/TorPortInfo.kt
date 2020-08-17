package io.matthewnelson.topl_service.service.components.onionproxy.model

/**
 * Contains information regarding what ports Tor is operating on.
 *
 * Example of what one of the fields will contain:
 *
 *   - "127.0.0.1:33432"
 * */
class TorPortInfo(
    val controlPort: String?,
    val dnsPort: String?,
    val httpPort: String?,
    val socksPort: String?
)