package br.com.zgsolucoes.plugins.autogen

import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

class AutoGenTask extends DefaultTask {

    @Input
    MapProperty<String, Map<String, Object>> configs

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

        Map<String, Object> config = configs.get().find {k, v -> k == gerador }?.value
        if (!config) {
            throw new UnsupportedOperationException("Gerador $gerador não está registrado")
        }
        println("Executando geração para $gerador")
        AutoGenProcessor processor = new AutoGenProcessor(
                parentParams: parentParams.get(),
                configGerador: config,
                gerador: config,
                taskArgs: taskArgs,
                raiz: raiz.get(),
                pastaTemplates: pastaTemplates.get()
        )
        processor.run()
    }

}
