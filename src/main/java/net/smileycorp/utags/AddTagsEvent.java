package net.smileycorp.utags;

import com.google.common.collect.Sets;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Event;

import java.util.Set;
import java.util.function.Predicate;

public class AddTagsEvent extends Event {

    protected Set<TagReference> tags = Sets.newHashSet();


    public void addTag(String name, Predicate<Item> condition) {
        TagKey<Item> tag = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", name));
        tags.add(new TagReference(tag, condition));
    }

    public Set<TagReference> getTags() {
        return tags;
    }

}
