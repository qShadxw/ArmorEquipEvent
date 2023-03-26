package com.codingforcookies.armorequip;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * @originalAuthor Arnah
 * @author qShadxw
 * @since March 26, 2023
 */
public class Main extends JavaPlugin {

	@Override
	public void onEnable(){
		saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new ArmorListener(getConfig().getStringList("blocked")), this);
		try{
			//Better way to check for this? Only in 1.13.1+?
			Class.forName("org.bukkit.event.block.BlockDispenseArmorEvent");
			getServer().getPluginManager().registerEvents(new DispenserArmorListener(), this);
		}catch(Exception ignored){}
	}

}