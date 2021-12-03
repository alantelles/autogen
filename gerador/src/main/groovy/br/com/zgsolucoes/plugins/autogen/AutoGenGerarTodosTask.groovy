package br.com.zgsolucoes.plugins.autogen

import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

class AutoGenGerarTodosTask extends DefaultTask {

    @Input
    ListProperty<ConfigGerador> configs

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
    void gerarTodos() {

        configs.get().each { ConfigGerador config ->
            try {
                println("Executando geração para $config.nome")
                AutoGenProcessor processor = new AutoGenProcessor(
                        parentParams: parentParams.get(),
                        configGerador: config,
                        taskArgs: taskArgs,
                        raiz: raiz.get(),
                        pastaTemplates: pastaTemplates.get()
                )
                processor.run()
            } catch (Exception e) {
                println("Processamento do gerador $config.nome falhou: ${e.message}")
            }
        }

    }

}
