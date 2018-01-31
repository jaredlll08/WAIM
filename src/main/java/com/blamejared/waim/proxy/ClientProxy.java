package com.blamejared.waim.proxy;

import com.blamejared.waim.events.ClientEventHandler;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void registerEvents() {
        super.registerEvents();
        new ClientEventHandler();
    }
}
