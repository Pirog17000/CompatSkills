package codersafterdark.compatskills.common.compats.minecraft.item;

import codersafterdark.compatskills.CompatSkills;
import codersafterdark.compatskills.utils.CheckMethods;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.base.LevelLockHandler;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.data.IData;
import crafttweaker.api.oredict.IOreDictEntry;
import crafttweaker.mc1120.data.NBTConverter;
import net.minecraft.nbt.NBTTagCompound;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.stream.Collectors;

@ZenClass("mods.compatskills.OreDictLock")
@ZenRegister
public class OreDictionaryTweaker {
    @ZenMethod
    public static void addOreDictLock(IOreDictEntry entry, String... locked) {
        CompatSkills.LATE_ADDITIONS.add(new AddOreDict(entry, locked));
    }

    @ZenMethod
    public static void addNBTOreDictLock(IOreDictEntry entry, IData tag, String... locked) {
        CompatSkills.LATE_ADDITIONS.add(new AddOreDictNBT(entry, tag, locked));
    }

    private static class AddOreDict implements IAction {
        private final IOreDictEntry entry;
        private final String[] requirements;

        private AddOreDict(IOreDictEntry entry, String... requirements) {
            this.entry = entry;
            this.requirements = requirements;
        }

        @Override
        public void apply() {
            if (CheckMethods.checkIOreDictEntry(entry) & CheckMethods.checkStringArray(requirements)) {
                LevelLockHandler.addLockByKey(new OreDictLock(entry.getName()), RequirementHolder.fromStringList(requirements));
            }
        }

        @Override
        public String describe() {
            return "Setting the requirement of Ore Dictionary Entry: " + entry + " to: " +
                    Arrays.stream(requirements).map(string -> string + ", ").collect(Collectors.joining());
        }
    }

    private static class AddOreDictNBT implements IAction {
        private final IOreDictEntry entry;
        private final IData data;
        private final String[] requirements;

        private AddOreDictNBT(IOreDictEntry entry, IData tag, String... locked) {
            this.entry = entry;
            this.data = tag;
            this.requirements = locked;
        }

        @Override
        public void apply() {
            if (CheckMethods.checkIOreDictEntry(entry) & CheckMethods.checkValidNBTTagCompound(data) & CheckMethods.checkStringArray(requirements)) {
                RequirementHolder holder = RequirementHolder.fromStringList(requirements);
                LevelLockHandler.addLockByKey(new OreDictLock(entry.getName(), (NBTTagCompound) NBTConverter.from(data)), holder);
            }
        }

        @Override
        public String describe() {
            return "Adding NBT lock: " + data + " for Ore Dictionary Entry: " + entry + " with Requirements: " +
                    Arrays.stream(requirements).map(string -> string + ", ").collect(Collectors.joining());
        }
    }
}