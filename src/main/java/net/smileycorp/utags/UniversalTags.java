package net.smileycorp.utags;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.*;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.logging.Level;
import java.util.logging.Logger;

@Mod(Constants.MODID)
@Mod.EventBusSubscriber
public class UniversalTags {

   private static Logger logger = Logger.getLogger(Constants.NAME);

   @SubscribeEvent
   public static void addTags(AddTagsEvent event) {
      event.addTag("foods", item -> item.isEdible());
      event.addTag("furnace_fuels", item -> FurnaceBlockEntity.isFuel(new ItemStack(item)));
      event.addTag("blocks", item -> item instanceof BlockItem);
      event.addTag("damageable", item -> item.canBeDepleted());
      event.addTag("unstackable", item -> item.getMaxStackSize() <= 1);
      event.addTag("redstone_components", item -> {
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
      event.addTag("brewing_ingredients", item -> {
         for (IBrewingRecipe recipe : BrewingRecipeRegistry.getRecipes()) {
            if (recipe.isIngredient(new ItemStack(item))) return true;
         }
         return false;
      });
      event.addTag("potions", item -> item instanceof PotionItem);
   }

   public static void logInfo(Object message) {
      logger.log(Level.INFO, String.valueOf(message));
   }

}
