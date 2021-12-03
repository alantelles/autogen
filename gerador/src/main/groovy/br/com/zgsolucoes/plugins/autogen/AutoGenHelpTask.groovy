package br.com.zgsolucoes.plugins.autogen

import org.gradle.api.DefaultTask
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

class AutoGenHelpTask extends DefaultTask {

    @Input
    ListProperty<ConfigGerador> configs

    @Input
    ListProperty<String> parentParams

    @Option(option = "gerador", description = "Nome do gerador a ser utilizado")
    @Input
    String gerador

    @TaskAction
    void help() {
        ConfigGerador config = configs.get().find {it.nome == gerador}
        if (!config) {
            throw new UnsupportedOperationException("Não existe configuração para o gerador $gerador")
        }

        List<String> paramsUsados = config.params == null ? parentParams.get() : config.params
        String argsExemplo = paramsUsados.collect {"--${it}=argumento"}.join(" ")
        String textoAjuda = """AUTOGEN - GERADOR DE CODIGO BASEADO EM TEMPLATE
Uso do gerador "$config.nome" :
\tNome: $config.nome
\tTemplate: $config.template
\tArquivo saída: $config.arquivoSaida
\tParams: $config.params ${config.params == null ? "(utilizando configuração pai ${parentParams.get()})" : ""}

Exemplo:
    gradle gerar --gerador=${config.nome} --taskArgs="${argsExemplo}"
"""
        println(textoAjuda)
    }

}
