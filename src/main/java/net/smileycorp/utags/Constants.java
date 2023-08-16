package net.smileycorp.utags;

import net.minecraft.resources.ResourceLocation;

public class Constants {

    public static final String MODID = "utags";

    public static final String NAME = "Universal Tags";

    public static ResourceLocation loc(String name) {
        return new ResourceLocation(MODID, name);
    }

}
