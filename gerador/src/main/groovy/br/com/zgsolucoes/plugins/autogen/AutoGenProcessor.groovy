package br.com.zgsolucoes.plugins.autogen

import groovy.text.SimpleTemplateEngine

class AutoGenProcessor {

    ConfigGerador configGerador
    String taskArgs
    String raiz
    String pastaTemplates

    List<String> rawArgs
    Map<String, String> parsed = [:]

    SimpleTemplateEngine engine = new SimpleTemplateEngine()

    void checarNumeroArgumentos() {
        rawArgs = taskArgs.split("--")[1..-1]
        println("rawArgs: $rawArgs")
        if (rawArgs.size() != configGerador.params.size()) {
            String msg = "Número de argumentos passados difere do declarado no gerador."
            msg = "${msg} (esperado: ${configGerador.params.size()}, fornecido: ${rawArgs.size()})"
            throw new IllegalArgumentException(msg)
        }
    }

    void parsearArgumentos() {
        configGerador.params.each { String nomeArg ->
            String valorArg = rawArgs.find{it.split("=")[0].strip() == nomeArg.strip()}?.split("=")[1]?.strip()
            if (!valorArg) {
                throw new IllegalArgumentException("Parâmetro obrigatório $nomeArg não encontrado.")
            }
            parsed.put(nomeArg, valorArg)
        }
    }

    void run() {
        checarNumeroArgumentos()
        parsearArgumentos()
        gerarArquivo()
    }

    private String carregarTemplate() {
        String caminho = "$pastaTemplates/${configGerador.template}.txt"
        try {
            return new File(caminho)?.text
        } catch (IOException ignored) {
            String msg = "O arquivo de template para o processamento de ${configGerador.nome} não foi encontrado."
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
                new File(caminhoSaida[0..caminhoSaida.lastIndexOf("/")]).mkdirs()
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

}
