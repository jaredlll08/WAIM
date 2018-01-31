package com.blamejared.waim.gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.*;
import net.minecraftforge.fml.common.*;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class GuiNewConfirmation extends GuiConfirmation {
    
    public int offset = 7;
    
    public GuiNewConfirmation(StartupQuery query) {
        super(query);
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 38,100,20, I18n.format("gui.yes")));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 155 + 210, this.height - 38, 100,20,I18n.format("gui.no")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 155 + 105, this.height - 38, 100, 20, I18n.format("gui.notepad")));
    }
    
    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        ;
        String text = query.getText();
        String[] lines = text.split("\n");
        
        List<String> newLines = new LinkedList<>();
        int count = 0;
        for(String s : lines) {
            if(count++ == 6) {
                newLines.add("You can use the scroll wheel to traverse the missing entries!");
                newLines.add("");
            }
            newLines.add(s);
        }
        lines = newLines.toArray(new String[0]);
        int spaceAvailable = this.height - 38 - 20;
        int spaceRequired = Math.min(spaceAvailable, 10 + 10 * lines.length);
        
        int offset = 10 + (spaceAvailable - spaceRequired) / 2; // vertically centered
        Map<String, List<String>> missingMap = new LinkedHashMap<>();
        String category = "";
        for(String s : lines) {
            if(s.startsWith("Missing ")) {
                category = s.split("Missing ")[1].trim();
                
            }
            if(!category.isEmpty() && !s.startsWith("Missing")) {
                List<String> list = missingMap.getOrDefault(category, new LinkedList<>());
                list.add(s);
                missingMap.put(category, list);
            }
        }
        
        int counter = 0;
        for(String line : lines) {
            if(counter++ > 3 + missingMap.size())
                if(counter < this.offset) {
                    continue;
                }
            if(offset >= spaceAvailable) {
                this.drawCenteredString(this.fontRenderer, "...", this.width / 2, offset, 0xFFFFFF);
                break;
            } else {
                if(!line.isEmpty()) {
                    if(line.startsWith("Missing ")) {
                        line = String.format("Missing: %s %s", missingMap.get(line.split("Missing ")[1]).size(), line.split("Missing ")[1]);
                        offset += 10;
                        this.drawCenteredString(this.fontRenderer, line, this.width / 2, offset, 0xFFFFFF);
                        offset += 10;
                    } else {
                        if(line.split(":").length == 2 && counter > 7) {
                            String reason;
                            String modid = line.split(":")[0].trim();
                            if(!Loader.isModLoaded(modid)) {
                                reason = "Mod removed!";
                            } else {
                                reason = "Entry removed or renamed!";
                            }
                            line += "        Reason: " + reason;
                        }
                        this.drawCenteredString(this.fontRenderer, line, this.width / 2, offset, 0xFFFFFF);
                    }
                }
                offset += 10;
            }
        }
        
//         super.drawScreen(mouseX, mouseY, partialTicks);
        for(int i = 0; i < this.buttonList.size(); ++i) {
            this.buttonList.get(i).drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
        
        for(int j = 0; j < this.labelList.size(); ++j) {
            this.labelList.get(j).drawLabel(this.mc, mouseX, mouseY);
        }
    }
    
    @Override
    public void handleInput() throws IOException {
        super.handleInput();
        if(Mouse.getEventDWheel() > 0) {
            offset++;
            if(offset > query.getText().split("\n").length) {
                offset = query.getText().split("\n").length;
            }
        }
        if(Mouse.getEventDWheel() < 0) {
            offset--;
            if(offset < 7) {
                offset = 7;
            }
        }
    }
    
    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    @Override
    protected void actionPerformed(GuiButton button) {
        if(button.enabled && (button.id == 0 || button.id == 1)) {
            FMLClientHandler.instance().showGuiScreen(null);
            query.setResult(button.id == 0);
            query.finish();
        }
        if(button.id == 2){
            try {
                File file = new File("logs" + File.separator + "waim.txt");
                file.createNewFile();
                PrintWriter writer = new PrintWriter(file);
                writer.println(query.getText());
                writer.close();
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}