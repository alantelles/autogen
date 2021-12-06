package br.com.zgsolucoes.plugins.autogen

import groovy.text.SimpleTemplateEngine

class AutoGenProcessor {

    List<String> parentParams
    Map<String, Object> configGerador
    String taskArgs
    String raiz
    String gerador
    String pastaTemplates

    List<String> rawArgs
    Map<String, String> parsed = [:]

    SimpleTemplateEngine engine = new SimpleTemplateEngine()

    void parsearArgumentos() {
        rawArgs = taskArgs.split("--")[1..-1]
        List<String> paramsUsados = configGerador.params != null ? configGerador.params as List<String> : parentParams
        println("Parâmetros utilizados: $paramsUsados")
        println("Argumentos: $rawArgs")
        paramsUsados.each { String nomeArg ->
            String valorArg = rawArgs.find{it.split("=")?.first()?.strip() == nomeArg.strip() }
            if (!valorArg) {
                throw new IllegalArgumentException("Parâmetro obrigatório $nomeArg não encontrado.")
            }
            valorArg = valorArg.split("=")[1]?.strip()
            parsed.put(nomeArg, valorArg)
        }
    }

    void run() {
        parsearArgumentos()
        configGerador.processarArgs?.call(parsed)
        gerarArquivo()
    }

    private String carregarTemplate() {
        String caminho = "$pastaTemplates/${configGerador.template}.txt"
        try {
            return new File(caminho)?.text
        } catch (IOException ignored) {
            String msg = "O arquivo de template para o processamento de ${gerador} não foi encontrado."
            msg = "${msg} Local esperado: $caminho"
            throw new FileNotFoundException(msg)
        }
    }

    private String formatarTemplate() {
        String template = carregarTemplate()
        return engine.createTemplate(template).make(parsed).toString()
    }

    private void gerarArquivo() {
        String caminhoSaida = engine
                .createTemplate("$raiz/${configGerador.arquivoSaida}")
                .make(parsed)
                .toString()
        if (!(new File(caminhoSaida).exists())) {
            String conteudo = formatarTemplate()
            println "O arquivo gerado será gravado em $caminhoSaida"
            try {
                mkdirs(caminhoSaida)
                File saida = new File(caminhoSaida)
                saida.write(conteudo)
                println("Arquivo gerado em $caminhoSaida com sucesso!")
            } catch (IOException e) {
                throw new IOException("Arquivo não pôde ser gerado: $e.message")
            }
        } else {
            println("O arquivo $caminhoSaida já existe. Não será sobreescrito.")
            println("Encerrando tarefa.")
        }
    }

    static private Boolean mkdirs(String caminhoArquivo) {
        println "Criando caminho para arquivo de saída."
        String pastaDestino = caminhoArquivo[0..caminhoArquivo.lastIndexOf("/") - 1]
        List<String> parts = pastaDestino.split("/")
        String previous = ""
        parts.each {
            previous = "${ previous == "" ? it : previous + '/' + it}"
            File caminho = new File(previous)
            if (!caminho.exists()) {
                Boolean ok = caminho.mkdir()
                if (!ok) {
                    throw new RuntimeException("Não foi possível criar o caminho de saída do arquivo")
                }
            }
        }
        return true
    }

}
