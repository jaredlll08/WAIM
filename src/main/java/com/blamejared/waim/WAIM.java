package com.blamejared.waim;

import com.blamejared.waim.proxy.CommonProxy;
import com.blamejared.waim.reference.Reference;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class WAIM {
    
    @Mod.Instance(Reference.MODID)
    public static WAIM INSTANCE;
    
    @SidedProxy(clientSide = "com.blamejared.waim.proxy.ClientProxy", serverSide = "com.blamejared.waim.proxy.CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.registerEvents();
    }
}
