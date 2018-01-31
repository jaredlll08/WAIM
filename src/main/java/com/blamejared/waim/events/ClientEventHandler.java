package com.blamejared.waim.events;

import com.blamejared.waim.gui.GuiNewConfirmation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.*;
import net.minecraftforge.fml.common.StartupQuery;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;

public class ClientEventHandler {
    
    public ClientEventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void guiInit(GuiOpenEvent e) {
        if(e.getGui() instanceof GuiConfirmation && !(e.getGui() instanceof GuiNewConfirmation)) {
            GuiNotification gui = (GuiNotification) e.getGui();
            StartupQuery query = null;
            try {
                Field f = gui.getClass().getSuperclass().getDeclaredField("query");
                f.setAccessible(true);
                query = (StartupQuery) f.get(gui);
            } catch(NoSuchFieldException | IllegalAccessException e1) {
                e1.printStackTrace();
            }
            if(query != null)
                e.setGui(new GuiNewConfirmation(query));
        }
    }
}
