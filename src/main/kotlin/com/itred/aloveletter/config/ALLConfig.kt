package com.itred.aloveletter.config

import com.electronwill.nightconfig.core.EnumGetMethod
import com.itred.aloveletter.ALoveLetter
import com.itred.aloveletter.config.impl.AbstractEncompassingConfigHolder
import com.itred.aloveletter.event.configurable.server.BlueAxolotlPing
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.AirItem
import net.minecraft.world.item.BowItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistry

object ALLConfig {

    const val SCREEN_NAME = "Mod config"

    val COMMON_CONFIG: CommonConfig
    val CLIENT_CONIFG: ClientConfig
    val SERVER_CONFIG: ServerConfig


    class CommonConfig : AbstractEncompassingConfigHolder {

        // Blue Axolotl tweaks
        val blueAxolotPingMasterSwitch: ForgeConfigSpec.BooleanValue
        val blueAxolotlPingRange: ForgeConfigSpec.IntValue
        val blueAxolotNaturalSpawn: ForgeConfigSpec.BooleanValue
        val blueAxolotNaturalSpawnChance: ForgeConfigSpec.IntValue
        val blueAxolotBredSpawnChance: ForgeConfigSpec.IntValue

        // Rebalancing
        val rebalanceEnableBowNerf: ForgeConfigSpec.BooleanValue
        val rebalanceEnableCrossbowBuffs: ForgeConfigSpec.BooleanValue
        val rebalanceEnableMultishotNoInvulnerability: ForgeConfigSpec.BooleanValue
        val rebalanceEnableBowDrawResetOnHit: ForgeConfigSpec.BooleanValue
        val rebalanceBowDrawResetExceptions: ForgeConfigSpec.ConfigValue<List<String>>

        // Durability
        val durabilityEnableUnbreakableItemTweaks: ForgeConfigSpec.BooleanValue
        val durabilityUnbreakableTierList: ForgeConfigSpec.ConfigValue<List<String>>
        val durabilityUnbreakableItemList: ForgeConfigSpec.ConfigValue<List<String>>

        constructor(commonBuilder: ForgeConfigSpec.Builder) {


            commonBuilder.push("blueaxolotltweaks")

                blueAxolotPingMasterSwitch = commonBuilder
                    .comment("Enable or disable playing a sound effect when a player goes near a blue axolotl.",
                        "",
                        "The sound effect itself can also be enabled or disabled on the client's end, but it will always respect this option on the server-side.")
                    .worldRestart()
                    .define("blueAxolotlPingMasterswitch", true)


                blueAxolotlPingRange = commonBuilder
                    .comment(
                        "If blueAxolotlPingMasterswitch is enabled, this controls the range around the Axolotl in which the sound will play.", "",
                        "(radius, in blocks, of a sphere centered around the axolotl).", "",
                        "Higher values may cause client-side lag, and also make it harder to pinpoint where the axolotl is!")
                    .defineInRange("blueAxolotlPingRange", 32, 1, 256) // REMEMBER TO UPDATE THIS IN THE CONFIG SCREEN!!!!!

                blueAxolotNaturalSpawn = commonBuilder
                    .comment(
                        "Enable or disable blue axolotls spawning naturally in the world."
                    )
                    .worldRestart()
                    .define("blueAxolotlNaturalSpawning", true)

                blueAxolotNaturalSpawnChance = commonBuilder
                    .comment(
                        "If the above option is enabled, the spawn chance of blue axolotls will follow this config value" ,"",
                        "(Functions as a 1 in N chance, with higher values making them rarer)"
                    )
                    .defineInRange("blueAxolotlNaturalSpawnChance", 4096, 1, 8192)

                blueAxolotBredSpawnChance = commonBuilder
                    .comment(
                        "Regardless of the state of the above two options, you can modify the chance of obtaining blue axolotls the vanilla way (from breeding)", "",
                        "(Functions as a 1 in N chance, with higher values making them rarer)", "",
                        "Default (vanilla) odds: 1 in 1200", ""
                    )
                    .defineInRange("blueAxolotlBredSpawnChance", 1200, 1, 8192)

            commonBuilder.pop()

            commonBuilder.push("rebalance")


                rebalanceEnableBowNerf = commonBuilder
                    .comment("Enable or disable the damage nerf applied to the bow & arrow.")
                    .worldRestart()
                    .define("enableBowNerf", true)

                rebalanceEnableCrossbowBuffs = commonBuilder
                    .comment("Enable or disable the small damage buff applied to the crossbow.")
                    .worldRestart()
                    .define("enableCrossbowBuffs", true)

                rebalanceEnableMultishotNoInvulnerability = commonBuilder
                    .comment(
                        "If enabled, the multishot enchantment will allow all of its arrows to hit an entity at once.", "",
                        "The damage of each subsequent arrow will be lowered quite a bit, however."
                    )
                    .worldRestart()
                    .define("enableMultishotNotInvulnerability", true)

                rebalanceEnableBowDrawResetOnHit = commonBuilder
                    .comment(
                        "If enabled, the draw time of all bow-and-arrow items will be reset on melee (or melee-adjacent) hit.", "",
                        "",
                        "TECHNICAL: This will attempt to apply to any modded items which extend the BowItem class.", "",
                        "It won't work for all of them, and may break some of them. Define exceptions as-needed in the list option below."
                    )
                    .worldRestart()
                    .define("enableBowDrawResetOnHit", true)

                rebalanceBowDrawResetExceptions = commonBuilder
                    .comment(
                        "Any item IDs in this list, if they are bow items that would normally be affected by the above option, will be exempt from the effects of the above option.", "",
                        "Use this if it's somehow breaking any modded items you wish to use."
                    )
                    .worldRestart()
                    .defineListAllowEmpty(
                        "bowDrawResetExceptions",
                        mutableListOf(),
                        ::bowDrawResetExceptionsValidator
                    )

            commonBuilder.pop()

            commonBuilder.push("durabilitytweaks")

                durabilityEnableUnbreakableItemTweaks = commonBuilder
                    .comment(
                        "Enables or disables the highest tiers of weapons, tools, and armor having no durability value, and being unbreakable.", "",
                        "Can be configured in the lists below."
                    )
                    .worldRestart()
                    .define("enableUnbreakableItemTweaks", true)

                durabilityUnbreakableTierList = commonBuilder
                    .comment(
                        "The following tool/item/armor tiers will be made unbreakable, if the above option is enabled.", "",
                        "To define a tier, place its corrosponding repair item (i.e., \"netherite ingot\") into this list."
                    )
                    .worldRestart()
                    .defineListAllowEmpty<String>(
                        "unbreakableTierList",
                        listOf<String>(
                            ForgeRegistries.ITEMS.getKey(Items.NETHERITE_INGOT).toString()
                        ),
                        ::unbreakableTiersListValidator
                    )

                durabilityUnbreakableItemList = commonBuilder
                    .comment("Similar to the list above, but allows you to define it by-item. Any items in this list will be made unbreakable.", "",
                            "Only takes effect if durabilityEnableUnbreakableItemTweaks is enabled.")
                    .worldRestart()
                    .defineListAllowEmpty<String>(
                        "unbreakableItemList",
                        mutableListOf<String>(
                            ForgeRegistries.ITEMS.getKey(Items.ELYTRA).toString(),
                            ForgeRegistries.ITEMS.getKey(Items.TURTLE_HELMET).toString()
                        ),
                        ::unbreakableItemListValidator
                    )

            commonBuilder.pop()

            this.configSpec = commonBuilder.build()
        }


        /** Attempts a lookup for the given object (supposedly a string) in the given registry.
         *
         * Either returns an object of that registry's type, or null, if the object isn't a string or isn't present in the given registry.
         */
        private fun <T> validateAsResourceLocationToRegistry(registry: IForgeRegistry<T>, potentialID: Any): T? {
            // If the item Id:
            // - Is a string,
            // - Can be a valid resource location,
            // - AND is present in the Items registry,
            // THEN we know it's a real item.

            if (potentialID !is String) {
                return null
            }

            if (!ResourceLocation.isValidResourceLocation(potentialID)) {
                ALoveLetter.LOGGER.warn(
                    "Listed id \"$potentialID\" failed validation, because it isn't a resource location!",
                    "It must contain a colon seperator (\":\"), with a namespace (mod id) to the left of the colon, and the id of the item to the right of the colon!"
                )
                return null
            }

            return registry.getValue(ResourceLocation.tryParse(potentialID))


        }

        private fun unbreakableTiersListValidator(itemId: Any): Boolean {
            val item = validateAsResourceLocationToRegistry<Item>(ForgeRegistries.ITEMS, itemId)
            if (item == null || item is AirItem) {
                ALoveLetter.LOGGER.warn("Item id \"$itemId\" failed validation, because it didn't resolve to any known item!")
                return false
            }
            return true
        }

        private fun unbreakableItemListValidator(itemId: Any): Boolean {
            val item = validateAsResourceLocationToRegistry<Item>(ForgeRegistries.ITEMS, itemId)

            return item?.isDamageable(item.defaultInstance) ?: false
        }

        private fun bowDrawResetExceptionsValidator(itemId: Any): Boolean {
            val item = validateAsResourceLocationToRegistry<Item>(ForgeRegistries.ITEMS, itemId)
            if (item == null || item is AirItem) {
                ALoveLetter.LOGGER.warn("Item id \"$itemId\" failed validation, because it didn't resolve to any known item!")
                return false
            }

            if (item !is BowItem) {
                ALoveLetter.LOGGER.warn("Item id \"$itemId\" is present, but failed validation because it isn't internally defined as a bow item!")
                return false
            }

            return true

        }

    }

    class ClientConfig: AbstractEncompassingConfigHolder {



        // Blue Axolot ping
        val blueAxolotlPingSFX: ForgeConfigSpec.EnumValue<BlueAxolotlPing.PingSoundEffect>

        constructor(clientBuilder: ForgeConfigSpec.Builder) {

            blueAxolotlPingSFX = clientBuilder
                .comment(
                    "Plays a fitting sound effect when you're near a blue axolotl, or when one spawns in near you.", "",
                    "Blue Axolotl Ping must be enabled in the server config for this to take effect.", "",
                    "",
                    "Values:", "",
                    "   NONE - No sound effect plays.", "",
                    "   BW - Plays the Shiny Pokemon sound effect from Pokemon Black and White.", "",
                    "   PLA - Plays the Shiny Pokemon sound effect from Pokemon Legends: Arceus and Pokemon Legends: ZA.", "",
            )
                .defineEnum(
                    "blueAxolotlPing",
                    BlueAxolotlPing.PingSoundEffect.PLA,
                    EnumGetMethod.ORDINAL_OR_NAME_IGNORECASE,
                    BlueAxolotlPing.PingSoundEffect.PLA,
                    BlueAxolotlPing.PingSoundEffect.BW,
                    BlueAxolotlPing.PingSoundEffect.NONE
                )

        }


    }

    class ServerConfig {
        // No in-world config screen in this version, so no point in making a server config screen
        lateinit var configSpec: ForgeConfigSpec

        constructor(serverBuilder: ForgeConfigSpec.Builder) {

        }

    }

    init {

        // Construct each of the config sub-classes, Forge turns them into pairs of the class itself and their spec for us
        val COMMON_PAIR = ForgeConfigSpec.Builder().configure(::CommonConfig)
        val CLIENT_PAIR = ForgeConfigSpec.Builder().configure(::ClientConfig)
        val SERVER_PAIR = ForgeConfigSpec.Builder().configure(::ServerConfig)

        // Store 'em all to be registered and grabbed later
        COMMON_CONFIG = COMMON_PAIR.left
        COMMON_CONFIG.configSpec = COMMON_PAIR.right

        CLIENT_CONIFG = CLIENT_PAIR.left
        CLIENT_CONIFG.configSpec = CLIENT_PAIR.right

        SERVER_CONFIG = SERVER_PAIR.left
        SERVER_CONFIG.configSpec = SERVER_PAIR.right

    }







}