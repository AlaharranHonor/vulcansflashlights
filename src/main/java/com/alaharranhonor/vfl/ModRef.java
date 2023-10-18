package com.alaharranhonor.vfl;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class ModRef {

    public static final String ID = "vfl";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation res(String name) {
        return new ResourceLocation(ID, name);
    }
}
