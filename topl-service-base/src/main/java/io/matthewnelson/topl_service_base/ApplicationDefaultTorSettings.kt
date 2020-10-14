package io.matthewnelson.topl_service_base

import io.matthewnelson.topl_core_base.TorSettings

/**
 * Simply extends [TorSettings] so that the `topl-service` module cannot be initialized
 * with [io.matthewnelson.topl_service_base.BaseServiceTorSettings].
 *
 * See [TorSettings] for information
 * */
abstract class ApplicationDefaultTorSettings: TorSettings()