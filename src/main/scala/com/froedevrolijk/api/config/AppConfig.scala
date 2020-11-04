package com.froedevrolijk.api.config

case class AppConfig(
    address: String,
    port: Int,
    selectorBlazeName: String,
    apiHttpPrefix: String,
    debug: ServerDebugConfig
)
