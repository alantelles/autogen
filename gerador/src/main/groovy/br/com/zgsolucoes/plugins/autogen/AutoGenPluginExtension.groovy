package br.com.zgsolucoes.plugins.autogen

import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

interface AutoGenPluginExtension {

    ListProperty<ConfigGerador> getGerador()

    Property<String> getRaiz()

}
