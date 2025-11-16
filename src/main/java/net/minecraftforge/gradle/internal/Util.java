/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */
package net.minecraftforge.gradle.internal;

import net.minecraftforge.gradleutils.shared.SharedUtil;
import org.codehaus.groovy.runtime.StringGroovyMethods;
import org.gradle.api.NamedDomainObjectSet;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ModuleIdentifier;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.ArrayList;
import java.util.Arrays;

final class Util extends SharedUtil {
    public static String forgeToMcVersion(String version) {
        // Save for a few april-fools versions, Minecraft doesn't use _ in their version names.
        // So when Forge needs to reference a version of Minecraft that uses - in the name, it replaces
        // it with _
        // This could cause issues if we ever support a version with _ in it, but fuck it I don't care right now.
        int idx = version.indexOf('-');
        if (idx == -1)
            throw new IllegalArgumentException("Invalid Forge version: " + version);
        return version.substring(0, idx).replace('_', '-');
    }

    public static String mcpToMcVersion(String version) {
        // MCP names can either be {MCVersion} or {MCVersion}-{Timestamp}, EXA: 1.21.1-20240808.132146
        // So lets see if the thing following the last - matches a timestamp
        int idx = version.lastIndexOf('-');
        if (idx < 0)
            return version;
        if (!version.substring(idx + 1).matches("\\d{8}\\.\\d{6}"))
            return version;
        return version.substring(0, idx);
    }

    static String checkMappingsParam(ForgeGradleProblems problems, @UnknownNullability Object param, String name) {
        if (param == null)
            throw problems.nullMappingsParam(name);

        return param.toString();
    }

    static boolean isPresent(String c) {
        return !c.isBlank();
    }

    static String dependencyToCamelCase(Dependency dependency) {
        return dependencyToCamelCase(dependency.getGroup(), dependency.getName());
    }

    static String dependencyToCamelCase(ModuleIdentifier dependency) {
        return dependencyToCamelCase(dependency.getGroup(), dependency.getName());
    }

    static String dependencyToCamelCase(@Nullable String group, String name) {
        var list = new ArrayList<String>(3);

        if (group != null)
            list.addAll(Arrays.asList(group.split("\\.")));

        list.add(name);

        var builder = new StringBuilder(64);
        for (var s : list) {
            builder.append(StringGroovyMethods.capitalize(s));
        }
        return builder.toString();
    }

    static @Nullable SourceSet getSourceSet(NamedDomainObjectSet<Configuration> configurations, SourceSetContainer sourceSets, Dependency dependency) {
        for (var sourceSet : sourceSets) {
            if (contains(configurations, sourceSet, false, dependency)) {
                return sourceSet;
            }
        }

        return null;
    }
}
