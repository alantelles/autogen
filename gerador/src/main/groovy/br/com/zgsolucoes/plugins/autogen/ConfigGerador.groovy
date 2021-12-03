package br.com.zgsolucoes.plugins.autogen

import groovy.transform.ToString


@ToString
class ConfigGerador {

    String template
    String arquivoSaida
    String nome
    List<String> params

    static ConfigGerador criar (Map<String, Object> args) {
        return new ConfigGerador(*:args)
    }

}
