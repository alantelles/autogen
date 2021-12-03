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

    @Option(option = "taskArgs", description = "Argumentos a serem utilizados pela tarefa")
    @Input
    String taskArgs

    @Input
    Provider<String> raiz

    @TaskAction
    void gerar() {
        println "Raiz: ${raiz.get()}"
        println "Argumentos: $taskArgs"
        configs.get().each {
            String texto = """
Gerador: 
\tNome: $it.nome
\tTemplate: $it.template
\tArquivo saída: $it.arquivoSaida
"""
            String content = new File('tops.txt').text
            new File('tops2.txt').write("${content}${texto}")
        }
    }

}
