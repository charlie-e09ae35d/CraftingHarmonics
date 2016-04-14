Very basic recipe manager implementation for 1.9.

Developing an addon:
- Add the following to your build.gradle at the top level (not inside the buildscript portion):

repositories {
    maven { url "http://maven.winterblade.org/content/repositories/minecraft/" }
}

- Add the following to dependencies, replacing the third part with your preferred build of Crafting Harmonics:
    deobfCompile "org.winterblade.minecraft:craftingharmonics:1.9.0-1.2.1.3"
