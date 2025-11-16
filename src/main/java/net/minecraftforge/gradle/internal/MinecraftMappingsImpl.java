/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.gradle.internal;

import net.minecraftforge.gradle.MinecraftMappings;
import org.gradle.api.model.ObjectFactory;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.regex.Pattern;

abstract class MinecraftMappingsImpl implements MinecraftMappingsInternal {
    private final String channel;
    private final @Nullable String version;

    protected abstract @Inject ObjectFactory getObjects();

    @Inject
    public MinecraftMappingsImpl(String channel, @Nullable String version) {
        var problems = this.getObjects().newInstance(ForgeGradleProblems.class);
        this.channel = Util.checkMappingsParam(problems, channel, "channel");
        this.version = version;
    }

    @Override
    public String getChannel() {
        return this.channel;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public MinecraftMappings withMinecraftVersion(String version) {
        return getObjects().newInstance(MinecraftMappingsImpl.class, this.channel, version);
    }

    static abstract class Parchment extends MinecraftMappingsImpl {
        private static final String TIMESTAMP_REGEX = "\\d{4}.\\d{2}.\\d{2}";
        private static final Pattern TIMESTAMP_LOOKUP = Pattern.compile(TIMESTAMP_REGEX);
        private static final Pattern TIMESTAMP_REMOVAL = Pattern.compile("-?" + TIMESTAMP_REGEX + "?");

        private final String timestamp;
        private final @Nullable String minecraft;

        @Inject
        public Parchment(@Nullable String version) {
            super("parchment", version);

            if (version == null)
                throw new IllegalArgumentException("Parchment mappings version must be present");
            else if (version.contains("-SNAPSHOT"))
                throw new IllegalArgumentException("Parchment snapshots are not supported: " + version);

            var matcher = TIMESTAMP_LOOKUP.matcher(version);
            if (!matcher.find())
                throw new IllegalArgumentException("Parchment version does not contain a timestamp: " + version);
            this.timestamp = matcher.group();

            String minecraft = null;
            var mcVersions = TIMESTAMP_REMOVAL.split(version);
            for (var mcVersion : mcVersions) {
                // We're just looking for the first instance of a Minecraft version
                //
                if (mcVersion.isEmpty()) continue;

                minecraft = mcVersion;
                break;
            }
            this.minecraft = minecraft;
        }

        @Override
        public MinecraftMappings withMinecraftVersion(String version) {
            // assume our version specifies the MC version
            return this.minecraft != null
                ? this
                : getObjects().newInstance(Parchment.class, version + '-' + timestamp);
        }
    }
}
