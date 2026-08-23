package com.itred.aloveletter.config.impl

import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.ListOption
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import net.minecraft.network.chat.Component
import net.minecraftforge.common.ForgeConfigSpec

abstract class AbstractConfigSection(val parentConfigSpec: ForgeConfigSpec) {

    companion object {
        const val CATEGORY_PREFIX: String = "config.aloveletter.category"
    }

    abstract val screenTranslationKey: String
    abstract val side: ConfigSide

    // Must be constructed from scratch whenever called because of how YACL works.
    // 'Is there a better way to do this?'? Yeah. Probably.
    protected fun encompassingCategoryBuilder(): ConfigCategory.Builder {
        return ConfigCategory.createBuilder()
            .name(Component.translatable("$CATEGORY_PREFIX.${side.name}.$screenTranslationKey"))
            .tooltip(Component.translatable("$CATEGORY_PREFIX.${side.name}.$screenTranslationKey.tooltip"))
    }


    protected fun <T> simpleTemplateOption(configValue: ForgeConfigSpec.ConfigValue<T>): Option.Builder<T> {

        val name = pathToHumanReadable(configValue.path.last())
        val tooltip = parentConfigSpec.get<ForgeConfigSpec.ValueSpec>(configValue.path).comment

        return Option.createBuilder<T>()
            .name(Component.literal(name))
            .description(OptionDescription.of(Component.literal(tooltip)))
            .binding(
                configValue.default!!,
                {configValue.get()!!},
                {newValue -> configValue.set(newValue)}
            )
    }

    protected fun <T> listTemplateOption(configValue: ForgeConfigSpec.ConfigValue<List<T>>): ListOption.Builder<T> {

        val name = pathToHumanReadable(configValue.path.last())
        val tooltip = parentConfigSpec.get<ForgeConfigSpec.ValueSpec>(configValue.path).comment

        return ListOption.createBuilder<T>()
            .name(Component.literal(name))
            .description(OptionDescription.of(Component.literal(tooltip)))
            .binding(
                configValue.default!!,
                {configValue.get()!!},
                {newValue -> configValue.set(newValue)}
            )
    }

    /** Converts a path in camelCase into a set of words for the display name of a config option. */
    // The lengths I will go to just so I can be a teensy bit more lazy...
    private fun pathToHumanReadable(nameCamelCase: String): String {

        var newName = ""

        for (i in 0..<nameCamelCase.length) {

            val character = nameCamelCase.get(i)

            // Capitalize the first character of the name
            if (i == 0) {
                newName += character.uppercase()
            } else if (character.isUpperCase()) {
                // Prepend a space to every subsequent capital letter
                newName += " $character"
            } else {
                newName += character
            }

        }

        return newName
    }

    enum class ConfigSide {
        SERVER("server"),
        CLIENT("client"),
        COMMON("common");

        val prefix: String

        constructor(prefix: String) {
            this.prefix = prefix
        }

    }
}