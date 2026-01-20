package net.minecraftforge.gradle.internal;

import net.minecraftforge.gradle.MinecraftMappings;
import org.gradle.api.artifacts.ModuleIdentifier;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.ValueSource;
import org.gradle.api.provider.ValueSourceParameters;
import org.gradle.process.ExecOperations;

import javax.inject.Inject;

abstract class SyncMavenizerAction implements ValueSource<Integer, SyncMavenizerAction.Parameters> {
    interface Parameters extends ValueSourceParameters {
        DirectoryProperty getCaches();

        DirectoryProperty getOutput();

        Property<ModuleIdentifier> getModule();

        Property<String> getVersion();

        Property<MinecraftMappings> getMappings();

        ConfigurableFileCollection getAccessTransformer();

        ListProperty<String> getRepositories();
    }

    protected abstract @Inject ExecOperations getExecOperations();

    @Override
    public Integer obtain() {
        return this.exec(getParameters());
    }

    private int exec(Parameters parameters) {

    }
}
