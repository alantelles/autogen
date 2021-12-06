package br.com.zgsolucoes.plugins.autogen

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property

abstract class AutoGenPluginExtension {

    abstract MapProperty<String, Map<String, Object>> getGerador()

    abstract Property<String> getRaizDestino()

    abstract Property<String> getPastaTemplates()

    abstract ListProperty<String> getParams()

    AutoGenPluginExtension() {
        params.convention([])
        raizDestino.convention(".")
        pastaTemplates.convention("templates")
    }

}
