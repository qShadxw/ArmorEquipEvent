# ArmorEquipEvent

You can either take the needed parts out and stick it into your plugin or compile it as a jar and use it as a plugin depend, either way it's useless without adding function to the event.

Should handle most ways to equip armor and not allow you to spam click equipping armor to get any possible benefits you might be adding using the event.

Click here to find the [Spigot](https://www.spigotmc.org/resources/lib-armorequipevent.5478/) page for this plugin/library.

Current ArmorEquipEvent Version: 1.8

## Usage
Git clone, build then use inside your project!
```java
@EventHandler
public void onEquip(ArmorEquipEvent event) {
    
    if (event.getNewArmorPiece() != null && event.getNewArmorPiece().getType() != Material.AIR) {
        // Player has Equipped an Armor Piece.
    }

    if (event.getOldArmorPiece() != null && event.getOldArmorPiece().getType() != Material.AIR) {
        // Player has Unequipped an Armor Piece.    
    }
    
}
```
You can also use the `getMethod()` function within the event to find how exactly they equipped or unequipped that armor piece.
Methods:
```
SHIFT_CLICK: Pressing shift + left or right click
DRAG: Holding shift and dragging the item over the inventory slots then releasing left click.
PICK_DROP: Picking up the item and dropping it into the armor slot or inventory
HOTBAR: Right clicking a piece of armor from the 9 hotbar slots.
HOTBAR_SWAP: When you press 1-9 while hovering over an armor slot to equip/unequip the armor piece to/from that slot
DISPENSER: Being in range of a dispenser when it shoots a piece of armor out.
BROKE: When an armor piece breaks(If cancelled durability gets set to 0 which means one hit till it breaks again)
DEATH: When a player dies.
```
These can be found in the [ArmorEquipEvent.EquipMethod](https://github.com/qShadxw/ArmorEquipEvent/blob/master/src/main/java/uk/co/tmdavies/armorequip/ArmorEquipEvent.java#L105) class.

## Disclaimer
*By [qShadxw](https://github.com/qShadxw) (me)*

This is a fork of an already-existing repository that is listed on GitHub by [Arnah](https://github.com/Arnuh).
<br>[Link to repository](https://github.com/Arnuh/ArmorEquipEvent).

I have forked this to update it to the current versions of Minecraft and fix bugs that I come across. 
This is completely allowed by the author if you check the original repository. They mention "I give full permission to anyone to take over this project." 
and that they are no-longer interested in the plugin.

Current Minecraft Version: [1.19.4](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-19-4)

### Original Author & Collaborators
Author:
- [Arnah](https://github.com/Arnuh)

Collaborator(s): 
- [badbones69](https://github.com/badbones69)
- [borlea](https://github.com/borlea)
- [Keebler17](https://github.com/Keebler17)
- [Kaeios](https://github.com/Kaeios)
