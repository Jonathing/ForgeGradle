/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.gradle.internal;

import net.minecraftforge.gradle.MinecraftMappings;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.reflect.HasPublicType;
import org.gradle.api.reflect.TypeOf;
import org.jetbrains.annotations.Nullable;

interface MinecraftMappingsInternal extends MinecraftMappings, HasPublicType {
    @Override
    default TypeOf<?> getPublicType() {
        return TypeOf.typeOf(MinecraftMappings.class);
    }

    static MinecraftMappings create(ObjectFactory objects, String channel, @Nullable String version) {

    }
}
