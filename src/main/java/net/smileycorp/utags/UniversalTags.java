package net.smileycorp.utags;

import com.google.common.collect.Sets;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

@Mod(Constants.MODID)
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

   public static void logInfo(Object message) {
      logger.log(Level.INFO, String.valueOf(message));
   }

}
