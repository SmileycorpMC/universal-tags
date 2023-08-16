package net.smileycorp.utags;

import com.google.common.collect.Sets;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Mod(Constants.MODID)
@Mod.EventBusSubscriber(modid = Constants.MODID)
public class UniversalTags {

   private static Logger logger = Logger.getLogger(Constants.NAME);

   public static final Set<TagReference> TAGS = Sets.newHashSet();

   public static final TagKey<Item> FOODS = createTag("foods", item -> item.isEdible());
   public static final TagKey<Item> FUELS = createTag("furnace_fuels", item -> FurnaceBlockEntity.isFuel(new ItemStack(item)));
   public static final TagKey<Item> BLOCKS = createTag("blocks", item -> item instanceof BlockItem);

   public static TagKey<Item> createTag(String name, Predicate<Item> condition) {
      TagKey<Item> tag = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", name));
      TAGS.add(new TagReference(tag, condition));
      return tag;
   }

   //@SubscribeEvent(priority = EventPriority.HIGHEST)
   public static void updateTags(TagsUpdatedEvent event) {
      if (event.getUpdateCause() != TagsUpdatedEvent.UpdateCause.SERVER_DATA_LOAD) return;
      ForgeRegistries.ITEMS.forEach(item -> {
         UniversalTags.TAGS.forEach(ref-> {
            if (ref.matches(item)) {
               UniversalTags.logInfo("adding " + item + " to tag " + ref.getTag().location());
               Set<TagKey<Item>> tags = item.builtInRegistryHolder().getTagKeys().collect(Collectors.toSet());
               tags.add(ref.getTag());
               item.builtInRegistryHolder().bindTags(tags);
            }
         });
      });
   }

   public static void logInfo(Object message) {
      logger.log(Level.INFO, String.valueOf(message));
   }

}
