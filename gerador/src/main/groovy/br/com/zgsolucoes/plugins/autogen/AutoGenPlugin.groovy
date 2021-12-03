package br.com.zgsolucoes.plugins.autogen

import org.gradle.api.Plugin
import org.gradle.api.Project

class AutoGenPlugin implements Plugin<Project> {

    void apply(Project project) {
        // Add the 'gerador' extension object
        AutoGenPluginExtension extension = project.extensions.create('configGeradores', AutoGenPluginExtension)
        // Add a task that uses configuration from the extension object
        project.tasks.register('gerar', AutoGenTask) {
            configs = extension.gerador
            raiz = extension.raiz
            pastaTemplates = extension.pastaTemplates
            parentParams = extension.params
        }
        project.getTasksByName('gerar', false).first().group = 'autogen'
    }

}
