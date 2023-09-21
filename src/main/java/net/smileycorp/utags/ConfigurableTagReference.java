package net.smileycorp.utags;

import com.google.common.collect.Lists;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Predicate;

public class ConfigurableTagReference implements TagReference {

    private final ResourceLocation location;
    private final Predicate<Item> condition;
    private TagKey<Item> tag;
    private ForgeConfigSpec.BooleanValue enabled;
    private ForgeConfigSpec.ConfigValue<List<String>> blacklist;
    private ForgeConfigSpec.ConfigValue<List<String>> whitelist;


    /*Used for tags that have an entry in a config
    addToConfig must be called and the tag added to a config spec or the game will not load the tag
    use SimpleTagReference for tags that don't need to be registered in a config
     */
    public ConfigurableTagReference(ResourceLocation location, Predicate<Item> condition) {
        this.location = location;
        this.condition = condition;
    }

    public boolean matches(Item item) {
        String name = ForgeRegistries.ITEMS.getKey(item).toString();
        if (blacklist.get().contains(name)) return false;
        if (whitelist.get().contains(name)) return true;
        return condition.test(item);
    }

    public TagKey<Item> getTag() {
        if (tag == null) tag = TagKey.create(Registry.ITEM_REGISTRY, location());
        return tag;
    }

    public ResourceLocation location() {
        return location;
    }

    public boolean isEnabled() {
        return enabled == null ? false : enabled.get();
    }

    public void addToConfig(ForgeConfigSpec.Builder builder) {
        String name = location().toString();
        builder.push(name);
        enabled = builder.comment("Whether the tag " + name + " should be enabled.").define("enabled", true);
        blacklist = builder.comment("Items that should never be given the tag " + name + ".").define("blacklist", Lists.newArrayList());
        whitelist = builder.comment("Items that should be given the tag " + name + ", even if they do not meet it's conditions").define("whitelist", Lists.newArrayList());
        builder.pop();
    }

}
