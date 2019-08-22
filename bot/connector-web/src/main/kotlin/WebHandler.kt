/*
 * Copyright (C) 2017/2019 VSCT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.vsct.tock.bot.connector.web

import fr.vsct.tock.bot.connector.ConnectorHandler
import fr.vsct.tock.bot.definition.ConnectorStoryHandler
import kotlin.reflect.KClass

/**
 * To specify [ConnectorStoryHandler] for Web connector.
 * [KClass] passed as [value] of this annotation must have a primary constructor
 * with a single not optional [StoryHandlerDefinitionBase] argument.
 */
@ConnectorHandler(connectorTypeId = WEB_CONNECTOR_ID)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class WebHandler(val value: KClass<out ConnectorStoryHandler<*>>)