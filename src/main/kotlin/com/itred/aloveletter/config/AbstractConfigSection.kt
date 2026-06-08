package com.itred.aloveletter.config

import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import net.minecraft.network.chat.Component
import net.minecraftforge.common.ForgeConfigSpec


abstract class AbstractConfigSection {
    protected abstract val screenName: String
    protected abstract val screenTooltip: String

    lateinit var configSpec: ForgeConfigSpec

    // Must be constructed from scratch whenever called because of how YACL works.
    // 'Is there a better way to do this?'? Yeah. Probably.
    protected fun encompassingCategoryBuilder(): ConfigCategory.Builder {
        return ConfigCategory.createBuilder()
            .name(Component.literal(screenName))
            .tooltip(Component.literal(screenTooltip))
    }


    protected fun <T> simpleTemplateOption(configValue: ForgeConfigSpec.ConfigValue<T>): Option.Builder<T> {

        val name = pathToHumanReadable(configValue.path.last())
        val tooltip = configSpec.get<ForgeConfigSpec.ValueSpec>(configValue.path).comment

        return Option.createBuilder<T>()
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
}