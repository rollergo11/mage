package mage.abilities.dynamicvalue.common;

import mage.abilities.Ability;
import mage.abilities.costs.Cost;
import mage.abilities.costs.common.SacrificeTargetCost;
import mage.abilities.dynamicvalue.DynamicValue;
import mage.abilities.effects.Effect;
import mage.game.Game;
import mage.game.permanent.Permanent;

/**
 * @author LevelX2
 */
public class SacrificeCostCreaturesPower implements DynamicValue {

    @Override
    public int calculate(Game game, Ability sourceAbility, Effect effect) {
        for (Cost cost : sourceAbility.getCosts()) {
            if (cost instanceof SacrificeTargetCost) {
                SacrificeTargetCost sacrificeCost = (SacrificeTargetCost) cost;
                int powerSum = 0;
                for (Permanent permanent : sacrificeCost.getPermanents()) {
                    powerSum += permanent.getPower().getValue();
                }
                return powerSum;
            }
        }
        return 0;
    }

    @Override
    public SacrificeCostCreaturesPower copy() {
        return new SacrificeCostCreaturesPower();
    }

    @Override
    public String toString() {
        return "X";
    }

    @Override
    public String getMessage() {
        return "the sacrificed creature's power";
    }
}
