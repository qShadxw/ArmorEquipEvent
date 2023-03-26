# ArmorEquipEvent
[![](https://jitpack.io/v/qShadxw/ArmorEquipEvent.svg)](https://jitpack.io/#qShadxw/ArmorEquipEvent)

You can either take the needed parts out and stick it into your plugin or compile it as a jar and use it as a plugin depend, either way it's useless without adding function to the event.

Should handle most ways to equip armor and not allow you to spam click equipping armor to get any possible benefits you might be adding using the event.

Current ArmorEquipEvent Version: 1.8

## Use in Project
### Maven
```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
```xml
	<dependency>
	    <groupId>com.github.qShadxw</groupId>
	    <artifactId>ArmorEquipEvent</artifactId>
	    <version>1.8-release</version>
	</dependency>
```
### Gradle
```gradle
	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```
```gradle
	dependencies {
	        implementation 'com.github.qShadxw:ArmorEquipEvent:1.8-release'
	}
```

## Disclaimer
*By [qShadxw](https://github.com/qShadxw) (me)*

This is a fork of an already-existing repository that is listed on GitHub by [Arnah](https://github.com/Arnuh).
<br>[Link to repository](https://github.com/Arnuh/ArmorEquipEvent).

I have forked this to update it to the current versions of Minecraft and fix bugs that I come across.

Current Minecraft Version: [1.19.4](https://www.minecraft.net/en-us/article/minecraft-java-edition-1-19-4)

### Original Author & Collaborators
Author:
- [Arnah](https://github.com/Arnuh)

Collaborator(s): 
- [badbones69](https://github.com/badbones69)
- [borlea](https://github.com/borlea)
- [Keebler17](https://github.com/Keebler17)
- [Kaeios](https://github.com/Kaeios)