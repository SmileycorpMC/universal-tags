package net.smileycorp.utags;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import java.util.List;
import java.util.function.Predicate;

public class DefaultTags {
    
    private static final List<ConfigurableTagReference> TAG_REFERENCES = Lists.newArrayList();

    public static final ConfigurableTagReference FOODS = register("foods", item -> item.isEdible());
    public static final ConfigurableTagReference FUELS = register("furnace_fuels", item -> FurnaceBlockEntity.isFuel(new ItemStack(item)));
    public static final ConfigurableTagReference BLOCKS = register("blocks", item -> item instanceof BlockItem);
    public static final ConfigurableTagReference DAMAGEABLE = register("damageable", item -> item.canBeDepleted());
    public static final ConfigurableTagReference UNSTACKABLE = register("unstackable", item -> item.getMaxStackSize() <= 1);
    public static final ConfigurableTagReference REDSTONE_COMPONENTS = register("redstone_components", item -> {
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
    public static final ConfigurableTagReference BREWING_INGREDIENTS = register("brewing_ingredients", item -> {
        for (IBrewingRecipe recipe : BrewingRecipeRegistry.getRecipes()) {
            if (recipe.isIngredient(new ItemStack(item))) return true;
        }
        return false;
    });
    public static final ConfigurableTagReference POTIONS = register("potions", item -> item instanceof PotionItem);

    private static ConfigurableTagReference register(String name, Predicate<Item> condition) {
        ConfigurableTagReference reference = new ConfigurableTagReference(new ResourceLocation("forge", name), condition);
        TAG_REFERENCES.add(reference);
        return reference;
    }

    public static List<ConfigurableTagReference> getTagReferences() {
        return TAG_REFERENCES;
    }
    
}
