package br.com.zgsolucoes.plugins.autogen

class ConfigGerador {

    String template
    String arquivoSaida
    String nome
    List<String> params

    static ConfigGerador criar (Map<String, Object> args) {
        return new ConfigGerador(*:args)
    }

    String toString() {
        return "template: ${template}\narquivoSaida: $arquivoSaida\nnome: $nome\nparams: $params"
    }

}
