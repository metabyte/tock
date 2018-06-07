/*
 * Copyright (C) 2017 VSCT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.vsct.tock.bot.engine

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse

/**
 *
 */
class BotVerticleTest {

    @Test
    fun `unregisterRouter activates secondary router if one exists`() {
        val verticle = BotVerticle()

        var service1Installed = false
        var service2Installed = false

        val installer1 = verticle.registerServices("/path", {
            service1Installed = true
            it.get("/path").handler { }
        })
        val installer2 = verticle.registerServices("/path", {
            service2Installed = true
            it.get("/path2").handler { }
        })

        verticle.configure()

        assert(service1Installed)
        assertFalse(service2Installed)

        verticle.unregisterServices(installer1)

        assert(service2Installed)
    }
}