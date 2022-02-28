package org.minecraftplus.gradle.assetator.task

import groovy.io.FileType
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.*
import java.security.MessageDigest

/**
 * Generates assets JSON index file from assets file list
 */
class GenerateAssets extends DefaultTask {

    @InputFiles File[] input
    @OutputFile File output
    @Optional @OutputDirectory File objectsDir

    @TaskAction
    def exec() {
        def list = [:]
        input.each {
            def root = new File(it, 'assets')
            if (!root.exists())
                throw new GradleException("Assets root directory not exist: " + root)

            it.eachFileRecurse(FileType.FILES) { file ->
                def name = root.relativePath(file).toString()
                def size = file.size()
                def hash = sha1(file)

                // Store asset info for JSON index
                list.put(name, [hash: hash, size: size])

                // Generate hashed object file
                if (objectsDir) {
                    def dir = new File(objectsDir, hash.take(2))
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                    def hashed = new File(dir, hash)
                    hashed.createNewFile()
                    hashed.bytes = file.getBytes()
                }
            }
        }

        // Generate JSON
        def json = new JsonBuilder()
        json objects: list
        output.text = JsonOutput.prettyPrint(json.toString())
    }

    // See https://stackoverflow.com/a/43082685
    @Internal MessageDigest digest = MessageDigest.getInstance("SHA-1")

    def sha1(file) {
        digest.update(file.getBytes())
        return digest.digest().collect {
            String.format('%02x', it)
        }.join()
    }
}
