package net.smileycorp.utags;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.awt.*;
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
   public static final TagKey<Item> DAMAGEABLE = createTag("damageable", item -> item.canBeDepleted());
   public static final TagKey<Item> REDSTONE_COMPONENTS = createTag("redstone_components", item -> {
      if (!(item instanceof BlockItem)) return false;
      Block block = ((BlockItem) item).getBlock();
      if (block.getStateDefinition().getProperties().contains(BlockStateProperties.EXTENDED)) return true;
      if (block.getStateDefinition().getProperties().contains(BlockStateProperties.POWERED)) return true;
      BlockPos pos = new BlockPos(0, 0, 0);
      for (BlockState state : block.getStateDefinition().getPossibleStates()) {
         if (state.isSignalSource()) return true;
         for (Direction dir : Direction.values()) {
            if (state.canRedstoneConnectTo(EmptyBlockGetter.INSTANCE, pos, dir)) return true;
            if (state.getSignal(EmptyBlockGetter.INSTANCE, pos, dir) > 0) return true;
         }
      }
      return false;
   });


   public static TagKey<Item> createTag(String name, Predicate<Item> condition) {
      TagKey<Item> tag = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("forge", name));
      TAGS.add(new TagReference(tag, condition));
      return tag;
   }

   public static void logInfo(Object message) {
      logger.log(Level.INFO, String.valueOf(message));
   }

}
