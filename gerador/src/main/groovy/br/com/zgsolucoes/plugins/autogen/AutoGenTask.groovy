package br.com.zgsolucoes.plugins.autogen

import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

class AutoGenTask extends DefaultTask {

    @Input
    ListProperty<ConfigGerador> configs

    @Option(option = "gerador", description = "Nome do gerador a ser utilizado")
    @Input
    String gerador

    @Option(option = "taskArgs", description = "Argumentos a serem utilizados pela tarefa")
    @Input
    String taskArgs

    @Input
    Provider<String> raiz

    @Input
    ListProperty<String> parentParams

    @Input
    Provider<String> pastaTemplates

    @TaskAction
    void gerar() {
        println "Raiz: ${raiz.get()}"
        println "Argumentos: $taskArgs"
        // configs.get().each { ConfigGerador config ->
        ConfigGerador config = configs.get().find {it.nome == gerador }
        if (!config) {
            throw new UnsupportedOperationException("Gerador $gerador não está registrado")
        }
        String texto = """
Gerador: 
\tNome: $config.nome
\tTemplate: $config.template
\tArquivo saída: $config.arquivoSaida
\tParams: $config.params
"""
        println texto

        AutoGenProcessor processor = new AutoGenProcessor(
                parentParams: parentParams.get(),
                configGerador: config,
                taskArgs: taskArgs,
                raiz: raiz.get(),
                pastaTemplates: pastaTemplates.get()
        )
        processor.run()
    }

}
