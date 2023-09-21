package net.smileycorp.utags;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

@Mod(Constants.MODID)
@Mod.EventBusSubscriber
public class UniversalTags {

   private static Logger logger = Logger.getLogger(Constants.NAME);
   private final ForgeConfigSpec config;

   public UniversalTags() {
      ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
      for (ConfigurableTagReference reference : DefaultTags.getTagReferences()) reference.addToConfig(builder);
      config = builder.build();
      ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, config);
   }

   @SubscribeEvent
   public static void addTags(AddTagsEvent event) {
      event.addTags(DefaultTags.getTagReferences());
   }

   public static void logInfo(Object message) {
      logger.log(Level.INFO, String.valueOf(message));
   }

}
