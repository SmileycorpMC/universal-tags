package net.smileycorp.utags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public interface TagReference {

    boolean matches(Item item);

    TagKey<Item> getTag();

    ResourceLocation location();

    boolean isEnabled();

}
