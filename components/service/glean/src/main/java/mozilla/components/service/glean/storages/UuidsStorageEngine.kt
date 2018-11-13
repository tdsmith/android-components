/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package mozilla.components.service.glean.storages

import android.annotation.SuppressLint
import java.util.UUID

import mozilla.components.service.glean.Lifetime
import mozilla.components.support.base.log.logger.Logger

/**
 * This singleton handles the in-memory storage logic for uuids. It is meant to be used by
 * the Specific UUID API and the ping assembling objects. No validation on the stored data
 * is performed at this point: validation must be performed by the Specific Uuids API.
 *
 * This class contains a reference to the Android application Context. While the IDE warns
 * us that this could leak, the application context lives as long as the application and this
 * object. For this reason, we should be safe to suppress the IDE warning.
 */
@SuppressLint("StaticFieldLeak")
internal object UuidsStorageEngine : UuidsStorageEngineImplementation()

open class UuidsStorageEngineImplementation(
    override val logger: Logger = Logger("glean/UuidsStorageEngine")
) : GenericScalarStorageEngine<UUID>() {

    override fun singleMetricDeserializer(value: Any?): UUID? {
        return try {
            if (value is String) UUID.fromString(value) else null
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    /**
     * Record a uuid in the desired stores.
     *
     * @param stores the list of stores to record the uuid into
     * @param category the category of the uuid
     * @param name the name of the uuid
     * @param lifetime the lifetime of the stored metric data
     * @param value the uuid value to record
     */
    @Synchronized
    fun record(
        stores: List<String>,
        category: String,
        name: String,
        lifetime: Lifetime,
        value: UUID
    ) {
        super.recordScalar(stores, category, name, lifetime, value)
    }
}
