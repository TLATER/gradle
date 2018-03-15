/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gradle.kotlin.dsl

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.PluginAware


/**
 * Applies the given plugin or script.
 *
 * @param from a script to apply, evaluated as per [Project.file]
 * @param plugin a id of the plugin to apply
 * @param to the plugin target object or collection of objects, target is self when null
 * @see [PluginAware.apply]
 */
fun PluginAware.apply(from: Any? = null, plugin: String? = null, to: Any? = null) {
    apply {
        if (plugin != null) it.plugin(plugin)
        if (from != null) it.from(from)
        if (to != null) it.to(to)
    }
}


/**
 * Applies the given plugin. Does nothing if the plugin has already been applied.
 *
 * The given class should implement the [Plugin] interface.
 *
 * @param T the plugin type.
 * @see [PluginAware.apply]
 */
inline
fun <reified T : Plugin<*>> PluginAware.apply() {
    apply {
        it.plugin(T::class.java)
    }
}


/**
 * Applies the given plugin to the specified object. Does nothing if the plugin has already been applied.
 *
 * The given class should implement the [Plugin] interface.
 *
 * @param T the plugin type.
 * @param to the plugin target object or collection of objects
 * @see [PluginAware.apply]
 */
inline
fun <reified T : Plugin<*>> PluginAware.apply(to: Any) {
    apply {
        it.plugin(T::class.java)
        it.to(to)
    }
}
