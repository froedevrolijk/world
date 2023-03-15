package com.froedevrolijk.api.config

case class ServerDebugConfig(
    header: Boolean = false,
    body: Boolean = false,
    logAction: Boolean = false
)
