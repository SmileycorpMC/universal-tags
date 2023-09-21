package net.smileycorp.utags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.function.Predicate;

public class SimpleTagReference implements TagReference {

    private final TagKey<Item> tag;
    private final Predicate<Item> condition;

    public SimpleTagReference(TagKey<Item> tag, Predicate<Item> condition) {
        this.tag = tag;
        this.condition = condition;
    }

    public boolean matches(Item item) {
        return condition.test(item);
    }

    public TagKey<Item> getTag() {
        return tag;
    }

    public ResourceLocation location() {
        return tag.location();
    }

    public boolean isEnabled() {
        return true;
    }

}
