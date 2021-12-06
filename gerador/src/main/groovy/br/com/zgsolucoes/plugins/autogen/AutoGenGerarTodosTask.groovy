package br.com.zgsolucoes.plugins.autogen

import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

class AutoGenGerarTodosTask extends DefaultTask {

    @Input
    MapProperty<String, Map<String, Object>> configs

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

        configs.get().each { config ->
            try {
                println("Executando geração para $config.key")
                AutoGenProcessor processor = new AutoGenProcessor(
                        parentParams: parentParams.get(),
                        configGerador: config.value,
                        gerador: config.key,
                        taskArgs: taskArgs,
                        raiz: raiz.get(),
                        pastaTemplates: pastaTemplates.get()
                )
                processor.run()
            } catch (Exception e) {
                println("Processamento do gerador $config.key falhou: ${e.message}")
            }
        }

    }

}
