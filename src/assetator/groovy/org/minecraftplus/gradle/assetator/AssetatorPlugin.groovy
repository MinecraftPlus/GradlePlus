package org.minecraftplus.gradle.assetator

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.minecraftplus.gradle.assetator.task.GenerateAssets

@SuppressWarnings('unused')
class AssetatorPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.with {
            def ext = extensions.create('assets', AssetatorExtension)
            afterEvaluate {
                tasks.create(name: 'generateAssets',  type: GenerateAssets) {
                    objectsDir = ext.generate.objectsDir
                    input = ext.generate.input
                    output = ext.generate.output
                }
            }
        }
    }
}
