package codersafterdark.compatskills.common.compats.reskillable.levellocking;

import codersafterdark.compatskills.CompatSkills;
import codersafterdark.compatskills.common.compats.reskillable.playerexpansion.wrapper.CTSkill;
import codersafterdark.compatskills.utils.CheckMethods;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.base.LevelLockHandler;
import crafttweaker.IAction;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ModOnly("crafttweaker")
@ZenClass("mods.compatskills.SkillLocks")
@ZenRegister
public class SkillLocksTweaker {
    @ZenMethod
    public static void addLevelLock(CTSkill skill, int level, String... defaultRequirements) {
        CompatSkills.LATE_ADDITIONS.add(new AddLevelLock(skill, level, defaultRequirements));
    }

    private static class AddLevelLock implements IAction {
        private final CTSkill skill;
        private final int level;
        private final String[] requirements;

        private AddLevelLock(CTSkill skill, int level, String... requirements) {
            this.skill = skill;
            this.level = level;
            this.requirements = requirements;
        }

        @Override
        public void apply() {
            if (CheckMethods.checkSkill(skill.getSkill()) & CheckMethods.checkInt(level) & CheckMethods.checkStringArray(requirements)) {
                RequirementHolder holder = RequirementHolder.fromStringList(requirements);
                LevelLockHandler.addLockByKey(new SkillLock(skill.getSkill(), level), holder);
            }
        }

        @Override
        public String describe() {
            StringBuilder descString = new StringBuilder("Requirements: ");
            for (String string : requirements) {
                descString.append(string).append(", ");
            }
            return "Added Level-Lock " + (skill == null ? "null" : skill.getName()) + ": " + level + " With Requirements: " + descString;
        }
    }
}
