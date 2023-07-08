package uk.co.tmdavies.armorequip;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import uk.co.tmdavies.armorequip.ArmorEquipEvent.EquipMethod;

import java.util.List;

/**
 * @originalAuthor Arnah
 * @author qShadxw
 * @since March 26, 2023
 */
public class ArmorListener implements Listener{

	private final List<String> blockedMaterials;

	public ArmorListener(List<String> blockedMaterials){
		this.blockedMaterials = blockedMaterials;
	}
	//Event Priority is highest because other plugins might cancel the events before we check.

	@EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
	public final void inventoryClick(final InventoryClickEvent e){
		boolean shift = false, numberKey = false;

		// Huge Block of Checks to make sure we want the correct state.
		// No longer need to check if cancelled because of ignoreCancelled in EventHandler annotation.
		if (e.getAction() == InventoryAction.NOTHING) return;// Why does this get called if nothing happens??
		if (e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) shift = true;
		if (e.getClick().equals(ClickType.NUMBER_KEY)) numberKey = true;
		if (e.getSlotType() != SlotType.ARMOR && e.getSlotType() != SlotType.QUICKBAR && e.getSlotType() != SlotType.CONTAINER) return;
		if (e.getClickedInventory() != null && !e.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
		if (!e.getInventory().getType().equals(InventoryType.CRAFTING) && !e.getInventory().getType().equals(InventoryType.PLAYER)) return;
		if (!(e.getWhoClicked() instanceof Player)) return;

		ArmorType newArmorType = ArmorType.matchType(shift ? e.getCurrentItem() : e.getCursor());

		// Used for drag and drop checking to make sure you aren't trying to place a helmet in the boots slot.
		if (!shift && newArmorType != null && e.getRawSlot() != newArmorType.getSlot()) return;
		if (shift) {
			newArmorType = ArmorType.matchType(e.getCurrentItem());

			if (newArmorType != null) {
				boolean equipping = e.getRawSlot() != newArmorType.getSlot();

				if (newArmorType.equals(ArmorType.HELMET)
						&& (equipping == isAirOrNull(e.getWhoClicked().getInventory().getHelmet()))
						|| newArmorType.equals(ArmorType.CHESTPLATE)
						&& (equipping == isAirOrNull(e.getWhoClicked().getInventory().getChestplate()))
						|| newArmorType.equals(ArmorType.LEGGINGS)
						&& (equipping == isAirOrNull(e.getWhoClicked().getInventory().getLeggings()))
						|| newArmorType.equals(ArmorType.BOOTS)
						&& (equipping == isAirOrNull(e.getWhoClicked().getInventory().getBoots()))) {

					ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getWhoClicked(), EquipMethod.SHIFT_CLICK, newArmorType, equipping ? null : e.getCurrentItem(), equipping ? e.getCurrentItem() : null);
					Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);

					if (armorEquipEvent.isCancelled()) e.setCancelled(true);
				}
			}
		} else {
			ItemStack newArmorPiece = e.getCursor();
			ItemStack oldArmorPiece = e.getCurrentItem();
			if (numberKey) {
				if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) { // Prevents shit in the 2by2 crafting
					ItemStack hotbarItem = e.getClickedInventory().getItem(e.getHotbarButton());
					if (!isAirOrNull(hotbarItem)) {// Equipping
						newArmorType = ArmorType.matchType(hotbarItem);
						newArmorPiece = hotbarItem;
						oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
					} else {// Unequipping
						newArmorType = ArmorType.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem() : e.getCursor());
					}
				}
			} else {
				// unequip with no new item going into the slot.
				if (isAirOrNull(e.getCursor()) && !isAirOrNull(e.getCurrentItem()))
					newArmorType = ArmorType.matchType(e.getCurrentItem());
			}
			if (newArmorType != null && e.getRawSlot() == newArmorType.getSlot()) {
				EquipMethod method = EquipMethod.PICK_DROP;

				if (e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberKey) method = EquipMethod.HOTBAR_SWAP;

				ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) e.getWhoClicked(), method, newArmorType, oldArmorPiece, newArmorPiece);
				Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);

				if (armorEquipEvent.isCancelled()) e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority =  EventPriority.HIGHEST)
	public void playerInteractEvent(PlayerInteractEvent e) {
		if (e.useItemInHand().equals(Result.DENY)) return;
		if (e.getAction() == Action.PHYSICAL) return;
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player player = e.getPlayer();

			if (!e.useInteractedBlock().equals(Result.DENY)) {
				// Having both of these checks is useless, might as well do it though.
				if (e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && !player.isSneaking()) {
					// Some blocks have actions when you right-click them which stops the client from equipping the armor in hand.
					Material mat = e.getClickedBlock().getType();
					for (String s : blockedMaterials) if (mat.name().equalsIgnoreCase(s)) return;
				}
			}

			ArmorType newArmorType = ArmorType.matchType(e.getItem());

			if (newArmorType != null) {
				boolean isAirOrNull = false;

				switch (newArmorType) {
					case HELMET -> isAirOrNull = isAirOrNull(e.getPlayer().getInventory().getHelmet());
					case CHESTPLATE -> isAirOrNull = isAirOrNull(e.getPlayer().getInventory().getChestplate());
					case LEGGINGS -> isAirOrNull = isAirOrNull(e.getPlayer().getInventory().getLeggings());
					case BOOTS -> isAirOrNull = isAirOrNull(e.getPlayer().getInventory().getBoots());
				}

				// Event is constructed with null oldPiece if nothing is equipped in the specific player armor slot.
				if (isAirOrNull) {
					ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), EquipMethod.HOTBAR, ArmorType.matchType(e.getItem()), null, e.getItem());
					Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);

					if (armorEquipEvent.isCancelled()) {
						e.setCancelled(true);
						player.updateInventory();
					}

					return;
				}

				// Event is constructed with oldPiece if they have something equipped in the specific player armor slot.
				ItemStack oldItemPiece = null;
				switch (newArmorType) {
					case HELMET -> oldItemPiece = e.getPlayer().getInventory().getHelmet();
					case CHESTPLATE -> oldItemPiece = e.getPlayer().getInventory().getChestplate();
					case LEGGINGS -> oldItemPiece = e.getPlayer().getInventory().getLeggings();
					case BOOTS -> oldItemPiece = e.getPlayer().getInventory().getBoots();
				}
				ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(e.getPlayer(), EquipMethod.HOTBAR, ArmorType.matchType(e.getItem()), oldItemPiece, e.getItem());
				Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);

				if (armorEquipEvent.isCancelled()) {
					e.setCancelled(true);
					player.updateInventory();
				}
			}
		}
	}
	
	@EventHandler(priority =  EventPriority.HIGHEST, ignoreCancelled = true)
	public void inventoryDrag(InventoryDragEvent event){
		// getType() seems to always be even. Old Cursor gives the item you are equipping
		// Raw slot is the ArmorType slot. Can't replace armor using this method making getCursor() useless.
		ArmorType type = ArmorType.matchType(event.getOldCursor());

		if (event.getRawSlots().isEmpty()) return;
		if (type != null && type.getSlot() == event.getRawSlots().stream().findFirst().orElse(0)) {
			ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent((Player) event.getWhoClicked(), EquipMethod.DRAG, type, null, event.getOldCursor());
			Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);

			if(armorEquipEvent.isCancelled()) {
				event.setResult(Result.DENY);
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void itemBreakEvent(PlayerItemBreakEvent e){
		ArmorType type = ArmorType.matchType(e.getBrokenItem());
		if (type == null) return;

		Player p = e.getPlayer();
		ArmorEquipEvent armorEquipEvent = new ArmorEquipEvent(p, EquipMethod.BROKE, type, e.getBrokenItem(), null);
		Bukkit.getServer().getPluginManager().callEvent(armorEquipEvent);

		if (!armorEquipEvent.isCancelled()) return;

		ItemStack i = e.getBrokenItem().clone();
		i.setAmount(1);
		ItemMeta iMeta = i.getItemMeta();

		// Set Durability is now deprecated. Changing to new method.
		if (iMeta instanceof Damageable) {
			Damageable damageable = (Damageable) iMeta;

			damageable.setDamage(damageable.getDamage() + 1);
			i.setItemMeta(damageable);
		}

		switch (type) {
			case HELMET -> p.getInventory().setHelmet(i);
			case CHESTPLATE -> p.getInventory().setChestplate(i);
			case LEGGINGS -> p.getInventory().setLeggings(i);
			case BOOTS -> p.getInventory().setBoots(i);
		}
	}

	@EventHandler
	public void playerDeathEvent(PlayerDeathEvent e){
		Player p = e.getEntity();
		if (e.getKeepInventory()) return;

		// No way to cancel a death event.
		for (ItemStack i : p.getInventory().getArmorContents()) {
			if (!isAirOrNull(i))
				Bukkit.getServer().getPluginManager().callEvent(new ArmorEquipEvent(p, EquipMethod.DEATH, ArmorType.matchType(i), i, null));
		}
	}

	/**
	 * A utility method to support versions that use null or air ItemStacks.
	 */
	public static boolean isAirOrNull(ItemStack item){
		return item == null || item.getType().equals(Material.AIR);
	}
}
