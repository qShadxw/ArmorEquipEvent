package com.codingforcookies.armorequip;

import org.bukkit.inventory.ItemStack;

/**
 * @originalAuthor Arnah
 * @author qShadxw
 * @since March 26, 2023
 */
public enum ArmorType{
	HELMET(5), CHESTPLATE(6), LEGGINGS(7), BOOTS(8);

	private final int slot;

	ArmorType(int slot){
		this.slot = slot;
	}

	/**
	 * Attempts to match the ArmorType for the specified ItemStack.
	 *
	 * @param itemStack The ItemStack to parse the type of.
	 * @return The parsed ArmorType, or null if not found.
	 */
	public static ArmorType matchType(final ItemStack itemStack) {
		if (ArmorListener.isAirOrNull(itemStack)) return null;
		String type = itemStack.getType().name();

		if (type.equals("ELYTRA")) return CHESTPLATE;
		String lastPart = type.split("_")[1];

		return switch(lastPart) {
			case "HELMET", "SKULL", "HEAD" -> HELMET;
			case "CHESTPLATE" -> CHESTPLATE;
			case "BOOTS" -> BOOTS;
			default -> null;
		};
	}

	public int getSlot(){
		return slot;
	}
}