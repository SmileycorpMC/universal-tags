package net.smileycorp.utags.mixin;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagLoader;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.smileycorp.utags.TagReference;
import net.smileycorp.utags.UniversalTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(TagLoader.class)
public class MixinTagLoader {

    @Shadow @Final private String directory;

    @Inject(method = "build(Ljava/util/Map;)Ljava/util/Map;", at = @At(value = "TAIL"), cancellable = true)
    private <T> void build(Map<ResourceLocation, List<TagLoader.EntryWithSource>> p_203899_, CallbackInfoReturnable<Map<ResourceLocation, Collection<T>>> callback) {
        if (!directory.equals("tags/items")) return;
        Map<ResourceLocation, Collection<T>> map = callback.getReturnValue();
        ForgeRegistries.ITEMS.forEach(item -> {
            UniversalTags.TAGS.forEach(ref-> {
                if (ref.matches(item)) {
                    Collection<T> collection = map.get(ref.location());
                    collection = collection == null ? Lists.newArrayList() : Lists.newArrayList(collection);
                    collection.add((T) item.builtInRegistryHolder());
                    map.put(ref.location(), collection);
                    UniversalTags.logInfo("adding " + item + " to tag " + ref.location());
                }
            });
        });
    }
}
