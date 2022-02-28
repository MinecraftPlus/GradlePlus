package org.minecraftplus.gradle.assetator

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.tasks.*

abstract class AssetatorExtension {

    Project project

    AssetatorExtension(Project project) {
        this.project = project
    }

    abstract static class GenerateAssetsExtension {
        @InputFiles File[] input
        @OutputFile File output
        @OutputDirectory @Optional File objectsDir
    }

    @Nested
    abstract GenerateAssetsExtension getGenerate()

    void generate(Closure configuration) {
        project.configure(generate, configuration)
    }

    void generate(Action<? super GenerateAssetsExtension> configuration) {
        configuration.execute(generate)
    }
}
